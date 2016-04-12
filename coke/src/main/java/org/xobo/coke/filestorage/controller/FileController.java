package org.xobo.coke.filestorage.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.xobo.coke.filestorage.domain.CokeFileInfo;
import org.xobo.coke.filestorage.service.FileStorageService;

@Controller
public class FileController {
  private Logger logger = LoggerFactory.getLogger(this.getClass());


  @RequestMapping(value = "/upload", produces = "application/json")
  public @ResponseBody Object handleFileUpload(
      @RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes)
      throws IOException {

    CokeFileInfo fileInfo = fileService.put(file, file.getOriginalFilename());

    Map<String, Object> result = new LinkedHashMap<String, Object>();
    result.put("fileNo", fileInfo.getFileNo());
    result.put("fileName", fileInfo.getFilename());
    return result;
  }

  @RequestMapping(value = "/download/{fileNo}")
  public void download(@PathVariable("fileNo") String fileNo, HttpServletRequest request,
      HttpServletResponse response) throws IOException {
    CokeFileInfo cokeFileInfo = fileService.get(fileNo);

    if (cokeFileInfo == null) {
      findNotFound(response, fileNo);
      return;
    }


    HttpSession session = request.getSession();

    String filename = cokeFileInfo.getFilename();
    response.setContentType(session.getServletContext().getMimeType(filename));

    String encodFilename = URLEncoder.encode(filename, "utf-8");
    response.setHeader("Cache-Control", "max-age=31556926");
    response.setHeader("Content-Disposition",
        String.format("attachment; filename=\"%1$s\"; filename*=utf-8''%1$s", encodFilename));

    // Content-Disposition:
    // if Content-Disposition header value is set to inline, the response is
    // displayed in browser

    // Copy input file's InputStream to response's OutputStream
    InputStream inputStream = fileService.getInputStream(cokeFileInfo);
    if (inputStream == null) {
      findNotFound(response, fileNo);
    }

    OutputStream outputStream = null;
    try {
      outputStream = response.getOutputStream();
      IOUtils.copy(inputStream, outputStream);
    } catch (FileNotFoundException fne) {
    } finally {
      IOUtils.closeQuietly(inputStream);
      IOUtils.closeQuietly(outputStream);
    }
  }

  void findNotFound(HttpServletResponse response, String fileNo) throws IOException {
    logger.info("File {} Not Found ", fileNo);
    response.sendError(HttpServletResponse.SC_NOT_FOUND);
  }

  @Resource(name = FileStorageService.BEAN_ID)
  private FileStorageService fileService;

}

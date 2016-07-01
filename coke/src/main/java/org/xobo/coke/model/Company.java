package org.xobo.coke.model;

import com.bstek.bdf2.core.business.ICompany;

public interface Company extends ICompany, java.io.Serializable {
  void setCompanyId(String companyId);

}

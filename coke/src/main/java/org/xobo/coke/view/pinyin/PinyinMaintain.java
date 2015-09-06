package org.xobo.coke.view.pinyin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.apache.commons.beanutils.BeanUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.xobo.coke.dao.CokeHibernate;
import org.xobo.coke.entity.PinyinConverter;
import org.xobo.coke.model.Pinyin;
import org.xobo.coke.querysupporter.service.impl.SynonymServiceImpl;
import org.xobo.coke.utility.PinyinUtility;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.Expose;

@Service("coke.pinyinMaintain")
public class PinyinMaintain {

	@DataProvider
	public Collection<PinyinConverter> loadPinyinConverters() {
		Collection<PinyinConverter> pinyinEntityList = new ArrayList<PinyinConverter>();
		Collection<Entry<Class<?>, Map<String, Collection<String>>>> clazzList = SynonymServiceImpl.getPinyinmap()
				.getData().entrySet();
		for (Entry<Class<?>, Map<String, Collection<String>>> clazz : clazzList) {
			PinyinConverter pinyinConverter = new PinyinConverter();
			pinyinConverter.setClazz(clazz.getKey().getName());

			Map<String, Collection<String>> pm = clazz.getValue();
			if (!pm.values().isEmpty()) {
				Collection<String> p = pm.values().iterator().next();
				for (String string : p) {
					if (string.toLowerCase().contains("quanpin")) {
						pinyinConverter.setQuanpinProperty(string);
					} else if (string.toLowerCase().contains("jianpin")) {
						pinyinConverter.setJianpinProperty(string);
					} else {
						pinyinConverter.setProperty(string);
					}
				}
				pinyinEntityList.add(pinyinConverter);
			}

		}
		return pinyinEntityList;
	}

	@Expose
	public void batchConvert(Map<String, Object> parameter) {
		String clazzName = (String) parameter.get("clazz");
		String property = (String) parameter.get("property");
		String quan = (String) parameter.get("quan");
		String jian = (String) parameter.get("jian");
		Integer batchSize = (Integer) parameter.get("batchSize");

		if (StringUtils.isEmpty(property)) {
			property = "name";
		}
		if (StringUtils.isEmpty(quan)) {
			quan = "Quanpin";
		}
		if (StringUtils.isEmpty(jian)) {
			jian = "Jianpin";
		}

		String quanProperty = property + quan;
		String jianProperty = property + jian;
		if (batchSize == null) {
			batchSize = 200;
		}
		Session session = cokeHibernate.getSession();

		Collection<?> list;
		do {
			DetachedCriteria dc = createDetachedCriteria(clazzName);
			dc.add(Restrictions.or(Restrictions.isNull(quanProperty), Restrictions.isNull(jianProperty)));
			dc.add(Restrictions.isNotNull(property));
			Criteria criteria = dc.getExecutableCriteria(session);
			criteria.setFirstResult(0);
			criteria.setMaxResults(batchSize);
			list = criteria.list();

			Transaction transaction = session.beginTransaction();
			for (Object object : list) {
				try {
					String value = BeanUtils.getProperty(object, property);
					Collection<Pinyin> pinyins = PinyinUtility.toPinyin(value);
					String quanValue;
					String jianValue;
					if (!pinyins.isEmpty()) {
						Pinyin pinyin = pinyins.iterator().next();
						quanValue = pinyin.getQuan();
						jianValue = pinyin.getJian();
					} else {
						quanValue = jianValue = value;
					}
					BeanUtils.setProperty(object, quanProperty, quanValue);
					BeanUtils.setProperty(object, jianProperty, jianValue);
					session.update(object);
				} catch (Exception e) {
					e.printStackTrace();
					break;
				}
			}
			transaction.commit();
			session.flush();
			session.clear();
		} while (!list.isEmpty());

	}

	public DetachedCriteria createDetachedCriteria(String name) {
		DetachedCriteria dc = null;
		if (name.contains(".")) {
			Class<?> clazz;
			try {
				clazz = Class.forName(name);
				dc = DetachedCriteria.forClass(clazz);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		} else {
			dc = DetachedCriteria.forEntityName(name);
		}
		return dc;
	}

	@Resource(name = CokeHibernate.BEAN_ID)
	private CokeHibernate cokeHibernate;
}

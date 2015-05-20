package org.xobo.coke.service.impl;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;

import javax.annotation.Resource;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.metadata.ClassMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xobo.coke.dao.CokeHibernate;
import org.xobo.coke.service.ReflectionRegister;
import org.xobo.coke.utility.BeanReflectionUtils;

import com.bstek.dorado.core.EngineStartupListener;

@Service
public class HibernateEntityEnhancerImpl extends EngineStartupListener {

	private static final Logger logger = LoggerFactory.getLogger(HibernateEntityEnhancerImpl.class);

	@Autowired
	private Collection<ReflectionRegister> reflectionRegisters;
	@Resource(name = CokeHibernate.BEAN_ID)
	private CokeHibernate cokeHibernate;

	@Override
	public void onStartup() throws Exception {
		logger.info("analyze hibernate entity");
		Session session = cokeHibernate.getSessionFactory().openSession();
		SessionFactory sessionFactory = session.getSessionFactory();
		Map<String, ClassMetadata> map = sessionFactory.getAllClassMetadata();
		for (String className : map.keySet()) {
			ClassMetadata classMetadata = map.get(className);
			String[] propertyNames = classMetadata.getPropertyNames();
			Collection<String> properties = new HashSet<String>(Arrays.asList(propertyNames));
			String identifierProperty = classMetadata.getIdentifierPropertyName();
			if (identifierProperty != null) {
				properties.add(identifierProperty);
			}
			try {
				Class<?> clazz = Class.forName(className);
				Collection<Field> fields = BeanReflectionUtils.loadClassFields(clazz);
				for (Field field : fields) {
					if (properties.contains(field.getName())) {
						for (ReflectionRegister register : reflectionRegisters) {
							register.register(clazz, field);
						}
					}
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}

	}

	@Override
	public int getOrder() {
		return 10000;
	}

}

package org.xobo.coke.dao;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.ResultTransformer;
import org.springframework.stereotype.Service;
import org.xobo.coke.entity.PersistWrapper;
import org.xobo.coke.entity.ReferenceWrapper;
import org.xobo.coke.model.Company;
import org.xobo.coke.model.IBase;
import org.xobo.coke.model.IDetail;
import org.xobo.coke.model.PathModel;
import org.xobo.coke.querysupporter.model.PropertyWrapper;
import org.xobo.coke.querysupporter.service.HibernateCriteriaBuilder;
import org.xobo.coke.querysupporter.service.impl.DoradoCriteriaBuilderImpl;
import org.xobo.coke.service.PersistAction;
import org.xobo.coke.service.impl.CriteriaImplHelper;
import org.xobo.coke.service.impl.NopPersistAction;
import org.xobo.coke.utility.BeanReflectionUtils;

import com.bstek.bdf2.core.business.IUser;
import com.bstek.bdf2.core.context.ContextHolder;
import com.bstek.bdf2.core.orm.hibernate.HibernateDao;
import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.entity.EntityUtils;
import com.bstek.dorado.data.provider.Criteria;
import com.bstek.dorado.data.provider.Page;

@Service(HibernateSupportDao.BEAN_ID)
public class HibernateSupportDao<K> extends HibernateDao {
	public static final String BEAN_ID = "cola.hibernateSupportDao";

	@Resource(name = HibernateCriteriaBuilder.BEAN_ID)
	private HibernateCriteriaBuilder criteriaBuilder;

	@SuppressWarnings("unchecked")
	public <T> T findPropertyValue(Class<?> clazz, String uniqueProperty, Object value, String property) {
		org.hibernate.Criteria criteria = getSession().createCriteria(clazz);
		criteria.add(Restrictions.eq(uniqueProperty, value));
		criteria.setProjection(Projections.property(property));
		return (T) criteria.uniqueResult();
	}

	public <T> Collection<T> load(Map<String, Object> queryParameter, Criteria criteria, Class<T> entityClass) {
		return load(null, queryParameter, null, criteria, entityClass);
	}

	public void load(Page<?> page, Map<String, Object> queryParameter, Criteria criteria, Class<?> entityClass) {
		load(page, queryParameter, null, criteria, entityClass);
	}

	@SuppressWarnings("unchecked")
	public <T> Collection<T> load(Page<T> page, Map<String, Object> queryParameter,
			Map<String, PropertyWrapper> propertyOperatorMap, Criteria criteria, Class<?> entityClass) {
		DetachedCriteria detachedCriteria = buildDetachedCriteria(queryParameter, propertyOperatorMap, criteria,
				entityClass);
		if (page != null) {
			this.pagingQuery(page, detachedCriteria);
			return page.getEntities();
		} else {
			return (Collection<T>) this.query(detachedCriteria);
		}
	}

	public DetachedCriteria buildDetachedCriteria(Map<String, Object> queryParameter, Criteria criteria,
			Class<?> entityClass) {
		return buildDetachedCriteria(queryParameter, null, criteria, entityClass);
	}

	public DetachedCriteria buildDetachedCriteria(Map<String, Object> queryParameter,
			Map<String, PropertyWrapper> propertyOperatorMap, Criteria criteria, Class<?> entityClass) {
		criteria = parameterToCriteria.mergeQueryParameterCriteria(queryParameter, propertyOperatorMap, criteria,
				entityClass);
		return criteriaBuilder.buildDetachedCriteria(criteria, entityClass);
	}

	@Resource(name = DoradoCriteriaBuilderImpl.BEAN_ID)
	private DoradoCriteriaBuilderImpl parameterToCriteria;

	public void find(Page<?> page, Criteria criteria, Class<?> entityClass) {
		DetachedCriteria detachedCriteria = this.buildDetachedCriteria(criteria, entityClass);
		this.pagingQuery(page, detachedCriteria);
	}

	@SuppressWarnings("unchecked")
	public <T> Page<T> find(Page<T> page, String hql, Object... parameters) {
		Query q = createQuery(hql, parameters);
		long totalCount = countHqlResult(hql, parameters);
		page.setEntityCount((int) totalCount);
		setPageParameterToQuery(q, page);
		page.setEntities(q.list());
		return page;
	}

	@SuppressWarnings("unchecked")
	public Page<?> find(Page<?> page, String hql, Map<String, ?> parameters) {
		Query q = createQuery(hql, parameters);
		long totalCount = countHqlResult(hql, parameters);
		page.setEntityCount((int) totalCount);
		setPageParameterToQuery(q, page);
		page.setEntities(q.list());
		return page;
	}

	protected Query setPageParameterToQuery(Query q, Page<?> page) {
		q.setFirstResult(page.getFirstEntityIndex());
		q.setMaxResults(page.getPageSize());
		return q;
	}

	@SuppressWarnings("unchecked")
	public <X> List<X> find(String hql, Object... parameters) {
		return createQuery(hql, parameters).list();
	}

	@SuppressWarnings("unchecked")
	public <X> List<X> find(String hql, Map<String, ?> parameters) {
		return createQuery(hql, parameters).list();
	}

	protected long countHqlResult(String hql, Object... parameters) {
		String countHql = generateCountHql(hql);
		return ((Number) findUnique(countHql, parameters)).longValue();
	}

	protected long countHqlResult(String hql, Map<String, ?> parameters) {
		String countHql = generateCountHql(hql);
		return ((Number) findUnique(countHql, parameters)).longValue();
	}

	private String generateCountHql(String hql) {
		String countHql = countHqlMap.get(hql);
		if (countHql == null) {
			hql = "from " + StringUtils.substringAfter(hql, "from");
			hql = StringUtils.substringBefore(hql, "order by");
			countHql = "select count(*) " + hql;
			countHqlMap.put(hql, countHql);
		}
		return countHql;
	}

	private static final Map<String, String> countHqlMap = new ConcurrentHashMap<String, String>();

	public Query createQuery(String hql, Object... parameters) {
		Query q = getSession().createQuery(hql);
		if (parameters != null) {
			for (int i = 0; i < parameters.length; ++i) {
				q.setParameter(i, parameters[i]);
			}
		}
		return q;
	}

	public Query createQuery(String queryString, Map<String, ?> parameters) {
		Query query = getSession().createQuery(queryString);
		if (parameters != null) {
			query.setProperties(parameters);
		}
		return query;
	}

	@SuppressWarnings("unchecked")
	public <X> X findUnique(String hql, Object... parameters) {
		return (X) createQuery(hql, parameters).uniqueResult();
	}

	@SuppressWarnings("unchecked")
	public <X> X findUnique(String hql, Map<String, ?> parameters) {
		return (X) createQuery(hql, parameters).uniqueResult();
	}

	public void persistEntities(Collection<? extends IBase<K>> entites) {
		persistEntities(entites, null, null);
	}

	public void persistEntities(Collection<? extends IBase<K>> entites, PersistWrapper persistWrapper) {
		persistEntities(entites, null, persistWrapper);
	}

	@SuppressWarnings("unchecked")
	public void persistEntities(Collection<? extends IBase<K>> entites, IBase<K> parent, PersistWrapper persistWrapper) {
		if (entites == null || entites.isEmpty()) {
			return;
		}

		IUser user = ContextHolder.getLoginUser();
		Session session = this.getSession();

		@SuppressWarnings("rawtypes")
		PersistAction persistAction = null;
		for (IBase<K> entity : entites) {

			if (persistAction == null && persistWrapper != null) {
				persistAction = persistWrapper.getPersistAction(entity);
			} else {
				persistAction = new NopPersistAction();
			}

			EntityState entityState = EntityUtils.getState(entity);

			if (EntityState.NEW == entityState) {
				setParentId(session, entity, parent);
				String companyId = user.getCompanyId();
				if (entity instanceof Company) {
					((Company) entity).setCompanyId(companyId);
				}
				persistAction.beforeCreate(session, entity, parent);
				insertEntity(session, entity, user);
				persistAction.afterCreate(session, entity, parent);
			} else if (EntityState.MODIFIED == entityState || EntityState.MOVED == entityState) {
				setParentId(session, entity, parent);
				persistAction.beforeUpdate(session, entity, parent);
				updateEntity(session, entity, user);
			} else if (EntityState.DELETED == entityState) {
				persistAction.beforeDelete(session, entity, parent);
				deleteEntity(session, entity, user);
			}

			if (EntityState.DELETED != entityState) {
				saveChildren(entity, persistWrapper);
			} else {
				deleteChildren(session, entity, persistWrapper);
			}

		}
	}

	@SuppressWarnings("unchecked")
	public void saveChildren(IBase<K> entity, PersistWrapper persistWrapper) {
		if (persistWrapper == null) {
			return;
		}
		Collection<ReferenceWrapper> properties = persistWrapper.getPropertyWrappers(entity);
		if (properties != null && !properties.isEmpty()) {
			for (ReferenceWrapper property : properties) {
				Object object = EntityUtils.getValue(entity, property.getProperty());
				if (object instanceof Collection<?>) {
					persistEntities((Collection<? extends IBase<K>>) object, entity, persistWrapper);
				}
			}
		}
	}

	public int deleteChildren(Session session, IBase<K> parent, PersistWrapper persistWrapper) {

		int result = 0;
		if (persistWrapper == null) {
			return result;
		}

		Collection<ReferenceWrapper> properties = persistWrapper.getPropertyWrappers(parent);
		if (properties != null && !properties.isEmpty()) {
			for (ReferenceWrapper property : properties) {
				Class<?> childClass = property.getClazz();
				if (IDetail.class.isAssignableFrom(childClass)) {
					result = session.createQuery("delete " + childClass.getName() + " c where c.parentId = :parentId")
							.setParameter("parentId", parent.getId()).executeUpdate();
				}
			}
		}
		return result;
	}

	public void setParentId(Session session, IBase<K> entity, IBase<K> parent) {
		if (entity instanceof IDetail<?>) {
			K parentId = null;
			if (parent == null) {
				if (entity instanceof IDetail<?>) {
					parentId = ((IDetail<K>) entity).getRoot();
				}
			} else {
				parentId = parent.getId();
			}
			((IDetail<K>) entity).setParentId(parentId);
		}
		setPathValue(session, entity, parent);
	}

	public void insertEntity(IBase<K> baseEntity) {
		insertEntity(getSession(), baseEntity, ContextHolder.getLoginUser());
	}

	public void insertEntity(IBase<K> baseEntity, IUser user) {
		insertEntity(getSession(), baseEntity, user);
	}

	public void updateEntity(IBase<K> baseEntity) {
		updateEntity(getSession(), baseEntity, ContextHolder.getLoginUser());
	}

	public void updateEntity(IBase<K> baseEntity, IUser user) {
		updateEntity(getSession(), baseEntity, user);
	}

	public void deleteEntity(IBase<K> baseEntity) {
		deleteEntity(getSession(), baseEntity, ContextHolder.getLoginUser());
	}

	public void deleteEntity(IBase<K> baseEntity, IUser user) {
		deleteEntity(getSession(), baseEntity, user);
	}

	public void insertEntity(Session session, IBase<?> baseEntity, IUser user) {
		if (user == null) {
			user = ContextHolder.getLoginUser();
		}
		if (baseEntity instanceof Company) {
			((Company) baseEntity).setCompanyId(user.getCompanyId());
		}

		baseEntity.setCreateUser(user.getUsername());
		baseEntity.setCreateDate(new Date());
		baseEntity.setDeleted(false);
		session.save(baseEntity);
	}

	private void setPathValue(Session session, IBase<K> baseEntity, IBase<K> parent) {
		if (baseEntity instanceof PathModel) {
			PathModel<K> pathEntity = (PathModel<K>) baseEntity;
			K parentId;
			String parentTypePath;
			if (parent == null || !(parent instanceof PathModel)) {
				parentId = pathEntity.getRoot();
				parentTypePath = PathModel.TypeSeparator;
			} else {
				PathModel<K> parentPathEntity = (PathModel<K>) parent;
				parentId = parent.getId();
				parentTypePath = parentPathEntity.getTypePath();
				if (StringUtils.isEmpty(parentTypePath)) {
					parentTypePath = PathModel.TypeSeparator;
				}
			}
			session.flush();

			Long indexNo = pathEntity.getIndexNo();
			if (indexNo == null) {
				indexNo = (Long) session.createCriteria(BeanReflectionUtils.getClass(baseEntity))
						.add(Restrictions.eq("parentId", parentId)).setProjection(Projections.max("indexNo"))
						.uniqueResult();
				indexNo = indexNo != null ? indexNo + 1 : 1;
				pathEntity.setOrderNo(indexNo);
				pathEntity.setIndexNo(indexNo);
			}
			String typePath = pathEntity.getTypePath();
			String newTypePath = parentTypePath + pathEntity.getType() + indexNo + PathModel.TypeSeparator;
			if (typePath == null || !typePath.equals(newTypePath)) {
				pathEntity.setTypePath(newTypePath);
			}
		}

	}

	public void updateEntity(Session session, IBase<?> baseEntity, IUser user) {
		if (user == null) {
			user = ContextHolder.getLoginUser();
		}
		baseEntity.setUpdateUser(user.getUsername());
		baseEntity.setUpdateDate(new Date());
		baseEntity.setDeleted(false);
		session.saveOrUpdate(baseEntity);
	}

	public void deleteEntity(Session session, IBase<?> baseEntity, IUser user) {
		if (user == null) {
			user = ContextHolder.getLoginUser();
		}
		baseEntity.setUpdateUser(user.getUsername());
		baseEntity.setUpdateDate(new Date());
		session.delete(baseEntity);
	}

	@SuppressWarnings("unchecked")
	public <T> T get(Class<T> clazz, String id) {
		return (T) this.getSession().get(clazz, id);
	}

	public org.hibernate.Criteria createCriteria(Class<?> persistentClass) {
		return this.getSession().createCriteria(persistentClass);
	}

	public Page<?> find(Page<?> page, DetachedCriteria detachedCriteria) {
		org.hibernate.Criteria criteria = detachedCriteria.getExecutableCriteria(getSession());
		return find(page, criteria);
	}

	@SuppressWarnings("unchecked")
	public Page<?> find(Page<?> page, org.hibernate.Criteria criteria) {
		long totalCount = countCriteriaResult(criteria);
		page.setEntityCount((int) totalCount);
		setPageParameterToCriteria(criteria, page);
		page.setEntities(criteria.list());
		return page;
	}

	protected org.hibernate.Criteria setPageParameterToCriteria(org.hibernate.Criteria c, Page<?> page) {
		c.setFirstResult(page.getFirstEntityIndex());
		c.setMaxResults(page.getPageSize());
		return c;
	}

	@SuppressWarnings("rawtypes")
	protected long countCriteriaResult(org.hibernate.Criteria criteria) {
		CriteriaImplHelper implHelper = new CriteriaImplHelper(criteria);
		long count = 0;
		List orderEntries = null;

		try {
			orderEntries = implHelper.getOrderEntries();
			implHelper.setOrderEntries(Collections.emptyList());

			Projection projection = implHelper.getProjection();
			ResultTransformer transformer = implHelper.getResultTransformer();

			count = ((Number) criteria.setProjection(Projections.rowCount()).uniqueResult()).longValue();
			criteria.setProjection(projection);
			if (projection == null) {
				criteria.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
			}
			if (transformer != null) {
				criteria.setResultTransformer(transformer);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			try {
				implHelper.setOrderEntries(orderEntries);
			} catch (Exception e) {
			}
		}
		return count;
	}

	public void rebuildTypePath(Criteria criteria, Class<?> clazz, K parentId, String parentTypePath) {
		parentTypePath = parentTypePath == null ? PathModel.TypeSeparator : parentTypePath;
		recursiveBuildTypePath(criteria, clazz, parentId, parentTypePath);
	}

	@SuppressWarnings("unchecked")
	private void recursiveBuildTypePath(Criteria criteria, Class<?> clazz, K parentId, String parentTypePath) {
		DetachedCriteria dc = buildDetachedCriteria(criteria, clazz);
		if (parentId == null) {
			dc.add(Restrictions.isNull("parentId"));
		} else {
			dc.add(Restrictions.eq("parentId", parentId));
		}
		Collection<?> list = query(dc);
		Long indexNo = 1L;
		Long orderNo = 1L;

		for (Object object : list) {
			if (object instanceof PathModel<?>) {
				PathModel<K> entity = (PathModel<K>) object;
				entity.setIndexNo(indexNo);
				entity.setOrderNo(orderNo);
				entity.setTypePath(parentTypePath + indexNo + PathModel.TypeSeparator);
				updateEntity(entity);
				recursiveBuildTypePath(criteria, clazz, entity.getId(), entity.getTypePath());
				indexNo++;
				orderNo++;
			}
		}
	}

	public <T> void manualSave(IBase<T> entity, IUser user) {
		Session session = getSessionFactory().openSession();
		Transaction transaction = session.beginTransaction();
		try {
			insertEntity(session, entity, user);
			transaction.commit();
		} catch (Exception e) {
			e.printStackTrace();
			transaction.rollback();
		} finally {
			session.flush();
			session.close();
		}
	}
}

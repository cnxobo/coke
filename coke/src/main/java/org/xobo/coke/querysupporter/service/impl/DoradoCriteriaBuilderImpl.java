package org.xobo.coke.querysupporter.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.xobo.coke.querysupporter.model.PropertyWrapper;
import org.xobo.coke.querysupporter.service.DoradoCriteriaBuilder;
import org.xobo.coke.querysupporter.service.QueryPropertyWrapperService;

import com.bstek.dorado.data.provider.Criteria;
import com.bstek.dorado.data.provider.Criterion;
import com.bstek.dorado.data.provider.filter.FilterOperator;
import com.bstek.dorado.data.provider.filter.SingleValueFilterCriterion;

@Service(DoradoCriteriaBuilderImpl.BEAN_ID)
public class DoradoCriteriaBuilderImpl implements DoradoCriteriaBuilder {
	public static final String BEAN_ID = "coke.parameterToDcriteria";

	@Resource(name = QueryPropertyWrapperService.BEAN_ID)
	private QueryPropertyWrapperService propertyWrapperService;

	@Override
	public Criteria mergeQueryParameterCriteria(Map<String, Object> queryParameter,
			Map<String, PropertyWrapper> propertyOperatorMap, Criteria criteria, Class<?> entityClass) {
		if (criteria == null) {
			criteria = new Criteria();
		}
		criteria.getCriterions().addAll(extractQueryParameter(queryParameter, propertyOperatorMap, entityClass));
		return criteria;
	}

	@Override
	public List<Criterion> extractQueryParameter(Map<String, Object> queryParameter,
			Map<String, PropertyWrapper> propertyOperatorMap, Class<?> entityClass) {

		List<Criterion> criterions = new ArrayList<Criterion>();
		if (queryParameter != null) {

			for (Entry<String, Object> entry : queryParameter.entrySet()) {
				String key = entry.getKey();
				Object value = entry.getValue();

				if (value == null || StringUtils.isEmpty(value.toString())) {
					continue;
				}

				PropertyWrapper propertyWrapper = propertyWrapperService.find(entityClass, key, propertyOperatorMap);

				if (propertyWrapper != null) { // process own property
					SingleValueFilterCriterion criterion = new SingleValueFilterCriterion();
					criterion.setProperty(propertyWrapper.getProperty());
					criterion.setFilterOperator(propertyWrapper.getFilterOperator());

					value = propertyWrapper.parseValue(value);

					if (value instanceof Date) {
						if (FilterOperator.le == propertyWrapper.getFilterOperator()) {
							value = getTomorrowDate((Date) value);
							criterion.setFilterOperator(FilterOperator.lt);
						}
					}
					criterion.setValue(value);
					criterions.add(criterion);
				}
			}
		}
		return criterions;
	}

	private Date getTomorrowDate(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, 1);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}
}

package org.xobo.coke.service.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.xobo.coke.dao.model.QueryResolver;
import org.xobo.coke.service.PinyinQueryService;
import org.xobo.coke.service.QueryDcriteriaToSql;

import com.bstek.dorado.data.provider.Criteria;
import com.bstek.dorado.data.provider.Criterion;
import com.bstek.dorado.data.provider.Junction;
import com.bstek.dorado.data.provider.Or;
import com.bstek.dorado.data.provider.Order;
import com.bstek.dorado.data.provider.filter.FilterOperator;
import com.bstek.dorado.data.provider.filter.SingleValueFilterCriterion;

@Service(QueryDcriteriaToSql.BEAN_ID)
public class QueryDcriteriaToSqlImpl implements QueryDcriteriaToSql {

	@Resource(name = QueryParameterToDcriteria.BEAN_ID)
	private QueryParameterToDcriteria parameterToCriteria;

	public QueryResolver extractQuery(Class<?> clazz, Criteria criteria, Map<String, Object> queryParameter,
			String alias) {
		criteria = parameterToCriteria.mergeQueryParameterCriteria(queryParameter, null, criteria, clazz);
		return extractCriteria(clazz, criteria, alias);
	}

	public QueryResolver extractCriteria(Class<?> clazz, Criteria criteria, String alias) {
		int parameterNameCount = 0;
		if (criteria == null) {
			return null;
		}
		QueryResolver result = new QueryResolver();

		// build where
		if (!criteria.getCriterions().isEmpty()) {
			StringBuilder whereCondition = result.getWhere();
			Map<String, Object> valueMap = result.getValueMap();
			int count = 0;
			for (Criterion c : criteria.getCriterions()) {
				if (count > 0) {
					whereCondition.append(" and ");
				}
				count++;
				parameterNameCount = buildCriterion(clazz, whereCondition, c, valueMap, parameterNameCount, alias);
			}
		}

		// build order
		StringBuilder orderBuilder = result.getOrder();
		List<Order> orders = criteria.getOrders();
		for (Order order : orders) {
			if (orderBuilder.length() > 0) {
				orderBuilder.append(",");
			}

			if (StringUtils.isNotEmpty(alias)) {
				orderBuilder.append(" " + alias + "." + order.getProperty());
			} else {
				orderBuilder.append(" " + order.getProperty());
			}

			if (order.isDesc()) {
				orderBuilder.append(" desc");
			} else {
				orderBuilder.append(" asc");
			}
		}
		return result;
	}

	void singleValueFilterCriterion(StringBuilder sb, Map<String, Object> valueMap, int parameterNameCount,
			String alias, String propertyName, String operator, Object value) {
		if (StringUtils.isNotEmpty(alias)) {
			sb.append(" " + alias + "." + propertyName);
		} else {
			sb.append(" " + propertyName);
		}
		sb.append(" " + processLike(operator) + " ");
		String prepareName = propertyName + "_" + parameterNameCount + "_";
		sb.append(" :" + prepareName + " ");
		if (operator.equals("like")) {
			valueMap.put(prepareName, "%" + value + "%");
		} else if (operator.startsWith("*")) {
			valueMap.put(prepareName, "%" + value);
		} else if (operator.endsWith("*")) {
			valueMap.put(prepareName, value + "%");
		} else {
			valueMap.put(prepareName, value);
		}
	}

	private int buildCriterion(Class<?> clazz, StringBuilder sb, Criterion c, Map<String, Object> valueMap,
			int parameterNameCount, String alias) {
		if (c instanceof SingleValueFilterCriterion) {
			parameterNameCount++;
			SingleValueFilterCriterion fc = (SingleValueFilterCriterion) c;
			String operator = buildOperator(fc.getFilterOperator());
			String propertyName = buildFieldName(fc.getProperty());

			Collection<String> unionPropertyList = pinyinQueryService.get(clazz, propertyName);

			if (unionPropertyList.isEmpty()) {
				singleValueFilterCriterion(sb, valueMap, parameterNameCount, alias, propertyName, operator,
						fc.getValue());
			} else {
				sb.append(" ( ");
				boolean first = true;
				for (String property : unionPropertyList) {
					if (!first) {
						sb.append(" or ");
					} else {
						first = false;
					}
					singleValueFilterCriterion(sb, valueMap, parameterNameCount, alias, property, operator,
							fc.getValue());
				}
				sb.append(" ) ");
			}
		}
		if (c instanceof Junction) {
			Junction jun = (Junction) c;
			String junction = " and ";
			if (jun instanceof Or) {
				junction = " or ";
			}
			int count = 0;
			Collection<Criterion> criterions = jun.getCriterions();
			if (criterions != null) {
				sb.append(" ( ");
				for (Criterion criterion : criterions) {
					if (count > 0) {
						sb.append(junction);
					}
					count++;
					parameterNameCount = buildCriterion(clazz, sb, criterion, valueMap, parameterNameCount, alias);
				}
				sb.append(" ) ");
			}
		}
		return parameterNameCount;
	}

	private String processLike(String operator) {
		String result = operator;
		if (operator.endsWith("*")) {
			result = operator.substring(0, operator.length() - 1);
		}
		if (operator.startsWith("*")) {
			result = operator.substring(1, operator.length());
		}
		return result;
	}

	protected String buildOperator(FilterOperator filterOperator) {
		String operator = "like";
		if (filterOperator != null) {
			operator = filterOperator.toString();
		}
		return operator;
	}

	protected String buildFieldName(String name) {
		return name;
	}

	@Resource(name = PinyinQueryService.BEAN_ID)
	private PinyinQueryService pinyinQueryService;

}

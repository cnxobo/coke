package org.xobo.coke.chains.service;

import java.util.ArrayList;
import java.util.List;

import org.xobo.coke.model.ObjectWrapper;

public class DataFilterChain implements DataFilter {
	List<DataFilter> filters = new ArrayList<DataFilter>();
	int index = 0;

	public DataFilterChain addDataFilter(DataFilter f) {
		filters.add(f);
		return this;
	}

	@Override
	public void doFilter(ObjectWrapper request, ObjectWrapper response, DataFilterChain chain) {
		if (index == filters.size()) {
			return;
		}

		DataFilter f = filters.get(index);
		index++;
		f.doFilter(request, response, chain);
	}
}
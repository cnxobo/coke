package org.xobo.coke.chains.service;

import org.xobo.coke.model.ObjectWrapper;

public interface DataFilter {
	void doFilter(ObjectWrapper request, ObjectWrapper response, DataFilterChain chain);

}

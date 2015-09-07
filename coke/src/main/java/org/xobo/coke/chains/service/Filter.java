package org.xobo.coke.chains.service;

public interface Filter<K, V> {
	void doFilter(K request, V response, FilterChain<K, V> chain);

}

package org.xobo.coke.service;

import java.util.Collection;

import org.xobo.coke.model.DictEntry;

public interface DictEntriesProvider {

	Collection<DictEntry> lookup(Object categorykey, Object... otherParameters);

	String getType();

}

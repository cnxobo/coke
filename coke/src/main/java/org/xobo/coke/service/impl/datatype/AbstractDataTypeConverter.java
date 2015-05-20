package org.xobo.coke.service.impl.datatype;

import org.xobo.coke.service.DataTypeConverter;

public abstract class AbstractDataTypeConverter implements DataTypeConverter {

	@Override
	public String toText(Object value) {
		return value.toString();
	}

}

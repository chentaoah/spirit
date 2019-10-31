package com.sum.shy.core.converter;

import java.util.List;

import com.sum.shy.core.api.Converter;
import com.sum.shy.core.entity.Clazz;
import com.sum.shy.core.entity.Method;
import com.sum.shy.core.entity.Stmt;

public class EndConverter implements Converter {

	@Override
	public int convert(StringBuilder sb, String indent, Clazz clazz, Method method, List<String> lines, int index,
			String line, Stmt stmt) {
		sb.append("\t\t" + stmt + "\n");
		return 0;
	}

}

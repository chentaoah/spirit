package com.sum.shy.java.convert;

import com.sum.shy.clazz.IClass;
import com.sum.shy.clazz.IMethod;
import com.sum.shy.core.entity.Line;
import com.sum.shy.core.entity.Stmt;
import com.sum.shy.java.JavaConverter;
import com.sum.shy.java.api.Converter;

public class SimpleConverter implements Converter {

	@Override
	public Stmt convert(IClass clazz, IMethod method, String indent, String block, Line line, Stmt stmt) {

		if (stmt.isDeclare()) {// String str
			JavaConverter.addLineEnd(clazz, stmt);
			return stmt;

		} else if (stmt.isCatch()) {// }catch Exception e {
			JavaConverter.insertBrackets(clazz, stmt);
			return stmt;

		} else if (stmt.isSync()) {// sync obj {
			String text = String.format("synchronized (%s) {", stmt.get(1));
			return new Stmt(text);

		} else if (stmt.isElse() || stmt.isEnd() || stmt.isTry()) {
			return stmt;

		} else if (stmt.isSuper() || stmt.isThis() || stmt.isFieldAssign() || stmt.isInvoke() || stmt.isReturn()
				|| stmt.isContinue() || stmt.isBreak() || stmt.isThrow()) {
			JavaConverter.convert(clazz, stmt);
			JavaConverter.addLineEnd(clazz, stmt);
			return stmt;
		}

		return stmt;
	}

}

package com.sum.shy.java.converter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sum.shy.core.clazz.IClass;
import com.sum.shy.core.clazz.IField;
import com.sum.shy.core.clazz.IType;
import com.sum.shy.core.clazz.type.TypeBuilder;
import com.sum.shy.core.document.Element;
import com.sum.shy.core.document.Line;
import com.sum.shy.core.document.Stmt;
import com.sum.shy.core.document.Token;
import com.sum.shy.core.entity.Constants;
import com.sum.shy.core.processor.FastDeducer;

public class StmtConverter {

	public static void convert(IClass clazz, Element element) {

		if (element.isSync()) {// sync s {
			element.replaceKeyword(Constants.SYNC_KEYWORD, "synchronized");

		} else if (element.isFor()) {// for i=0; i<100; i++ {
			Token token = element.getToken(1);
			if (!token.isType() && token.isVar() && !token.isDerivedAtt())
				element.addToken(1, new Token(Constants.TYPE_TOKEN, TypeBuilder.build(clazz, token.getTypeAtt())));

		} else if (element.isForIn()) {// for item in list {
			Token item = element.getToken(1);
			Stmt subStmt = element.subStmt(3, element.getSize() - 1);
			String text = String.format("for (%s %s : %s) {", TypeBuilder.build(clazz, item.getTypeAtt()), item,
					subStmt);
			element.replace(0, element.getSize(), new Token(Constants.CUSTOM_EXPRESS_TOKEN, text));

		} else if (element.isAssign()) {// var = list.get(0)
			Token token = element.getToken(0);
			if (token.isVar() && token.isDerivedAtt())
				element.addToken(0, new Token(Constants.TYPE_TOKEN, TypeBuilder.build(clazz, token.getTypeAtt())));

		} else if (element.isIf() || element.isWhile()) {// if s { // while s {
			Stmt subStmt = element.subStmt(1, element.getSize() - 1);
			IType type = FastDeducer.deriveStmt(clazz, subStmt);
			if (type.isStr()) {
				String text = String.format("StringUtils.isNotEmpty(%s)", subStmt);
				element.replace(1, element.getSize() - 1, new Token(Constants.CUSTOM_EXPRESS_TOKEN, text));
			}

		} else if (element.isPrint() || element.isDebug() || element.isError()) {
			if (element.isPrint()) {
				element.setToken(0, new Token(Constants.CUSTOM_PREFIX_TOKEN, "logger.info("));
			} else if (element.isDebug()) {
				element.setToken(0, new Token(Constants.CUSTOM_PREFIX_TOKEN, "logger.debug("));// 替换
			} else if (element.isError()) {
				element.setToken(0, new Token(Constants.CUSTOM_PREFIX_TOKEN, "logger.error("));// 替换
			}
			element.addToken(new Token(Constants.CUSTOM_SUFFIX_TOKEN, ");"));

			if (!clazz.existField("logger")) {
				// 添加依赖
				clazz.addImport(Logger.class.getName());
				clazz.addImport(LoggerFactory.class.getName());
				// 添加字段
				Element element1 = new Element(
						new Line("Logger logger = LoggerFactory.getLogger(" + clazz.getSimpleName() + ".class)"));
				IField field = new IField(null, true, element1);
				clazz.fields.add(0, field);
			}

		}

	}

}

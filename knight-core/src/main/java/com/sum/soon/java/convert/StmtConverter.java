package com.sum.soon.java.convert;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sum.pisces.api.annotation.Order;
import com.sum.pisces.core.ProxyFactory;
import com.sum.soon.api.convert.ElementConverter;
import com.sum.soon.api.deduce.FastDeducer;
import com.sum.soon.api.lexer.ElementBuilder;
import com.sum.soon.java.TypeBuilder;
import com.sum.soon.pojo.clazz.IClass;
import com.sum.soon.pojo.clazz.IField;
import com.sum.soon.pojo.clazz.IType;
import com.sum.soon.pojo.common.Constants;
import com.sum.soon.pojo.element.Element;
import com.sum.soon.pojo.element.Statement;
import com.sum.soon.pojo.element.Token;

@Order(-40)
public class StmtConverter implements ElementConverter {

	public static ElementBuilder builder = ProxyFactory.get(ElementBuilder.class);

	public static FastDeducer deducer = ProxyFactory.get(FastDeducer.class);

	@Override
	public void convert(IClass clazz, Element element) {

		if (element.isSync()) {// sync s {
			element.replaceKeyword(Constants.SYNC_KEYWORD, "synchronized");

		} else if (element.isFor()) {// for i=0; i<100; i++ {
			Token token = element.getToken(1);
			if (!token.isType() && token.isVar() && token.isDerived())
				element.addToken(1, new Token(Constants.TYPE_TOKEN, TypeBuilder.build(clazz, token.getTypeAtt())));

		} else if (element.isForIn()) {// for item in list {
			Token item = element.getToken(1);
			Statement subStmt = element.subStmt(3, element.size() - 1);
			String text = String.format("for (%s %s : %s) {", TypeBuilder.build(clazz, item.getTypeAtt()), item, subStmt);
			element.replace(0, element.size(), new Token(Constants.CUSTOM_EXPRESS_TOKEN, text));

		} else if (element.isAssign()) {// var = list.get(0)
			Token token = element.getToken(0);
			if (token.isVar() && token.isDerived())
				element.addToken(0, new Token(Constants.TYPE_TOKEN, TypeBuilder.build(clazz, token.getTypeAtt())));

		} else if (element.isIf() || element.isWhile()) {// if s { // while s {
			Statement subStmt = element.subStmt(1, element.size() - 1);
			IType type = deducer.derive(clazz, subStmt);
			if (type.isStr()) {
				String text = String.format("StringUtils.isNotEmpty(%s)", subStmt);
				element.replace(1, element.size() - 1, new Token(Constants.CUSTOM_EXPRESS_TOKEN, text));
			}

		} else if (element.isPrint() || element.isDebug() || element.isError()) {
			if (element.isPrint()) {
				element.setToken(0, new Token(Constants.CUSTOM_PREFIX_TOKEN, "logger.info("));
			} else if (element.isDebug()) {
				element.setToken(0, new Token(Constants.CUSTOM_PREFIX_TOKEN, "logger.debug("));
			} else if (element.isError()) {
				element.setToken(0, new Token(Constants.CUSTOM_PREFIX_TOKEN, "logger.error("));
			}
			element.addToken(new Token(Constants.CUSTOM_SUFFIX_TOKEN, ");"));

			if (clazz.getField("logger") == null) {
				clazz.addImport(Logger.class.getName());
				clazz.addImport(LoggerFactory.class.getName());

				Element logger = builder.build("Logger logger = LoggerFactory.getLogger(" + clazz.getSimpleName() + ".class)");
				IField field = new IField(null, true, logger);
				clazz.fields.add(0, field);
			}
		}
	}

}

package com.sum.spirit.java.convert;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sum.pisces.api.annotation.Order;
import com.sum.pisces.core.ProxyFactory;
import com.sum.spirit.api.convert.ElementConverter;
import com.sum.spirit.api.deduce.FastDeducer;
import com.sum.spirit.api.lexer.ElementBuilder;
import com.sum.spirit.java.TypeBuilder;
import com.sum.spirit.pojo.clazz.IClass;
import com.sum.spirit.pojo.clazz.IField;
import com.sum.spirit.pojo.clazz.IType;
import com.sum.spirit.pojo.common.Constants;
import com.sum.spirit.pojo.element.Element;
import com.sum.spirit.pojo.element.Statement;
import com.sum.spirit.pojo.element.Token;

@Order(-40)
public class StmtConverter implements ElementConverter {

	public static ElementBuilder builder = ProxyFactory.get(ElementBuilder.class);

	public static FastDeducer deducer = ProxyFactory.get(FastDeducer.class);

	@Override
	public void convert(IClass clazz, Element element) {

		if (element.isDeclare() || element.isDeclareAssign() || element.isAssign())
			element.replaceModifier(Constants.CONST_KEYWORD, Constants.FINAL_KEYWORD);

		if (element.isSync()) {// sync s {
			element.replaceKeyword(Constants.SYNC_KEYWORD, "synchronized");

		} else if (element.isFor()) {// for i=0; i<100; i++ {
			Token token = element.getToken(1);
			if (!token.isType() && token.isVar() && token.isDerived())
				element.addToken(1, new Token(Constants.TYPE_TOKEN, TypeBuilder.build(clazz, token.getTypeAtt())));

		} else if (element.isForIn()) {// for item in list {
			Token item = element.getToken(1);
			Statement statement = element.subStmt(3, element.size() - 1);
			String text = String.format("for (%s %s : %s) {", TypeBuilder.build(clazz, item.getTypeAtt()), item, statement);
			element.replace(0, element.size(), new Token(Constants.CUSTOM_EXPRESS_TOKEN, text));

		} else if (element.isAssign()) {// var = list.get(0)
			Token token = element.getToken(0);
			if (token.isVar() && token.isDerived())
				element.addToken(0, new Token(Constants.TYPE_TOKEN, TypeBuilder.build(clazz, token.getTypeAtt())));

		} else if (element.isIf() || element.isWhile()) {// if s { // while s {
			Statement statement = element.subStmt(1, element.size() - 1);
			IType type = deducer.derive(clazz, statement);
			if (type.isStr()) {
				String text = String.format("StringUtils.isNotEmpty(%s)", statement);
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
				IField field = new IField(null,
						logger.addModifier(Constants.FINAL_KEYWORD).addModifier(Constants.STATIC_KEYWORD).addModifier(Constants.PUBLIC_KEYWORD));
				clazz.fields.add(0, field);
			}
		}
	}

}
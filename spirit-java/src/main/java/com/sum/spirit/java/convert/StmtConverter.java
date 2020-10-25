package com.sum.spirit.java.convert;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.sum.spirit.core.deduce.FastDeducer;
import com.sum.spirit.core.lexer.ElementBuilder;
import com.sum.spirit.java.api.ElementConverter;
import com.sum.spirit.java.build.JavaBuilder;
import com.sum.spirit.java.build.TypeBuilder;
import com.sum.spirit.java.utils.TypeUtils;
import com.sum.spirit.pojo.clazz.IClass;
import com.sum.spirit.pojo.clazz.IField;
import com.sum.spirit.pojo.clazz.IType;
import com.sum.spirit.pojo.element.Element;
import com.sum.spirit.pojo.element.Statement;
import com.sum.spirit.pojo.element.Token;
import com.sum.spirit.pojo.enums.AttributeEnum;
import com.sum.spirit.pojo.enums.KeywordEnum;
import com.sum.spirit.pojo.enums.TokenTypeEnum;

@Component
@Order(-40)
public class StmtConverter implements ElementConverter {

	@Autowired
	public ElementBuilder builder;

	@Autowired
	public FastDeducer deducer;

	@Override
	public void convert(IClass clazz, Element element) {

		if (element.isDeclare() || element.isDeclareAssign() || element.isAssign())
			element.replaceModifier(KeywordEnum.CONST.value, JavaBuilder.FINAL_KEYWORD);

		if (element.isSync()) {// sync s {
			element.replaceKeyword(KeywordEnum.SYNC.value, "synchronized");

		} else if (element.isFor()) {// for (i=0; i<100; i++) {
			Token secondToken = element.getToken(1);
			if (secondToken.isSubexpress()) {
				Statement statement = secondToken.getValue();
				Token token = statement.getToken(1);
				if (!token.isType() && token.isVar()) {
					boolean derived = token.getAttribute(AttributeEnum.DERIVED, false);
					if (derived) {
						IType type = token.getAttribute(AttributeEnum.TYPE);
						statement.addToken(1, new Token(TokenTypeEnum.TYPE, TypeBuilder.build(clazz, type)));
					}
				}
			}

		} else if (element.isForIn()) {// for item in list {
			Token item = element.getToken(1);
			IType type = item.getAttribute(AttributeEnum.TYPE);
			Statement statement = element.subStmt(3, element.size() - 1);
			String text = String.format("for (%s %s : %s) {", TypeBuilder.build(clazz, type), item, statement);
			element.replace(0, element.size(), new Token(TokenTypeEnum.CUSTOM_EXPRESS, text));

		} else if (element.isAssign()) {// var = list.get(0)
			Token token = element.getToken(0);
			boolean derived = token.getAttribute(AttributeEnum.DERIVED, false);
			IType type = token.getAttribute(AttributeEnum.TYPE);
			if (token.isVar() && derived)
				element.addToken(0, new Token(TokenTypeEnum.TYPE, TypeBuilder.build(clazz, type)));

		} else if (element.isIf() || element.isWhile()) {// if s { // while s {
			Statement statement = element.subStmt(1, element.size() - 1);
			IType type = deducer.derive(clazz, statement);
			if (TypeUtils.isStr(type)) {
				String text = String.format("StringUtils.isNotEmpty(%s)", statement);
				element.replace(1, element.size() - 1, new Token(TokenTypeEnum.CUSTOM_EXPRESS, text));
			}

		} else if (element.isPrint() || element.isDebug() || element.isError()) {
			if (element.isPrint()) {
				element.setToken(0, new Token(TokenTypeEnum.CUSTOM_PREFIX, "logger.info("));
			} else if (element.isDebug()) {
				element.setToken(0, new Token(TokenTypeEnum.CUSTOM_PREFIX, "logger.debug("));
			} else if (element.isError()) {
				element.setToken(0, new Token(TokenTypeEnum.CUSTOM_PREFIX, "logger.error("));
			}
			element.addToken(new Token(TokenTypeEnum.CUSTOM_SUFFIX, ");"));

			if (clazz.getField("logger") == null) {
				clazz.addImport(Logger.class.getName());
				clazz.addImport(LoggerFactory.class.getName());
				Element logger = builder.build("Logger logger = LoggerFactory.getLogger(" + clazz.getSimpleName() + ".class)");
				IField field = new IField(null,
						logger.addModifier(JavaBuilder.FINAL_KEYWORD).addModifier(KeywordEnum.STATIC.value).addModifier(KeywordEnum.PUBLIC.value));
				clazz.fields.add(0, field);
			}
		}
	}

}

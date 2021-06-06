package com.gitee.spirit.output.java.action;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.gitee.spirit.common.constants.Attribute;
import com.gitee.spirit.common.enums.KeywordEnum;
import com.gitee.spirit.common.enums.TokenTypeEnum;
import com.gitee.spirit.core.api.ElementBuilder;
import com.gitee.spirit.core.clazz.entity.IClass;
import com.gitee.spirit.core.clazz.entity.IField;
import com.gitee.spirit.core.clazz.entity.IType;
import com.gitee.spirit.core.compile.AutoImporter;
import com.gitee.spirit.core.compile.deduce.FragmentDeducer;
import com.gitee.spirit.core.compile.entity.VisitContext;
import com.gitee.spirit.core.element.entity.Element;
import com.gitee.spirit.core.element.entity.Statement;
import com.gitee.spirit.core.element.entity.Token;
import com.gitee.spirit.output.java.JavaBuilder;
import com.gitee.spirit.output.java.utils.TypeUtils;

@Component
@Order(-40)
public class StatementAction extends AbstractExtElementAction {

	@Autowired
	public ElementBuilder builder;
	@Autowired
	public FragmentDeducer deducer;
	@Autowired
	public AutoImporter importer;

	@Override
	public void visitElement(VisitContext context, Element element) {
		IClass clazz = context.clazz;

		if (element.isDeclare() || element.isDeclareAssign() || element.isAssign()) {
			element.replaceModifier(KeywordEnum.CONST.value, JavaBuilder.FINAL_KEYWORD);
		}

		if (element.isSync()) {// sync s {
			element.replaceKeyword(KeywordEnum.SYNC.value, JavaBuilder.SYNCHRONIZED_KEYWORD);

		} else if (element.isFor()) {// for (i=0; i<100; i++) {
			Token secondToken = element.get(1);
			if (secondToken.isSubexpress()) {
				Statement statement = secondToken.getValue();
				Token token = statement.get(1);
				if (!token.isType() && token.isVariable()) {
					boolean derived = token.attr(Attribute.DERIVED, false);
					if (derived) {
						IType type = token.attr(Attribute.TYPE);
						statement.add(1, new Token(TokenTypeEnum.TYPE, importer.getFinalName(clazz, type)));
					}
				}
			}

		} else if (element.isForIn()) {// for item in list {
			Token item = element.get(1);
			IType type = item.attr(Attribute.TYPE);
			Statement statement = element.subStmt(3, element.size() - 1);
			String text = String.format("for (%s %s : %s) {", importer.getFinalName(clazz, type), item, statement);
			element.replaceTokens(0, element.size(), new Token(TokenTypeEnum.CUSTOM_EXPRESS, text));

		} else if (element.isAssign()) {// var = list.get(0)
			Token token = element.get(0);
			boolean derived = token.attr(Attribute.DERIVED, false);
			IType type = token.attr(Attribute.TYPE);
			if (token.isVariable() && derived) {
				element.add(0, new Token(TokenTypeEnum.TYPE, importer.getFinalName(clazz, type)));
			}

		} else if (element.isIf() || element.isWhile()) {// if s { // while s {
			Statement statement = element.subStmt(1, element.size() - 1);
			IType type = deducer.derive(clazz, statement);
			if (TypeUtils.isString(type)) {
				String text = String.format("StringUtils.isNotEmpty(%s)", statement);
				element.replaceTokens(1, element.size() - 1, new Token(TokenTypeEnum.CUSTOM_EXPRESS, text));
			}

		} else if (element.isPrint() || element.isDebug() || element.isError()) {
			if (element.isPrint()) {
				element.set(0, new Token(TokenTypeEnum.CUSTOM_PREFIX, "logger.info("));
			} else if (element.isDebug()) {
				element.set(0, new Token(TokenTypeEnum.CUSTOM_PREFIX, "logger.debug("));
			} else if (element.isError()) {
				element.set(0, new Token(TokenTypeEnum.CUSTOM_PREFIX, "logger.error("));
			}
			element.add(new Token(TokenTypeEnum.CUSTOM_SUFFIX, ");"));
			if (clazz.getField("logger") == null) {
				clazz.addImport(Logger.class.getName());
				clazz.addImport(LoggerFactory.class.getName());
				Element loggerElement = builder
						.build("Logger logger = LoggerFactory.getLogger(" + clazz.getSimpleName() + ".class)");
				loggerElement.addModifiers(KeywordEnum.PUBLIC.value, KeywordEnum.STATIC.value,
						JavaBuilder.FINAL_KEYWORD);
				IField field = new IField(new ArrayList<>(), loggerElement);
				clazz.fields.add(0, field);
			}
		}
	}

}

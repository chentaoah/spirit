package com.sum.spirit.java.visiter;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.sum.spirit.common.enums.AttributeEnum;
import com.sum.spirit.common.enums.KeywordEnum;
import com.sum.spirit.common.enums.TokenTypeEnum;
import com.sum.spirit.core.clazz.entity.IClass;
import com.sum.spirit.core.clazz.entity.IField;
import com.sum.spirit.core.clazz.entity.IType;
import com.sum.spirit.core.compile.AutoImporter;
import com.sum.spirit.core.compile.action.AbstractElementAction;
import com.sum.spirit.core.compile.deduce.FastDeducer;
import com.sum.spirit.core.compile.deduce.ImportManager;
import com.sum.spirit.core.compile.entity.ElementEvent;
import com.sum.spirit.core.element.ElementBuilderImpl;
import com.sum.spirit.core.element.entity.Element;
import com.sum.spirit.core.element.entity.Statement;
import com.sum.spirit.core.element.entity.Token;
import com.sum.spirit.java.JavaBuilder;
import com.sum.spirit.java.utils.TypeUtils;

@Component
@Order(-40)
public class StatementAction extends AbstractElementAction {

	@Autowired
	public ElementBuilderImpl builder;
	@Autowired
	public FastDeducer deducer;
	@Autowired
	public AutoImporter importer;
	@Autowired
	public ImportManager manager;

	@Override
	public void visit(ElementEvent event) {
		IClass clazz = event.clazz;
		Element element = event.element;

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
					boolean derived = token.attr(AttributeEnum.DERIVED, false);
					if (derived) {
						IType type = token.attr(AttributeEnum.TYPE);
						statement.add(1, new Token(TokenTypeEnum.TYPE, importer.getFinalName(clazz, type)));
					}
				}
			}

		} else if (element.isForIn()) {// for item in list {
			Token item = element.get(1);
			IType type = item.attr(AttributeEnum.TYPE);
			Statement statement = element.subStmt(3, element.size() - 1);
			String text = String.format("for (%s %s : %s) {", importer.getFinalName(clazz, type), item, statement);
			element.replaceTokens(0, element.size(), new Token(TokenTypeEnum.CUSTOM_EXPRESS, text));

		} else if (element.isAssign()) {// var = list.get(0)
			Token token = element.get(0);
			boolean derived = token.attr(AttributeEnum.DERIVED, false);
			IType type = token.attr(AttributeEnum.TYPE);
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
				manager.addImport(clazz, Logger.class.getName());
				manager.addImport(clazz, LoggerFactory.class.getName());
				Element loggerElement = builder.build("Logger logger = LoggerFactory.getLogger(" + clazz.getSimpleName() + ".class)");
				loggerElement.addModifier(JavaBuilder.FINAL_KEYWORD);
				loggerElement.addModifier(KeywordEnum.STATIC.value);
				loggerElement.addModifier(KeywordEnum.PUBLIC.value);
				IField field = new IField(new ArrayList<>(), loggerElement);
				clazz.fields.add(0, field);
			}
		}
	}

}

package com.sum.spirit.core.element.action;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sum.spirit.common.constants.Attribute;
import com.sum.spirit.common.enums.TokenTypeEnum;
import com.sum.spirit.common.pattern.CommonPattern;
import com.sum.spirit.core.api.Lexer;
import com.sum.spirit.core.element.entity.SemanticContext;
import com.sum.spirit.core.element.entity.Statement;
import com.sum.spirit.core.element.entity.Token;

import cn.hutool.core.lang.Assert;

@Component
public class DefaultSemanticParser extends AbstractSemanticParser {

	@Autowired
	public Lexer lexer;

	@Override
	public Token getToken(SemanticContext context, String word) {
		Token token = new Token();

		token.tokenType = getTokenType(word, context.insideType);
		Assert.notNull(token.tokenType, "Token type cannot be null!word:[" + word + "]");

		token.value = getTokenValue(token, word);
		Assert.notNull(token.value, "Token value cannot be null!word:[" + word + "]");

		setTokenAttributes(token, word);

		return token;
	}

	public TokenTypeEnum getTokenType(String word, boolean insideType) {
		if (isPath(word)) {
			return TokenTypeEnum.PATH;

		} else if (isAnnotation(word)) {
			return TokenTypeEnum.ANNOTATION;

		} else if (isKeyword(word)) {
			return TokenTypeEnum.KEYWORD;

		} else if (isOperator(word) && !insideType) {// 类型声明中，一般不包含操作符
			return TokenTypeEnum.OPERATOR;

		} else if (isSeparator(word)) {
			return TokenTypeEnum.SEPARATOR;

		} else if (isType(word) || (insideType && "?".equals(word))) {
			return TokenTypeEnum.TYPE;
		}

		TokenTypeEnum tokenType = getInitTokenType(word);
		if (tokenType != null) {
			return tokenType;
		}

		tokenType = getLiteralTokenType(word);
		if (tokenType != null) {
			return tokenType;
		}

		tokenType = getSubexpressTokenType(word);
		if (tokenType != null) {
			return tokenType;
		}

		if (isVariable(word)) {
			return TokenTypeEnum.VARIABLE;
		}

		tokenType = getAccessTokenType(word);
		if (tokenType != null) {
			return tokenType;
		}

		return null;
	}

	public Object getTokenValue(Token token, String word) {
		if (token.isType()) {
			return getStatement(word, true);

		} else if (token.isArrayInit() || token.isList() || token.isMap() || token.isSubexpress() || token.isInvoke()) {
			return getStatement(word, false);// 拆分数组是为了更好的添加new这个关键字
		}
		return word;
	}

	public Object getStatement(String word, boolean insideType) {
		if (insideType && (!word.contains("<") && !word.contains(">"))) {
			return word;
		}

		// 如果是类型，则直接用尖括号进行拆分，如果是其他，则不使用尖括号进行拆分
		List<String> words = insideType ? lexer.getSubWords(word, '<', '>') : lexer.getSubWords(word, '(', ')', '[', ']', '{', '}');
		List<Token> tokens = null;

		String first = words.get(0);
		// 如果第一个单词是一个前缀的话，则添加前缀
		if (CommonPattern.isPrefix(first)) {
			List<String> subWords = words.subList(1, words.size());
			tokens = getTokens(new SemanticContext(insideType), subWords);
			tokens.add(0, new Token(TokenTypeEnum.PREFIX, first));

		} else {
			tokens = getTokens(new SemanticContext(insideType), words);
		}

		Assert.notNull(tokens, "Tokens cannot be null!");
		return new Statement(tokens);
	}

	public void setTokenAttributes(Token token, String word) {
		if (token.isAnnotation()) {
			token.setAttr(Attribute.SIMPLE_NAME, getAnnotationName(word));

		} else if (token.isArrayInit()) {
			token.setAttr(Attribute.SIMPLE_NAME, getPrefix(word) + "[]");

		} else if (token.isTypeInit()) {
			token.setAttr(Attribute.SIMPLE_NAME, getPrefix(word));

		} else if (token.isCast()) {
			token.setAttr(Attribute.SIMPLE_NAME, getCastType(word));

		} else if (token.isAccess()) {
			token.setAttr(Attribute.MEMBER_NAME, getPrefix(word));
		}
	}

	public String getAnnotationName(String word) {
		if (word.contains("(")) {
			word = word.substring(0, word.indexOf('('));
		}
		return word.substring(word.indexOf('@') + 1);
	}

	public String getPrefix(String word) {
		int start = word.startsWith(".") ? 1 : 0;
		int end = word.length();
		if (word.contains("[")) {
			int index = word.indexOf("[");
			end = index < end ? index : end;
		}
		if (word.contains("(")) {
			int index = word.indexOf("(");
			end = index < end ? index : end;
		}
		return word.substring(start, end);
	}

}

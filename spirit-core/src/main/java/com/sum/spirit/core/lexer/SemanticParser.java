package com.sum.spirit.core.lexer;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sum.spirit.lib.Assert;
import com.sum.spirit.pojo.element.Statement;
import com.sum.spirit.pojo.element.Token;
import com.sum.spirit.pojo.enums.AttributeEnum;
import com.sum.spirit.pojo.enums.TokenTypeEnum;

@Component
public class SemanticParser extends AbsSemanticParser {

	@Autowired
	public Lexer lexer;

	public Token getToken(String word, boolean insideType) {
		Token token = new Token();
		getTokenType(word, token, insideType);
		getTokenValue(word, token);
		getTokenAttributes(word, token);
		return token;
	}

	public void getTokenType(String word, Token token, boolean insideType) {

		if (isPath(word)) {
			token.type = TokenTypeEnum.PATH;

		} else if (isAnnotation(word)) {
			token.type = TokenTypeEnum.ANNOTATION;

		} else if (isKeyword(word)) {
			token.type = TokenTypeEnum.KEYWORD;

		} else if (isOperator(word) && !insideType) {// 类型声明中，一般不包含操作符
			token.type = TokenTypeEnum.OPERATOR;

		} else if (isSeparator(word)) {
			token.type = TokenTypeEnum.SEPARATOR;

		} else if (isType(word) || (insideType && "?".equals(word))) {
			token.type = TokenTypeEnum.TYPE;

		} else if (isInit(word)) {
			token.type = getInitTokenType(word);

		} else if (isValue(word)) {
			token.type = getValueTokenType(word);

		} else if (isSubexpress(word)) {
			token.type = getSubexpressTokenType(word);

		} else if (isVar(word)) {
			token.type = TokenTypeEnum.VAR;

		} else if (isAccess(word)) {
			token.type = getAccessTokenType(word);
		}

		Assert.notNull(token.type, "Token type cannot be null!");
	}

	public void getTokenValue(String word, Token token) {

		if (token.isType()) {
			token.value = getStatement(word, true);

		} else if (token.isArrayInit() || token.isList() || token.isMap() || token.isSubexpress() || token.isInvoke()) {
			// 拆分数组是为了更好的添加new这个关键字
			token.value = getStatement(word, false);

		} else {
			token.value = word;
		}
	}

	public Object getStatement(String word, boolean insideType) {
		if (insideType && (!word.contains("<") && !word.contains(">")))
			return word;
		// 如果是类型，则直接用尖括号进行拆分
		// 如果是其他，则不使用尖括号进行拆分
		List<String> words = insideType ? lexer.getWords(word, '<') : lexer.getWords(word, '(', '[', '{');
		String first = words.get(0);
		List<Token> tokens = null;
		// 如果第一个单词是一个前缀的话，则添加前缀
		if (PREFIX_PATTERN.matcher(first).matches()) {
			tokens = getTokens(words.subList(1, words.size()), insideType);
			tokens.add(0, new Token(TokenTypeEnum.PREFIX, first));
		} else {
			tokens = getTokens(words, insideType);
		}
		Assert.notNull(tokens, "Tokens can not be null!");
		return new Statement(tokens);
	}

	public void getTokenAttributes(String word, Token token) {
		if (token.isAnnotation()) {
			token.setAttribute(AttributeEnum.SIMPLE_NAME, getAnnotationName(word));

		} else if (token.isArrayInit()) {
			token.setAttribute(AttributeEnum.SIMPLE_NAME, getPrefix(word) + "[]");

		} else if (token.isTypeInit()) {
			token.setAttribute(AttributeEnum.SIMPLE_NAME, getPrefix(word));

		} else if (token.isCast()) {
			token.setAttribute(AttributeEnum.SIMPLE_NAME, getCastType(word));

		} else if (token.isAccess()) {
			token.setAttribute(AttributeEnum.MEMBER_NAME, getPrefix(word));
		}
	}

	private String getAnnotationName(String word) {
		if (word.contains("("))
			word = word.substring(0, word.indexOf('('));
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

package com.sum.spirit.core.lexer;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sum.spirit.api.lexer.Lexer;
import com.sum.spirit.api.lexer.SemanticParser;
import com.sum.spirit.lib.Assert;
import com.sum.spirit.pojo.element.Statement;
import com.sum.spirit.pojo.element.Token;
import com.sum.spirit.pojo.enums.TokenEnum;

@Component
public class SemanticParserImpl implements SemanticParser {

	@Autowired
	public Lexer lexer;

	@Override
	public Token getToken(String word, boolean isInsideType) {
		Token token = new Token();
		getTokenType(word, token, isInsideType);
		getTokenValue(word, token);
		getTokenAttas(word, token);
		return token;
	}

	public void getTokenType(String word, Token token, boolean isInsideType) {

		if (isPath(word)) {
			token.type = TokenEnum.PATH;

		} else if (isAnnotation(word)) {
			token.type = TokenEnum.ANNOTATION;

		} else if (isKeyword(word)) {
			token.type = TokenEnum.KEYWORD;

		} else if (isOperator(word) && !isInsideType) {// 类型声明中，一般不包含操作符
			token.type = TokenEnum.OPERATOR;

		} else if (isSeparator(word)) {
			token.type = TokenEnum.SEPARATOR;

		} else if (isType(word) || (isInsideType && "?".equals(word))) {
			token.type = TokenEnum.TYPE;

		} else if (isInit(word)) {
			token.type = getInitTokenType(word);

		} else if (isValue(word)) {
			token.type = getValueTokenType(word);

		} else if (isSubexpress(word)) {
			token.type = getSubexpressTokenType(word);

		} else if (isVar(word)) {
			token.type = TokenEnum.VAR;

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

	public Object getStatement(String word, boolean isInsideType) {
		if (isInsideType && (!word.contains("<") && !word.contains(">")))
			return word;
		// 如果是类型，则直接用尖括号进行拆分
		// 如果是其他，则不使用尖括号进行拆分
		List<String> words = isInsideType ? lexer.getWords(word, '<') : lexer.getWords(word, '(', '[', '{');
		String first = words.get(0);
		List<Token> tokens = null;
		// 如果第一个单词是一个前缀的话，则添加前缀
		if (PREFIX_PATTERN.matcher(first).matches()) {
			tokens = getTokens(words.subList(1, words.size()), isInsideType);
			tokens.add(0, new Token(TokenEnum.PREFIX, first));
		} else {
			tokens = getTokens(words, isInsideType);
		}
		Assert.notNull(tokens, "Tokens can not be null!");
		return new Statement(tokens);
	}

	public void getTokenAttas(String word, Token token) {
		if (token.isAnnotation()) {
			token.setSimpleName(getAnnotationName(word));

		} else if (token.isArrayInit()) {
			token.setSimpleName(getPrefix(word) + "[]");

		} else if (token.isTypeInit()) {
			token.setSimpleName(getPrefix(word));

		} else if (token.isCast()) {
			token.setSimpleName(getCastType(word));

		} else if (token.isAccess()) {
			token.setMemberName(getPrefix(word));
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

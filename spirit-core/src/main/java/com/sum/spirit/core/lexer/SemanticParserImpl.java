package com.sum.spirit.core.lexer;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sum.spirit.api.lexer.Lexer;
import com.sum.spirit.api.lexer.SemanticParser;
import com.sum.spirit.lib.Assert;
import com.sum.spirit.pojo.common.Constants;
import com.sum.spirit.pojo.element.Statement;
import com.sum.spirit.pojo.element.Token;

@Component
public class SemanticParserImpl implements SemanticParser {

	@Autowired
	public Lexer lexer;

	@Override
	public Token getToken(String word) {

		Token token = new Token();
		// 1.get token type
		getTokenType(word, token);
		// 2.get token value
		getTokenValue(word, token);
		// 3.get token Attachments
		getAttachments(word, token);

		return token;
	}

	public void getTokenType(String word, Token token) {

		if (isPath(word)) {
			token.type = Constants.PATH_TOKEN;

		} else if (isAnnotation(word)) {
			token.type = Constants.ANNOTATION_TOKEN;

		} else if (isKeyword(word)) {
			token.type = Constants.KEYWORD_TOKEN;

		} else if (isOperator(word)) {
			token.type = Constants.OPERATOR_TOKEN;

		} else if (isSeparator(word)) {
			token.type = Constants.SEPARATOR_TOKEN;

		} else if (isType(word)) {
			token.type = Constants.TYPE_TOKEN;

		} else if (isInit(word)) {
			token.type = getInitTokenType(word);

		} else if (isValue(word)) {
			token.type = getValueTokenType(word);

		} else if (isSubexpress(word)) {
			token.type = getSubexpressTokenType(word);

		} else if (isVar(word)) {
			token.type = Constants.VAR_TOKEN;

		} else if (isAccess(word)) {
			token.type = getAccessTokenType(word);
		}

		Assert.notNull(token.type, "Token type cannot be null!");
	}

	public void getTokenValue(String word, Token token) {

		if (token.isType()) {
			token.value = getStatement(word, true);

		} else if (token.isArrayInit() || token.isList() || token.isMap() || token.isSubexpress() || token.isInvoke()) {
			token.value = getStatement(word, false);// split array init is to better add the keyword 'new'

		} else {
			token.value = word;
		}
	}

	public Object getStatement(String word, boolean isType) {

		if (isType && (!word.contains("<") && !word.contains(">")))
			return word;

		List<String> words = isType ? lexer.getWords(word, '<') : lexer.getWords(word, '[', '{', '(');
		List<Token> tokens = null;
		String first = words.get(0);
		if (PREFIX_PATTERN.matcher(first).matches()) {
			tokens = getTokens(words.subList(1, words.size()));
			tokens.add(0, new Token(Constants.PREFIX_TOKEN, first));
		} else {
			tokens = getTokens(words);
		}

		if (isType) {
			for (int i = 0; i < tokens.size(); i++) {
				Token token = tokens.get(i);
				if ("?".equals(token.toString())) {
					tokens.set(i, new Token(Constants.TYPE_TOKEN, "?"));

				} else if ("<".equals(token.toString())) {
					tokens.set(i, new Token(Constants.SEPARATOR_TOKEN, "<"));

				} else if (">".equals(token.toString())) {
					tokens.set(i, new Token(Constants.SEPARATOR_TOKEN, ">"));
				}
			}
		}

		Assert.notNull(tokens, "Tokens can not be null!");
		return new Statement(tokens);
	}

	public void getAttachments(String word, Token token) {

		if (token.isArrayInit()) {
			token.setSimpleName(getPrefix(word) + "[]");

		} else if (token.isTypeInit()) {
			token.setSimpleName(getPrefix(word));

		} else if (token.isCast()) {
			token.setSimpleName(getCastType(word));

		} else if (token.isAccess()) {
			token.setMemberName(getPrefix(word));
		}
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

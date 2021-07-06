package com.gitee.spirit.core.element.action;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gitee.spirit.common.constants.Attribute;
import com.gitee.spirit.common.enums.TokenTypeEnum;
import com.gitee.spirit.common.pattern.AccessPattern;
import com.gitee.spirit.common.pattern.CommonPattern;
import com.gitee.spirit.common.pattern.LiteralPattern;
import com.gitee.spirit.common.pattern.TypePattern;
import com.gitee.spirit.core.api.Lexer;
import com.gitee.spirit.core.element.entity.SemanticContext;
import com.gitee.spirit.core.element.entity.Statement;
import com.gitee.spirit.core.element.entity.Token;

import cn.hutool.core.lang.Assert;

@Component
public class DefaultSemanticParser extends AbstractSemanticParser {

    @Autowired
    public Lexer lexer;

    @Override
    public List<Token> getTokens(SemanticContext context, List<String> words) {
        context.words = words;
        List<Token> tokens = new ArrayList<>();
        for (context.index = 0; context.index < words.size(); context.index++) {
            Token token = getToken(context, words.get(context.index));
            tokens.add(token);
        }
        return tokens;
    }

    @Override
    public Token getToken(SemanticContext context, String word) {
        Token token = new Token();

        token.tokenType = getTokenType(context, word);
        Assert.notNull(token.tokenType, "Token type cannot be null!word:[" + word + "]");

        token.value = getTokenValue(token, word);
        Assert.notNull(token.value, "Token value cannot be null!word:[" + word + "]");

        setTokenAttributes(token, word);
        return token;
    }

    public TokenTypeEnum getTokenType(SemanticContext context, String word) {
        if (!context.subStatement) {
            return getCommonTokenType(context, word);
        } else {
            if (context.insideType) {
                if ("<".equals(word) || ">".equals(word)) {
                    return TokenTypeEnum.SEPARATOR;

                } else if ("?".equals(word)) {
                    return TokenTypeEnum.TYPE;
                }
            }
            if (context.index == 0 && CommonPattern.isPrefix(word)) {
                return TokenTypeEnum.PREFIX;
            }
            return getCommonTokenType(context, word);
        }
    }

    public TokenTypeEnum getCommonTokenType(SemanticContext context, String word) {
        if (isAccessPath(word)) {
            return TokenTypeEnum.ACCESS_PATH;

        } else if (isAnnotation(word)) {
            return TokenTypeEnum.ANNOTATION;

        } else if (isKeyword(word)) {
            return TokenTypeEnum.KEYWORD;

        } else if (isOperator(word)) {
            return TokenTypeEnum.OPERATOR;

        } else if (isSeparator(word)) {
            return TokenTypeEnum.SEPARATOR;

        } else if (isType(word)) {
            return TokenTypeEnum.TYPE;
        }

        TokenTypeEnum tokenType = TypePattern.getTokenType(word);
        if (tokenType != null) {
            return tokenType;
        }

        tokenType = LiteralPattern.getTokenType(word);
        if (tokenType != null) {
            return tokenType;
        }

        tokenType = CommonPattern.getSubexpressTokenType(word);
        if (tokenType != null) {
            return tokenType;
        }

        if (isVariable(word)) {
            return TokenTypeEnum.VARIABLE;
        }

        tokenType = AccessPattern.getTokenType(word);
        if (tokenType != null) {
            return tokenType;
        }

        return null;
    }

    public Object getTokenValue(Token token, String word) {
        if (token.isType()) {
            return word.contains("<") || word.contains(">") ? getStatement(true, word) : word;

        } else if (token.isArrayInit() || token.isList() || token.isMap() || token.isSubexpress() || token.isInvoke()) {
            // 拆分数组是为了更好的添加new这个关键字
            return getStatement(false, word);
        }
        return word;
    }

    public Statement getStatement(boolean insideType, String word) {
        List<String> words = insideType ? lexer.getSubWords(word, '<', '>') : lexer.getSubWords(word, '(', ')', '[', ']', '{', '}');
        List<Token> tokens = getTokens(new SemanticContext(true, insideType), words);
        Assert.notNull(tokens, "Tokens cannot be null!");
        return new Statement(tokens);
    }

    public void setTokenAttributes(Token token, String word) {
        if (token.isAnnotation()) {
            token.setAttr(Attribute.SIMPLE_NAME, CommonPattern.getAnnotationName(word));

        } else if (token.isArrayInit()) {
            token.setAttr(Attribute.SIMPLE_NAME, CommonPattern.getPrefix(word) + "[]");

        } else if (token.isTypeInit() || token.isTypeBuilderInit()) {
            token.setAttr(Attribute.SIMPLE_NAME, CommonPattern.getPrefix(word));

        } else if (token.isCast()) {
            token.setAttr(Attribute.SIMPLE_NAME, CommonPattern.getSubexpressValue(word));

        } else if (token.isAccess()) {
            token.setAttr(Attribute.MEMBER_NAME, CommonPattern.getPrefix(word));
        }
    }

}

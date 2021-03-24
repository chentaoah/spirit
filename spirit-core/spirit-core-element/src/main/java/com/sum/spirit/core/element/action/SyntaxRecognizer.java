package com.sum.spirit.core.element.action;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sum.spirit.common.enums.AttributeEnum;
import com.sum.spirit.common.enums.KeywordEnum;
import com.sum.spirit.common.enums.SymbolEnum;
import com.sum.spirit.common.enums.SyntaxEnum;
import com.sum.spirit.core.api.TreeBuilder;
import com.sum.spirit.core.element.entity.Node;
import com.sum.spirit.core.element.entity.Statement;
import com.sum.spirit.core.element.entity.SyntaxTree;
import com.sum.spirit.core.element.entity.Token;

import cn.hutool.core.lang.Assert;

@Component
public class SyntaxRecognizer {

	@Autowired
	public TreeBuilder builder;

	/**
	 * 1.能够不通过语法树获取语法，且后续功能不需要语法树
	 * 2.能够不通过语法树获取语法，但后续功能需要语法树
	 * 3.必须通过语法树获取语法
	 * 
	 * @param tokens
	 * @param statement
	 * @return
	 */
	public Object[] parseSyntax(List<Token> tokens, Statement statement) {
		Assert.notEmpty(tokens, "The tokens cannot be empty!");
		SyntaxEnum syntax = getSyntaxWithoutTree(tokens);
		SyntaxTree syntaxTree = null;
		if (syntax == null) {
			syntax = getSyntaxWithTree(tokens);
			if (syntax != null) {
				syntaxTree = builder.buildTree(statement);
			} else {
				syntaxTree = builder.buildTree(statement);
				syntax = getSyntaxByTree(syntaxTree);
			}
		}
		Assert.notNull(syntax, "The syntax cannot be null!");
		return new Object[] { syntax, syntaxTree };
	}

	public SyntaxEnum getSyntaxWithoutTree(List<Token> tokens) {
		Token firstToken = tokens.get(0);
		Token secondToken = tokens.size() >= 2 ? tokens.get(1) : null;
		Token thirdToken = tokens.size() >= 3 ? tokens.get(2) : null;
		// END
		if (tokens.size() == 1 && "}".equals(firstToken.toString())) {
			return SyntaxEnum.END;
		}
		// ANNOTATION
		if (tokens.size() == 1 && firstToken.isAnnotation()) {
			return SyntaxEnum.ANNOTATION;
		}
		// STRUCT
		if (KeywordEnum.isStruct(firstToken.toString())) {
			return SyntaxEnum.valueOf(firstToken.toString().toUpperCase());
		}
		// DECLARE_FUNC
		if (firstToken.isType()) {
			if (secondToken != null && secondToken.isLocalMethod()) {
				return SyntaxEnum.DECLARE_FUNC;
			}
		}
		// ELSE / CATCH / FINALLY
		if (SymbolEnum.RIGHT_CURLY_BRACKET.value.equals(firstToken.toString())) {
			if (KeywordEnum.ELSE.value.equals(secondToken.toString())) {// } else {
				if (SymbolEnum.LEFT_CURLY_BRACKET.value.equals(thirdToken.toString())) {
					return SyntaxEnum.ELSE;
				}

			} else if (KeywordEnum.CATCH.value.equals(secondToken.toString())) {// } catch Exception x {
				return SyntaxEnum.CATCH;

			} else if (KeywordEnum.FINALLY.value.equals(secondToken.toString())) {// } finally {
				return SyntaxEnum.FINALLY;
			}
		}
		return null;
	}

	public SyntaxEnum getSyntaxWithTree(List<Token> tokens) {
		Token firstToken = tokens.get(0);
		Token secondToken = tokens.size() >= 2 ? tokens.get(1) : null;
		Token thirdToken = tokens.size() >= 3 ? tokens.get(2) : null;
		// LINE
		if (KeywordEnum.isLine(firstToken.toString())) {
			return SyntaxEnum.valueOf(firstToken.toString().toUpperCase());
		}
		// SUPER / THIS
		if (firstToken.isLocalMethod()) {
			String memberName = firstToken.attr(AttributeEnum.MEMBER_NAME);
			if (KeywordEnum.SUPER.value.equals(memberName)) {
				return SyntaxEnum.SUPER;

			} else if (KeywordEnum.THIS.value.equals(memberName)) {
				return SyntaxEnum.THIS;
			}
		}
		// FOR / FOR_IN
		if (KeywordEnum.FOR.value.equals(firstToken.toString())) {
			if (secondToken.isSubexpress()) {// for (i=0; i<10; i++) {
				return SyntaxEnum.FOR;
			}
			if (KeywordEnum.IN.value.equals(thirdToken.toString())) {// for ? in ? {
				return SyntaxEnum.FOR_IN;
			}
			throw new RuntimeException("Unknown syntax!");
		}
		// ELSE_IF
		if (SymbolEnum.RIGHT_CURLY_BRACKET.value.equals(firstToken.toString())) {
			if (KeywordEnum.ELSE.value.equals(secondToken.toString())) {
				if (KeywordEnum.IF.value.equals(thirdToken.toString())) {// } else if ? {
					return SyntaxEnum.ELSE_IF;
				}
			}
			throw new RuntimeException("Unknown syntax!");
		}
		return null;
	}

	public SyntaxEnum getSyntaxByTree(SyntaxTree syntaxTree) {
		return syntaxTree.nodes.size() == 1 ? getSyntaxByOneNode(syntaxTree) : null;
	}

	public SyntaxEnum getSyntaxByOneNode(SyntaxTree syntaxTree) {

		Node firstNode = syntaxTree.nodes.get(0);
		Token firstToken = firstNode.token;

		if (firstToken.isType()) {
			Token nextToken = firstNode.next.token;
			if (nextToken.isVariable()) { // String text
				return SyntaxEnum.DECLARE;
			}

		} else if (firstToken.isAssign()) {
			Token prevToken = firstNode.prev.token;
			if (prevToken.isType()) {// String text = "abc"
				return SyntaxEnum.DECLARE_ASSIGN;

			} else if (prevToken.isVariable()) {// text = "abc"
				return SyntaxEnum.ASSIGN;

			} else if (prevToken.isVisitField()) {// var.text = "abc"
				return SyntaxEnum.FIELD_ASSIGN;
			}

		} else if (firstToken.isLocalMethod()) {// doSomething()
			return SyntaxEnum.INVOKE;

		} else if (firstToken.isVisitMethod()) {// list.get(0)
			return SyntaxEnum.INVOKE;
		}

		return null;
	}

}

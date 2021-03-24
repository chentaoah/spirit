package com.sum.spirit.core.element.action;

import java.util.List;

import org.springframework.stereotype.Component;

import com.sum.spirit.common.enums.AttributeEnum;
import com.sum.spirit.common.enums.KeywordEnum;
import com.sum.spirit.common.enums.SymbolEnum;
import com.sum.spirit.common.enums.SyntaxEnum;
import com.sum.spirit.core.element.entity.Node;
import com.sum.spirit.core.element.entity.SyntaxTree;
import com.sum.spirit.core.element.entity.Token;

import cn.hutool.core.lang.Assert;

@Component
public class SyntaxRecognizer {

	public SyntaxEnum getSimpleSyntax(List<Token> tokens) {

		Assert.notEmpty(tokens, "The tokens cannot be empty!");

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

		// KEYWORDS
		if (KeywordEnum.isStruct(firstToken.toString()) || KeywordEnum.isLine(firstToken.toString())) {
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

		// DECLARE_FUNC
		if (firstToken.isType()) {
			if (secondToken != null && secondToken.isLocalMethod()) {
				return SyntaxEnum.DECLARE_FUNC;
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

		// ELSE / ELSE_IF / CATCH / FINALLY
		if (SymbolEnum.RIGHT_CURLY_BRACKET.value.equals(firstToken.toString())) {
			if (KeywordEnum.ELSE.value.equals(secondToken.toString())) {
				if (thirdToken != null && KeywordEnum.IF.value.equals(thirdToken.toString())) {// } else if ? {
					return SyntaxEnum.ELSE_IF;
				} else {// } else {
					return SyntaxEnum.ELSE;
				}

			} else if (KeywordEnum.CATCH.value.equals(secondToken.toString())) {// } catch Exception x {
				return SyntaxEnum.CATCH;

			} else if (KeywordEnum.FINALLY.value.equals(secondToken.toString())) {// } finally {
				return SyntaxEnum.FINALLY;
			}
			throw new RuntimeException("Unknown syntax!");
		}

		return null;
	}

	public boolean needBuildTree(SyntaxEnum syntax) {
		if (syntax == null) {
			return true;
		} else {
			if (KeywordEnum.isStruct(syntax.toString().toLowerCase())) {
				return false;
			} else if (syntax == SyntaxEnum.DECLARE_FUNC) {
				return false;
			}
			return true;
		}
	}

	public SyntaxEnum getSyntax(SyntaxTree syntaxTree) {
		SyntaxEnum syntax = null;
		if (syntaxTree.nodes.size() == 1) {
			syntax = getSyntaxByOneNode(syntaxTree);
		}
		Assert.notNull(syntax, "The syntax cannot be null!");
		return syntax;
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

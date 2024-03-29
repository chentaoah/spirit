package com.gitee.spirit.core.element.action;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gitee.spirit.common.constants.Attribute;
import com.gitee.spirit.common.enums.KeywordEnum;
import com.gitee.spirit.common.enums.SyntaxEnum;
import com.gitee.spirit.core.api.SyntaxParser;
import com.gitee.spirit.core.api.TreeBuilder;
import com.gitee.spirit.core.element.entity.Node;
import com.gitee.spirit.core.element.entity.Statement;
import com.gitee.spirit.core.element.entity.SyntaxResult;
import com.gitee.spirit.core.element.entity.SyntaxTree;
import com.gitee.spirit.core.element.entity.Token;

import cn.hutool.core.lang.Assert;

@Component
public class DefaultSyntaxParser implements SyntaxParser {

	@Autowired
	public TreeBuilder builder;

	@Override
	public SyntaxResult parseSyntax(List<Token> tokens, Statement statement) {
		Assert.notEmpty(tokens, "The tokens cannot be empty!");

		// 1.能够不通过语法树获取语法，且后续功能不需要语法树
		SyntaxEnum syntax = getSyntaxWithoutTree(tokens);
		if (syntax != null) {
			return new SyntaxResult(syntax, null);
		}

		// 2.能够不通过语法树获取语法，但后续功能需要语法树
		syntax = getSyntaxWithTree(tokens);
		if (syntax != null) {
			SyntaxTree syntaxTree = builder.buildTree(statement);
			return new SyntaxResult(syntax, syntaxTree);
		}

		// 3.必须通过语法树获取语法
		SyntaxTree syntaxTree = builder.buildTree(statement);
		syntax = getSyntaxByTree(syntaxTree);
		Assert.notNull(syntax, "The syntax cannot be null!");

		return new SyntaxResult(syntax, syntaxTree);
	}

	@Override
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
		if ("}".equals(firstToken.toString())) {
			if ("else".equals(secondToken.toString())) {// } else {
				if ("{".equals(thirdToken.toString())) {
					return SyntaxEnum.ELSE;
				}

			} else if ("catch".equals(secondToken.toString())) {// } catch Exception x {
				return SyntaxEnum.CATCH;

			} else if ("finally".equals(secondToken.toString())) {// } finally {
				return SyntaxEnum.FINALLY;
			}
		}
		return null;
	}

	@Override
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
			String memberName = firstToken.attr(Attribute.MEMBER_NAME);
			if ("super".equals(memberName)) {
				return SyntaxEnum.SUPER;

			} else if ("this".equals(memberName)) {
				return SyntaxEnum.THIS;
			}
		}
		// FOR / FOR_IN
		if ("for".equals(firstToken.toString())) {
			if (secondToken.isSubexpress()) {// for (i=0; i<10; i++) {
				return SyntaxEnum.FOR;
			}
			if ("in".equals(thirdToken.toString())) {// for ? in ? {
				return SyntaxEnum.FOR_IN;
			}
			throw new RuntimeException("Unknown syntax!");
		}
		// ELSE_IF
		if ("}".equals(firstToken.toString())) {
			if ("else".equals(secondToken.toString())) {
				if ("if".equals(thirdToken.toString())) {// } else if ? {
					return SyntaxEnum.ELSE_IF;
				}
			}
			throw new RuntimeException("Unknown syntax!");
		}
		return null;
	}

	@Override
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
			Token nextToken = firstNode.next.token;
			if (nextToken.isSeparator() && "{".equals(nextToken.toString())) {// var = {
				return SyntaxEnum.STRUCT_ASSIGN;

			} else if (prevToken.isType()) {// String text = "abc"
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

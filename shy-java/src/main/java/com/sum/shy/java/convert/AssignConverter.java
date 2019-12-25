package com.sum.shy.java.convert;

import java.util.ArrayList;
import java.util.List;

import com.sum.shy.core.analyzer.AbsSyntaxTree;
import com.sum.shy.core.clazz.impl.CtClass;
import com.sum.shy.core.clazz.impl.CtMethod;
import com.sum.shy.core.entity.Constants;
import com.sum.shy.core.entity.Line;
import com.sum.shy.core.entity.Node;
import com.sum.shy.core.entity.Stmt;
import com.sum.shy.core.entity.Token;

public class AssignConverter extends DefaultConverter {

	@Override
	public Stmt convert(CtClass clazz, CtMethod method, String indent, String block, Line line, Stmt stmt) {
		// 查找==节点
		Node node = AbsSyntaxTree.grow(stmt);
		List<Node> nodes = new ArrayList<>();
		findEqualJudgment(node, nodes);
		// 一般的转换
		stmt = convertStmt(clazz, stmt);
		// 添加类型声明
		Token token = stmt.getToken(0);
		if (token.isVar() && !token.isDeclaredAtt()) {
			stmt.tokens.add(0, new Token(Constants.TYPE_TOKEN, token.getTypeAtt(), null));
		}
		return stmt;

	}

	private void findEqualJudgment(Node node, List<Node> nodes) {
		// 如果当前节点就是
		Token token = node.token;
		if (token.isOperator() && "==".equals(token.value)) {

		}
		// 查找子节点
		if (node.left != null)
			findEqualJudgment(node.left, nodes);
		if (node.right != null)
			findEqualJudgment(node.right, nodes);

	}

}

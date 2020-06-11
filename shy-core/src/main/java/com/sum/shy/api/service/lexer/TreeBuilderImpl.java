package com.sum.shy.api.service.lexer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.sum.shy.api.lexer.TreeBuilder;
import com.sum.shy.common.Constants;
import com.sum.shy.common.Symbol;
import com.sum.shy.common.SymbolTable;
import com.sum.shy.element.Node;
import com.sum.shy.element.Statement;
import com.sum.shy.element.Token;

public class TreeBuilderImpl implements TreeBuilder {

	@Override
	public List<Token> build(List<Token> tokens) {

		if (tokens.size() == 1)
			return tokens;

		tokens = new ArrayList<>(tokens);

		for (int i = 0; i < tokens.size(); i++) {
			Token token = tokens.get(i);
			if (token.canSplit()) {
				token = token.copy();
				Statement subStmt = token.getValue();
				subStmt.tokens = build(subStmt.tokens);
				tokens.set(i, token);
			}
		}

		return getNodeByGraph(tokens);

	}

	@Override
	public void markTreeId(List<Token> tokens) {
		int count = 0;
		for (Token token : tokens) {
			if (token.isNode())
				markTreeId(count++ + "", token.getValue());
		}
	}

	public void markTreeId(String treeId, Node node) {

		node.token.setTreeId(treeId);

		if (node.left != null)
			markTreeId(treeId + "-" + "0", node.left);

		if (node.right != null)
			markTreeId(treeId + "-" + "1", node.right);

		if (node.token.canSplit()) {
			Statement stmt = node.token.getValue();
			markTreeId(stmt.tokens);
		}

	}

	public List<Token> getNodeByGraph(List<Token> tokens) {

		List<?>[] graph = new List<?>[12];

		for (int i = 0; i < tokens.size(); i++) {
			Token currToken = tokens.get(i);
			Token nextToken = i + 1 < tokens.size() ? tokens.get(i + 1) : null;
			int priority = -1;
			int operand = -1;

			if (currToken.isType()) {
				if (nextToken != null && (nextToken.isVar() || nextToken.isLocalMethod())) {
					priority = 55;
					operand = Symbol.RIGHT;
				}

			} else if (currToken.isFluent()) {
				priority = 50;
				operand = Symbol.LEFT;

			} else if (currToken.isOperator()) {
				String value = currToken.toString();
				Symbol symbol = SymbolTable.getSymbol(value);
				priority = symbol.priority;
				operand = symbol.operand;

			} else if (currToken.isCast()) {
				priority = 35;
				operand = Symbol.RIGHT;

			} else if (currToken.isInstanceof()) {
				priority = 20;
				operand = Symbol.BINARY;
			}

			if (priority > 0) {
				int index = 12 - priority / 5;
				if (graph[index] == null)
					graph[index] = new ArrayList<Integer>();
				@SuppressWarnings("unchecked")
				List<Integer> list = (List<Integer>) graph[index];
				list.add(i);
				currToken.setOperand(operand);
			}
		}

		for (int i = 0; i < graph.length; i++) {
			@SuppressWarnings("unchecked")
			List<Integer> indexs = (List<Integer>) graph[i];

			if (indexs == null)
				continue;

			for (int j = 0; j < indexs.size(); j++) {
				int index = indexs.get(j);

				Token currToken = tokens.get(index);

				resetOperandIfMultiple(tokens, index, currToken);
				if (currToken.getOperand() == Symbol.MULTIPLE)
					throw new RuntimeException("Unable to know the operand of the symbol!");

				Node node = new Node(currToken);

				if (currToken.getOperand() == Symbol.LEFT || currToken.getOperand() == Symbol.BINARY)
					node.left = removeLeft(tokens, index);

				if (currToken.getOperand() == Symbol.RIGHT || currToken.getOperand() == Symbol.BINARY)
					node.right = removeRight(tokens, index);

				tokens.set(index, new Token(Constants.NODE_TOKEN, node));
			}
		}

		return tokens.stream().filter((token) -> {
			return token != null;
		}).collect(Collectors.toList());

	}

	public void resetOperandIfMultiple(List<Token> tokens, int index, Token currToken) {
		if (currToken.getOperand() == Symbol.MULTIPLE) {

			Token lastToken = getLastToken(tokens, index);
			Token nextToken = getNextToken(tokens, index);

			String value = currToken.toString();
			if ("++".equals(value) || "--".equals(value)) {
				if (lastToken != null && (lastToken.isVar() || lastToken.isNode())) {
					currToken.setOperand(Symbol.LEFT);

				} else if (nextToken != null && (nextToken.isVar() || nextToken.isNode())) {
					currToken.setOperand(Symbol.RIGHT);
				}

			} else if ("-".equals(value)) {// 100 + (-10) // var = -1
				if (lastToken != null && (lastToken.isNumber() || lastToken.isVar() || lastToken.isNode())) {
					currToken.setOperand(Symbol.BINARY);

				} else {
					currToken.setOperand(Symbol.RIGHT);
				}
			}
		}
	}

	public Token getLastToken(List<Token> tokens, int index) {
		for (int i = index - 1; i >= 0; i--) {
			Token token = tokens.get(i);
			if (token != null)
				return token;
		}
		return null;
	}

	public Token getNextToken(List<Token> tokens, int index) {
		for (int i = index + 1; i < tokens.size(); i++) {
			Token token = tokens.get(i);
			if (token != null)
				return token;
		}
		return null;
	}

	public Node removeLeft(List<Token> tokens, int index) {
		for (int i = index - 1; i >= 0; i--) {
			Token token = tokens.get(i);
			if (token != null) {
				tokens.set(i, null);
				return getNode(token);
			}
		}
		throw new RuntimeException("No available token found!");
	}

	public Node removeRight(List<Token> tokens, int index) {
		for (int i = index + 1; i < tokens.size(); i++) {
			Token token = tokens.get(i);
			if (token != null) {
				tokens.set(i, null);
				return getNode(token);
			}
		}
		throw new RuntimeException("No available token found!");
	}

	public Node getNode(Token token) {
		return token.isNode() ? token.getValue() : new Node(token);
	}

}

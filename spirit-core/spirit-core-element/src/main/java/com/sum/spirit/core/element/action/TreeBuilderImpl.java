package com.sum.spirit.core.element.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.sum.spirit.common.enums.AttributeEnum;
import com.sum.spirit.common.enums.SymbolEnum;
import com.sum.spirit.common.enums.SymbolEnum.OperandEnum;
import com.sum.spirit.common.utils.Lists;
import com.sum.spirit.core.element.entity.Node;
import com.sum.spirit.core.element.entity.SyntaxTree;
import com.sum.spirit.core.element.entity.Token;
import com.sum.spirit.core.element.utils.PriorityNode;
import com.sum.spirit.core.element.utils.PriorityNode.PriorityComparator;

@Component
public class TreeBuilderImpl extends AbstractTreeBuilder {

	@Override
	public List<Node> buildNodes(List<Token> tokens) {
		final List<Node> nodes = new ArrayList<>();
		Lists.visit(tokens, (index, token) -> {
			if (token.canSplit()) {// 嵌套语法树
				SyntaxTree syntaxTree = buildTree(token.getValue());
				token = new Token(token.tokenType, syntaxTree, token.attributes);
			}
			nodes.add(new Node(index, token));
		});
		Queue<PriorityNode<Integer>> queue = getPriorityQueue(tokens);
		List<Node> newNodes = gatherNodesByQueue(queue, nodes);
		return newNodes;
	}

	public Queue<PriorityNode<Integer>> getPriorityQueue(List<Token> tokens) {
		Queue<PriorityNode<Integer>> queue = new PriorityQueue<>(16, new PriorityComparator<Integer>());
		for (int index = 0; index < tokens.size(); index++) {
			Token currentToken = tokens.get(index);
			Token nextToken = index + 1 < tokens.size() ? tokens.get(index + 1) : null;
			int priority = -1;
			OperandEnum operand = null;

			if (currentToken.isVisit()) {
				priority = 55;
				operand = OperandEnum.LEFT;

			} else if (currentToken.isType()) {
				if (nextToken != null && (nextToken.isVariable() || nextToken.isLocalMethod())) {
					priority = 50;
					operand = OperandEnum.RIGHT;
				}

			} else if (currentToken.isOperator()) {
				String value = currentToken.toString();
				SymbolEnum symbol = SymbolEnum.getOperator(value);
				priority = symbol.priority;
				operand = symbol.operand;

			} else if (currentToken.isCast()) {
				priority = 35;
				operand = OperandEnum.RIGHT;

			} else if (currentToken.isInstanceof()) {
				priority = 20;
				operand = OperandEnum.BINARY;
			}

			if (priority > 0) {
				queue.add(new PriorityNode<Integer>(priority, index));
				currentToken.setAttr(AttributeEnum.OPERAND, operand);
			}
		}
		return queue;
	}

	public List<Node> gatherNodesByQueue(Queue<PriorityNode<Integer>> queue, List<Node> nodes) {
		while (!queue.isEmpty()) {
			PriorityNode<Integer> priorityNode = queue.poll();
			int index = priorityNode.item;
			Node node = nodes.get(index);
			Token currentToken = node.token;
			resetOperandIfMultiple(nodes, index, currentToken);// 如果是多义的操作符，则进行判断后，确定真正的操作数

			OperandEnum operandEnum = currentToken.attr(AttributeEnum.OPERAND);
			if (operandEnum == OperandEnum.LEFT) {
				Node lastNode = Lists.findOne(nodes, index - 1, -1, Objects::nonNull);
				node.prev = lastNode;
				nodes.set(lastNode.index, null);

			} else if (operandEnum == OperandEnum.RIGHT) {
				Node nextNode = Lists.findOne(nodes, index + 1, nodes.size(), Objects::nonNull);
				node.next = nextNode;
				nodes.set(nextNode.index, null);

			} else if (operandEnum == OperandEnum.BINARY) {
				Node lastNode = Lists.findOne(nodes, index - 1, -1, Objects::nonNull);
				node.prev = lastNode;
				nodes.set(lastNode.index, null);

				Node nextNode = Lists.findOne(nodes, index + 1, nodes.size(), Objects::nonNull);
				node.next = nextNode;
				nodes.set(nextNode.index, null);

			} else {
				throw new RuntimeException("Unable to know the operand of the symbol!");
			}
		}
		return nodes.stream().filter(Objects::nonNull).collect(Collectors.toList());// 过滤掉所有null
	}

	public void resetOperandIfMultiple(List<Node> nodes, int index, Token currentToken) {
		OperandEnum operandEnum = currentToken.attr(AttributeEnum.OPERAND);
		if (operandEnum == OperandEnum.MULTIPLE) {
			Node lastNode = Lists.findOne(nodes, index - 1, -1, Objects::nonNull);
			String value = currentToken.toString();
			if (SymbolEnum.SUBTRACT.value.equals(value)) {// 100 + (-10) // var = -1
				if (lastNode != null) {
					if (lastNode.isMounted() || (lastNode.token.isNumber() || lastNode.token.isVariable())) {
						currentToken.setAttr(AttributeEnum.OPERAND, OperandEnum.BINARY);
					} else {
						currentToken.setAttr(AttributeEnum.OPERAND, OperandEnum.RIGHT);
					}
				} else {
					currentToken.setAttr(AttributeEnum.OPERAND, OperandEnum.RIGHT);
				}
			}
		}
	}

}

package com.sum.spirit.core.build;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import org.springframework.stereotype.Component;

import com.sum.spirit.pojo.element.impl.Node;
import com.sum.spirit.pojo.element.impl.SyntaxTree;
import com.sum.spirit.pojo.element.impl.Token;
import com.sum.spirit.pojo.enums.AttributeEnum;
import com.sum.spirit.pojo.enums.SymbolEnum;
import com.sum.spirit.pojo.enums.SymbolEnum.OperandEnum;

@Component
public class TreeBuilder extends AbstractTreeBuilder {

	@Override
	public List<Node> buildNodes(List<Token> tokens) {
		List<Node> nodes = new LinkedList<>();
		// index是为了让node记住自己的索引位置
		for (int index = 0; index < tokens.size(); index++) {
			Token token = tokens.get(index);
			if (token.canSplit()) {
				// 1.设置语法树
				SyntaxTree syntaxTree = buildTree(token.getValue());
				// 拷贝一个新的token
				Token newToken = new Token(token.tokenType, syntaxTree, token.attributes);
				nodes.add(new Node(index, newToken));

			} else {
				// 2.设置为token
				nodes.add(new Node(index, token));
			}
		}
		// 构建图谱
		List<Integer>[] graph = getGraphByTokens(tokens);
		// 通过图谱快速建立二叉树
		gatherNodesByGraph(graph, nodes);

		return nodes;
	}

	public List<Integer>[] getGraphByTokens(List<Token> tokens) {
		// 图谱
		@SuppressWarnings("unchecked")
		List<Integer>[] graph = (List<Integer>[]) new List<?>[12];
		// 将节点按照优先级添加到图谱中
		for (int i = 0; i < tokens.size(); i++) {
			// 获取当前节点的内容
			Token currentToken = tokens.get(i);
			// 获取下一个节点
			Token nextToken = i + 1 < tokens.size() ? tokens.get(i + 1) : null;
			// 优先级和操作数
			int priority = -1;

			OperandEnum operand = null;

			if (currentToken.isType()) {
				if (nextToken != null && (nextToken.isVariable() || nextToken.isLocalMethod())) {
					priority = 55;
					operand = OperandEnum.RIGHT;
				}

			} else if (currentToken.isFluent()) {
				priority = 50;
				operand = OperandEnum.LEFT;

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

			// 如果优先级大于0，则添加到图谱中
			if (priority > 0) {
				int index = 12 - priority / 5;
				if (graph[index] == null) {
					graph[index] = new ArrayList<Integer>();
				}
				graph[index].add(i);
				// 记录操作数
				currentToken.setAttr(AttributeEnum.OPERAND, operand);
			}
		}
		return graph;
	}

	public void gatherNodesByGraph(List<Integer>[] graph, List<Node> nodes) {
		for (List<Integer> indexs : graph) {
			if (indexs == null) {
				continue;
			}
			for (int index : indexs) {
				ListIterator<Node> iterator = nodes.listIterator();
				while (iterator.hasNext()) {
					// 这里注意，next已经将索向后推进了
					Node node = iterator.next();
					if (node.index != index) {
						continue;
					}
					// 获取当前节点的token
					Token currentToken = node.token;
					// 如果是多义的操作符，则进行判断后，确定真正的操作数
					resetOperandIfMultiple(iterator, currentToken);

					OperandEnum operandEnum = currentToken.attr(AttributeEnum.OPERAND);
					if (operandEnum == OperandEnum.LEFT) {
						// 使用迭代器，必须非常小心nextIndex
						iterator.previous();
						node.prev = iterator.previous();
						iterator.remove();

					} else if (operandEnum == OperandEnum.RIGHT) {
						node.next = iterator.next();
						iterator.remove();

					} else if (operandEnum == OperandEnum.BINARY) {
						iterator.previous();
						node.prev = iterator.previous();
						iterator.remove();

						iterator.next();
						node.next = iterator.next();
						iterator.remove();

					} else {
						throw new RuntimeException("Unable to know the operand of the symbol!");
					}
					break;
				}
			}
		}
	}

	public void resetOperandIfMultiple(ListIterator<Node> iterator, Token currentToken) {
		OperandEnum operandEnum = currentToken.attr(AttributeEnum.OPERAND);
		if (operandEnum == OperandEnum.MULTIPLE) {
			Node lastNode = getLastNode(iterator);
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

	public Node getLastNode(ListIterator<Node> iterator) {
		Node node = null;
		if (iterator.hasPrevious()) {
			iterator.previous();
			if (iterator.hasPrevious()) {
				node = iterator.previous();
				iterator.next();
			}
			iterator.next();
		}
		return node;
	}

	public Node getNextNode(ListIterator<Node> iterator) {
		Node node = null;
		if (iterator.hasNext()) {
			node = iterator.next();
			iterator.previous();
		}
		return node;
	}

}

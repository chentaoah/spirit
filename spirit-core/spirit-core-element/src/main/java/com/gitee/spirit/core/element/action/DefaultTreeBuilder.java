package com.gitee.spirit.core.element.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.gitee.spirit.common.constants.Attribute;
import com.gitee.spirit.common.entity.PriorityNode;
import com.gitee.spirit.common.entity.PriorityNode.PriorityComparator;
import com.gitee.spirit.common.enums.OperatorEnum;
import com.gitee.spirit.common.enums.OperatorEnum.OperandEnum;
import com.gitee.spirit.common.utils.ListUtils;
import com.gitee.spirit.core.element.entity.Node;
import com.gitee.spirit.core.element.entity.SyntaxTree;
import com.gitee.spirit.core.element.entity.Token;

@Component
public class DefaultTreeBuilder extends AbstractTreeBuilder {

    @Override
    public List<Node> buildNodes(List<Token> tokens) {
        final List<Node> nodes = new ArrayList<>();
        ListUtils.visitAll(tokens, (index, token) -> {
            if (token.hasSubStmt()) {
                SyntaxTree syntaxTree = buildTree(token.getValue());
                token = new Token(token.tokenType, syntaxTree, token.attributes);
            }
            nodes.add(new Node(index, token));
        });
        Queue<PriorityNode<Integer>> queue = getPriorityQueue(tokens);
        return gatherNodesByQueue(queue, nodes);
    }

    public Queue<PriorityNode<Integer>> getPriorityQueue(List<Token> tokens) {
        Queue<PriorityNode<Integer>> queue = new PriorityQueue<>(16, new PriorityComparator<>());
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
                OperatorEnum operator = OperatorEnum.getOperator(currentToken.toString());
                priority = operator.priority;
                operand = operator.operand;

            } else if (currentToken.isCast()) {
                priority = 35;
                operand = OperandEnum.RIGHT;

            } else if (currentToken.isInstanceof()) {
                priority = 20;
                operand = OperandEnum.BINARY;
            }

            if (priority > 0) {
                queue.add(new PriorityNode<>(priority, index));
                currentToken.setAttr(Attribute.OPERAND, operand);
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
            // 如果是多义的操作符，则进行判断后，确定真正的操作数
            resetOperandIfMultiple(nodes, index, currentToken);

            OperandEnum operandEnum = currentToken.attr(Attribute.OPERAND);
            if (operandEnum == OperandEnum.LEFT) {
                Node lastNode = ListUtils.findOneByIndex(nodes, index - 1, -1, Objects::nonNull);
                node.prev = lastNode;
                nodes.set(Objects.requireNonNull(lastNode).index, null);

            } else if (operandEnum == OperandEnum.RIGHT) {
                Node nextNode = ListUtils.findOneByIndex(nodes, index + 1, nodes.size(), Objects::nonNull);
                node.next = nextNode;
                nodes.set(Objects.requireNonNull(nextNode).index, null);

            } else if (operandEnum == OperandEnum.BINARY) {
                Node lastNode = ListUtils.findOneByIndex(nodes, index - 1, -1, Objects::nonNull);
                node.prev = lastNode;
                nodes.set(Objects.requireNonNull(lastNode).index, null);

                Node nextNode = ListUtils.findOneByIndex(nodes, index + 1, nodes.size(), Objects::nonNull);
                node.next = nextNode;
                nodes.set(Objects.requireNonNull(nextNode).index, null);

            } else {
                throw new RuntimeException("Unable to know the operand of the symbol!");
            }
        }
        return nodes.stream().filter(Objects::nonNull).collect(Collectors.toList());// 过滤掉所有null
    }

    public void resetOperandIfMultiple(List<Node> nodes, int index, Token currentToken) {
        OperandEnum operandEnum = currentToken.attr(Attribute.OPERAND);
        if (operandEnum == OperandEnum.MULTIPLE) {
            Node lastNode = ListUtils.findOneByIndex(nodes, index - 1, -1, Objects::nonNull);
            String value = currentToken.toString();
            if ("-".equals(value)) {// 100 + (-10) // var = -1
                if (lastNode != null) {
                    if (lastNode.isMounted() || (lastNode.token.isNumber() || lastNode.token.isVariable())) {
                        currentToken.setAttr(Attribute.OPERAND, OperandEnum.BINARY);
                    } else {
                        currentToken.setAttr(Attribute.OPERAND, OperandEnum.RIGHT);
                    }
                } else {
                    currentToken.setAttr(Attribute.OPERAND, OperandEnum.RIGHT);
                }
            }
        }
    }

}

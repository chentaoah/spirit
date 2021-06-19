package com.gitee.spirit.core.element.utils;

import java.util.List;

import com.gitee.spirit.core.element.entity.Node;

public class NodeVisiter {

	public static Object forEachNode(List<Node> nodes, Consumer<Node> consumer) {
		for (Node node : nodes) {
			Object result = forEachNode(node, consumer);
			if (result != null) {
				return result;
			}
		}
		throw new RuntimeException("There is no return value!");
	}

	@SuppressWarnings("unchecked")
	public static Object forEachNode(Node node, Consumer<Node> consumer) {
		Object result = consumer.accept(node);
		return result != null && result instanceof List ? forEachNode((List<Node>) result, consumer) : result;
	}

	public static interface Consumer<T> {
		Object accept(T t);
	}

}

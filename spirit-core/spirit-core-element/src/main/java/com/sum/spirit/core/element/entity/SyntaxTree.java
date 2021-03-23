package com.sum.spirit.core.element.entity;

import java.util.List;

public class SyntaxTree {

	public List<Node> nodes;

	public SyntaxTree(List<Node> nodes) {
		this.nodes = nodes;
	}

	@Override
	public String toString() {
		throw new RuntimeException("The method toString() should not be called");
	}

}

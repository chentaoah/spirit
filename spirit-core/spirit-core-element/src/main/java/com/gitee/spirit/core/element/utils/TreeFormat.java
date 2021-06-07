package com.gitee.spirit.core.element.utils;

//import java.util.ArrayList;
//import java.util.List;
//
//import org.apache.commons.lang3.StringUtils;
//
//import com.gitee.spirit.common.enums.AttributeEnum;
//import com.gitee.spirit.common.utils.LineUtils;
//import com.gitee.spirit.core.element.entity.Line;
//import com.gitee.spirit.core.element.entity.Node;
//import com.gitee.spirit.core.element.entity.SyntaxTree;
//import com.gitee.spirit.core.element.entity.Token;

public class TreeFormat {

//	public String debug() {
//		// 生成打印的面板
//		List<Line> lines = new ArrayList<>();
//		for (int i = 0; i < 20; i++) {
//			lines.add(new Line(i + 1, LineUtils.getSpaces(150)));
//		}
//
//		// 遍历节点，并构造树结构
//		buildTree(lines, 0, nodes);
//
//		// 打印
//		StringBuilder builder = new StringBuilder();
//		for (Line line : lines) {
//			if (!line.isIgnore()) {
//				builder.append(line.text + "\n");
//			}
//		}
//		return builder.toString();
//	}
//
//	public void buildTree(List<Line> lines, int depth, List<Node> nodes) {
//		for (Node node : nodes) {
//			buildTree(lines, depth, "", node);
//		}
//	}
//
//	public void buildTree(List<Line> lines, int depth, String separator, Node node) {
//
//		Token token = node.token;
//		int position = token.attr(AttributeEnum.POSITION);
//		int length = token.attr(AttributeEnum.LENGTH);
//
//		// 尽量上上面的分割符在中间,奇数在中间,偶数在中间偏左一个
//		if (StringUtils.isNotEmpty(separator)) {
//			println(lines, depth - 1, position + length / 2 + length % 2 - 1, separator);
//		}
//
//		// 如果该节点是语法树
//		if (node.canSplit()) {
//			SyntaxTree syntaxTree = token.getValue();
//			buildTree(lines, depth, syntaxTree.nodes);
//
//		} else {
//			println(lines, depth, position, token.toString());
//		}
//
//		// 左边节点
//		if (node.prev != null) {
//			buildTree(lines, depth + 2, "/", node.prev);
//		}
//
//		// 右边节点
//		if (node.next != null) {
//			buildTree(lines, depth + 2, "\\", node.next);
//		}
//	}
//
//	public void println(List<Line> lines, int depth, int position, String text) {
//		Line line = lines.get(depth);
//		StringBuilder builder = new StringBuilder(line.text);
//		builder.replace(position, position + text.length(), text);
//		line.text = builder.toString();
//	}
}

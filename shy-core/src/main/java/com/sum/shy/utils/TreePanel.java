package com.sum.shy.utils;

import java.util.ArrayList;
import java.util.List;

import com.sum.shy.api.service.ElementBuilderImpl;
import com.sum.shy.element.Element;
import com.sum.shy.element.Line;
import com.sum.shy.element.Node;
import com.sum.shy.element.Stmt;
import com.sum.shy.element.Token;
import com.sum.shy.lib.StringUtils;

public class TreePanel {

	public List<Line> lines = new ArrayList<>();

	public TreePanel() {
		for (int i = 0; i < 20; i++)
			lines.add(new Line(i + 1, LineUtils.getSpaces(150)));
	}

	public void debug(Element element) {
		markPosition(0, element.stmt);
		buildTree(0, "", element.tree.tokens);
		System.out.println(toString());
	}

	public void markPosition(int position, Stmt stmt) {
		List<Token> tokens = stmt.format();// 获取到插入空格后
		for (int i = 0; i < tokens.size(); i++) {
			Token token = tokens.get(i);
			token.setPosition(position);
			if (token.canVisit())
				markPosition(position, token.getStmt());
			position += token.toString().length();
		}
	}

	public void buildTree(int depth, String separator, List<Token> tokens) {
		for (int i = 0; i < tokens.size(); i++) {
			Token token = tokens.get(i);
			if (token.canVisit()) {// 有子节点先打印子节点
				buildTree(depth, separator, token.getStmt().tokens);
			} else {
				if (!token.isNode()) {// 如果不是一个聚合的节点，则直接打印
					print(depth, token.getPosition(), token.toString());
				} else {// 如果是聚合以后的节点，则构建树结构
					buildTree(depth, separator, token.getNode());
				}
			}
		}
	}

	public void buildTree(int depth, String separator, Node node) {

		if (node == null)
			return;

		Token token = node.token;
		int position = token.getPosition();// 节点位置
		String text = token.toString(); // 节点内容
		// 获取上一行
		if (StringUtils.isNotEmpty(separator))
			print(depth - 1, position + text.length() / 2 + text.length() % 2 - 1, separator);// 尽量上上面的分割符在中间,奇数在中间,偶数在中间偏左一个

		if (token.canVisit()) {// 如果有语句，则先打印子语句
			buildTree(depth, "", token.getStmt().tokens);
		} else {
			print(depth, position, text);
		}

		// 左边节点
		buildTree(depth + 2, "/", node.left);
		// 右边节点
		buildTree(depth + 2, "\\", node.right);

	}

	public void print(int number, int position, String text) {
		Line line = lines.get(number);
		StringBuilder sb = new StringBuilder(line.text);
		sb.replace(position, position + text.length(), text);
		line.text = sb.toString();
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (Line line : lines) {
			if (!line.isIgnore())
				sb.append(line.text + "\n");
		}
		return sb.toString();
	}

	/**
	 * 测试案例
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

//		String text = "var =(x>100) ? true || (int)x++ > 100.0 && list.size((x+y>0))>100 && obj instanceof Object || !((x++) > 0 || y < 100)";
//		String text = "((x+1>0)&&(y<100)) && s==\"test\"";
//		String text = "(int)var + 1000 + list.size().toString()";
//		String text = "(int)obj.toString().length+ 100";
//		String text = "list.get(1)";
//		String text = "map={\"key1\":100}.getSize().toString()+100>0";
//		String text = "b= x >= 100";
//		String text = "b=((String)list.get(1)).length() <= 100";
//		String text = "b=x>=((String)list.get(1)).length().get().set()";
//		String text = "(x >= 0 && y<100)";
		String text = "b=list.get(1) != null";
//		String text = "print \"test print\", list.get(1)==\"test\", ((String)list.get(1)).length() <= 100";
//		String text = "for i=0; i<list.size(); i++ {";
//		String text = "sequence = (sequence + 1) & sequenceMask";

		Element element = new ElementBuilderImpl().buildElement(new Line(text));
		System.out.println(element.stmt.debug());
		System.out.println(element.stmt.toString());
		new TreePanel().debug(element);

	}

}
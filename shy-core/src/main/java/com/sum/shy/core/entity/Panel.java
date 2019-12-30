package com.sum.shy.core.entity;

import java.util.ArrayList;
import java.util.List;

import com.sum.shy.core.utils.LineUtils;
import com.sum.shy.lib.StringUtils;

public class Panel {

	public List<Line> lines = new ArrayList<>();

	public Panel() {
		for (int i = 0; i < 20; i++)
			lines.add(new Line(i + 1, LineUtils.getSpaceByNumber(100)));
	}

	public void debug(Stmt stmt) {
		markPosition(0, stmt);
		buildTree(0, "", stmt);
		show();
	}

	public static void markPosition(int position, Stmt stmt) {
		for (int i = 0; i < stmt.size(); i++) {
			Token token = stmt.getToken(i);
			if (!token.isNode()) {
				// 格式化的长度
				String text = stmt.format(i, token);
				// 先使用位置,再将自己的长度追加到位置中
				token.setPosition(position + (text.startsWith(" ") ? 1 : 0));
				// 给子节点也计算位置
				if (token.hasSubStmt())
					markPosition(position, (Stmt) token.value);
				// 加上当前的长度
				position += text.length();

			} else {
				List<Node> nodes = ((Node) token.value).getNodes();
				for (Node node : nodes) {
					String text = node.format();
					node.token.setPosition(position + (text.startsWith(" ") ? 1 : 0));
					if (node.token.hasSubStmt())
						markPosition(position, (Stmt) node.token.value);
					position += text.length();
				}
			}
		}
	}

	private void buildTree(int depth, String separator, Stmt stmt) {

		for (int i = 0; i < stmt.size(); i++) {
			Token token = stmt.getToken(i);
			if (!token.isNode()) {
				print(depth, token.getPosition(), token.toString());

			} else {
				buildTree(depth, separator, (Node) token.value);
			}
		}

	}

	private void buildTree(int depth, String separator, Node node) {

		if (node == null)
			return;
		// 节点位置
		int position = node.token.getPosition();
		// 节点内容
		String text = node.token.toString();
		// 获取上一行
		if (StringUtils.isNotEmpty(separator))
			print(depth - 1, position + text.length() / 2 + text.length() % 2 - 1, separator);// 尽量上上面的分割符在中间,奇数在中间,偶数在中间偏左一个
		// 打印
		print(depth, position, text);

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

	private void show() {
		// 打印
		for (Line line : lines) {
			if (!line.isIgnore())
				System.out.println(line.text);
		}
	}

	/**
	 * 测试案例
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

//		String text = "var = true || (int)x++ > 100.0 && list.size()>100 && obj instanceof Object || !(x > 0 || y < 100)";
//		String text = "((x+1>0)&&(y<100)) && s==\"test\"";
//		String text = "(int)var + 1000 + list.size().toString()";
//		String text = "(int)obj.toString().length+ 100";
//		String text = "list.get(1)";
//		String text = "map={\"key1\":100}.getSize().toString()+100>0";
//		String text = "b= x >= 100";
//		String text = "b=((String)list.get(1)).length() <= 100";
//		String text = "b=x>=((String)list.get(1)).length().get().set()";
//		String text = "(x >= 0 && y<100)";
		String text = "b = (x + 1 > 0 && y < 100) && s == \"test\" && s instanceof Object";
//		String text = "print \"test print\", list.get(1)==\"test\", ((String)list.get(1)).length() <= 100";
//		String text = "for i=0; i<list.size(); i++ {";

		Stmt stmt = Stmt.create(text);
		System.out.println(stmt.debug());
		System.out.println(stmt.toString());
		new Panel().debug(stmt);

	}

}

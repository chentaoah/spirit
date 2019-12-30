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
		// 打印
		System.out.println(stmt.debug());
		System.out.println(stmt.toString());

		markPosition(0, stmt);

		buildTree(0, "", stmt);

		show();
	}

	public static void markPosition(int position, Stmt stmt) {
		for (int i = 0; i < stmt.size(); i++) {
			Token token = stmt.getToken(i);
			// 格式化的长度
			String text = stmt.format(i, token);
			// 先使用位置,再将自己的长度追加到位置中
			token.setPosition(position + (text.startsWith(" ") ? 1 : 0));
			// 给子节点也计算位置
			if (token.hasSubStmt()) {
				markPosition(position, (Stmt) token.value);
			}
			// 加上当前的长度
			position += text.length();
		}
	}

	private void buildTree(int depth, String separator, Stmt stmt) {

		if (node == null)
			return;
		// 节点内容
		String text = node.token.value.toString();
		// 获取上一行
		if (StringUtils.isNotEmpty(separator)) {
			Line lastLine = lines.get(depth - 1);
			StringBuilder sb = new StringBuilder(lastLine.text);
			// 尽量上上面的分割符在中间,奇数在中间,偶数在中间偏左一个
			int position = node.token.getPosition() + text.length() / 2 + text.length() % 2 - 1;
			sb.replace(position, position + 1, separator);
			lastLine.text = sb.toString();
		}
		// 在节点的上方
		Line line = lines.get(depth);
		StringBuilder sb = new StringBuilder(line.text);
		int position = node.token.getPosition();
		sb.replace(position, position + text.length(), text);
		line.text = sb.toString();

		// 左边节点
		buildTree(depth + 2, "/", node.left);
		// 右边节点
		buildTree(depth + 2, "\\", node.right);

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
//		String text = "b = (x + 1 > 0 && y < 100) && s == \"test\" && s instanceof Object";
//		String text = "print \"test print\", list.get(1)==\"test\", ((String)list.get(1)).length() <= 100";
		String text = "for i=0; i<list.size(); i++ {";

		Stmt stmt = Stmt.create(text);

		new Panel().debug(stmt);

	}

}

package com.sum.shy.core.analyzer;

import java.util.ArrayList;
import java.util.List;

import com.sum.shy.core.entity.Constants;
import com.sum.shy.core.entity.Line;
import com.sum.shy.core.entity.Node;
import com.sum.shy.core.entity.Stmt;
import com.sum.shy.core.entity.Token;
import com.sum.shy.core.utils.LineUtils;
import com.sum.shy.lib.StringUtils;

/**
 * 抽象语法树
 * 
 * @author chentao26275
 *
 */
public class TreePlanter {

	public static final String[] OPERATORS = new String[] { "++", "--", "!", "*", "/", "%", "+", "-", "==", "!=", "<",
			">", "<=", ">=", "&&", "||", "<<", "?", "=" };

	public static final int[] PRIORITY = new int[] { 40, 40, 40, 30, 30, 30, 25, 25, 20, 20, 20, 20, 20, 20, 15, 15, 10,
			10, 5 };

	public enum Category {
		LEFT, RIGHT, DOUBLE
	}

	/**
	 * 语法树,并不能处理所有的语句,比如for循环语句,只能处理一个简单的表达式
	 * 
	 * @param stmt
	 * @return
	 */
	public static Stmt grow(Stmt stmt) {
		// 为每个token计算位置
		markPosition(0, stmt);
		// 递归获取节点树
		return getNodeByLoop(stmt);
	}

	public static void markPosition(int position, Stmt stmt) {
		for (int i = 0; i < stmt.size(); i++) {
			Token token = stmt.getToken(i);
			// 格式化的长度
			String text = stmt.format(i, token);
			// 先使用位置,再将自己的长度追加到位置中
			token.setPosition(position + (text.startsWith(" ") ? 1 : 0));
			// 保留指向语句的引用
			token.setStmt(stmt);
			// 给子节点也计算位置
			if (token.hasSubStmt()) {
				markPosition(position, (Stmt) token.value);
			}
			// 加上当前的长度
			position += text.length();
		}
	}

	private static Stmt getNodeByLoop(Stmt stmt) {

		// 如果只有一个元素
		if (stmt.size() == 1)
			return stmt;

		// 1.为每个操作符,或者特殊的关键字,进行优先级分配
		Token finalLastToken = null;
		Token finalCurrToken = null;// 当前优先级最高的操作符
		Token finalNextToken = null;
		int maxPriority = -1;// 优先级
		Category finalCategory = null;// 一元左元,一元右元,二元
		int index = -1;

		// 每个token在一行里面的位置
		for (int i = 0; i < stmt.size(); i++) {

			Token lastToken = i - 1 >= 0 ? stmt.getToken(i - 1) : null;
			Token currToken = stmt.getToken(i);
			Token nextToken = i + 1 < stmt.size() ? stmt.getToken(i + 1) : null;
			int priority = -1;
			Category category = null;

//			1.括号，如 ( ) 和 [ ]
//			2.一元运算符，如 -、++、--和 !
//			3.算术运算符，如 *、/、%、+ 和 -
//			4.关系运算符，如 >、>=、<、<=、== 和 !=
//			5.逻辑运算符，如 &、^、|、&&、||
//			6.条件运算符和赋值运算符，如 ? ：、=、*=、/=、+= 和 -=

			if (currToken.isFluent()) {
				priority = 50;// 优先级最高
				category = Category.LEFT;

			} else if (currToken.isOperator()) {// 如果是操作符
				String value = currToken.value.toString();
				// 优先级
				priority = getPriority(value);
				if ("++".equals(value) || "--".equals(value)) {
					if (lastToken != null && lastToken.isVar()) {// 左元
						category = Category.LEFT;
					} else if (nextToken != null && nextToken.isVar()) {// 右元
						category = Category.RIGHT;
					}
				} else if ("-".equals(value)) {// -可能是个符号 100+(-10) var = -1
					category = (lastToken == null || lastToken.isOperator()) && nextToken != null ? Category.RIGHT
							: Category.DOUBLE;

				} else if ("!".equals(value)) {// 右元
					category = Category.RIGHT;

				} else {// 一般操作符都是二元的
					category = Category.DOUBLE;
				}

			} else if (currToken.isCast()) {
				priority = 35;// 介于!和 *之间
				category = Category.RIGHT;

			} else if (currToken.isInstanceof()) {// instanceof
				priority = 20;// 相当于一个==
				category = Category.DOUBLE;

			}

			if (priority > maxPriority) {
				finalLastToken = lastToken;
				finalCurrToken = currToken;
				finalNextToken = nextToken;
				maxPriority = priority;
				finalCategory = category;
				index = i;
			}

		}
		// 校验
		if (finalCurrToken == null)
			return stmt;

		Node node = getNode(finalCurrToken);
		if (finalCategory == Category.LEFT || finalCategory == Category.DOUBLE) {
			if (finalLastToken != null) {
				node.left = getNode(finalLastToken);
			}
		}
		if (finalCategory == Category.RIGHT || finalCategory == Category.DOUBLE) {
			if (finalNextToken != null) {
				node.right = getNode(finalNextToken);
			}
		}

		int start = finalCategory == Category.LEFT || finalCategory == Category.DOUBLE ? index - 1 : index;
		int end = finalCategory == Category.RIGHT || finalCategory == Category.DOUBLE ? index + 1 + 1 : index + 1;

		// 替换
		stmt = stmt.replace(start, end, new Token(Constants.NODE_TOKEN, node, null));

		// 递归
		return getNodeByLoop(stmt);

	}

	public static int getPriority(String value) {
		int count = 0;
		for (String operator : OPERATORS) {
			if (operator.equals(value)) {
				return PRIORITY[count];
			}
			count++;
		}
		return -1;
	}

	public static Node getNode(Token token) {
		if (token.isNode()) {
			return (Node) token.value;
		} else {
			return new Node(token);
		}
	}

	/**
	 * 测试案例
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		// 构建一个画布
		List<Line> lines = new ArrayList<>();
		for (int i = 0; i < 20; i++) {
			lines.add(new Line(i + 1, LineUtils.getSpaceByNumber(100)));
		}

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
		String text = "if this.name=true || (int)x++ > 100.0 && list.size()>100 {";

		Stmt stmt = Stmt.create(text);
		System.out.println(stmt.debug());
		System.out.println(stmt.toString());

		stmt = grow(stmt);
		for (Node node : stmt.findNodes()) {
			buildTree(lines, 0, null, node);
		}

		// 打印
		for (Line line : lines) {
			if (!line.isIgnore()) {
				System.out.println(line.text);
			}
		}
	}

	private static void buildTree(List<Line> lines, int depth, String separator, Node node) {

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
		buildTree(lines, depth + 2, "/", node.left);
		// 右边节点
		buildTree(lines, depth + 2, "\\", node.right);

	}

}

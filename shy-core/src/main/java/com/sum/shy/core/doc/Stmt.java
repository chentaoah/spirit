package com.sum.shy.core.doc;

import java.util.ArrayList;
import java.util.List;

import com.sum.shy.core.entity.Node;
import com.sum.shy.core.entity.Token;

public class Stmt {

	public List<Token> tokens;

	public Stmt(List<Token> tokens) {
		this.tokens = tokens;
	}

	public int size() {
		return tokens.size();
	}

	public Token getToken(int index) {
		return tokens.get(index);
	}

	public int indexOf(Token token) {
		return tokens.indexOf(token);
	}

	public String get(int index) {// 修改为从token获取字符串
		return getToken(index).toString();
	}

	public String frist() {
		return get(0);
	}

	public String last() {
		return get(size() - 1);
	}

	public int indexOf(String str) {
		for (int i = 0; i < size(); i++) {
			Token token = tokens.get(i);
			if ((token.isSeparator() || token.isOperator()) && str.equals(token.toString()))
				return i;
		}
		return -1;
	}

	public int lastIndexOf(String str) {
		int index = -1;
		for (int i = 0; i < size(); i++) {
			Token token = tokens.get(i);
			if ((token.isSeparator() || token.isOperator()) && str.equals(token.toString())) {
				index = i > index ? i : index;
			}
		}
		return index;
	}

	public Stmt subStmt(int start, int end) {
		// 这里一定要new一个,不然subList返回的是原来集合的一个视图
		return new Stmt(new ArrayList<>(tokens.subList(start, end)));
	}

	public Stmt subStmt(String left, String right) {
		return subStmt(indexOf(left), lastIndexOf(right));
	}

	public List<Stmt> split(String separator) {// 通过分隔符来获取子语句
		List<Stmt> subStmts = new ArrayList<>();
		for (int i = 0, last = 0; i < size(); i++) {
			Token token = tokens.get(i);
			if ((token.isSeparator() || token.isOperator()) && separator.equals(token.toString())) {// 分隔符
				Stmt subStmt = subStmt(last, i);
				subStmts.add(subStmt);
				last = i + 1;// 记录截取开始的地方
			} else if (i == size() - 1) {// 到达最后
				Stmt subStmt = subStmt(last, i + 1);
				subStmts.add(subStmt);
			}
		}
		return subStmts;
	}

	public List<Node> findNodes() {
		List<Node> nodes = new ArrayList<>();
		for (Token token : tokens) {
			if (token.isNode())
				nodes.add(token.getNode());
		}
		return nodes;
	}

	@Override
	public String toString() {
//		// 如果没有token,则直接返回line
//		if (size() == 0)
//			return line.text;
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < size(); i++) {
			String str = format(i, getToken(i));
			sb.append(str);
		}
		return sb.toString();
	}

	public String format(int index, Token token) {

		if (token == null)
			return "";

		if (token.isKeyword()) {// 关键字一般后面加个空格
			if ("try".equals(token.toString())) {// try语句不动
				return token.toString();

			} else if ("else".equals(token.toString()) && size() == 3) {// } else { 语句
				return token.toString();

			} else if ("return".equals(token.toString()) && size() == 2) {// return;
				return token.toString();

			} else if ("continue".equals(token.toString())) {
				return token.toString();

			} else if ("break".equals(token.toString())) {
				return token.toString();

			} else if ("in".equals(token.toString())) {// for xxx in xxx :
				return " " + token + " ";

			} else if ("extends".equals(token.toString())) {// extends
				return " " + token + " ";

			} else if ("impl".equals(token.toString())) {// impl shy代码显示的更好看点
				return " " + token + " ";

			} else if ("finally".equals(token.toString())) {// impl shy代码显示的更好看点
				return token.toString();

			} else {
				return token + " ";
			}

		} else if (token.isOperator()) {
			if ("=".equals(token.toString())) {
				return " " + token + " ";

			} else if ("<<".equals(token.toString())) {
				return " " + token + " ";

			} else if ("?".equals(token.toString())) {
				return " " + token + " ";

			} else {
				return token.toString();
			}

		} else if (token.isSeparator()) {// 末尾的括号前面加个空格
			if (",".equals(token.toString())) {// ,后面加空格
				return token + " ";

			} else if (";".equals(token.toString())) {// ;在后面加空格
				return token + " ";

			} else if (":".equals(token.toString())) {// if xxx==xxx : print xxx
				return " " + token + " ";

			} else if ("{".equals(token.toString()) && index == size() - 1) {// 如果{结尾,则在前面加个空格
				return " " + token;

			} else if ("}".equals(token.toString()) && index == 0 && size() != 1) {// 如果}是开头,并且不是孤零零的一个
				return token + " ";

			} else {
				return token.toString();
			}

		} else if (token.isType()) {
//			if (syntax != null) {// 泛型里面的类型后面就不用加空格了,判断条件时,子语句是的语法参数是null
//				return token + " ";
//
//			} else {
//				return token.toString();
//			}

		} else {
			return token.toString();
		}
		return null;

	}

	public String debug() {
		StringBuilder sb = new StringBuilder();
		for (Token token : tokens) {
			if (token.isNode()) {
				sb.append(token.debug() + "[" + token.getNode().toStmt().debug() + "]");
			} else {
				sb.append(token.debug() + " ");
			}
		}
		return sb.toString().trim();
	}

//	public boolean isAssign() {
//		return Constants.ASSIGN_SYNTAX.equals(syntax);
//	}
//
//	public boolean isIf() {
//		return Constants.IF_SYNTAX.equals(syntax);
//	}
//
//	public boolean isElseIf() {
//		return Constants.ELSEIF_SYNTAX.equals(syntax);
//	}
//
//	public boolean isElse() {
//		return Constants.ELSE_SYNTAX.equals(syntax);
//	}
//
//	public boolean isEnd() {
//		return Constants.END_SYNTAX.equals(syntax);
//	}
//
//	public boolean isReturn() {
//		return Constants.RETURN_SYNTAX.equals(syntax);
//	}
//
//	public boolean isDeclare() {
//		return Constants.DECLARE_SYNTAX.equals(syntax);
//	}
//
//	public boolean isCatch() {
//		return Constants.CATCH_SYNTAX.equals(syntax);
//	}
//
//	public boolean isForIn() {
//		return Constants.FOR_IN_SYNTAX.equals(syntax);
//	}
//
//	public boolean isFor() {
//		return Constants.FOR_SYNTAX.equals(syntax);
//	}
//
//	public boolean isWhile() {
//		return Constants.WHILE_SYNTAX.equals(syntax);
//	}
//
//	public boolean isSync() {
//		return Constants.SYNC_SYNTAX.equals(syntax);
//	}
//
//	public boolean isTry() {
//		return Constants.TRY_SYNTAX.equals(syntax);
//	}
//
//	public boolean isFinally() {
//		return Constants.FINALLY_SYNTAX.equals(syntax);
//	}
//
//	public boolean isSuper() {
//		return Constants.SUPER_SYNTAX.equals(syntax);
//	}
//
//	public boolean isThis() {
//		return Constants.THIS_SYNTAX.equals(syntax);
//	}
//
//	public boolean isFieldAssign() {
//		return Constants.FIELD_ASSIGN_SYNTAX.equals(syntax);
//	}
//
//	public boolean isInvoke() {
//		return Constants.INVOKE_SYNTAX.equals(syntax);
//	}
//
//	public boolean isContinue() {
//		return Constants.CONTINUE_SYNTAX.equals(syntax);
//	}
//
//	public boolean isBreak() {
//		return Constants.BREAK_SYNTAX.equals(syntax);
//	}
//
//	public boolean isThrow() {
//		return Constants.THROW_SYNTAX.equals(syntax);
//	}
//
//	public boolean isJudgeInvoke() {
//		return Constants.JUDGE_INVOKE_SYNTAX.equals(syntax);
//	}

}

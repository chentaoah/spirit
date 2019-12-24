package com.sum.shy.core.analyzer;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.sum.shy.core.api.Handler;
import com.sum.shy.core.clazz.impl.CtClass;
import com.sum.shy.core.clazz.impl.CtMethod;
import com.sum.shy.core.clazz.impl.Variable;
import com.sum.shy.core.entity.Line;
import com.sum.shy.core.entity.Stmt;
import com.sum.shy.core.entity.Token;
import com.sum.shy.core.type.api.Type;
import com.sum.shy.core.type.impl.CodeType;
import com.sum.shy.core.utils.LineUtils;
import com.sum.shy.lib.Collection;

/**
 * 快速遍历器,快速遍历一个方法里面的所有结构
 * 
 * @author chentao26275
 *
 */
public class MethodResolver {

	/**
	 * 快速遍历
	 * 
	 * @param clazz
	 * @param method
	 * @param isAutoDerived 是否自动进行推导,而不是仅仅返回一个代表性的codeType
	 * @param handler
	 * @return
	 */
	public static Object resolve(CtClass clazz, CtMethod method, Handler handler) {

		Position position = new Position();

		List<Line> lines = method.methodLines;
		for (int i = 0; i < lines.size(); i++) {
			Line line = lines.get(i);
			if (line.isIgnore())
				continue;

			Stmt stmt = Stmt.create(line);

			// 处理单行语句,拆分成多行语句
			if (stmt.isIf() || stmt.isFor() || stmt.isForIn() || stmt.isWhile()) {
				if (!"{".equals(stmt.last())) {
					List<Stmt> subStmts = stmt.split(":");
					int count = 0;
					for (Stmt subStmt : subStmts) {
						String text = subStmt.toString();
						if (count == 0)
							text = text + " {";
						resolveStmt(clazz, method, handler, position, line, Stmt.create(text));
						count++;
					}
					resolveStmt(clazz, method, handler, position, line, Stmt.create("}"));
					continue;
				}
			}

			// 其他语句走默认分支
			Object result = resolveStmt(clazz, method, handler, position, line, stmt);
			if (result != null) {
				method.variables.clear();// 返回前,清理掉所有的变量
				return result;
			}

		}
		method.variables.clear();// 返回前,清理掉所有的变量
		return null;

	}

	private static Object resolveStmt(CtClass clazz, CtMethod method, Handler handler, Position position, Line line,
			Stmt stmt) {

		// 根据{和}重新计算位置
		boolean inCondition = calcPosition(position, stmt);
		// 根据位置生成块的标记
		String block = getBlock(position);

		// 某些语句,先行定义一些变量
		if (stmt.isDeclare()) {
			method.addVariable(new Variable(block, new CodeType(clazz, stmt.getToken(0)), stmt.get(1)));

		} else if (stmt.isCatch()) {
			method.addVariable(new Variable(block, new CodeType(clazz, stmt.getToken(2)), stmt.get(3)));

		} else if (stmt.isForIn()) {// for item in list {循环里面,也可以定义变量
			// 处理表达式
			Type type = processExpression(clazz, method, block, line, stmt, stmt.subStmt(3, stmt.size() - 1), 1);
			// 如果是数组,则用数组内的类型
			Type finalType = type.isArray() ? new CodeType(clazz, type.getTypeName()) : type.getGenericTypes().get(0);
			// 添加变量到上下文
			method.addVariable(new Variable(block, finalType, stmt.get(1)));

		} else if (stmt.isFor()) {// for i=0; i<100; i++ {循环里面,也可以定义变量

			String subText = line.text.substring(line.text.indexOf("for ") + 3, line.text.indexOf(";"));
			Stmt subStmt = Stmt.create(subText);
			// 处理表达式
			Type type = processExpression(clazz, method, block, line, subStmt, subStmt, 0);
			// 添加变量到上下文
			method.addVariable(new Variable(block, type, subStmt.get(0)));

		} else if (stmt.isAssign()) {
			// 处理表达式
			Type type = processExpression(clazz, method, block, line, stmt, stmt, 0);
			// 标记是否已经被声明
			Token token = stmt.getToken(0);
			token.setDeclaredAtt(token.getTypeAtt() != null);
			token.setTypeAtt(type);
			// 添加变量到上下文
			method.addVariable(new Variable(block, type, stmt.get(0)));

		} else {
			VariableTracker.track(clazz, method, block, line, stmt);
			TypeVisiter.visitStmt(clazz, stmt);// 返回值推导

		}
		// 再校验一次
		VariableTracker.track(clazz, method, block, line, stmt);

		// 条件语句没必要那么快增加缩进
		String indent = LineUtils
				.getIndentByNumber(inCondition ? position.depth.get() + 2 - 1 : position.depth.get() + 2);

		Object result = handler.handle(clazz, method, indent, block, line, stmt);
		return result;

	}

	private static boolean calcPosition(Position position, Stmt stmt) {

		AtomicInteger depth = position.depth;
		List<Integer> counts = position.counts;
		boolean inCondition = false;

		// 判断进入的深度
		if ("}".equals(stmt.frist())) {
			depth.decrementAndGet();// --
		}
		if ("{".equals(stmt.last())) {
			depth.incrementAndGet();// ++
			counts.set(depth.get(), counts.get(depth.get()) + 1);
			inCondition = true;
		}

		return inCondition;
	}

	private static String getBlock(Position position) {

		AtomicInteger depth = position.depth;
		List<Integer> counts = position.counts;

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < depth.get(); i++) {
			sb.append(counts.get(depth.get()) + "-");
		}
		if (sb.length() > 0)
			sb.deleteCharAt(sb.length() - 1);

		return sb.toString();
	}

	private static Type processExpression(CtClass clazz, CtMethod method, String block, Line line, Stmt stmt,
			Stmt express, Integer... ignores) {
		// 变量追踪
		VariableTracker.track(clazz, method, block, line, stmt, ignores);
		// 字面类型推导
		TypeVisiter.visitStmt(clazz, stmt);
		// 推导表达式的返回类型
		Type type = FastDerivator.deriveExpression(clazz, express);

		return type;
	}

	public static class Position {

		public AtomicInteger depth = new AtomicInteger(0);
		// 这里默认给了八级的深度
		public List<Integer> counts = Collection.newArrayList(1, 0, 0, 0, 0, 0, 0, 0);

	}

}

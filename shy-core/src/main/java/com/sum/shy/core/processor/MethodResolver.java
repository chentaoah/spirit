package com.sum.shy.core.processor;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.sum.shy.clazz.IClass;
import com.sum.shy.clazz.IMethod;
import com.sum.shy.clazz.Variable;
import com.sum.shy.core.deduce.FastDerivator;
import com.sum.shy.core.deduce.InvokeVisiter;
import com.sum.shy.core.deduce.VariableTracker;
import com.sum.shy.core.entity.Line;
import com.sum.shy.core.entity.Stmt;
import com.sum.shy.core.entity.Token;
import com.sum.shy.core.processor.api.Handler;
import com.sum.shy.lib.Collection;
import com.sum.shy.type.CodeType;
import com.sum.shy.type.api.Type;
import com.sum.shy.utils.LineUtils;

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
	public static Object resolve(IClass clazz, IMethod method, Handler handler) {

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

	private static Object resolveStmt(IClass clazz, IMethod method, Handler handler, Position position, Line line,
			Stmt stmt) {

		// 根据{和}重新计算位置
		boolean inCondition = calcPosition(position, stmt);
		// 根据位置生成块的标记
		String block = getBlock(position);

		if (stmt.isDeclare()) {// Type type
			processBridge(clazz, method, block, line, stmt, 0, 1, 1, null);

		} else if (stmt.isCatch()) {// }catch Exception e{
			processBridge(clazz, method, block, line, stmt, 2, 3, 3, null);

		} else if (stmt.isForIn()) {// for item in list {
			processBridge(clazz, method, block, line, stmt, 3, stmt.size() - 1, 1,
					(Type type) -> type.isArray() ? new CodeType(clazz, type.getTypeName())
							: type.getGenericTypes().get(0));

		} else if (stmt.isFor()) {// for i=0; i<100; i++ {
			processBridge(clazz, method, block, line, stmt, stmt.indexOf("=") + 1, stmt.indexOf(";"), 1, null);

		} else if (stmt.isAssign()) {// var=list.get(0)

			Token token = stmt.getToken(0);
			Type type = VariableTracker.findType(clazz, method, block, token.toString());
			token.setDeclaredAtt(type != null);

			processBridge(clazz, method, block, line, stmt, 2, stmt.size(), 0, null);

		} else {

			VariableTracker.trackStmt(clazz, method, block, line, stmt);
			InvokeVisiter.visitStmt(clazz, stmt);

		}

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
		for (int i = 0; i < depth.get(); i++)
			sb.append(counts.get(depth.get()) + "-");
		if (sb.length() > 0)
			sb.deleteCharAt(sb.length() - 1);

		return sb.toString();
	}

	private static void processBridge(IClass clazz, IMethod method, String block, Line line, Stmt stmt, int start,
			int end, int index, Filter filter) {
		Stmt subStmt = stmt.subStmt(start, end);
		VariableTracker.trackStmt(clazz, method, block, line, subStmt);
		InvokeVisiter.visitStmt(clazz, subStmt);
		Type type = FastDerivator.deriveStmt(clazz, subStmt);
		if (filter != null)
			type = filter.processType(type);
		Token token = stmt.getToken(index);
		token.setTypeAtt(type);
		method.addVariable(new Variable(block, type, token.toString()));
	}

	public static class Position {
		// 深度
		public AtomicInteger depth = new AtomicInteger(0);
		// 这里默认给了八级的深度
		public List<Integer> counts = Collection.newArrayList(1, 0, 0, 0, 0, 0, 0, 0);
	}

	public static interface Filter {
		Type processType(Type type);
	}

}

package com.sum.shy.core.analyzer;

import java.util.ArrayList;
import java.util.List;

import com.sum.shy.core.api.Handler;
import com.sum.shy.core.api.Type;
import com.sum.shy.core.entity.CodeType;
import com.sum.shy.core.entity.CtClass;
import com.sum.shy.core.entity.CtMethod;
import com.sum.shy.core.entity.Line;
import com.sum.shy.core.entity.Stmt;
import com.sum.shy.core.entity.Token;
import com.sum.shy.core.entity.Variable;
import com.sum.shy.core.utils.LineUtils;

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
	public static Object resolve(CtClass clazz, CtMethod method, boolean isAutoDerived, Handler handler) {

		int depth = 0;
		// 这里默认给了八级的深度
		List<Integer> counts = new ArrayList<>();
		counts.add(1);
		counts.add(0);
		counts.add(0);
		counts.add(0);
		counts.add(0);
		counts.add(0);
		counts.add(0);
		counts.add(0);

		List<Line> lines = method.methodLines;
		for (int i = 0; i < lines.size(); i++) {
			Line line = lines.get(i);
			if (line.isIgnore())
				continue;

			Stmt stmt = Stmt.create(line);
			boolean inCondition = false;
			// 判断进入的深度
			if ("}".equals(stmt.frist())) {
				depth--;
			}
			if ("{".equals(stmt.last())) {
				depth++;
				counts.set(depth, counts.get(depth) + 1);
				inCondition = true;
			}
			// 生成block
			StringBuilder sb = new StringBuilder();
			for (int j = 0; j < depth; j++) {
				sb.append(counts.get(depth) + "-");
			}
			if (sb.length() > 0)
				sb.deleteCharAt(sb.length() - 1);
			String block = sb.toString();

			// 变量追踪
			VariableTracker.track(clazz, method, block, line, stmt);

			if (stmt.isDeclare()) {
				method.addVariable(new Variable(block, new CodeType(stmt.get(0)), stmt.get(1)));

			} else if (stmt.isAssign()) {
				// 判断变量追踪是否帮我们找到了该变量的类型
				Token token = stmt.getToken(0);
				// 如果变量追踪,并没有找到类型声明
				if (token.isVar() && !token.isDeclaredAtt()) {
					// 这里使用了快速推导,但是返回的类型并不是最终类型
					Type type = FastDerivator.getType(stmt);
					// 如果需要精确推导,并且返回的类型并不是完全准确的,则进行深度推导
					if (isAutoDerived && !type.isFinal()) {
						type = InvokeVisiter.visitType(clazz, (CodeType) type);
					}
					// 设置到第一个token里
					token.setTypeAtt(type);
					// 添加到方法变量里
					method.addVariable(new Variable(block, type, stmt.get(0)));
				}
			}
			// 条件语句没必要那么快增加缩进
			String indent = LineUtils.getIndentByNumber(inCondition ? depth + 2 - 1 : depth + 2);

			Object result = handler.handle(clazz, method, indent, block, line, stmt);
			if (result != null) {
				method.variables.clear();// 返回前,清理掉所有的变量
				return result;
			}

		}
		method.variables.clear();// 返回前,清理掉所有的变量
		return null;

	}

}
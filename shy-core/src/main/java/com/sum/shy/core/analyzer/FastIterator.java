package com.sum.shy.core.analyzer;

import java.util.ArrayList;
import java.util.List;

import com.sum.shy.core.api.Listener;
import com.sum.shy.core.entity.CtClass;
import com.sum.shy.core.entity.CtMethod;
import com.sum.shy.core.entity.Line;
import com.sum.shy.core.entity.Stmt;

/**
 * 快速遍历器,快速遍历一个方法里面的所有结构
 * 
 * @author chentao26275
 *
 */
public class FastIterator {

	/**
	 * 快速遍历方法
	 * 
	 * @param clazz
	 * @param method
	 * @param listener
	 * @return
	 */
	public static Object traver(CtClass clazz, CtMethod method, Listener listener) {

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
			// 判断进入的深度
			if ("}".equals(stmt.frist())) {
				depth--;
			}
			if ("{".equals(stmt.last())) {
				depth++;
				counts.set(depth, counts.get(depth) + 1);
			}
			// 生成block
			StringBuilder sb = new StringBuilder();
			for (Integer count : counts) {
				if (count == 0)
					break;
				sb.append(count + "-");
			}
			if (sb.length() > 0)
				sb.deleteCharAt(sb.length() - 1);
			String block = sb.toString();

			Object result = listener.handle(clazz, method, depth, block, line, stmt);
			if (result != null)
				return result;

		}
		return null;

	}

}

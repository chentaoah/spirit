package com.sum.shy.core;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.sum.shy.core.entity.Line;
import com.sum.shy.core.entity.Stmt;
import com.sum.shy.core.utils.LineUtils;

public class ShyDebugger {

	/**
	 * 这里仅仅测试一个语句被拆分成token以后,是什么样的,是否符合预期
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public void read(File file) {
		try {
			List<String> fileLines = Files.readLines(file, Charsets.UTF_8);
			List<Line> lines = new ArrayList<>();
			for (int i = 0; i < fileLines.size(); i++) {
				lines.add(new Line(i + 1, fileLines.get(i)));
			}
			readLines(lines);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void readLines(List<Line> lines) {

		// 获取所有行
		for (int i = 0; i < lines.size(); i++) {
			// 取出第一个单词,判断是否在关键字中
			Line line = lines.get(i);
			if (line.isIgnore()) {
				System.out.println(line.text);
			} else {
				Stmt stmt = Stmt.create(line);
				System.out.println(line.text + LineUtils.getSpaceByNumber(45 - line.text.length()) + ">>> "
						+ stmt.syntax + " " + stmt.debug());
			}

		}

	}

}

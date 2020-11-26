package com.sum.spirit.core.build;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import com.sum.spirit.api.LexerAction;
import com.sum.spirit.core.lexer.AbsLexerAction;
import com.sum.spirit.core.lexer.LexerEvent;
import com.sum.spirit.utils.LineUtils;
import com.sum.spirit.utils.SpringUtils;

@Component
@DependsOn("springUtils")
public class Lexer extends AbsLexerAction implements InitializingBean {

	public static final Pattern TYPE_END_PATTERN = Pattern.compile("^[\\s\\S]+\\.[A-Z]+\\w+$");

	public List<LexerAction> actions;

	@Override
	public void afterPropertiesSet() throws Exception {
		actions = SpringUtils.getBeansAndSort(LexerAction.class, Lexer.class);// 排除自己
	}

	public List<String> getWords(String text, Character... ignoreOnceChars) {

		if (StringUtils.isEmpty(text)) {// 拆分方法体时，会传入空的text
			return new ArrayList<>();
		}

		StringBuilder builder = new StringBuilder(text.trim());
		Map<String, String> replacedStrs = replace(builder, ignoreOnceChars);// 触发事件
		text = LineUtils.mergeSpaces(builder.toString());// 去掉多余的空格
		List<String> words = new ArrayList<>(Arrays.asList(text.split(" ")));

		for (int i = 0; i < words.size(); i++) {// 如果一个片段中，包含“.”，那么进行更细致的拆分
			String word = words.get(i);
			if (word.indexOf(".") > 0 && !TYPE_END_PATTERN.matcher(word).matches() && !AbsSemanticParser.isDouble(word)) {
				List<String> subWords = Arrays.asList(word.replaceAll("\\.", " .").split(" "));
				words.remove(i);
				words.addAll(i, subWords);
			}
		}

		for (int i = 0; i < words.size(); i++) {// 替换回去
			String str = replacedStrs.get(words.get(i));
			if (str != null) {
				words.set(i, str);
			}
		}

		return words;
	}

	public Map<String, String> replace(StringBuilder builder, Character... ignoreOnceChars) {

		List<Character> ignoreChars = new ArrayList<>(Arrays.asList(ignoreOnceChars));
		Map<String, String> replacedStrs = new HashMap<>();
		AtomicInteger index = new AtomicInteger(0);
		AtomicInteger count = new AtomicInteger(0);
		AtomicInteger start = new AtomicInteger(-1);
		AtomicInteger end = new AtomicInteger(-1);

		for (; index.get() < builder.length(); index.incrementAndGet()) {
			char c = builder.charAt(index.get());

			if ((start.get() < 0 && isContinuous(c)) || isRefreshed(c)) {
				start.set(index.get());
			}

			// 这里使用统一的逻辑处理
			LexerEvent event = new LexerEvent(builder, index, c, count, start, end, replacedStrs, ignoreChars);
			if (isTrigger(event)) {
				pushStack(event);
			}

			if (!isContinuous(c)) {
				start.set(-1);
			}
		}

		return replacedStrs;
	}

	public boolean isContinuous(char c) {// 是否连续
		return c == '@' || (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c >= '0' && c <= '9') || c == '_' || c == '.';
	}

	public boolean isRefreshed(char c) {// 是否需要刷新
		return c == '.';
	}

	@Override
	public boolean isTrigger(LexerEvent event) {
		return true;
	}

	@Override
	public void pushStack(LexerEvent event) {
		for (LexerAction action : actions) {
			if (action.isTrigger(event)) {
				action.pushStack(event);
				return;
			}
		}
	}

}

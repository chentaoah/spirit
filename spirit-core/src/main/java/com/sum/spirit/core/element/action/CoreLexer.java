package com.sum.spirit.core.element.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import com.sum.spirit.api.LexerAction;
import com.sum.spirit.core.element.action.lexer.AbstractLexerAction;
import com.sum.spirit.core.element.action.lexer.LexerContext;
import com.sum.spirit.core.element.action.lexer.LexerEvent;
import com.sum.spirit.utils.LineUtils;
import com.sum.spirit.utils.SpringUtils;

@Component
@DependsOn("springUtils")
public class CoreLexer extends AbstractLexerAction implements InitializingBean {

	public static final Pattern TYPE_END_PATTERN = Pattern.compile("^[\\s\\S]+\\.[A-Z]+\\w+$");

	public List<LexerAction> actions;

	@Override
	public void afterPropertiesSet() throws Exception {
		actions = SpringUtils.getBeansAndSort(LexerAction.class, CoreLexer.class);// 排除自己
	}

	public List<String> getWords(String text, Character... ignoreOnceChars) {
		// 拆分方法体时，会传入空的text
		if (StringUtils.isEmpty(text)) {
			return new ArrayList<>();
		}
		StringBuilder builder = new StringBuilder(text.trim());
		// 触发事件
		Map<String, String> replacedStrs = replace(builder, ignoreOnceChars);
		// 去掉多余的空格
		text = LineUtils.mergeSpaces(builder.toString());
		// 利用空格，进行拆分
		List<String> words = new ArrayList<>(Arrays.asList(text.split(" ")));
		// 继续拆分单词
		splitWords(words);
		// 还原被替换的单词
		restoreWords(words, replacedStrs);

		return words;
	}

	public Map<String, String> replace(StringBuilder builder, Character... ignoreOnceChars) {
		LexerContext context = new LexerContext(builder, new ArrayList<>(Arrays.asList(ignoreOnceChars)));
		for (; context.index.get() < builder.length(); context.index.incrementAndGet()) {
			char c = builder.charAt(context.index.get());
			// 是否连续字符
			if ((context.start.get() < 0 && isContinuous(c)) || isRefreshed(c)) {
				context.start.set(context.index.get());
			}
			// 这里使用统一的逻辑处理
			LexerEvent event = new LexerEvent(context, c);
			if (isTrigger(event)) {
				pushStack(event);
			}
			if (!isContinuous(c)) {
				context.start.set(-1);
			}
		}
		return context.replacedStrs;
	}

	public void splitWords(List<String> words) {
		for (int i = 0; i < words.size(); i++) {// 如果一个片段中，包含“.”，那么进行更细致的拆分
			String word = words.get(i);
			if (word.indexOf(".") > 0 && !TYPE_END_PATTERN.matcher(word).matches() && !AbstractSemanticParser.isDouble(word)) {
				List<String> subWords = Arrays.asList(word.replaceAll("\\.", " .").split(" "));
				words.remove(i);
				words.addAll(i, subWords);
			}
		}
	}

	public void restoreWords(List<String> words, Map<String, String> replacedStrs) {
		for (int i = 0; i < words.size(); i++) {// 替换回去
			String str = replacedStrs.get(words.get(i));
			if (str != null) {
				words.set(i, str);
			}
		}
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

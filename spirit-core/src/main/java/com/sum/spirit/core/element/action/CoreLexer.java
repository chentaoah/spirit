package com.sum.spirit.core.element.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import com.sum.spirit.api.LexerAction;
import com.sum.spirit.core.element.lexer.AbstractLexerAction;
import com.sum.spirit.core.element.lexer.BorderAction;
import com.sum.spirit.core.element.lexer.LexerContext;
import com.sum.spirit.core.element.lexer.LexerEvent;
import com.sum.spirit.utils.LineUtils;
import com.sum.spirit.utils.SpringUtils;

@Component
@DependsOn("springUtils")
public class CoreLexer extends AbstractLexerAction implements InitializingBean {

	public static final Pattern TYPE_END_PATTERN = Pattern.compile("^[\\s\\S]+\\.[A-Z]+\\w+$");

	public List<LexerAction> actions;

	@Autowired
	public BorderAction borderAction;

	@Override
	public void afterPropertiesSet() throws Exception {
		actions = SpringUtils.getBeansAndSort(LexerAction.class, CoreLexer.class, BorderAction.class);// 排除自己
	}

	public List<String> getWords(String text) {
		// 拆分方法体时，会传入空的text
		if (StringUtils.isEmpty(text)) {
			return new ArrayList<>();
		}
		// 上下文
		LexerContext context = new LexerContext(new StringBuilder(text.trim()));
		// 触发事件
		process(context, this);
		// 去掉多余的空格
		text = LineUtils.mergeSpaces(context.builder.toString());
		// 利用空格，进行拆分
		List<String> words = new ArrayList<>(Arrays.asList(text.split(" ")));
		// 继续拆分单词
		splitWords(words);
		// 还原被替换的单词
		restoreWords(words, context.replacedStrs);

		return words;
	}

	public List<String> getSubWords(String text, Character... splitChars) {
		// 上下文
		LexerContext context = new LexerContext(new StringBuilder(text.trim()), splitChars);
		// 触发事件
		process(context, borderAction);

		return null;
	}

	public void process(LexerContext context, LexerAction action) {
		StringBuilder builder = context.builder;
		for (; context.index < builder.length(); context.index++) {
			char ch = builder.charAt(context.index);
			// 是否连续字符
			if ((context.startIndex < 0 && isContinuous(ch)) || isRefreshed(ch)) {
				context.startIndex = context.index;
			}
			// 这里使用统一的逻辑处理
			LexerEvent event = new LexerEvent(context, ch);
			if (action.isTrigger(event)) {
				action.pushStack(event);
			}
			// 如果不是连续字符，则重置游标
			if (!isContinuous(ch)) {
				context.startIndex = -1;
			}
		}
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

package com.sum.spirit.core.lexer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import com.sum.spirit.common.utils.LineUtils;
import com.sum.spirit.common.utils.SpringUtils;
import com.sum.spirit.core.api.CharAction;
import com.sum.spirit.core.api.Lexer;
import com.sum.spirit.core.api.LexerAction;
import com.sum.spirit.core.lexer.action.AbstractLexerAction;
import com.sum.spirit.core.lexer.action.BorderAction;
import com.sum.spirit.core.lexer.entity.LexerContext;
import com.sum.spirit.core.lexer.entity.LexerEvent;

import cn.hutool.core.lang.Assert;

@Component
@Primary
@DependsOn("springUtils")
public class CoreLexer extends AbstractLexerAction implements Lexer, InitializingBean {

	public static final Pattern TYPE_END_PATTERN = Pattern.compile("^[\\s\\S]+\\.[A-Z]+\\w+$");
	public static final Pattern DOUBLE_PATTERN = Pattern.compile("^\\d+\\.\\d+$");

	public List<CharAction> actions;
	@Autowired
	public BorderAction borderAction;

	@Override
	public void afterPropertiesSet() throws Exception {
		actions = SpringUtils.getBeansAndSort(CharAction.class, CoreLexer.class, AliasLexer.class, BorderAction.class);
	}

	@Override
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

	@Override
	public List<String> getSubWords(String text, Character... splitChars) {
		// 上下文
		LexerContext context = new LexerContext(new StringBuilder(text.trim()), splitChars);
		// 触发事件
		process(context, borderAction);
		// 校验
		Assert.notNull(context.words, "SubWords can not be null!");
		// 继续拆分
		List<String> words = new ArrayList<>();
		for (String word : context.words) {
			words.addAll(getWords(word));
		}
		return words;
	}

	public void process(LexerContext context, LexerAction action) {
		// 开始遍历
		for (StringBuilder builder = context.builder; context.index < builder.length(); context.index++) {
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
		for (int index = 0; index < words.size(); index++) {// 如果一个片段中，包含“.”，那么进行更细致的拆分
			String word = words.get(index);
			if (word.indexOf(".") > 0 && !TYPE_END_PATTERN.matcher(word).matches() && !DOUBLE_PATTERN.matcher(word).matches()) {
				List<String> subWords = Arrays.asList(word.replaceAll("\\.", " .").split(" "));
				words.remove(index);
				words.addAll(index, subWords);
			}
		}
	}

	public void restoreWords(List<String> words, Map<String, String> replacedStrs) {
		for (int index = 0; index < words.size(); index++) {// 替换回去
			String str = replacedStrs.get(words.get(index));
			if (str != null) {
				words.set(index, str);
			}
		}
	}

	public boolean isContinuous(char ch) {// 是否连续
		return ch == '@' || (ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z') || (ch >= '0' && ch <= '9') || ch == '_' || ch == '.';
	}

	public boolean isRefreshed(char ch) {// 是否需要刷新
		return ch == '.';
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

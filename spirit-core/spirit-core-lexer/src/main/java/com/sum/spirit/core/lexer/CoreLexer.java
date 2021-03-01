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
import com.sum.spirit.core.lexer.action.AbstractLexerAction;
import com.sum.spirit.core.lexer.action.BorderAction;
import com.sum.spirit.core.lexer.action.CursorAction;
import com.sum.spirit.core.lexer.entity.CharEvent;
import com.sum.spirit.core.lexer.entity.LexerContext;

import cn.hutool.core.lang.Assert;

@Component
@Primary
@DependsOn("springUtils")
public class CoreLexer extends AbstractCharsHandler implements Lexer, InitializingBean {

	public static final Pattern TYPE_END_PATTERN = Pattern.compile("^[\\s\\S]+\\.[A-Z]+\\w+$");
	public static final Pattern DOUBLE_PATTERN = Pattern.compile("^\\d+\\.\\d+$");

	public List<AbstractLexerAction> actions;
	@Autowired
	public BorderAction borderAction;

	@Override
	public void afterPropertiesSet() throws Exception {
		actions = SpringUtils.getBeansAndSort(AbstractLexerAction.class, BorderAction.class);
	}

	@Override
	public List<String> getWords(String text) {
		// 拆分方法体时，会传入空的text
		if (StringUtils.isEmpty(text)) {
			return new ArrayList<>();
		}
		// 处理字符串
		StringBuilder builder = new StringBuilder(text.trim());
		LexerContext context = new LexerContext(builder);
		handle(context, builder, new CursorAction(this));
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
		// 处理字符串
		StringBuilder builder = new StringBuilder(text.trim());
		LexerContext context = new LexerContext(builder, splitChars);
		handle(context, builder, new CursorAction(borderAction));
		// 校验
		Assert.notNull(context.words, "words of context can not be null!");
		// 继续拆分
		List<String> words = new ArrayList<>();
		for (String word : context.words) {
			words.addAll(getWords(word));
		}
		return words;
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

	@Override
	public boolean isTrigger(CharEvent event) {
		return true;
	}

	@Override
	public void handle(CharEvent event) {
		for (CharAction action : actions) {
			if (action.isTrigger(event)) {
				action.handle(event);
				return;
			}
		}
	}

}

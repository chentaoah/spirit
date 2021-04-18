package com.sum.spirit.core.lexer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import com.sum.spirit.common.enums.LiteralEnum;
import com.sum.spirit.common.enums.TypeEnum;
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
@DependsOn("springUtils")
public class CoreLexer extends AbstractCharsHandler implements Lexer, InitializingBean {

	public List<AbstractLexerAction> actions;
	@Autowired
	public BorderAction borderAction;

	@Override
	public void afterPropertiesSet() throws Exception {
		actions = SpringUtils.getBeansByExcludedTypes(AbstractLexerAction.class, BorderAction.class);
	}

	/**
	 * -1.整体替换语句中的某些区域
	 * -2.去掉多余的空格
	 * -3.利用空格，进行拆分
	 * -4.继续拆分带有“.”的单词
	 * -5.还原被替换的单词
	 */
	@Override
	public List<String> getWords(String text) {
		Assert.notBlank(text, "text cannot be blank!");
		StringBuilder builder = new StringBuilder(text.trim());
		LexerContext context = new LexerContext(builder);
		handle(context, builder, new CursorAction(this));
		text = LineUtils.mergeSpaces(context.builder.toString());
		List<String> words = new ArrayList<>(Arrays.asList(text.split(" ")));
		splitWords(words);
		restoreWords(words, context.replacedStrs);
		return words;
	}

	/**
	 * -根据弹栈规则，拆分分隔符和单词
	 */
	@Override
	public List<String> getSubWords(String text, Character... splitChars) {
		Assert.notBlank(text, "text cannot be blank!");
		StringBuilder builder = new StringBuilder(text.trim());
		LexerContext context = new LexerContext(builder, splitChars);
		handle(context, builder, new CursorAction(borderAction));
		Assert.notNull(context.words, "words of context cannot be null!");
		List<String> words = new ArrayList<>();
		for (String word : context.words) {
			if (word.length() == 1) {
				words.add(word);
			} else {
				words.addAll(getWords(word));
			}
		}
		return words;
	}

	/**
	 * -继续拆分带有“.”的单词
	 * 
	 * @param words
	 */
	public void splitWords(List<String> words) {
		for (int index = 0; index < words.size(); index++) {
			String word = words.get(index);
			if (word.indexOf(".") > 0 && !LiteralEnum.isDouble(word) && !TypeEnum.isTypeEnd(word)) {
				List<String> subWords = Arrays.asList(word.replaceAll("\\.", " .").split(" "));
				words.remove(index);
				words.addAll(index, subWords);
			}
		}
	}

	/**
	 * -替换之前被替换的单词
	 * 
	 * @param words
	 * @param replacedStrs
	 */
	public void restoreWords(List<String> words, Map<String, String> replacedStrs) {
		for (int index = 0; index < words.size(); index++) {
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

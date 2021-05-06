package com.sum.spirit.core.lexer;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.sum.spirit.common.pattern.LiteralPattern;
import com.sum.spirit.common.pattern.TypePattern;
import com.sum.spirit.core.api.Lexer;
import com.sum.spirit.core.api.LexerAction;
import com.sum.spirit.core.lexer.entity.CharEvent;
import com.sum.spirit.core.lexer.entity.CharsContext;
import com.sum.spirit.core.lexer.entity.CharsResult;
import com.sum.spirit.core.lexer.entity.LexerContext;
import com.sum.spirit.core.lexer.entity.LexerResult;
import com.sum.spirit.core.lexer.entity.Region;
import com.sum.spirit.core.lexer.utils.RegionUtils;
import com.sum.spirit.core.lexer.utils.RegionUtils.completedRegion;
import com.sum.spirit.core.lexer.entity.LexerResult.State;

public abstract class AbstractLexer extends AbstractCharsHandler implements Lexer {

	@Autowired
	public List<LexerAction> actions;

	@Override
	public CharsResult buildResult(CharsContext context, StringBuilder builder) {
		LexerContext lexerContext = (LexerContext) context;
		List<Region> regions = RegionUtils.completeRegions(builder, lexerContext.regions);// 使用标记收集算法后，补全未标记的部分
		List<String> words = RegionUtils.subRegions(builder, regions, this::addToWords);
		return new CharsResult(words);
	}

	public void addToWords(List<String> words, Region region, String text) {
		if (region instanceof completedRegion) {
			if (text.indexOf(".") > 0 && !LiteralPattern.isDouble(text) && !TypePattern.isTypeEnd(text)) {
				List<String> subWords = Arrays.asList(text.replaceAll("\\.", " .").split(" "));
				words.addAll(subWords);
				return;
			}
		}
		words.add(text);
	}

	@Override
	public boolean isTrigger(CharEvent event) {
		return true;
	}

	@Override
	public void handle(CharEvent event) {
		for (LexerAction action : actions) {
			if (action.isTrigger(event)) {
				LexerResult result = action.handle(event);
				if (result.region != null) {
					LexerContext context = (LexerContext) event.context;
					context.regions.add(result.region);
				}
				if (result.state == State.CONTINUE) {
					continue;
				} else if (result.state == State.BREAK) {
					break;
				}
			}
		}
	}

}

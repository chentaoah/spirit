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
import com.sum.spirit.core.lexer.entity.CharsState;
import com.sum.spirit.core.lexer.entity.CommonResult;
import com.sum.spirit.core.lexer.entity.LexerContext;
import com.sum.spirit.core.lexer.entity.Region;
import com.sum.spirit.core.lexer.utils.RegionUtils;
import com.sum.spirit.core.lexer.utils.RegionUtils.completedRegion;

public abstract class AbstractLexer extends AbstractCharsHandler implements Lexer {

	@Autowired
	public List<LexerAction> actions;

	@Override
	public boolean isTrigger(CharEvent event) {
		return true;
	}

	@Override
	public CommonResult handle(CharEvent event) {
		LexerContext context = (LexerContext) event.context;
		for (LexerAction action : actions) {
			if (action.isTrigger(event)) {
				CommonResult result = action.handle(event);
				Region region = result.get();
				if (region != null) {
					context.regions.add(region);
				}
				if (result.state == CharsState.CONTINUE) {
					continue;
				} else if (result.state == CharsState.BREAK) {
					break;
				}
			}
		}
		return null;
	}

	@Override
	public CommonResult buildResult(CharsContext context, StringBuilder builder) {
		LexerContext lexerContext = (LexerContext) context;
		List<Region> regions = RegionUtils.completeRegions(builder, lexerContext.regions);// 使用标记收集算法后，补全未标记的部分
		List<String> words = RegionUtils.subRegions(builder, regions, this::addToWords);
		return new CommonResult(words);
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

}

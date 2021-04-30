package com.sum.spirit.core.lexer;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import com.sum.spirit.common.enums.LiteralEnum;
import com.sum.spirit.common.enums.TypeEnum;
import com.sum.spirit.core.api.Lexer;
import com.sum.spirit.core.api.LexerAction;
import com.sum.spirit.core.lexer.entity.CharEvent;
import com.sum.spirit.core.lexer.entity.CharsContext;
import com.sum.spirit.core.lexer.entity.LexerContext;
import com.sum.spirit.core.lexer.entity.LexerResult;
import com.sum.spirit.core.lexer.entity.Region;
import com.sum.spirit.core.lexer.utils.RegionUtils;
import com.sum.spirit.core.lexer.entity.LexerResult.State;

public abstract class AbstractRegionLexer extends AbstractCharsHandler implements Lexer {

	@Autowired
	public List<LexerAction> actions;

	@Override
	public void handle(CharsContext context, StringBuilder builder) {
		super.handle(context, builder);
		completeRegions(context, builder);// 使用标记收集算法后，补全未标记的部分
	}

	public void completeRegions(CharsContext context, StringBuilder builder) {
		LexerContext lexerContext = (LexerContext) context;
		List<Region> regions = lexerContext.regions;
		Set<Region> completedRegions = new HashSet<Region>();
		regions = RegionUtils.completeRegions(builder, regions, region -> completedRegions.add(region));
		lexerContext.words = RegionUtils.subRegions(builder, regions, (words, region, text) -> addToWords(words, completedRegions, region, text));
	}

	public void addToWords(List<String> words, Set<Region> completedRegions, Region region, String text) {
		if (completedRegions.contains(region)) {
			if (text.indexOf(".") > 0 && !LiteralEnum.isDouble(text) && !TypeEnum.isTypeEnd(text)) {
				List<String> subWords = Arrays.asList(text.replaceAll("\\.", " .").split(" "));
				words.addAll(subWords);
			} else {
				words.add(text);
			}
		} else {
			words.add(text);
		}
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

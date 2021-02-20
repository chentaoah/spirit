package com.sum.spirit.core.lexer.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.sum.spirit.common.utils.LineUtils;
import com.sum.spirit.core.api.LexerAction;
import com.sum.spirit.core.lexer.entity.Region;

import cn.hutool.core.lang.Assert;

public abstract class AbstractLexerAction implements LexerAction {

	public Region findRegion(StringBuilder builder, int fromIndex, char leftChar, char rightChar) {
		int endIndex = LineUtils.findEndIndex(builder, fromIndex, leftChar, rightChar);
		return endIndex != -1 ? new Region(fromIndex, endIndex + 1) : null;
	}

	public Region mergeRegions(List<Region> regions) {
		Region finalRegion = new Region(-1, -1);
		for (Region region : regions) {
			Assert.notNull(region, "Region can not be null!");
			if (finalRegion.startIndex == -1 || region.startIndex < finalRegion.startIndex) {
				finalRegion.startIndex = region.startIndex;
			}
			if (finalRegion.endIndex == -1 || region.endIndex > finalRegion.endIndex) {
				finalRegion.endIndex = region.endIndex;
			}
		}
		Assert.isTrue(finalRegion.startIndex != -1 && finalRegion.endIndex != -1, "The index of region can not be -1!");
		return finalRegion;
	}

	public List<String> splitRegion(StringBuilder builder, Region region) {
		List<String> words = new ArrayList<>();
		words.add(builder.substring(region.startIndex, region.startIndex + 1));
		if (region.endIndex - 1 > region.startIndex + 1) {
			String content = builder.substring(region.startIndex + 1, region.endIndex - 1);
			if (StringUtils.isNotBlank(content)) {
				words.add(content);
			}
		}
		words.add(builder.substring(region.endIndex - 1, region.endIndex));
		return words;
	}

	public String subRegion(StringBuilder builder, Region region) {
		return builder.substring(region.startIndex, region.endIndex);
	}

	public String replaceRegion(StringBuilder builder, Region region, String markName) {
		if (region == null) {
			return null;
		}
		String content = builder.substring(region.startIndex, region.endIndex);
		builder.replace(region.startIndex, region.endIndex, " " + markName + " ");
		return content;
	}

	public void replaceRegion(StringBuilder builder, Region region, String markName, Map<String, String> replacedStrs) {
		String content = replaceRegion(builder, region, markName);
		if (StringUtils.isNotBlank(content) && replacedStrs != null) {
			replacedStrs.put(markName, content);
		}
	}

}

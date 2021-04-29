package com.sum.spirit.core.lexer.action;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.sum.spirit.common.utils.LineUtils;
import com.sum.spirit.common.utils.ListUtils;
import com.sum.spirit.core.api.CharAction;
import com.sum.spirit.core.lexer.entity.Region;

import cn.hutool.core.lang.Assert;

public abstract class AbstractLexerAction implements CharAction {

	public Region findRegion(StringBuilder builder, int fromIndex, char leftChar, char rightChar) {
		int endIndex = LineUtils.findEndIndex(builder, fromIndex, leftChar, rightChar);
		return endIndex != -1 ? new Region(fromIndex, endIndex + 1) : null;
	}

	public String subRegion(StringBuilder builder, Region region) {
		return builder.substring(region.startIndex, region.endIndex);
	}

	public Region mergeRegions(List<Region> regions) {
		Assert.notEmpty(regions, "The regions cannot be empty!");
		Region startRegion = ListUtils.findOneByScore(regions, region -> 0 - region.startIndex);
		Region endRegion = ListUtils.findOneByScore(regions, region -> region.endIndex);
		return new Region(startRegion.startIndex, endRegion.endIndex);
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

package com.sum.spirit.core.lexer.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.sum.spirit.common.utils.LineUtils;
import com.sum.spirit.common.utils.ListUtils;
import com.sum.spirit.core.lexer.entity.Region;

import cn.hutool.core.lang.Assert;

public class RegionUtils {

	public static Region findRegion(StringBuilder builder, int fromIndex, char leftChar, char rightChar) {
		int endIndex = LineUtils.findEndIndex(builder, fromIndex, leftChar, rightChar);
		return endIndex != -1 ? new Region(fromIndex, endIndex + 1) : null;
	}

	public static String subRegion(StringBuilder builder, Region region) {
		return builder.substring(region.startIndex, region.endIndex);
	}

	public static Region mergeRegions(List<Region> regions) {
		Assert.notEmpty(regions, "The regions cannot be empty!");
		Region startRegion = ListUtils.findOneByScore(regions, region -> 0 - region.startIndex);
		Region endRegion = ListUtils.findOneByScore(regions, region -> region.endIndex);
		return new Region(startRegion.startIndex, endRegion.endIndex);
	}

	public static List<Region> completeRegions(StringBuilder builder, List<Region> regions, Consumer<Region> consumer) {
		List<Region> newRegions = new ArrayList<>();
		int lastIndex = 0;
		for (Region region : regions) {
			if (region.startIndex > lastIndex) {
				Region newRegion = new Region(lastIndex, region.startIndex);
				newRegions.add(newRegion);
				if (consumer != null) {
					consumer.accept(newRegion);
				}
			}
			newRegions.add(region);
			lastIndex = region.endIndex;
		}
		if (lastIndex < builder.length()) {
			Region newRegion = new Region(lastIndex, builder.length());
			newRegions.add(newRegion);
			if (consumer != null) {
				consumer.accept(newRegion);
			}
		}
		return newRegions;
	}

	public static List<Region> completeRegions(StringBuilder builder, List<Region> regions) {
		return completeRegions(builder, regions, null);
	}

	public static List<String> subRegions(StringBuilder builder, List<Region> regions, Consumer0<Region> consumer) {
		List<String> words = new ArrayList<>();
		for (Region region : regions) {
			String text = subRegion(builder, region);
			if (StringUtils.isNotBlank(text)) {
				consumer.accept(words, region, text.trim());
			}
		}
		return words;
	}

	public static interface Consumer<T> {
		void accept(T t);
	}

	public static interface Consumer0<T> {
		void accept(List<String> words, T t, String text);
	}

}

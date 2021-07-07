package com.gitee.spirit.core.lexer.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.gitee.spirit.common.utils.LineUtils;
import com.gitee.spirit.common.utils.ListUtils;
import com.gitee.spirit.core.lexer.entity.Region;

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
        Region startRegion = ListUtils.findOneByScore(regions, region -> -region.startIndex);
        Region endRegion = ListUtils.findOneByScore(regions, region -> region.endIndex);
        return new Region(startRegion.startIndex, endRegion.endIndex);
    }

    public static List<Region> completeRegions(StringBuilder builder, List<Region> regions) {
        List<Region> completedRegions = new ArrayList<>();
        int lastIndex = 0;
        for (Region region : regions) {
            if (region.startIndex > lastIndex) {
                Region newRegion = new completedRegion(lastIndex, region.startIndex);
                completedRegions.add(newRegion);
            }
            completedRegions.add(region);
            lastIndex = region.endIndex;
        }
        if (lastIndex < builder.length()) {
            Region newRegion = new completedRegion(lastIndex, builder.length());
            completedRegions.add(newRegion);
        }
        return completedRegions;
    }

    public static List<String> subRegions(StringBuilder builder, List<Region> regions, Consumer consumer) {
        List<String> words = new ArrayList<>();
        for (Region region : regions) {
            String text = subRegion(builder, region);
            if (StringUtils.isNotBlank(text)) {
                consumer.accept(words, region, text.trim());
            }
        }
        return words;
    }

    public interface Consumer {
        void accept(List<String> words, Region region, String text);
    }

    public static class completedRegion extends Region {
        public completedRegion(int startIndex, int endIndex) {
            super(startIndex, endIndex);
        }
    }

}

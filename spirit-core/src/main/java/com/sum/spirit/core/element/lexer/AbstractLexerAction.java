package com.sum.spirit.core.element.lexer;

import java.util.Map;

import com.sum.spirit.api.LexerAction;
import com.sum.spirit.utils.LineUtils;

public abstract class AbstractLexerAction implements LexerAction {

	public Region findRegion(StringBuilder builder, int fromIndex, char leftChar, char rightChar) {
		int endIndex = LineUtils.findEndIndex(builder, fromIndex, leftChar, rightChar);
		return endIndex != -1 ? new Region(fromIndex, endIndex + 1) : null;
	}

	public Region mergeRegions(Region... regions) {
		Region finalRegion = new Region(-1, -1);
		for (Region region : regions) {
			if (region == null) {
				continue;
			}
			if (finalRegion.startIndex == -1 || region.startIndex < finalRegion.startIndex) {
				finalRegion.startIndex = region.startIndex;
			}
			if (finalRegion.endIndex == -1 || region.endIndex > finalRegion.endIndex) {
				finalRegion.endIndex = region.endIndex;
			}
		}
		return finalRegion;
	}

	public int replaceStr(StringBuilder builder, Region region, String markName, Map<String, String> replacedStrs) {
		if (region == null) {
			return 0;
		}
		String content = builder.substring(region.startIndex, region.endIndex);
		if (replacedStrs != null) {
			replacedStrs.put(markName, content);
		}
		markName = " " + markName + " ";
		builder.replace(region.startIndex, region.endIndex, markName);
		return markName.length() - region.size();
	}

}

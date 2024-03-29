package com.gitee.spirit.core.lexer.action;

import java.util.List;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.gitee.spirit.common.entity.Result;
import com.gitee.spirit.common.enums.StateEnum;
import com.gitee.spirit.common.pattern.TypePattern;
import com.gitee.spirit.common.utils.ListUtils;
import com.gitee.spirit.core.api.LexerAction;
import com.gitee.spirit.core.lexer.entity.CharEvent;
import com.gitee.spirit.core.lexer.entity.LexerContext;
import com.gitee.spirit.core.lexer.entity.Region;
import com.gitee.spirit.core.lexer.utils.RegionUtils;

@Component
@Order(-80)
public class RegionAction implements LexerAction {

    @Override
    public boolean isTrigger(CharEvent event) {
        LexerContext context = (LexerContext) event.context;
        StringBuilder builder = context.builder;
        char ch = event.ch;

        if (context.index == builder.length() - 1) {
            return false;

        } else if (ch == '"' || ch == '\'' || ch == '{' || ch == '(' || ch == '[') {
            return true;

        } else if (ch == '<') {
            if (context.startIndex >= 0) {
                char d = builder.charAt(context.startIndex);
                if (d >= 'A' && d <= 'Z') {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public Result handle(CharEvent event) {
        LexerContext context = (LexerContext) event.context;
        StringBuilder builder = context.builder;
        char ch = event.ch;

        if (ch == '"') {
            Region region = RegionUtils.findRegion(builder, context.index, '"', '"');
            return pushStack(event, ListUtils.asListNonNull(region));

        } else if (ch == '\'') {
            Region region = RegionUtils.findRegion(builder, context.index, '\'', '\'');
            return pushStack(event, ListUtils.asListNonNull(region));

        } else if (ch == '{') {
            Region region0 = context.startIndex >= 0 ? new Region(context.startIndex, context.index) : null;
            Region region1 = RegionUtils.findRegion(builder, context.index, '{', '}');
            return pushStack(event, ListUtils.asListNonNull(region0, region1));

        } else if (ch == '(') {
            Region region0 = context.startIndex >= 0 ? new Region(context.startIndex, context.index) : null;
            Region region1 = RegionUtils.findRegion(builder, context.index, '(', ')');
            return pushStack(event, ListUtils.asListNonNull(region0, region1));

        } else if (ch == '[') {
            Region region0 = context.startIndex >= 0 ? new Region(context.startIndex, context.index) : null;
            region0 = region0 != null && TypePattern.isTypePrefix(RegionUtils.subRegion(builder, region0)) ? region0 : null;
            Region region1 = RegionUtils.findRegion(builder, context.index, '[', ']');
            Region region2 = null;
            if (region0 != null) {
                if (isCharAt(builder, region1.endIndex, '{')) {
                    region2 = RegionUtils.findRegion(builder, region1.endIndex, '{', '}');

                } else if (isCharAt(builder, region1.endIndex, ' ') && isCharAt(builder, region1.endIndex + 1, '{')) {
                    region2 = RegionUtils.findRegion(builder, region1.endIndex + 1, '{', '}');
                }
            }
            return pushStack(event, ListUtils.asListNonNull(region0, region1, region2));

        } else if (ch == '<') {
            Region region0 = context.startIndex >= 0 ? new Region(context.startIndex, context.index) : null;
            Region region1 = RegionUtils.findRegion(builder, context.index, '<', '>');
            Region region2 = isCharAt(builder, region1.endIndex, '(') ? RegionUtils.findRegion(builder, region1.endIndex, '(', ')') : null;
            return pushStack(event, ListUtils.asListNonNull(region0, region1, region2));
        }

        throw new RuntimeException("Unhandled branch!");
    }

    public boolean isCharAt(StringBuilder builder, int index, char ch) {
        return index < builder.length() && builder.charAt(index) == ch;
    }

    public Result pushStack(CharEvent event, List<Region> regions) {
        Region mergedRegion = RegionUtils.mergeRegions(regions);
        return new Result(StateEnum.SKIP.ordinal(), mergedRegion);
    }

}

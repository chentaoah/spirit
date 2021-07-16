package com.gitee.spirit.core.lexer;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.gitee.spirit.common.entity.Result;
import com.gitee.spirit.common.enums.StateEnum;
import com.gitee.spirit.common.pattern.AccessPattern;
import com.gitee.spirit.common.pattern.LiteralPattern;
import com.gitee.spirit.core.api.Lexer;
import com.gitee.spirit.core.api.LexerAction;
import com.gitee.spirit.core.lexer.entity.CharEvent;
import com.gitee.spirit.core.lexer.entity.CharsContext;
import com.gitee.spirit.core.lexer.entity.LexerContext;
import com.gitee.spirit.core.lexer.entity.Region;
import com.gitee.spirit.core.lexer.utils.RegionUtils;
import com.gitee.spirit.core.lexer.utils.RegionUtils.completedRegion;

public abstract class AbstractLexer extends AbstractCharsHandler implements Lexer {

    @Autowired
    public List<LexerAction> actions;

    @Override
    public boolean isTrigger(CharEvent event) {
        LexerContext context = (LexerContext) event.context;
        List<Region> regions = context.regions;
        if (regions != null && !regions.isEmpty()) {
            for (int index = regions.size() - 1; index >= 0; index--) {
                Region existRegion = regions.get(index);
                if (existRegion.contains(context.index)) {
                    return false;
                } else if (context.index >= existRegion.endIndex) {
                    return true;
                }
            }
        }
        return true;
    }

    @Override
    public Result handle(CharEvent event) {
        LexerContext context = (LexerContext) event.context;
        for (LexerAction action : actions) {
            if (action.isTrigger(event)) {
                Result result = action.handle(event);
                if (result != null) {
                    if (result.data != null) {
                        if (result.data instanceof Region) {
                            appendRegion(context, result.get());

                        } else if (result.data instanceof List) {
                            List<Region> regions = result.get();
                            regions.forEach(region -> appendRegion(context, region));
                        }
                    }
                    if (result.code == StateEnum.SKIP.ordinal()) {
                        break;
                    } else if (result.code == StateEnum.RESET.ordinal() || result.code == StateEnum.BREAK.ordinal()) {
                        return result;
                    }
                }
            }
        }
        return null;
    }

    public void appendRegion(LexerContext context, Region region) {
        List<Region> regions = context.regions;
        if (regions.isEmpty()) {
            regions.add(region);
            return;
        }
        for (int index = regions.size() - 1; index >= 0; index--) {
            Region existRegion = regions.get(index);
            if (region.isOverlap(existRegion)) {
                throw new RuntimeException("There are overlapping regions!");
            }
            if (region.isAfter(existRegion)) {
                regions.add(index + 1, region);
                break;
            }
        }
    }

    @Override
    public Result buildResult(CharsContext context, StringBuilder builder) {
        LexerContext lexerContext = (LexerContext) context;
        List<Region> regions = RegionUtils.completeRegions(builder, lexerContext.regions);// 使用标记收集算法后，补全未标记的部分
        List<String> words = RegionUtils.subRegions(builder, regions, this::addToWords);
        return new Result(words);
    }

    public void addToWords(List<String> words, Region region, String text) {
        if (region instanceof completedRegion) {
            if (text.indexOf(".") > 0 && !LiteralPattern.isDouble(text) && !AccessPattern.isAccessPath(text)) {
                List<String> subWords = Arrays.asList(text.replaceAll("\\.", " .").split(" "));
                words.addAll(subWords);
                return;
            }
        }
        words.add(text);
    }

}

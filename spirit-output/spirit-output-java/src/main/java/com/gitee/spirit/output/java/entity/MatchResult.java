package com.gitee.spirit.output.java.entity;

import com.gitee.spirit.core.clazz.entity.IType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MatchResult {
    public List<IType> parameterTypes;
    public Map<String, IType> qualifyingTypes;
}

package com.gitee.spirit.core.compile.entity;

import com.gitee.spirit.core.clazz.entity.IMethod;
import com.gitee.spirit.core.clazz.entity.IType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MatchResult {
    public IMethod method;
    public List<IType> parameterTypes;
}

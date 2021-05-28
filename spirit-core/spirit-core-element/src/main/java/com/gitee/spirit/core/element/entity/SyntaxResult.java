package com.gitee.spirit.core.element.entity;

import com.gitee.spirit.common.enums.SyntaxEnum;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SyntaxResult {
	public SyntaxEnum syntax;
	public SyntaxTree syntaxTree;
}

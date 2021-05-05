package com.sum.spirit.core.element.entity;

import com.sum.spirit.common.enums.SyntaxEnum;

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

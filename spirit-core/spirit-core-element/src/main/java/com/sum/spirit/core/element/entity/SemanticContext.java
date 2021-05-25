package com.sum.spirit.core.element.entity;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SemanticContext {

	public List<String> words;
	public int index = -1;
	public boolean substatement = false;
	public boolean insideType = false;

	public SemanticContext(boolean substatement, boolean insideType) {
		this.substatement = substatement;
		this.insideType = insideType;
	}

}

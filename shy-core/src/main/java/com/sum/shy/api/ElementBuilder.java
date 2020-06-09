package com.sum.shy.api;

import com.sum.pisces.api.Service;
import com.sum.shy.element.Element;
import com.sum.shy.element.Line;

@Service("elementBuilder")
public interface ElementBuilder {

	Element buildElement(Line line);

}

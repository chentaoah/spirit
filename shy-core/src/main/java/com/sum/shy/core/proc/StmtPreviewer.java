package com.sum.shy.core.proc;

import com.sum.shy.core.clazz.IClass;
import com.sum.shy.core.doc.Element;

public class StmtPreviewer {

	public static void preview(IClass clazz, Element element) {

		if (element.isDeclare()) {// String text
			
		} else if (element.isDeclareAssign()) {// String text = "abc"

		} else if (element.isAssign()) {// text = "abc"

		} else if (element.isCatch()) {// }catch Exception e{

		} else if (element.isForIn()) {// for item in list {

		} else if (element.isFor()) {// for i=0; i<100; i++ {
			
		}

	}

}

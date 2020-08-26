package com.sum.spirit.api;

import java.io.File;

import com.sum.spirit.pojo.element.Document;

public interface DocumentReader {

	Document readFile(File file);

}

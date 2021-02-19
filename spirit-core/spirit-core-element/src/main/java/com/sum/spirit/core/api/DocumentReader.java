package com.sum.spirit.core.api;

import java.io.InputStream;

import com.sum.spirit.core.element.entity.Document;

public interface DocumentReader {

	Document read(String fileName, InputStream input);

}

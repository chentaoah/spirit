package com.gitee.spirit.core.api;

import java.io.InputStream;

import com.gitee.spirit.core.element.entity.Document;

public interface DocumentReader {

	Document read(String fileName, InputStream input);

}

package com.sum.spirit.api;

import java.io.InputStream;

import com.sum.spirit.common.entity.Document;

public interface DocumentReader {

	Document read(String fileName, InputStream input);

}

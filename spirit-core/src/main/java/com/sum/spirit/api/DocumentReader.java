package com.sum.spirit.api;

import java.io.File;

import com.sum.pisces.api.annotation.Service;
import com.sum.spirit.pojo.element.Document;

@Service("document_reader")
public interface DocumentReader {

	Document readFile(File file);

}

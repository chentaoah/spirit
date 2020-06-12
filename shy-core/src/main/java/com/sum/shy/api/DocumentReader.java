package com.sum.shy.api;

import java.io.File;

import com.sum.pisces.api.Service;
import com.sum.shy.pojo.element.Document;

@Service("document_reader")
public interface DocumentReader {

	Document read(File file);

}

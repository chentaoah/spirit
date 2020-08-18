package com.sum.soon.api;

import java.io.File;

import com.sum.pisces.api.annotation.Service;
import com.sum.soon.pojo.element.Document;

@Service("document_reader")
public interface DocumentReader {

	Document read(File file);

}

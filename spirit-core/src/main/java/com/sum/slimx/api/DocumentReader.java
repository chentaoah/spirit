package com.sum.slimx.api;

import java.io.File;

import com.sum.pisces.api.annotation.Service;
import com.sum.slimx.pojo.element.Document;

@Service("document_reader")
public interface DocumentReader {

	Document read(File file);

}

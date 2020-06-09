package com.sum.shy.api;

import java.io.File;

import com.sum.pisces.api.Service;
import com.sum.shy.element.Document;

@Service("documentReader")
public interface DocumentReader {

	Document readDocument(File file);

}
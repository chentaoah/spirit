package com.sum.shy.api;

import java.io.File;
import java.io.IOException;

import com.sum.shy.clazz.Clazz;

public interface ClassReader {

	Clazz readFile(File file) throws IOException;
}

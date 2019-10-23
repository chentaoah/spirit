package com.sum.shy.api;

import java.io.File;
import java.io.IOException;

import com.sum.shy.entity.Class;

public interface ClassReader {

	Class readFile(File file) throws IOException;
}

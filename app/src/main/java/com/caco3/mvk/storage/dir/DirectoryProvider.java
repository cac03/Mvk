package com.caco3.mvk.storage.dir;

import java.io.File;
import java.io.IOException;


public interface DirectoryProvider {
  File getDirectory() throws IOException;
  boolean isDirectoryReadable();
  boolean isDirectoryWritable();
}

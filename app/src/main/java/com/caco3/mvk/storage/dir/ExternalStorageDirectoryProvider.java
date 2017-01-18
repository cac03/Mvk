package com.caco3.mvk.storage.dir;


import android.os.Environment;

import java.io.File;

public class ExternalStorageDirectoryProvider implements DirectoryProvider {
  @Override
  public File getDirectory() {
    return Environment.getExternalStorageDirectory();
  }

  @Override
  public boolean isDirectoryReadable() {
    String state = Environment.getExternalStorageState();
    return Environment.MEDIA_MOUNTED.equals(state) ||
            Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
  }

  @Override
  public boolean isDirectoryWritable() {
    String state = Environment.getExternalStorageState();
    return Environment.MEDIA_MOUNTED.equals(state);
  }
}

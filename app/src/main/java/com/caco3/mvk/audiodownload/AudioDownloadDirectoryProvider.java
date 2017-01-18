package com.caco3.mvk.audiodownload;


import com.caco3.mvk.storage.dir.DirectoryProvider;

import java.io.File;
import java.io.IOException;

import static com.caco3.mvk.util.Preconditions.checkNotNull;

public class AudioDownloadDirectoryProvider implements DirectoryProvider {
  private static final String DIRECTORY_NAME = "Mvk Audios";
  private DirectoryProvider baseDirectoryProvider;

  public AudioDownloadDirectoryProvider(DirectoryProvider baseDirectoryProvider) {
    this.baseDirectoryProvider
            = checkNotNull(baseDirectoryProvider, "baseDirectoryProvider == null");
  }

  @Override
  public File getDirectory() throws IOException {
    File base = baseDirectoryProvider.getDirectory();
    File dir = new File(base, DIRECTORY_NAME);
    if (!dir.exists()) {
      return createDirectoryOrThrow(dir);
    } else {
      return dir;
    }
  }

  private File createDirectoryOrThrow(File file) throws IOException {
    if (!file.mkdirs()) {
      throw new IOException(String.format("Unable to create directory '%s'", file));
    }

    return file;
  }

  @Override
  public boolean isDirectoryReadable() {
    return baseDirectoryProvider.isDirectoryReadable();
  }

  @Override
  public boolean isDirectoryWritable() {
    return baseDirectoryProvider.isDirectoryWritable();
  }
}

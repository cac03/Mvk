package com.caco3.mvk.storage.file;

import com.caco3.mvk.util.Strings;

import java.io.File;
import java.io.IOException;

import static com.caco3.mvk.util.Preconditions.checkArgument;
import static com.caco3.mvk.util.Preconditions.checkState;

/**
 * An abstraction over a file that might be downloaded by this app.
 * We don't want to give access to a file while it's not downloaded.
 * Thus, we will download a file with temporary name and the restore it,
 * {@link #prepareForDownload()}, {@link #restoreAfterDownload()};
 */
public abstract class AbstractDownloadableMvkFile {
  private static final String EXTENSION_SEPARATOR = ".";

  private final File directory;
  private File downloadableFile;

  protected AbstractDownloadableMvkFile(File directory) {
    this.directory = directory;
    checkArgument(directory.exists(), String.format("directory not exist('%s')", directory));
    checkArgument(directory.isDirectory(),
            String.format("directory actually is not a directory ('%s')", directory));
  }

  public File prepareForDownload() throws IOException {
    createFile();
    return getFile();
  }

  public File restoreAfterDownload() {
    renameBack();
    return getFile();
  }

  public boolean remove() {
    return getFile().delete();
  }

  private boolean renameBack() {
    checkState(downloadableFile != null, "renameBack() called, but downloadableFile == null");
    String newFileName = prepareFileName() + EXTENSION_SEPARATOR + getRealExtension();
    File renameTo = new File(directory, newFileName);
    boolean success =  downloadableFile.renameTo(renameTo);
    downloadableFile = renameTo;

    return success;
  }

  private File getFile() {
    return new File(downloadableFile.getPath());
  }


  private boolean createFile() throws IOException {
    String filename = prepareFileName() + EXTENSION_SEPARATOR + getTemporaryExtension();
    downloadableFile = new File(directory, filename);
    return downloadableFile.createNewFile();
  }

  private String prepareFileName() {
    String dirtyFileName = Strings.requireNotNullAndNotEmpty(generateFileName());
    return removeInvalidSymbolsFromFileName(dirtyFileName);
  }

  private String removeInvalidSymbolsFromFileName(String filename) {
    return filename.replaceAll("/", "");
  }


  protected abstract String generateFileName();
  protected abstract String getRealExtension();

  protected String getTemporaryExtension() {
    return "mvkFile";
  }
}

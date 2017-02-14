package com.caco3.mvk.util.io;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static com.caco3.mvk.util.Preconditions.checkArgument;
import static com.caco3.mvk.util.Preconditions.checkNotNull;
import static com.caco3.mvk.util.Preconditions.checkState;


public class BytesTransfer {
  private static final int DEFAULT_BUFFER_SIZE = 8192;
  final OutputStream out;
  final InputStream in;
  private final byte[] buffer;
  private boolean transferCalled = false;

  public BytesTransfer(OutputStream out, InputStream in, int bufferSize) {
    checkNotNull(out, "out == null");
    checkNotNull(in, "source == null");
    checkArgument(bufferSize > 0, String.format("chunkSize is too small: '%d'", bufferSize));
    if (out instanceof BufferedOutputStream) {
      this.out = out;
    } else {
      this.out = new BufferedOutputStream(out);
    }
    if (in instanceof BufferedInputStream) {
      this.in = in;
    } else {
      this.in = new BufferedInputStream(in);
    }
    this.buffer = new byte[bufferSize];
  }

  public BytesTransfer(OutputStream out, InputStream in) {
    this(out, in, DEFAULT_BUFFER_SIZE);
  }

  public void transfer() throws IOException {
    transfer(null);
  }

  public void transfer(ProgressListener listener) throws IOException {
    checkState(!transferCalled, "transfer was called previously. Cannot transfer bytes again");
    transferCalled = true;
    try {
      int readBytes;
      long startNanos = System.nanoTime();
      long readTotal = 0;
      long bytesTotal = in.available();
      while ((readBytes = in.read(buffer)) != -1) {
        out.write(buffer, 0, readBytes);

        if (listener != null) {
          readTotal += readBytes;
          updateListener(listener, startNanos, readTotal, bytesTotal);
        }

      }
    } finally {
      Closeables.closeOrLog(in);
      Closeables.closeOrLog(out);
    }
  }

  private void updateListener(ProgressListener listener, long startNanos,
                              long readTotal, long bytesTotal) {
    long nanosElapsed = System.nanoTime() - startNanos;
    listener.update(readTotal, bytesTotal, nanosElapsed);
  }

  public interface ProgressListener {
    void update(long bytesRead, long bytesTotal, long nanosElapsed);
  }
}

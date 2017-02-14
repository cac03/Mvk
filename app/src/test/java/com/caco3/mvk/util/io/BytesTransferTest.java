package com.caco3.mvk.util.io;

import org.junit.Test;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class BytesTransferTest {
  private static final InputStream EMPTY_INPUT_STREAM = new ByteArrayInputStream(new byte[0]);
  private static final OutputStream EMPTY_OUTPUT_STREAM = new ByteArrayOutputStream();

  private final Random random = new Random();

  @Test(expected = NullPointerException.class)
  public void outIsNull_npeThrown() {
    new BytesTransfer(null, EMPTY_INPUT_STREAM);
  }

  @Test(expected = NullPointerException.class)
  public void inIsNull_npeThrown() {
    new BytesTransfer(EMPTY_OUTPUT_STREAM, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void chunkSizeIsNegative_iaeThrown() {
    new BytesTransfer(EMPTY_OUTPUT_STREAM, EMPTY_INPUT_STREAM, -1);
  }

  @Test
  public void transferringBytesWorksCorrectly() throws Exception {
    byte[] expected = new byte[666];
    random.nextBytes(expected);
    ByteArrayInputStream in = new ByteArrayInputStream(expected);
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    new BytesTransfer(out, in)
            .transfer();
    byte[] actual = out.toByteArray();

    assertThat(expected)
            .isEqualTo(actual);
  }

  @Test
  public void transferringBytesWithListenerWorksCorrectly() throws Exception {
    final int BYTES_COUNT = 1024 * 1024;
    byte[] expected = new byte[BYTES_COUNT];
    random.nextBytes(expected);
    ByteArrayInputStream in = new ByteArrayInputStream(expected);
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    final AtomicLong lastBytesCount = new AtomicLong();
    new BytesTransfer(out, in)
            .transfer(new BytesTransfer.ProgressListener() {
              @Override
              public void update(long bytesRead, long bytesTotal, long nanosElapsed) {
                lastBytesCount.set(bytesTotal);
              }
            });

    assertThat(lastBytesCount.get())
            .isEqualTo(BYTES_COUNT);
  }

  @Test(expected = IllegalStateException.class)
  public void transferCalledTwice_iseThrown() throws Exception {
    BytesTransfer bytesTransfer = new BytesTransfer(EMPTY_OUTPUT_STREAM, EMPTY_INPUT_STREAM);
    bytesTransfer.transfer();
    bytesTransfer.transfer();
  }

  @Test
  public void outIsClosedAfterTransferringBytes() throws Exception {
    final AtomicBoolean closeCalled = new AtomicBoolean();
    new BytesTransfer(new OutputStream() {
      @Override
      public void write(int b) throws IOException {
      }

      @Override
      public void close() throws IOException {
        closeCalled.set(true);
      }
    }, EMPTY_INPUT_STREAM).transfer();

    assertThat(closeCalled.get())
            .isTrue();
  }

  @Test
  public void inIsClosedAfterTransferringBytes() throws Exception {
    final AtomicBoolean closeCalled = new AtomicBoolean();
    new BytesTransfer(EMPTY_OUTPUT_STREAM, new InputStream() {
      @Override
      public int read() throws IOException {
        return -1;
      }

      @Override
      public void close() throws IOException {
        closeCalled.set(true);
      }
    }).transfer();

    assertThat(closeCalled.get())
            .isTrue();
  }

  @Test
  public void internallyStreamsAreBuffered() {
    BytesTransfer bytesTransfer = new BytesTransfer(EMPTY_OUTPUT_STREAM, EMPTY_INPUT_STREAM);
    assertThat(bytesTransfer.in)
            .isInstanceOf(BufferedInputStream.class);
    assertThat(bytesTransfer.out)
            .isInstanceOf(BufferedOutputStream.class);
  }
}

package com.caco3.mvk.network;


import com.caco3.mvk.Rxs;
import com.caco3.mvk.util.io.Closeables;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.mockito.MockitoAnnotations;

import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okio.Buffer;
import okio.Okio;
import okio.Source;
import rx.Subscriber;
import rx.exceptions.Exceptions;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FileDownloaderTest {
  private static final String DUMMY_FILE_NAME = "downloadable";
  private static final int FILE_SIZE = 8192 * 4;
  @Rule
  public final MockWebServer mockWebServer = new MockWebServer();
  @Rule
  public TemporaryFolder temporaryFolder = new TemporaryFolder();

  private final OkHttpClient okHttpClient = new OkHttpClient();
  private final HttpUrl DUMMY_URL = mockWebServer.url("dummy.url");


  private File file;
  private final Random random = new Random();
  private final Buffer downloadableContent = generateFileContent();

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
    Rxs.setUpRx();
  }

  @After
  public void tearDown() {
    Rxs.tearDownRx();
  }

  @Test
  public void everythingIsOk_fileDownloaded() throws Exception {
    file = temporaryFolder.newFile(DUMMY_FILE_NAME);

    mockWebServer.enqueue(new MockResponse().setBody(downloadableContent));
    final AtomicBoolean onCompletedCalled = new AtomicBoolean(false);
    new FileDownloader(okHttpClient, DUMMY_URL, file)
            .download()
            .subscribe(new Subscriber<FileDownloader.DownloadProgress>() {
              @Override
              public void onCompleted() {
                onCompletedCalled.set(true);
                assertEquals(readFileContent(), downloadableContent);
              }

              @Override
              public void onError(Throwable e) {
                throw Exceptions.propagate(e);
              }

              @Override
              public void onNext(FileDownloader.DownloadProgress downloadProgress) {
              }
            });

    assertTrue(onCompletedCalled.get());
  }

  @Test(expected = IllegalArgumentException.class)
  public void fileNotExists_iaeThrown() {
    file = mock(File.class);
    when(file.exists()).thenReturn(false);
    new FileDownloader(okHttpClient, DUMMY_URL, new File(""));
  }

  @Test
  public void serverReturns404_ioExceptionReceivedInOnError() throws Exception {
    file = temporaryFolder.newFile(DUMMY_FILE_NAME);

    mockWebServer.enqueue(new MockResponse().setResponseCode(404));
    final AtomicBoolean ioExceptionReceived = new AtomicBoolean(false);
    new FileDownloader(okHttpClient, DUMMY_URL, file)
            .download()
            .subscribe(new Subscriber<FileDownloader.DownloadProgress>() {
              @Override
              public void onCompleted() {
                fail("onCompleted called");
              }

              @Override
              public void onError(Throwable e) {
                if (e instanceof IOException) {
                  ioExceptionReceived.set(true);
                }
              }

              @Override
              public void onNext(FileDownloader.DownloadProgress downloadProgress) {
                fail("onNext");
              }
            });
    assertTrue(ioExceptionReceived.get());
  }



  private Buffer readFileContent() {
    Buffer buffer = new Buffer();
    Source source = null;
    try {
      source = Okio.source(file);
      long read;
      do {
        read = source.read(buffer, 2048);
      } while (read != -1);
    } catch (IOException e) {
      throw new RuntimeException(e);
    } finally {
      Closeables.closeOrLog(source);
      Closeables.closeOrLog(buffer);
    }

    return buffer;
  }

  private Buffer generateFileContent() {
    byte[] sink = new byte[FILE_SIZE];
    random.nextBytes(sink);

    Buffer buffer = new Buffer();
    buffer.write(sink);
    return buffer;
  }
}

package com.caco3.mvk.audiodownload;


import com.caco3.mvk.Rxs;
import com.caco3.mvk.data.audio.AudiosRepository;
import com.caco3.mvk.myaudios.AudiosGenerator;
import com.caco3.mvk.timber.SystemOutTree;
import com.caco3.mvk.vk.audio.Audio;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.File;
import java.io.FileInputStream;
import java.util.concurrent.atomic.AtomicBoolean;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okio.ByteString;
import okio.Okio;
import timber.log.Timber;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

public class AudioDownloadPresenterImplTest {
  private static final OkHttpClient okHttpClient = new OkHttpClient();
  @Rule
  public TemporaryFolder temporaryFolder = new TemporaryFolder();
  @Rule
  public MockWebServer mockWebServer = new MockWebServer();
  private HttpUrl DUMMY_URL = mockWebServer.url("s.mp3?some=garbage");
  @Mock
  private AudioDownloadView view;
  @Mock
  private AudioDownloadDirectoryProvider directoryProvider;
  @Mock
  private AudiosRepository audiosRepository;

  private AudiosGenerator audiosGenerator = new AudiosGenerator();
  private Audio dummyAudio;

  private AudioDownloadPresenter presenter;

  @Before
  public void setUp() throws Exception {
    Timber.plant(new SystemOutTree());
    MockitoAnnotations.initMocks(this);
    Rxs.setUpRx();
    when(directoryProvider.getDirectory()).thenReturn(temporaryFolder.getRoot());
    dummyAudio = audiosGenerator.generateAudio();
    dummyAudio.setDownloadUrl(DUMMY_URL.toString());
    presenter = new AudioDownloadPresenterImpl(okHttpClient, directoryProvider,
            audiosRepository, view);
  }

  @After
  public void tearDown() {
    Rxs.tearDownRx();
  }


  @Test
  public void downloadStarted_showDownloadPendingCalled() throws Exception {
    final AtomicBoolean showDownloadPendingCalled = new AtomicBoolean(false);
    mockWebServer.enqueue(new MockResponse().setBody("does not matter"));
    doAnswer(new Answer<Object>() {
      @Override
      public Object answer(InvocationOnMock invocation) throws Throwable {
        showDownloadPendingCalled.set(true);
        return null;
      }
    }).when(view).showDownloadPending(any(Audio.class));
    presenter.startDownload(dummyAudio);

    assertTrue(showDownloadPendingCalled.get());
  }

  @Test
  public void downloadSuccessful_showDownloadSuccessfulCalled() throws Exception {
    final AtomicBoolean showDownloadSuccessfulCalled = new AtomicBoolean(false);
    mockWebServer.enqueue(new MockResponse().setBody("does not matter"));
    doAnswer(new Answer<Object>() {
      @Override
      public Object answer(InvocationOnMock invocation) throws Throwable {
        showDownloadSuccessfulCalled.set(true);
        return null;
      }
    }).when(view).showDownloadSuccessful(dummyAudio);
    presenter.startDownload(dummyAudio);

    assertTrue(showDownloadSuccessfulCalled.get());
  }

  @Test
  public void downloadSuccessful_audiosRepositoryUpdated() throws Exception {
    final AtomicBoolean audiosRepositoryUpdated = new AtomicBoolean(false);
    mockWebServer.enqueue(new MockResponse().setBody("does not matter"));
    doAnswer(new Answer<Object>() {
      @Override
      public Object answer(InvocationOnMock invocation) throws Throwable {
        audiosRepositoryUpdated.set(true);
        return null;
      }
    }).when(audiosRepository).update(dummyAudio);
    presenter.startDownload(dummyAudio);

    assertTrue(audiosRepositoryUpdated.get());
  }

  @Test
  public void serverReturns404_showDownloadFailedCalled() throws Exception {
    final AtomicBoolean showDownloadFailedCalled = new AtomicBoolean(false);
    mockWebServer.enqueue(new MockResponse().setResponseCode(404));
    doAnswer(new Answer() {
      @Override
      public Object answer(InvocationOnMock invocation) throws Throwable {
        showDownloadFailedCalled.set(true);
        return null;
      }
    }).when(view).showDownloadFailed(dummyAudio);
    presenter.startDownload(dummyAudio);

    assertTrue(showDownloadFailedCalled.get());
  }

  @Test
  public void downloadSuccessful_fileExistsWithExpectedContent() throws Exception {
    String expected = "expectedContent";
    mockWebServer.enqueue(new MockResponse().setBody(expected));
    presenter.startDownload(dummyAudio);

    File downloaded = temporaryFolder.getRoot().listFiles()[0];
    FileInputStream fis = new FileInputStream(downloaded);
    ByteString byteString = ByteString.read(fis, fis.available());
    fis.close();
    assertEquals(expected, byteString.utf8());
  }

  @Test
  public void downloadFailed_noFileCreated() {
    mockWebServer.enqueue(new MockResponse().setResponseCode(404));
    presenter.startDownload(dummyAudio);

    assertEquals(0, temporaryFolder.getRoot().listFiles().length);
  }
}

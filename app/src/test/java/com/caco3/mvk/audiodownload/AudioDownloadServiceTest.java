package com.caco3.mvk.audiodownload;

import android.content.Intent;
import android.os.Bundle;

import com.caco3.mvk.BuildConfig;
import com.caco3.mvk.network.interceptors.NotSuccessfulResponseInterceptor;
import com.caco3.mvk.rxbus.RxBus;
import com.caco3.mvk.timber.SystemOutTree;
import com.caco3.mvk.util.CurrentThreadExecutor;
import com.caco3.mvk.vk.audio.Audio;
import com.caco3.mvk.vk.audio.AudiosGenerator;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import okhttp3.OkHttpClient;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okio.Okio;
import rx.observers.TestSubscriber;
import timber.log.Timber;

import static junit.framework.Assert.fail;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.when;


@RunWith(RobolectricTestRunner.class)
@Config(
        constants = BuildConfig.class,
        sdk = 23
)
public class AudioDownloadServiceTest {
  private AudioDownloadService service;
  @Rule
  public MockWebServer mockWebServer = new MockWebServer();
  private final AudiosGenerator audiosGenerator = new AudiosGenerator();
  @Rule
  public TemporaryFolder temporaryFolder = new TemporaryFolder();
  @Mock
  private AudioDownloadDirectoryProvider directoryProvider;
  private final RxBus rxBus = RxBus.getInstance();

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
    service = Robolectric.buildService(AudioDownloadService.class).get();
    service.okHttpClient = new OkHttpClient.Builder()
            .addInterceptor(new NotSuccessfulResponseInterceptor()).build();
    when(directoryProvider.getDirectory()).thenReturn(temporaryFolder.getRoot());
    service.directoryProvider = directoryProvider;
    service.rxBus = rxBus;
    Timber.plant(new SystemOutTree());
  }

  @Test
  public void intentHasNotExtras_iaeThrown() {
    try {
      service.onStartCommand(new Intent(RuntimeEnvironment.application,
              AudioDownloadService.class), 0, 0);
    } catch (IllegalArgumentException expected) {
      assertThat(expected.getMessage())
              .isEqualTo("No extras in intent");
    }
  }

  @Test
  public void intentHasNotAudioExtra_iaeThrown() {
    try {
      service.onStartCommand(new Intent(RuntimeEnvironment.application,
              AudioDownloadService.class).putExtras(new Bundle()), 0, 0);
    } catch (IllegalArgumentException expected) {
      assertThat(expected.getMessage())
              .contains("No Audio in the intent ");
    }
  }

  @Test
  public void audioPosted_wakeLockAcquired() throws Exception {
    Audio audio = audiosGenerator.generateOne();
    audio.setDownloadUrl(mockWebServer.url("asf").toString());
    mockWebServer.enqueue(new MockResponse().setBodyDelay(1, TimeUnit.HOURS));
    service.onStartCommand(new Intent(RuntimeEnvironment.application,
            AudioDownloadService.class).putExtra(AudioDownloadService.EXTRA_AUDIO, audio), 0, 0);
    assertThat(service.wakeLock.isHeld())
            .isTrue();
  }

  @Test
  public void audioDownloaded_wakeLockIsReleased() throws Exception {
    service.executor = new CurrentThreadExecutor();
    Audio audio = audiosGenerator.generateOne();
    audio.setDownloadUrl(mockWebServer.url("asf.mp3").toString());
    mockWebServer.enqueue(new MockResponse());
    service.onStartCommand(new Intent(RuntimeEnvironment.application,
            AudioDownloadService.class).putExtra(AudioDownloadService.EXTRA_AUDIO, audio), 0, 0);
    assertThat(service.wakeLock.isHeld())
            .isFalse();
  }

  @Test
  public void audioDownloaded_fileCreatedWithExpectedContent() throws Exception {
    String content = "expectedContent";

    Audio audio = audiosGenerator.generateOne();
    audio.setDownloadUrl(mockWebServer.url("q.mp3").toString());
    service.executor = new CurrentThreadExecutor();
    mockWebServer.enqueue(new MockResponse().setBody(content));
    service.onStartCommand(AudioDownloadService
            .forAudio(RuntimeEnvironment.application, audio), 0, 0);

    File downloaded = temporaryFolder.getRoot().listFiles()[0];
    assertThat(Okio.buffer(Okio.source(downloaded)).readUtf8())
            .isEqualTo(content);
  }

  @Test
  public void audioPosted_audioDownloadProgressPostedViaRxBus() {
    final AtomicBoolean progressPosted = new AtomicBoolean();
    TestSubscriber<Object> testSubscriber = new TestSubscriber<Object>() {
      @Override
      public void onNext(Object o) {
        super.onNext(o);
        if (o instanceof AudioDownloadProgressUpdateEvent) {
          progressPosted.set(true);
        }
      }
    };
    rxBus.observable().subscribe(testSubscriber);
    Audio audio = audiosGenerator.generateOne();
    audio.setDownloadUrl(mockWebServer.url("asf.mp3").toString());
    mockWebServer.enqueue(new MockResponse());
    service.onStartCommand(new Intent(RuntimeEnvironment.application,
            AudioDownloadService.class).putExtra(AudioDownloadService.EXTRA_AUDIO, audio), 0, 0);

    assertThat(progressPosted.get())
            .isTrue();
  }

  @Test
  public void ioExceptionThrown_audioDownloadErrorPostedViaRxBus() {
    TestSubscriber<Object> testSubscriber = new TestSubscriber<>();
    rxBus.observable().subscribe(testSubscriber);
    Audio audio = audiosGenerator.generateOne();
    audio.setDownloadUrl(mockWebServer.url("asf.mp3").toString());
    service.executor = new CurrentThreadExecutor();
    mockWebServer.enqueue(new MockResponse().setResponseCode(404));
    service.onStartCommand(AudioDownloadService.forAudio(RuntimeEnvironment.application, audio), 0, 0);

    List<Object> onNextEvents = testSubscriber.getOnNextEvents();
    try {
      UnableDownloadAudioEvent downloadError = (UnableDownloadAudioEvent)onNextEvents
              .get(onNextEvents.size() - 1);
      assertThat(downloadError.getAudio())
              .isEqualTo(audio);
    } catch (ClassCastException e) {
      fail(UnableDownloadAudioEvent.class.getSimpleName() + " was not posted into RxBus");
    }

  }

  @Test
  public void audioDownloaded_audioDownloadedEventPostedViaRxBus() {
    TestSubscriber<Object> testSubscriber = new TestSubscriber<>();
    rxBus.observable().subscribe(testSubscriber);
    Audio audio = audiosGenerator.generateOne();
    audio.setDownloadUrl(mockWebServer.url("asf.mp3").toString());
    service.executor = new CurrentThreadExecutor();
    mockWebServer.enqueue(new MockResponse().setBody("DummySong"));
    service.onStartCommand(AudioDownloadService.forAudio(RuntimeEnvironment.application, audio), 0, 0);

    List<Object> onNextEvents = testSubscriber.getOnNextEvents();
    try {
      AudioDownloadedEvent event = (AudioDownloadedEvent)onNextEvents
              .get(onNextEvents.size() - 1);
      assertThat(event.getAudio())
              .isEqualTo(audio);
    } catch (ClassCastException e) {
      fail(AudioDownloadedEvent.class.getSimpleName() + " was not posted into RxBus");
    }
  }
}

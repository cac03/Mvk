package com.caco3.mvk.audiodownload;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.caco3.mvk.BuildConfig;
import com.caco3.mvk.audiodownload.events.AudioAcceptedEvent;
import com.caco3.mvk.audiodownload.events.AudioDownloadProgressUpdatedEvent;
import com.caco3.mvk.audiodownload.events.AudioDownloadedEvent;
import com.caco3.mvk.audiodownload.events.UnableDownloadAudioEvent;
import com.caco3.mvk.audiodownload.storage.AudioDownloadDirectoryProvider;
import com.caco3.mvk.network.interceptors.NotSuccessfulResponseInterceptor;
import com.caco3.mvk.rxbus.RxBus;
import com.caco3.mvk.util.CurrentThreadExecutor;
import com.caco3.mvk.vk.audio.Audio;
import com.caco3.mvk.vk.audio.AudiosGenerator;

import org.junit.After;
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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

import okhttp3.OkHttpClient;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okio.Okio;
import rx.observers.TestSubscriber;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.assertj.core.api.Java6Assertions.fail;
import static org.mockito.Mockito.when;


@RunWith(RobolectricTestRunner.class)
@Config(
        constants = BuildConfig.class,
        sdk = 23
)
public class AudioDownloadServiceTest {
  private static final Executor currentThreadExecutor = new CurrentThreadExecutor();

  private AudioDownloadService service;
  @Rule
  public MockWebServer mockWebServer = new MockWebServer();
  private final AudiosGenerator audiosGenerator = new AudiosGenerator();
  @Rule
  public TemporaryFolder temporaryFolder = new TemporaryFolder();
  @Mock
  private AudioDownloadDirectoryProvider directoryProvider;
  private final RxBus rxBus = RxBus.getInstance();
  private TestSubscriber<Object> eventsListener = new TestSubscriber<>();

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
    prepareService();
    rxBus.observable().subscribe(eventsListener);
  }

  private void prepareService() throws Exception {
    service = Robolectric.buildService(AudioDownloadService.class).get();
    service.directoryProvider = directoryProvider;
    service.rxBus = rxBus;
    service.executor = currentThreadExecutor;
    service.okHttpClient = new OkHttpClient.Builder()
            .addInterceptor(new NotSuccessfulResponseInterceptor()).build();
    when(directoryProvider.getDirectory()).thenReturn(temporaryFolder.getRoot());
  }

  @After
  public void tearDown() throws Exception {
    eventsListener.unsubscribe();
  }

  @Test
  public void intentHasNotExtras_iaeThrown() {
    try {
      service.onStartCommand(new Intent(RuntimeEnvironment.application,
              AudioDownloadService.class), 0, 0);
      fail("Iae was not thrown");
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
      fail("Iae was not thrown");
    } catch (IllegalArgumentException expected) {
      assertThat(expected.getMessage())
              .contains("No Audio in the intent ");
    }
  }

  @Test
  public void audioPosted_wakeLockAcquired() throws Exception {
    service.executor = new Executor() {
      @Override
      public void execute(@NonNull Runnable command) {
        // ignore command since we release wakeLock after its execution.
        // but we want to test that after posting audio wake lock is acquired
      }
    };

    Audio audio = prepareAudio();
    mockWebServer.enqueue(new MockResponse().setBody("dummyResponse"));
    startForAudio(audio);
    assertThat(service.wakeLock.isHeld())
            .isTrue();
  }

  @Test
  public void audioDownloaded_wakeLockIsReleased() throws Exception {
    Audio audio = prepareAudio();
    mockWebServer.enqueue(new MockResponse());
    startForAudio(audio);
    assertThat(service.wakeLock.isHeld())
            .isFalse();
  }

  @Test
  public void audioDownloaded_fileCreatedWithExpectedContent() throws Exception {
    String content = "expectedContent";
    Audio audio = prepareAudio();
    mockWebServer.enqueue(new MockResponse().setBody(content));
    startForAudio(audio);
    try {
      File downloaded = temporaryFolder.getRoot().listFiles()[0];

      assertThat(Okio.buffer(Okio.source(downloaded)).readUtf8())
              .isEqualTo(content);
    } catch (IndexOutOfBoundsException e) {
      fail("File was not created (root directory is empty)");
    }

  }

  @Test
  public void audioPosted_audioDownloadProgressUpdatedEventPosted() {
    Audio audio = prepareAudio();
    mockWebServer.enqueue(new MockResponse().setBody("does not matter, but must be non-empty"));
    startForAudio(audio);
    List<AudioDownloadProgressUpdatedEvent> events
            = getEventsByClass(AudioDownloadProgressUpdatedEvent.class);

    assertThat(events)
            .isNotEmpty();
    assertThat(events.get(0).getAudio())
            .isEqualTo(audio);
  }

  @Test
  public void ioExceptionThrown_audioDownloadErrorPostedViaRxBus() {
    Audio audio = prepareAudio();
    mockWebServer.enqueue(new MockResponse().setResponseCode(404));
    startForAudio(audio);
    List<UnableDownloadAudioEvent> events = getEventsByClass(UnableDownloadAudioEvent.class);

    assertThat(events)
            .isNotEmpty();
    assertThat(events.get(0).getAudio())
            .isEqualTo(audio);
  }

  @Test
  public void audioDownloaded_audioDownloadedEventPosted() {
    Audio audio = prepareAudio();
    mockWebServer.enqueue(new MockResponse().setBody("DummySong"));
    startForAudio(audio);
    List<AudioDownloadedEvent> events = getEventsByClass(AudioDownloadedEvent.class);

    assertThat(events)
            .isNotEmpty();
    assertThat(events.get(0).getAudio())
            .isEqualTo(audio);
  }

  @Test
  public void audioAcceptedByService_audioAcceptedEventPosted() {
    Audio audio = prepareAudio();
    startForAudio(audio);
    List<AudioAcceptedEvent> events = getEventsByClass(AudioAcceptedEvent.class);
    assertThat(events)
            .isNotEmpty();
    assertThat(events.get(0).getAudio())
            .isEqualTo(audio);
  }

  private void startForAudio(Audio audio) {
    service.onStartCommand(AudioDownloadService
            .forAudio(RuntimeEnvironment.application, audio), 0, 0);
  }

  private Audio prepareAudio() {
    Audio audio = audiosGenerator.generateOne();
    audio.setDownloadUrl(mockWebServer.url("dummyUrl.mp3").toString());

    return audio;
  }

  @SuppressWarnings("unchecked") // eventClass.isInstance(o) succeed so cast is correct
  private <T> List<T> getEventsByClass(Class<T> eventClass) {
    List<T> events = new ArrayList<>();
    for(Object o : eventsListener.getOnNextEvents()) {
      if (eventClass.isInstance(o)) {
        events.add((T)o);
      }
    }

    return events;
  }
}

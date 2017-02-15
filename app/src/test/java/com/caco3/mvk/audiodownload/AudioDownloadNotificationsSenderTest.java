package com.caco3.mvk.audiodownload;

import android.annotation.TargetApi;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.service.notification.StatusBarNotification;

import com.caco3.mvk.BuildConfig;
import com.caco3.mvk.R;
import com.caco3.mvk.audiodownload.events.AudioAcceptedEvent;
import com.caco3.mvk.audiodownload.events.AudioDownloadProgressUpdatedEvent;
import com.caco3.mvk.audiodownload.events.AudioDownloadedEvent;
import com.caco3.mvk.audiodownload.events.UnableDownloadAudioEvent;
import com.caco3.mvk.audiodownload.events.handle.AbstractAudioDownloadEventsHandler;
import com.caco3.mvk.rxbus.RxBus;
import com.caco3.mvk.vk.audio.Audio;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowNotification;
import org.robolectric.shadows.ShadowNotificationManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import rx.schedulers.Schedulers;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.robolectric.Shadows.shadowOf;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
@RunWith(RobolectricTestRunner.class)
@Config(
        constants = BuildConfig.class,
        sdk = 22
)
public class AudioDownloadNotificationsSenderTest {
  private final RxBus rxBus = RxBus.getInstance();
  private AbstractAudioDownloadEventsHandler eventsHandler;
  private Context context;
  private ShadowNotificationManager shadowNotificationManager;

  @Before
  public void setUp() throws Exception {
    context = RuntimeEnvironment.application;
    shadowNotificationManager = shadowOf((NotificationManager)context
            .getSystemService(Context.NOTIFICATION_SERVICE));
    eventsHandler = new AudioDownloadNotificationsSender(context, rxBus);
    eventsHandler.startHandling(Schedulers.immediate());
  }

  @After
  public void tearDown() throws Exception {
    eventsHandler.stopHandling();
  }

  @Test
  public void audioAcceptedEventPosted_notificationShown() {
    Audio audio = createDummyAudio("Camelphat", "The Act");
    rxBus.post(new AudioAcceptedEvent(audio));
    List<ShadowNotification> notifications = getShadowNotifications();
    assertThat(notifications)
            .hasSize(1);

    assertThat(notifications.get(0).getContentText())
            .isEqualTo(context.getString(R.string.download_pending));
    assertThat(notifications.get(0).getContentTitle())
            .isEqualTo(getExpectedTitle(audio));
    assertThat(notifications.get(0).isIndeterminate())
            .isTrue();
    assertThat(notifications.get(0).isOngoing())
            .isTrue();
  }

  @Test
  public void unableDownloadAudioEventPosted_notificationShown() {
    Audio audio = createDummyAudio("123", "asf");
    rxBus.post(new UnableDownloadAudioEvent(new IOException(), audio));
    List<ShadowNotification> notifications = getShadowNotifications();
    assertThat(notifications)
            .hasSize(1);
    assertThat(notifications.get(0).getContentText())
            .isEqualTo(context.getString(R.string.download_failed));
    assertThat(notifications.get(0).getContentTitle())
            .isEqualTo(getExpectedTitle(audio));
    assertThat(notifications.get(0).isOngoing())
            .isFalse();
  }

  @Test
  public void audioDownloadedEventPosted_notificationShown() {
    Audio audio = createDummyAudio("464", "asf");
    rxBus.post(new AudioDownloadedEvent(audio));
    List<ShadowNotification> notifications = getShadowNotifications();
    assertThat(notifications)
            .hasSize(1);
    assertThat(notifications.get(0).getContentText())
            .isEqualTo(context.getString(R.string.download_complete));
    assertThat(notifications.get(0).getContentTitle())
            .isEqualTo(getExpectedTitle(audio));
    assertThat(notifications.get(0).isOngoing())
            .isFalse();
  }

  @Test
  public void audioDownloadProgressUpdatedEventPosted_notificationShown() {
    Audio audio = createDummyAudio("464", "asf");
    final int bytesTotal = 1024 * 50;
    final int bytesDownloaded = 2048 + 130;
    rxBus.post(AudioDownloadProgressUpdatedEvent.builder()
            .audio(audio)
            .bytesTotal(bytesTotal)
            .bytesDownloaded(bytesDownloaded)
            .nanosElapsed(1_000_000_000)
            .build());
    final int expectedPercentage = bytesDownloaded * 100 / bytesTotal;
    List<ShadowNotification> notifications = getShadowNotifications();
    assertThat(notifications)
            .hasSize(1);
    assertThat(notifications.get(0).getContentTitle())
            .isEqualTo(getExpectedTitle(audio));
    assertThat(notifications.get(0).isOngoing())
            .isTrue();
    assertThat(notifications.get(0).getProgress())
            .isEqualTo(expectedPercentage);
  }



  private List<ShadowNotification> getShadowNotifications() {
    List<ShadowNotification> shadowNotifications = new ArrayList<>();
    for(StatusBarNotification statusBarNotification :
            shadowNotificationManager.getActiveNotifications()) {
      shadowNotifications.add(shadowOf(statusBarNotification.getNotification()));
    }

    return shadowNotifications;
  }

  private Audio createDummyAudio(String artist, String title) {
    Audio audio = new Audio();
    audio.setArtist(artist);
    audio.setTitle(title);

    return audio;
  }

  private String getExpectedTitle(Audio audio) {
    return audio.getArtist() + " - " + audio.getTitle();
  }
}

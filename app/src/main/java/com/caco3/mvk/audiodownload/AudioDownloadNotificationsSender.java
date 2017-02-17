package com.caco3.mvk.audiodownload;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.DrawableRes;
import android.support.v4.app.NotificationCompat;

import com.caco3.mvk.R;
import com.caco3.mvk.audiodownload.events.AudioAcceptedEvent;
import com.caco3.mvk.audiodownload.events.AudioDownloadProgressUpdatedEvent;
import com.caco3.mvk.audiodownload.events.AudioDownloadedEvent;
import com.caco3.mvk.audiodownload.events.UnableDownloadAudioEvent;
import com.caco3.mvk.audiodownload.events.handle.AbstractAudioDownloadEventsHandler;
import com.caco3.mvk.myaudios.MyAudiosActivity;
import com.caco3.mvk.rxbus.RxBus;
import com.caco3.mvk.vk.audio.Audio;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import static com.caco3.mvk.util.Preconditions.checkNotNull;


public class AudioDownloadNotificationsSender extends AbstractAudioDownloadEventsHandler {
  private static final Locale locale = Locale.getDefault();

  @DrawableRes
  private static final int DOWNLOAD_PROGRESS_ICON = android.R.drawable.stat_sys_download;
  @DrawableRes
  private static final int DOWNLOAD_COMPLETE_ICON = android.R.drawable.stat_sys_download_done;
  @DrawableRes
  private static final int DOWNLOAD_FAILED_ICON = android.R.drawable.stat_notify_error;
  @DrawableRes
  private static final int DOWNLOAD_PENDING_ICON = android.R.drawable.stat_sys_download;

  private final PendingIntent myAudiosActivityIntent;

  private final String DOWNLOAD_FAILED;
  private final String DOWNLOAD_PENDING;
  private final String DOWNLOAD_COMPLETE;
  private final String KILOBYTE;
  private final String MEGABYTE;
  private final String SECOND;

  private final Context context;
  private final NotificationManager notificationManager;
  @SuppressLint("UseSparseArrays")
  private Map<Long, Integer> notificationIds = new HashMap<>();
  @SuppressLint("UseSparseArrays")
  private Map<Long, NotificationCompat.Builder> progressNotifications
          = new HashMap<>();
  private int idCounter = 0;

  @Inject
  public AudioDownloadNotificationsSender(Context context, RxBus rxBus) {
    super(rxBus);
    this.context = checkNotNull(context, "context == null");
    notificationManager = (NotificationManager) context
            .getSystemService(Context.NOTIFICATION_SERVICE);
    myAudiosActivityIntent = PendingIntent.getActivity(context, 0,
            new Intent(context, MyAudiosActivity.class), 0);

    DOWNLOAD_FAILED = context.getString(R.string.download_failed);
    DOWNLOAD_PENDING = context.getString(R.string.download_pending);
    DOWNLOAD_COMPLETE = context.getString(R.string.download_complete);
    KILOBYTE = context.getString(R.string.kilobyte_short);
    MEGABYTE = context.getString(R.string.megabyte_short);
    SECOND = context.getString(R.string.second_short);
  }

  private void showDownloadPending(Audio audio) {

    NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
            .setContentTitle(makeTitle(audio))
            .setSmallIcon(DOWNLOAD_PENDING_ICON)
            .setContentText(DOWNLOAD_PENDING)
            .setContentIntent(myAudiosActivityIntent)
            .setProgress(100, 42/*does not matter*/, true)
            .setOngoing(true);

    fireNotification(audio, builder);
  }

  private String makeTitle(Audio audio) {
    return audio.getArtist() + " - " + audio.getTitle();
  }

  private void fireNotification(Audio audio, NotificationCompat.Builder builder) {
    notificationManager.notify(getOrCreateId(audio), builder.build());
  }

  private int getOrCreateId(Audio audio) {
    int id;
    if (!notificationIds.containsKey(audio.getId())) {
      id = idCounter++;
      notificationIds.put(audio.getId(), id);
    } else {
      id = notificationIds.get(audio.getId());
    }

    return id;
  }

  private void fireLastNotification(Audio audio, NotificationCompat.Builder builder) {
    fireNotification(audio, builder);
    notificationIds.remove(audio.getId());
    if (progressNotifications.containsKey(audio.getId())) {
      progressNotifications.remove(audio.getId());
    }
  }

  @Override
  public void handle(AudioAcceptedEvent audioAcceptedEvent) {
    Audio audio = audioAcceptedEvent.getAudio();
    showDownloadPending(audio);
  }

  @Override
  public void handle(AudioDownloadedEvent audioDownloadedEvent) {
    Audio audio = audioDownloadedEvent.getAudio();
    showDownloadSuccessful(audio);
  }

  @Override
  public void handle(AudioDownloadProgressUpdatedEvent progressUpdatedEvent) {
    Audio audio = progressUpdatedEvent.getAudio();
    updateProgress(audio, progressUpdatedEvent.getBytesDownloaded(),
            progressUpdatedEvent.getBytesTotal(), progressUpdatedEvent.getNanosElapsed());
  }

  @Override
  public void handle(UnableDownloadAudioEvent unableDownloadAudioEvent) {
    Audio audio = unableDownloadAudioEvent.getAudio();
    showDownloadFailed(audio);
  }

  private void updateProgress(Audio audio, long bytesDownloaded, long bytesTotal, long nanosElapsed) {
    NotificationCompat.Builder builder;
    if (progressNotifications.containsKey(audio.getId())) {
      builder = progressNotifications.get(audio.getId());
    } else {
      builder = new NotificationCompat.Builder(context);
      progressNotifications.put(audio.getId(), builder);
    }
    builder.setContentTitle(makeTitle(audio))
            .setContentText(makeProgressContentText(bytesDownloaded, bytesTotal, nanosElapsed))
            .setSmallIcon(DOWNLOAD_PROGRESS_ICON)
            .setContentIntent(myAudiosActivityIntent)
            .setOngoing(true);
    int percentage = (int)(bytesDownloaded * 100 / bytesTotal);
    builder.setProgress(100, percentage, false);
    fireNotification(audio, builder);
  }

  private void showDownloadFailed(Audio audio) {
    NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
            .setContentTitle(makeTitle(audio))
            .setContentText(DOWNLOAD_FAILED)
            .setSmallIcon(DOWNLOAD_FAILED_ICON)
            .setContentIntent(myAudiosActivityIntent)
            .setAutoCancel(true);

    fireLastNotification(audio, builder);
  }

  private void showDownloadSuccessful(Audio audio) {
    NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
            .setContentTitle(makeTitle(audio))
            .setContentText(DOWNLOAD_COMPLETE)
            .setSmallIcon(DOWNLOAD_COMPLETE_ICON)
            .setAutoCancel(true);

    fireLastNotification(audio, builder);
  }

  private String makeProgressContentText(long bytesDownloaded, long bytesTotal, long nanosElapsed) {
    if (showInMegaBytes(bytesDownloaded)) {
      return makeProgressContentTextInMegaBytes(bytesDownloaded, bytesTotal, nanosElapsed);
    } else {
      return makeProgressContentTextInKiloBytes(bytesDownloaded, bytesTotal, nanosElapsed);
    }
  }

  private String makeProgressContentTextInMegaBytes(long bytesDownloaded,
                                                    long bytesTotal, long nanosElapsed) {
    double megaBytesDownloaded = (double)bytesDownloaded / 1024 / 1024;
    double megaBytesTotal = (double)bytesTotal / 1024 / 1024;
    String speed = makeSpeedString(bytesDownloaded, nanosElapsed);

    return String.format(locale, "%.1f/%.1f%s, %s",
            megaBytesDownloaded, megaBytesTotal, MEGABYTE, speed);
  }

  private String makeProgressContentTextInKiloBytes(long bytesDownloaded,
                                                    long bytesTotal, long nanosElapsed) {
    double kiloBytesDownloaded = (double)bytesDownloaded / 1024;
    double kiloBytesTotal = (double)bytesTotal / 1024;
    String speed = makeSpeedString(bytesDownloaded, nanosElapsed);

    return String.format(locale, "%.1f/%.1f%s, %s",
            kiloBytesDownloaded, kiloBytesTotal, KILOBYTE, speed);
  }

  private String makeSpeedString(long bytesDownloaded, long nanosElapsed) {
    double kBDownloaded = (double)bytesDownloaded / 1024;
    long secondsElapsed = TimeUnit.NANOSECONDS.toSeconds(nanosElapsed);
    double kBSpeed = kBDownloaded / secondsElapsed;
    if (kBSpeed > 1024) {
      return String.format(locale, "%.1f%s/%s", kBSpeed / 1024, MEGABYTE, SECOND);
    } else {
      return String.format(locale, "%.1f%s/%s", kBSpeed, KILOBYTE, SECOND);
    }
  }

  private boolean showInMegaBytes(long bytesDownloaded) {
    return bytesDownloaded / 1024 / 1024 > 0;
  }
}

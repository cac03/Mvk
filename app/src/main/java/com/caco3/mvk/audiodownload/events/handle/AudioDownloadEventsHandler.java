package com.caco3.mvk.audiodownload.events.handle;

import com.caco3.mvk.audiodownload.events.AudioAcceptedEvent;
import com.caco3.mvk.audiodownload.events.AudioDownloadProgressUpdatedEvent;
import com.caco3.mvk.audiodownload.events.AudioDownloadedEvent;
import com.caco3.mvk.audiodownload.events.UnableDownloadAudioEvent;

public interface AudioDownloadEventsHandler {
  void handle(AudioAcceptedEvent audioAcceptedEvent);
  void handle(AudioDownloadedEvent audioDownloadedEvent);
  void handle(AudioDownloadProgressUpdatedEvent progressUpdatedEvent);
  void handle(UnableDownloadAudioEvent unableDownloadAudioEvent);
}

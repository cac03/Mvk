package com.caco3.mvk.audiodownload;

public interface AudioDownloadEventsHandler {
  void handle(AudioAcceptedEvent audioAcceptedEvent);
  void handle(AudioDownloadedEvent audioDownloadedEvent);
  void handle(AudioDownloadProgressUpdatedEvent progressUpdatedEvent);
  void handle(UnableDownloadAudioEvent unableDownloadAudioEvent);
}

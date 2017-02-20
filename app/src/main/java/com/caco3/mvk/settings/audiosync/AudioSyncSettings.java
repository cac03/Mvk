package com.caco3.mvk.settings.audiosync;

public interface AudioSyncSettings {
  boolean isSyncEnabled();
  long getSyncIntervalMillis();
  boolean isSyncOnlyViaWifiAllowed();
}

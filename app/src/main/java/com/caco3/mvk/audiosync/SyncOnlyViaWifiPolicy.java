package com.caco3.mvk.audiosync;

import com.caco3.mvk.network.NetworkManager;
import com.caco3.mvk.settings.audiosync.AudioSyncSettings;

import static com.caco3.mvk.util.Preconditions.checkNotNull;

public class SyncOnlyViaWifiPolicy implements AudioSyncPolicy {
  private NetworkManager networkManager;
  private final AudioSyncSettings settings;

  public SyncOnlyViaWifiPolicy(AudioSyncSettings settings, NetworkManager networkManager) {
    this.networkManager = checkNotNull(networkManager, "networkManager == null");
    this.settings = checkNotNull(settings, "settings == null");
  }

  @Override
  public boolean canSync() {
    return !settings.isSyncOnlyViaWifiAllowed() || networkManager.isConnectedWithWiFi();
  }
}

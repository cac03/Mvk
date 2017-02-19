package com.caco3.mvk.audiosync;

import com.caco3.mvk.network.NetworkManager;

import static com.caco3.mvk.util.Preconditions.checkNotNull;

public class SyncOnlyViaWifiPolicy implements AudioSyncPolicy {
  private NetworkManager networkManager;

  public SyncOnlyViaWifiPolicy(NetworkManager networkManager) {
    this.networkManager = checkNotNull(networkManager, "networkManager == null");
  }

  @Override
  public boolean canSync() {
    return networkManager.isConnectedWithWiFi();
  }
}

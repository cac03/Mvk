package com.caco3.mvk.audiosync;

import com.caco3.mvk.network.NetworkManager;

import static com.caco3.mvk.util.Preconditions.checkNotNull;

/*package*/ class SyncOnlyViaWifiPolicy implements AudioSyncPolicy {
  private NetworkManager networkManager;

  /*package*/ SyncOnlyViaWifiPolicy(NetworkManager networkManager) {
    this.networkManager = checkNotNull(networkManager, "networkManager == null");
  }

  @Override
  public boolean canSync() {
    return networkManager.isConnectedWithWiFi();
  }
}

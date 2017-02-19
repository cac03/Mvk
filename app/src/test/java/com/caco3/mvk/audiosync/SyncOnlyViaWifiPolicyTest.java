package com.caco3.mvk.audiosync;

import com.caco3.mvk.network.NetworkManager;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.when;

public class SyncOnlyViaWifiPolicyTest {
  @Mock private NetworkManager networkManager;
  private SyncOnlyViaWifiPolicy policy;

  @Before public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
    policy = new SyncOnlyViaWifiPolicy(networkManager);
  }

  @Test public void wifiNotAvailable_canSyncReturnsFalse() {
    when(networkManager.isConnectedWithWiFi()).thenReturn(false);
    assertThat(policy.canSync())
            .isFalse();
  }

  @Test public void wifiIsAvailable_canSyncReturnsTrue() {
    when(networkManager.isConnectedWithWiFi()).thenReturn(true);
    assertThat(policy.canSync())
            .isTrue();
  }
}

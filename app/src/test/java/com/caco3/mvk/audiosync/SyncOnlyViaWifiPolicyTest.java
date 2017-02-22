package com.caco3.mvk.audiosync;

import com.caco3.mvk.network.NetworkManager;
import com.caco3.mvk.settings.audiosync.AudioSyncSettings;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.when;

public class SyncOnlyViaWifiPolicyTest {
  @Mock private NetworkManager networkManager;
  @Mock private AudioSyncSettings settings;
  private SyncOnlyViaWifiPolicy policy;

  @Before public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
    policy = new SyncOnlyViaWifiPolicy(settings, networkManager);
  }

  @Test public void settingsAllowToSyncWithoutWifi_canSyncReturnsTrue() {
    when(settings.isSyncOnlyViaWifiAllowed()).thenReturn(false);
    assertThat(policy.canSync())
            .isTrue();
  }

  @Test public void settingsDoesNotAllowToSyncWithoutWifiAndWifiIsUnavailable_canSyncReturnsFalse() {
    when(settings.isSyncOnlyViaWifiAllowed()).thenReturn(true);
    when(networkManager.isConnectedWithWiFi()).thenReturn(false);
    assertThat(policy.canSync())
            .isFalse();
  }

  @Test public void settingsDoesNotAllowToSyncWithoutWifiAndWifiIsAvailable_canSyncReturnsTrue() {
    when(settings.isSyncOnlyViaWifiAllowed()).thenReturn(true);
    when(networkManager.isConnectedWithWiFi()).thenReturn(true);
    assertThat(policy.canSync())
            .isTrue();
  }
}

package com.caco3.mvk.audiosync.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.caco3.mvk.BuildConfig;
import com.caco3.mvk.audiosync.SyncAudiosService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowApplication;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.robolectric.Shadows.shadowOf;

@RunWith(RobolectricTestRunner.class)
@Config(
        constants = BuildConfig.class,
        sdk = 23
)
public class SyncAudiosAlarmReceiverTest {
  private Context context;
  private ShadowApplication shadowApplication;

  @Before public void setUp() throws Exception {
    context = RuntimeEnvironment.application;
    shadowApplication = shadowOf(RuntimeEnvironment.application);
  }

  @Test public void receiverRegisteredInManifest() {
    assertThat(findReceiver())
            .isNotNull();
  }

  @Test public void intentForThisReceiverSent_serviceStarted() {
    BroadcastReceiver broadcastReceiver = findReceiver();
    assertThat(broadcastReceiver)
            .isNotNull();
    broadcastReceiver.onReceive(context, new Intent());
    assertThat(shadowApplication.getNextStartedService().getComponent().getClassName())
            .isEqualTo(SyncAudiosService.class.getName());

  }

  private BroadcastReceiver findReceiver() {
    for(ShadowApplication.Wrapper wrapper : shadowApplication.getRegisteredReceivers()) {
      if (wrapper.broadcastReceiver.getClass().equals(SyncAudiosAlarmReceiver.class)) {
        return wrapper.broadcastReceiver;
      }
    }

    return null;
  }
}

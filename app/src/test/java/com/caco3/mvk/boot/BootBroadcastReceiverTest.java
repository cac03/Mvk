package com.caco3.mvk.boot;

import android.content.BroadcastReceiver;
import android.content.Intent;

import com.caco3.mvk.BuildConfig;
import com.caco3.mvk.boot.command.BootCommand;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowApplication;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.caco3.mvk.Stubbers.setTrue;
import static junit.framework.Assert.fail;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.robolectric.Shadows.shadowOf;

@RunWith(RobolectricTestRunner.class)
@Config(
        constants = BuildConfig.class,
        sdk = 23
)
public class BootBroadcastReceiverTest {
  private ShadowApplication shadowApplication;

  @Before public void setUp() throws Exception {
    shadowApplication = shadowOf(RuntimeEnvironment.application);
  }

  @Test public void thereIsReceiverForBootCompletedEvent() {
    assertThat(findReceiver())
            .isNotNull();
  }

  @Test public void bootReceived_commandsExecuted() {
    BootCommand mockCommand = mock(BootCommand.class);
    final AtomicBoolean executeCalled = new AtomicBoolean();
    setTrue(executeCalled).when(mockCommand).execute();
    findReceiver().commands = Collections.singletonList(mockCommand);
    shadowApplication.sendBroadcast(new Intent("android.intent.action.BOOT_COMPLETED"));
    assertThat(executeCalled.get())
            .isTrue();
  }

  private BootBroadcastReceiver findReceiver() {
    List<BroadcastReceiver> receivers = shadowApplication
            .getReceiversForIntent(new Intent("android.intent.action.BOOT_COMPLETED"));
    for(BroadcastReceiver receiver : receivers) {
      if (receiver instanceof BootBroadcastReceiver) {
        return (BootBroadcastReceiver)receiver;
      }
    }
    fail("BootBroadcastReceiver was not register as android.intent.action.BOOT_COMPLETED receiver");
    return null;
  }
}

package com.caco3.mvk.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.caco3.mvk.BuildConfig;
import com.caco3.mvk.timber.SystemOutTree;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.util.FragmentController;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;

import timber.log.Timber;

import static com.caco3.mvk.Stubbers.setTrue;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.assertj.core.api.Java6Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;


@RunWith(RobolectricTestRunner.class)
@Config(
        constants = BuildConfig.class,
        sdk = 22
)
public class SettingsFragmentTest {
  private SettingsFragment fragment;
  private SharedPreferences sharedPreferences;
  private Context context;
  @Mock private SharedPreferences.OnSharedPreferenceChangeListener dummyListener;
  private FragmentController<SettingsFragment> fragmentController;

  @Before public void setUp() throws Exception {
    System.out.println("Before:");
    MockitoAnnotations.initMocks(this);
    context = RuntimeEnvironment.application;
    sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    fragment = new SettingsFragment();
    fragmentController = FragmentController.of(fragment);
    fragmentController.attach().create().start().resume();
    fragment.listeners = Arrays.asList(dummyListener);
    Timber.plant(new SystemOutTree());
  }

  @Test public void fragmentStarted_settingsDisplayed() {
    assertThat(fragment.getPreferenceScreen())
            .isNotNull();
  }

  @Test public void sharedPreferencesChanged_listenerMethodIsCalled() throws Exception {
    final AtomicBoolean listenerMethodCalled = new AtomicBoolean();
    setTrue(listenerMethodCalled).when(dummyListener)
            .onSharedPreferenceChanged(any(SharedPreferences.class), anyString());
    sharedPreferences.edit().putString("someKey", "someValue").apply();
    assertThat(listenerMethodCalled.get())
            .isTrue();
  }

  @Test public void fragmentPaused_noMethodsOnListenersInvoked() {
    doAnswer(new Answer() {
      @Override
      public Object answer(InvocationOnMock invocation) throws Throwable {
        fail("Method invoked on dummyListener");
        return null;
      }
    }).when(dummyListener).onSharedPreferenceChanged(any(SharedPreferences.class), anyString());
    fragmentController.pause();
    sharedPreferences.edit().putString("someKey", "someValue").apply();
  }
}

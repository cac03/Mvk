package com.caco3.mvk.login;

import android.app.Activity;
import android.content.Intent;

import com.caco3.mvk.BuildConfig;
import com.caco3.mvk.R;
import com.caco3.mvk.audios.AudiosActivity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowActivity;
import org.robolectric.shadows.ShadowIntent;
import org.robolectric.shadows.ShadowToast;

import java.util.concurrent.atomic.AtomicBoolean;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.robolectric.Shadows.shadowOf;
import static org.robolectric.shadows.support.v4.SupportFragmentTestUtil.startFragment;


@RunWith(RobolectricTestRunner.class)
@Config(
        constants = BuildConfig.class,
        sdk = 22
)
public class LogInFragmentTest {
  @Mock
  private LogInPresenter presenter;
  private LogInFragment fragment;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    fragment = new LogInFragment();
    fragment.presenter = presenter;
    startFragment(fragment);
  }

  @Test
  public void showLogInProgressCalled_progressDialogIsShowing() {
    fragment.showLogInProgress();
    assertTrue(fragment.loggingInProgressDialog.isShowing());
  }

  @Test(expected = IllegalStateException.class)
  public void hideLogInProgressCalled_IllegalStateExceptionThrown() {
    fragment.hideLogInProgress();
  }

  @Test
  public void hideLogInProgressCalledAfterShowLogInProgress_progressDialogIsNull() {
    fragment.showLogInProgress();
    fragment.hideLogInProgress();
    assertNull(fragment.loggingInProgressDialog);
  }

  @Test
  public void showUsernameIsEmptyStringErrorCalled_errorShown() {
    fragment.showUsernameIsEmptyStringError();
    String expected = fragment.getString(R.string.this_field_required);
    String actual = (String)fragment.usernameEditText.getError();
    assertEquals(expected, actual);
  }

  @Test
  public void showPasswordIsEmptyStringErrorCalled_errorShown() {
    fragment.showPasswordIsEmptyStringError();
    String expected = fragment.getString(R.string.this_field_required);
    String actual = (String)fragment.passwordEditText.getError();
    assertEquals(expected, actual);
  }

  @Test
  public void showNetworkIsUnavailableCalled_toastShown() {
    fragment.showNetworkIsUnavailableError();
    String expected = fragment.getString(R.string.network_is_unavailable);
    String actual = ShadowToast.getTextOfLatestToast();
    assertEquals(expected, actual);
  }

  @Test
  public void showNetworkErrorOccurredErrorCalled_toastShown() {
    fragment.showNetworkErrorOccurredError();
    String expected = fragment.getString(R.string.network_error_occurred);
    String actual = ShadowToast.getTextOfLatestToast();
    assertEquals(expected, actual);
  }

  @Test
  public void showUsernameOrPasswordIncorrectErrorCalled_toastShown() {
    fragment.showUsernameOrPasswordIncorrectError();
    String expected = fragment.getString(R.string.username_or_password_incorrect);
    String actual = ShadowToast.getTextOfLatestToast();
    assertEquals(expected, actual);
  }

  @Test
  public void navigateToAudiosActivityCalled_intentFired() {
    Activity activity = fragment.getActivity();
    Intent expected = new Intent(activity, AudiosActivity.class);
    fragment.navigateToAudiosActivity();
    assertEquals(expected.getComponent(), shadowOf(activity).getNextStartedActivity().getComponent());
  }

  @Test
  public void navigateToAudiosActivityCalled_currentActivityIsFinishing() {
    Activity activity = fragment.getActivity();
    fragment.navigateToAudiosActivity();
    assertTrue(activity.isFinishing());
  }

  @Test
  public void logInButtonClicked_attemptToLogInCalled() {
    final AtomicBoolean attemptToLogInCalled = new AtomicBoolean(false);
    doAnswer(new Answer<Object>(){
      @Override
      public Object answer(InvocationOnMock invocation) throws Throwable {
        attemptToLogInCalled.set(true);
        return null;
      }
    }).when(presenter).attemptToLogIn(anyString(), anyString());
    fragment.getView().findViewById(R.id.log_in_frag_log_in_btn).performClick();
    assertTrue(attemptToLogInCalled.get());
  }

  @Test
  public void loggingInProgressDialogCanceled_cancelLoggingInCalled() {
    final AtomicBoolean cancelLoggingInCalled = new AtomicBoolean(false);
    doAnswer(new Answer() {
      @Override
      public Object answer(InvocationOnMock invocation) throws Throwable {
        cancelLoggingInCalled.set(true);
        return null;
      }
    }).when(presenter).cancelLoggingIn();
    fragment.showLogInProgress();
    fragment.loggingInProgressDialog.cancel();
  }

  @Test
  public void loggingInProgressDialogCanceled_progressDialogIsNull() {
    fragment.showLogInProgress();
    fragment.loggingInProgressDialog.cancel();
    assertNull(fragment.loggingInProgressDialog);
  }

  @Test
  public void onDestroyViewCalled_fragmentDetachedFromPresenter() {
    final AtomicBoolean fragmentDetached = new AtomicBoolean(false);
    doAnswer(new Answer() {
      @Override
      public Object answer(InvocationOnMock invocation) throws Throwable {
        fragmentDetached.set(true);
        return null;
      }
    }).when(presenter).onViewDetached(fragment);
    fragment.onDestroyView();
    assertTrue(fragmentDetached.get());
  }

  @Test
  public void onDestroyViewCalledWithLoggingInProgressShown_loggingInProgressHidden() {
    fragment.showLogInProgress();
    fragment.onDestroyView();
    assertNull(fragment.loggingInProgressDialog);
  }
}

package com.caco3.mvk.login;


import com.caco3.mvk.Rxs;
import com.caco3.mvk.data.appuser.AppUser;
import com.caco3.mvk.data.appuser.AppUsersRepository;
import com.caco3.mvk.timber.SystemOutTree;
import com.caco3.mvk.vk.Vk;
import com.caco3.mvk.vk.auth.Credentials;
import com.caco3.mvk.vk.auth.UsernameOrPasswordIncorrectException;
import com.caco3.mvk.vk.auth.VkAuthService;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

import timber.log.Timber;

import static junit.framework.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LogInPresenterImplTest {
  private static final String USERNAME_INVALID = "";
  private static final String USERNAME_VALID = "dummy_username";
  private static final String PASSWORD_INVALID = "";
  private static final String PASSWORD_VALID = "dummy_password";

  @Mock
  private Vk vk;
  @Mock
  private VkAuthService authService;
  @Mock
  private AppUsersRepository repository;
  @Mock
  private LogInView view;
  private LogInPresenter presenter;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    when(vk.auth()).thenReturn(authService);
    presenter = new LogInPresenterImpl(vk, repository);
    Timber.plant(new SystemOutTree());
    Rxs.setUpRx();
  }

  @After
  public void tearDown() {
    Rxs.tearDownRx();
  }

  @Test
  public void invalidUsernameProvided_showUsernameInvalidErrorCalled() {
    final AtomicBoolean showUsernameInvalidCalled = new AtomicBoolean(false);
    doAnswer(new Answer() {
      @Override
      public Object answer(InvocationOnMock invocation) throws Throwable {
        showUsernameInvalidCalled.set(true);
        return null;
      }
    }).when(view).showUsernameIsEmptyStringError();
    presenter.onViewAttached(view);
    presenter.attemptToLogIn(USERNAME_INVALID, PASSWORD_VALID);
    assertTrue(showUsernameInvalidCalled.get());
  }

  @Test
  public void invalidPasswordProvided_showInvalidPasswordErrorCalled() {
    final AtomicBoolean showInvalidPasswordErrorCalled = new AtomicBoolean(false);
    doAnswer(new Answer() {
      @Override
      public Object answer(InvocationOnMock invocation) throws Throwable {
        showInvalidPasswordErrorCalled.set(true);
        return null;
      }
    }).when(view).showPasswordIsEmptyStringError();
    presenter.onViewAttached(view);
    presenter.attemptToLogIn(USERNAME_VALID, PASSWORD_INVALID);

    assertTrue(showInvalidPasswordErrorCalled.get());
  }


  @Test
  public void incorrectUsernameOrPasswordExceptionThrown_showIncorrectUsernameOrPasswordErrorCalled() throws Exception {
    final AtomicBoolean showIncorrectUsernameOrPasswordErrorCalled = new AtomicBoolean(false);
    when(authService.getUserToken(any(Credentials.class)))
            .thenThrow(UsernameOrPasswordIncorrectException.class);
    doAnswer(new Answer() {
      @Override
      public Object answer(InvocationOnMock invocation) throws Throwable {
        showIncorrectUsernameOrPasswordErrorCalled.set(true);
        return null;
      }
    }).when(view).showUsernameOrPasswordIncorrectError();
    presenter.onViewAttached(view);
    presenter.attemptToLogIn(USERNAME_VALID, PASSWORD_VALID);

    assertTrue(showIncorrectUsernameOrPasswordErrorCalled.get());
  }

  @Test
  public void everythingIsOk_accountCreatedAndSaved() throws Exception {
    final AtomicBoolean accountSaved = new AtomicBoolean(false);
    doAnswer(new Answer() {
      @Override
      public Object answer(InvocationOnMock invocation) throws Throwable {
        accountSaved.set(true);
        return null;
      }
    }).when(repository).save(any(AppUser.class));
    final AtomicBoolean accountSetAsActive = new AtomicBoolean(false);
    doAnswer(new Answer() {
      @Override
      public Object answer(InvocationOnMock invocation) throws Throwable {
        accountSetAsActive.set(true);
        return null;
      }
    }).when(repository).setAsActive(any(AppUser.class));

    presenter.onViewAttached(view);
    presenter.attemptToLogIn(USERNAME_VALID, PASSWORD_VALID);


    assertTrue(accountSaved.get());
    assertTrue(accountSetAsActive.get());
  }

  @Test
  public void ioExceptionThrown_showNetworkErrorOccurredErrorCalled() throws Exception {
    when(authService.getUserToken(any(Credentials.class)))
            .thenThrow(IOException.class);
    final AtomicBoolean showNetworkErrorOccurredErrorCalled = new AtomicBoolean(false);
    doAnswer(new Answer() {
      @Override
      public Object answer(InvocationOnMock invocation) throws Throwable {
        showNetworkErrorOccurredErrorCalled.set(true);
        return null;
      }
    }).when(view).showNetworkErrorOccurredError();
    presenter.onViewAttached(view);
    presenter.attemptToLogIn(USERNAME_VALID, PASSWORD_VALID);

    assertTrue(showNetworkErrorOccurredErrorCalled.get());
  }
}

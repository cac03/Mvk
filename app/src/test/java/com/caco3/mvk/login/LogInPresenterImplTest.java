package com.caco3.mvk.login;


import com.caco3.mvk.Rxs;
import com.caco3.mvk.data.appuser.AppUser;
import com.caco3.mvk.data.appuser.AppUsersRepository;
import com.caco3.mvk.data.usertoken.UserTokenRepository;
import com.caco3.mvk.timber.SystemOutTree;
import com.caco3.mvk.vk.Vk;
import com.caco3.mvk.vk.auth.Credentials;
import com.caco3.mvk.vk.auth.UserToken;
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

import static com.caco3.mvk.Stubbers.setTrue;
import static junit.framework.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
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
  private UserTokenRepository userTokenRepository;
  @Mock
  private LogInView view;
  private LogInPresenter presenter;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    presenter = new LogInPresenterImpl(authService, repository, userTokenRepository);
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
    setTrue(showUsernameInvalidCalled).when(view).showUsernameIsEmptyStringError();
    presenter.onViewAttached(view);
    presenter.attemptToLogIn(USERNAME_INVALID, PASSWORD_VALID);
    assertTrue(showUsernameInvalidCalled.get());
  }

  @Test
  public void invalidPasswordProvided_showInvalidPasswordErrorCalled() {
    final AtomicBoolean showInvalidPasswordErrorCalled = new AtomicBoolean(false);
    setTrue(showInvalidPasswordErrorCalled).when(view).showPasswordIsEmptyStringError();
    presenter.onViewAttached(view);
    presenter.attemptToLogIn(USERNAME_VALID, PASSWORD_INVALID);

    assertTrue(showInvalidPasswordErrorCalled.get());
  }


  @Test
  public void incorrectUsernameOrPasswordExceptionThrown_showIncorrectUsernameOrPasswordErrorCalled() throws Exception {
    final AtomicBoolean showIncorrectUsernameOrPasswordErrorCalled = new AtomicBoolean(false);
    when(authService.getUserToken(any(Credentials.class)))
            .thenThrow(UsernameOrPasswordIncorrectException.class);
    setTrue(showIncorrectUsernameOrPasswordErrorCalled)
            .when(view).showUsernameOrPasswordIncorrectError();
    presenter.onViewAttached(view);
    presenter.attemptToLogIn(USERNAME_VALID, PASSWORD_VALID);

    assertTrue(showIncorrectUsernameOrPasswordErrorCalled.get());
  }

  @Test
  public void everythingIsOk_accountCreatedAndSaved() throws Exception {
    when(authService.getUserToken(any(Credentials.class))).thenReturn(new UserToken("asdfasdf"));
    final AtomicBoolean accountSaved = new AtomicBoolean(false);
    setTrue(accountSaved).when(repository).save(any(AppUser.class));
    final AtomicBoolean accountSetAsActive = new AtomicBoolean(false);
    setTrue(accountSetAsActive).when(repository).setAsActive(any(AppUser.class));
    final AtomicBoolean userTokenSaved = new AtomicBoolean(false);
    setTrue(userTokenSaved).when(userTokenRepository).save(any(UserToken.class));

    presenter.onViewAttached(view);
    presenter.attemptToLogIn(USERNAME_VALID, PASSWORD_VALID);


    assertTrue(accountSaved.get());
    assertTrue(accountSetAsActive.get());
    assertTrue(userTokenSaved.get());
  }

  @Test
  public void ioExceptionThrown_showNetworkErrorOccurredErrorCalled() throws Exception {
    when(authService.getUserToken(any(Credentials.class)))
            .thenThrow(IOException.class);
    final AtomicBoolean showNetworkErrorOccurredErrorCalled = new AtomicBoolean(false);
    setTrue(showNetworkErrorOccurredErrorCalled).when(view).showNetworkErrorOccurredError();
    presenter.onViewAttached(view);
    presenter.attemptToLogIn(USERNAME_VALID, PASSWORD_VALID);

    assertTrue(showNetworkErrorOccurredErrorCalled.get());
  }
}

package com.caco3.mvk.navdrawer;

import com.caco3.mvk.Rxs;
import com.caco3.mvk.data.appuser.AppUser;
import com.caco3.mvk.data.appuser.AppUsersRepository;
import com.caco3.mvk.data.vkuser.InMemoryVkUsersRepository;
import com.caco3.mvk.data.vkuser.VkUsersRepository;
import com.caco3.mvk.vk.users.VkUserGenerator;
import com.caco3.mvk.timber.SystemOutTree;
import com.caco3.mvk.vk.Vk;
import com.caco3.mvk.vk.auth.UserToken;
import com.caco3.mvk.vk.users.VkUser;
import com.caco3.mvk.vk.users.VkUsersService;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

import timber.log.Timber;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class NavDrawerPresenterImplTest {
  private static final VkUserGenerator vkUserGenerator = new VkUserGenerator();
  private static final UserToken VALID_DUMMY_USER_TOKEN = new UserToken("dummyAccessToken");
  @Mock
  private Vk vk;
  @Mock
  private VkUsersService vkUsersService;
  @Mock
  private AppUsersRepository appUsersRepository;
  private VkUsersRepository vkUsersRepository = new InMemoryVkUsersRepository();
  @Mock
  private NavDrawerView view;
  @Mock
  private AppUser appUser;
  private NavDrawerPresenterImpl presenter;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    Timber.plant(new SystemOutTree());
    Rxs.setUpRx();
    presenter = new NavDrawerPresenterImpl(appUsersRepository, appUser, vkUsersRepository, vk);
    when(vk.users()).thenReturn(vkUsersService);
  }

  @After
  public void tearDown() {
    Rxs.tearDownRx();
    vkUsersRepository.deleteAll();
  }

  @Test
  public void noVkUserLoaded_getVkUserCalled() throws Exception {
    appUserReturnsValidUserToken();
    thereIsNoVkUserPreviouslyLoaded();
    final AtomicBoolean getVkUserCalled = new AtomicBoolean(false);
    when(vkUsersService.get(any(UserToken.class))).then(new Answer<VkUser>() {
      @Override
      public VkUser answer(InvocationOnMock invocation) throws Throwable {
        getVkUserCalled.set(true);
        return vkUserGenerator.generateOne();
      }
    });
    presenter.onViewAttached(view);

    assertTrue(getVkUserCalled.get());
  }

  @Test
  public void vkUserLoadedTooLongAgo_getVkUserCalled() throws Exception {
    appUserReturnsValidUserToken();
    thereIsTooLongAgoLoadedVkUser();
    final AtomicBoolean getVkUserCalled = new AtomicBoolean(false);
    when(vkUsersService.get(any(UserToken.class))).then(new Answer<VkUser>() {
      @Override
      public VkUser answer(InvocationOnMock invocation) throws Throwable {
        getVkUserCalled.set(true);
        return vkUserGenerator.generateOne();
      }
    });
    presenter.onViewAttached(view);

    assertTrue(getVkUserCalled.get());
  }

  @Test
  public void vkUserUpdatedRecently_getVkUserNotCalled() throws Exception {
    appUserReturnsValidUserToken();
    thereIsRecentlyUpdatedVkUser();
    when(vkUsersService.get(any(UserToken.class))).then(new Answer<VkUser>() {
      @Override
      public VkUser answer(InvocationOnMock invocation) throws Throwable {
        fail("get vkUser called.");
        return vkUserGenerator.generateOne();
      }
    });
    presenter.onViewAttached(view);
  }

  @Test
  public void noVkUserLoaded_afterGetVkUserCalledItSavedToVkUsersRepository() throws Exception {
    appUserReturnsValidUserToken();
    VkUser receivedVkUser = vkUserGenerator.generateOne();
    when(vkUsersService.get(VALID_DUMMY_USER_TOKEN)).thenReturn(receivedVkUser);
    presenter.onViewAttached(view);

    assertTrue(vkUsersRepository.getAll().contains(receivedVkUser));
  }

  @Test
  public void vkUserUpdatedTooLongAgo_afterGetVkUserCalledItUpdatedInRepository() throws Exception {
    appUserReturnsValidUserToken();
    thereIsTooLongAgoLoadedVkUser();
    VkUser receivedVkUser = vkUserGenerator.generateOne();
    when(vkUsersService.get(VALID_DUMMY_USER_TOKEN)).thenReturn(receivedVkUser);
    presenter.onViewAttached(view);

    List<VkUser> allUsers = vkUsersRepository.getAll();
    assertTrue(allUsers.size() == 1);
  }

  @Test
  public void vkUserUpdated_allViewsNotified() throws Exception {
    appUserReturnsValidUserToken();
    VkUser returnedByVk = vkUserGenerator.generateOne();
    final AtomicLong showVkUserCalls = new AtomicLong();
    List<NavDrawerView> views = new ArrayList<>();
    final int numOfViews = 100;
    for(int i = 0; i < numOfViews; i++) {
      NavDrawerView toAdd = mock(NavDrawerView.class);
      doAnswer(new Answer() {
        @Override
        public Object answer(InvocationOnMock invocation) throws Throwable {
          showVkUserCalls.incrementAndGet();
          return null;
        }
      }).when(toAdd).showVkUser(returnedByVk);
      views.add(toAdd);
    }
    ensureVkUserWillNotBeUpdated();
    for (NavDrawerView view: views) {
      presenter.onViewAttached(view);
    }
    when(vkUsersService.get(VALID_DUMMY_USER_TOKEN)).thenReturn(returnedByVk);
    presenter.updateVkUser();

    assertEquals(numOfViews, showVkUserCalls.get());
  }

  private void ensureVkUserWillNotBeUpdated() {
    thereIsRecentlyUpdatedVkUser();
  }

  private void appUserReturnsValidUserToken() {
    when(appUser.getUserToken()).thenReturn(VALID_DUMMY_USER_TOKEN);
  }

  private void thereIsNoVkUserPreviouslyLoaded() {
    when(appUser.getVkUser()).thenReturn(null);
  }

  private void thereIsTooLongAgoLoadedVkUser() {
    VkUser tooLongAgoLoadedVkUser = vkUserGenerator.generateOne();
    tooLongAgoLoadedVkUser.setLastUpdated(getTimeToTriggerVkUserUpdating());
    when(appUser.getVkUser()).thenReturn(tooLongAgoLoadedVkUser);
    vkUsersRepository.save(tooLongAgoLoadedVkUser);
  }

  private void thereIsRecentlyUpdatedVkUser() {
    VkUser user = vkUserGenerator.generateOne();
    user.setLastUpdated(System.currentTimeMillis());
    when(appUser.getVkUser()).thenReturn(user);
    vkUsersRepository.save(user);
  }

  private long getTimeToTriggerVkUserUpdating() {
    long rightNow = System.currentTimeMillis();
    return rightNow - 2 * NavDrawerPresenterImpl.MIN_TIME_DIFF_TO_UPDATE_CURRENT_VK_USER;
  }
}

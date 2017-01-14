package com.caco3.mvk.audios;

import com.caco3.mvk.Rxs;
import com.caco3.mvk.data.appuser.AppUser;
import com.caco3.mvk.data.audio.AudiosRepository;
import com.caco3.mvk.timber.SystemOutTree;
import com.caco3.mvk.vk.Vk;
import com.caco3.mvk.vk.audio.Audio;
import com.caco3.mvk.vk.audio.VkAudiosService;
import com.caco3.mvk.vk.auth.UserToken;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import timber.log.Timber;

import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

public class AudiosPresenterImplTest {
  @Mock
  private Vk vk;
  @Mock
  private VkAudiosService audiosService;
  @Mock
  private AppUser appUser;
  @Mock
  private AudiosRepository audiosRepository;
  @Mock
  private AudiosView view;
  private AudiosPresenter presenter;
  private AudiosGenerator audiosGenerator = new AudiosGenerator();

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    initAppUser();
    initVk();
    presenter = new AudiosPresenterImpl(appUser, audiosRepository, vk);
    Rxs.setUpRx();
    Timber.plant(new SystemOutTree());
  }

  private void initAppUser() {
    when(appUser.getUsername()).thenReturn("dummy_username");
    when(appUser.getUserToken()).thenReturn(new UserToken("dummy_access_token"));
  }

  private void initVk() {
    when(vk.audios()).thenReturn(audiosService);
  }

  @After
  public void tearDown() {
    Rxs.tearDownRx();
  }

  @Test
  public void viewAttached_songsFromRepositoryShown() {
    final List<Audio> audios = audiosGenerator.generateList(10);
    when(audiosRepository.getAllByAppUser(appUser)).thenReturn(audios);
    final AtomicBoolean audiosFromRepositoryShown = new AtomicBoolean(false);
    doAnswer(new Answer() {
      @Override
      public Object answer(InvocationOnMock invocation) throws Throwable {
        audiosFromRepositoryShown.set(true);
        return null;
      }
    }).when(view).showAudios(audios);
    presenter.onViewAttached(view);

    assertTrue(audiosFromRepositoryShown.get());
  }

  @Test
  public void viewAttached_showGlobalProgressCalled() {
    final AtomicBoolean showGlobalProgressCalled = new AtomicBoolean(false);
    doAnswer(new Answer() {
      @Override
      public Object answer(InvocationOnMock invocation) throws Throwable {
        showGlobalProgressCalled.set(true);
        return null;
      }
    }).when(view).showGlobalProgress();
    presenter.onViewAttached(view);

    assertTrue(showGlobalProgressCalled.get());
  }

  @Test
  public void viewAttached_afterShowProgressHideProgressCalled() {
    final AtomicBoolean showGlobalProgressCalled = new AtomicBoolean(false);
    final AtomicBoolean hideProgressCalledAfterShowProgress = new AtomicBoolean(false);
    doAnswer(new Answer() {
      @Override
      public Object answer(InvocationOnMock invocation) throws Throwable {
        showGlobalProgressCalled.set(true);
        return null;
      }
    }).when(view).showGlobalProgress();
    doAnswer(new Answer() {
      @Override
      public Object answer(InvocationOnMock invocation) throws Throwable {
        if (showGlobalProgressCalled.get()) {
          hideProgressCalledAfterShowProgress.set(true);
        }
        return null;
      }
    }).when(view).hideGlobalProgress();
    presenter.onViewAttached(view);

    assertTrue(hideProgressCalledAfterShowProgress.get());
  }

  @Test
  public void onRefreshRequestCalled_showRefreshLayoutCalled() {
    final AtomicBoolean showRefreshLayoutCalled = new AtomicBoolean(false);
    doAnswer(new Answer() {
      @Override
      public Object answer(InvocationOnMock invocation) throws Throwable {
        showRefreshLayoutCalled.set(true);
        return null;
      }
    }).when(view).showRefreshLayout();
    presenter.onViewAttached(view);
    presenter.onRefreshRequest();

    assertTrue(showRefreshLayoutCalled.get());
  }

  @Test
  public void onRefreshRequestCalled_hideRefreshLayoutCalledAfterShowRefreshLayout() {
    final AtomicBoolean showRefreshLayoutCalled = new AtomicBoolean(false);
    doAnswer(new Answer() {
      @Override
      public Object answer(InvocationOnMock invocation) throws Throwable {
        showRefreshLayoutCalled.set(true);
        return null;
      }
    }).when(view).showRefreshLayout();
    final AtomicBoolean hideRefreshLayoutCalled = new AtomicBoolean(false);
    doAnswer(new Answer() {
      @Override
      public Object answer(InvocationOnMock invocation) throws Throwable {
        if (showRefreshLayoutCalled.get()) {
          hideRefreshLayoutCalled.set(true);
        }
        return null;
      }
    }).when(view).hideRefreshLayout();
    presenter.onViewAttached(view);
    presenter.onRefreshRequest();

    assertTrue(hideRefreshLayoutCalled.get());
  }

  @Test
  public void ioExceptionThrown_showNetworkErrorOccurredErrorCalled() throws Exception {
    when(audiosService.get(any(UserToken.class))).thenThrow(IOException.class);
    final AtomicBoolean showNetworkErrorOccurredErrorCalled = new AtomicBoolean(false);
    doAnswer(new Answer() {
      @Override
      public Object answer(InvocationOnMock invocation) throws Throwable {
        showNetworkErrorOccurredErrorCalled.set(true);
        return null;
      }
    }).when(view).showNetworkErrorOccurredError();
    presenter.onViewAttached(view);
    presenter.onRefreshRequest();

    assertTrue(showNetworkErrorOccurredErrorCalled.get());
  }

  @Test
  public void errorOccurred_hideRefreshLayoutCalled() throws Exception {
    when(audiosService.get(any(UserToken.class))).thenThrow(IOException.class);
    final AtomicBoolean hideRefreshLayoutCalled = new AtomicBoolean(false);
    doAnswer(new Answer() {
      @Override
      public Object answer(InvocationOnMock invocation) throws Throwable {
        hideRefreshLayoutCalled.set(true);
        return null;
      }
    }).when(view).hideRefreshLayout();
    presenter.onViewAttached(view);
    presenter.onRefreshRequest();

    assertTrue(hideRefreshLayoutCalled.get());
  }

  @Test
  public void audiosUpdated_repositoryDoesNotContainsDuplicates() throws Exception {
    final List<Audio> audiosInRepository = audiosGenerator.generateList(1);
    when(audiosRepository.getAllByAppUser(appUser)).thenReturn(audiosInRepository);
    doAnswer(new Answer() {
      @Override
      public Object answer(InvocationOnMock invocation) throws Throwable {
        audiosInRepository.clear();
        return null;
      }
    }).when(audiosRepository).deleteAllByAppUser(appUser);
    doAnswer(new Answer() {
      @Override
      public Object answer(InvocationOnMock invocation) throws Throwable {
        // noinspection unchecked
        List<Audio> audios = (List<Audio>)invocation.getArguments()[0];
        audiosInRepository.addAll(audios);
        return null;
      }
    }).when(audiosRepository).saveAll(any(Iterable.class));

    List<Audio> vkReturnsSameAudios = new ArrayList<>();
    for(Audio audio : audiosInRepository) {
      Audio a = new Audio();
      a.setDownloadUrl(audio.getDownloadUrl());
      a.setTitle(audio.getTitle());
      a.setArtist(audio.getArtist());
      a.setDurationSeconds(audio.getDurationSeconds());
      vkReturnsSameAudios.add(a);
    }
    when(audiosService.get(any(UserToken.class))).thenReturn(vkReturnsSameAudios);
    presenter.onRefreshRequest();

    for(Audio outer : audiosInRepository) {
      for(Audio inner : audiosInRepository) {
        if (outer != inner && outer.equals(inner)) {
          fail();
        }
      }
    }
  }
}

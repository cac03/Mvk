package com.caco3.mvk.myaudios;

import com.caco3.mvk.Rxs;
import com.caco3.mvk.audiodownload.AudioDownloader;
import com.caco3.mvk.data.appuser.AppUser;
import com.caco3.mvk.data.audio.AudiosRepository;
import com.caco3.mvk.timber.SystemOutTree;
import com.caco3.mvk.util.Integers;
import com.caco3.mvk.vk.Vk;
import com.caco3.mvk.vk.audio.Audio;
import com.caco3.mvk.vk.audio.AudiosGenerator;
import com.caco3.mvk.vk.audio.VkAudiosService;
import com.caco3.mvk.vk.auth.UserToken;
import com.caco3.mvk.vk.users.VkUser;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import timber.log.Timber;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

public class MyAudiosPresenterImplTest {
  @Mock
  private Vk vk;
  @Mock
  private VkAudiosService audiosService;
  @Mock
  private AppUser appUser;
  @Mock
  private VkUser vkUser;
  @Mock
  private AudiosRepository audiosRepository;
  @Mock
  private MyAudiosView view;
  @Mock private AudioDownloader downloader;
  private MyAudiosPresenter presenter;
  private AudiosGenerator audiosGenerator = new AudiosGenerator();

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    initAppUser();
    initVk();
    presenter = new MyAudiosPresenterImpl(appUser, audiosRepository, vk, downloader);
    Rxs.setUpRx();
    Timber.plant(new SystemOutTree());
    when(appUser.getVkUser()).thenReturn(vkUser);
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
    when(audiosRepository.getAllByVkUserId(anyLong())).thenReturn(audios);
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
    when(audiosService.get()).thenThrow(IOException.class);
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
    when(audiosService.get()).thenThrow(IOException.class);
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
    when(audiosRepository.getAllByVkUserId(anyLong())).thenReturn(audiosInRepository);
    doAnswer(new Answer() {
      @Override
      public Object answer(InvocationOnMock invocation) throws Throwable {
        audiosInRepository.clear();
        return null;
      }
    }).when(audiosRepository).deleteAllByVkUserId(anyLong());
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
    when(audiosService.get()).thenReturn(vkReturnsSameAudios);
    presenter.onRefreshRequest();

    for(Audio outer : audiosInRepository) {
      for(Audio inner : audiosInRepository) {
        if (outer != inner && outer.equals(inner)) {
          fail();
        }
      }
    }
  }

  @Test
  public void onSearchCalled_showItemsCalled() {
    List<Audio> audiosInRepository = audiosGenerator.generateList(100);
    AudiosFilter filter = new AudiosFilter();
    when(audiosRepository.getAllByVkUserId(anyLong())).thenReturn(audiosInRepository);
    presenter.onViewAttached(view);
    final AtomicReference<List<Audio>> actual = new AtomicReference<>();
    doAnswer(new Answer() {
      @Override
      public Object answer(InvocationOnMock invocation) throws Throwable {
        // noinspection unchecked
        List<Audio> arg = (List<Audio>)invocation.getArguments()[0];
        actual.set(arg);
        return null;
      }
    }).when(view).showAudios(ArgumentMatchers.<Audio>anyList());
    String query = "gs";
    List<Audio> expected = filter.filter(audiosInRepository, query);
    presenter.onSearch(query);
    assertEquals(expected, actual.get());
  }

  @Test
  public void onSearchCanceledCalled_itemsFromRepositoryShown() {
    List<Audio> audiosInRepository = audiosGenerator.generateList(100);
    when(audiosRepository.getAllByVkUserId(anyLong())).thenReturn(audiosInRepository);
    presenter.onViewAttached(view);
    final AtomicReference<List<Audio>> actual = new AtomicReference<>();
    doAnswer(new Answer() {
      @Override
      public Object answer(InvocationOnMock invocation) throws Throwable {
        // noinspection unchecked
        List<Audio> arg = (List<Audio>)invocation.getArguments()[0];
        actual.set(arg);
        return null;
      }
    }).when(view).showAudios(ArgumentMatchers.<Audio>anyList());
    presenter.onSearchCanceled();
    assertEquals(audiosInRepository, actual.get());
  }

  @Test
  public void filterAppliedThenOnRefreshRequestCalled_itemsFilteredAfterRefreshShown() throws Exception {
    List<Audio> fromRepository = audiosGenerator.generateList(5);
    AudiosFilter audiosFilter = new AudiosFilter();
    when(audiosRepository.getAllByVkUserId(anyLong())).thenReturn(fromRepository);
    String dummyQuery = "af";
    List<Audio> filtered = audiosFilter.filter(fromRepository, dummyQuery);
    presenter.onViewAttached(view);
    final AtomicReference<List<Audio>> actuallyShown = new AtomicReference<>();
    doAnswer(new Answer() {
      @Override
      public Object answer(InvocationOnMock invocation) throws Throwable {
        // noinspection unchecked
        actuallyShown.set((List<Audio>)invocation.getArguments()[0]);
        return null;
      }
    }).when(view).showAudios(any(List.class));
    presenter.onSearch(dummyQuery);
    Audio mustBeShown = audiosGenerator.generateOne();
    mustBeShown.setArtist("afoiqwru");
    Audio mustNotBeShown = audiosGenerator.generateOne();
    mustNotBeShown.setArtist("");
    mustNotBeShown.setTitle("");
    fromRepository.add(mustBeShown);
    fromRepository.add(mustNotBeShown);
    when(audiosService.get()).thenReturn(fromRepository);
    presenter.onRefreshRequest();

    assertThat(actuallyShown.get())
            .containsAll(filtered)
            .contains(mustBeShown)
            .doesNotContain(mustNotBeShown);
  }

  @Test
  public void audiosReceivedFromVk_theyHaveSetVkPlaylistPositionWhenReplaceInRepositoryCalled()
          throws Exception{
    when(audiosRepository.getAllByVkUserId(anyLong())).thenReturn(Collections.<Audio>emptyList());
    List<Audio> audios = audiosGenerator.generateList(100);
    when(audiosService.get()).thenReturn(audios);
    final AtomicBoolean replaceCalled = new AtomicBoolean();
    doAnswer(new Answer() {
      @Override
      public Object answer(InvocationOnMock invocation) throws Throwable {
        replaceCalled.set(true);
        List<Audio> toSave = (List<Audio>)invocation.getArguments()[1];
        assertThat(toSave)
                .usingElementComparator(MyAudiosPresenterImpl.audioByPositionComparator)
                .isSorted();
        return null;
      }
    }).when(audiosRepository).replaceAllByVkUserId(anyLong(), ArgumentMatchers.<Audio>anyList());
    presenter.onRefreshRequest();
    assertThat(replaceCalled.get())
            .isTrue();
  }

  @Test
  public void viewAttached_showAudiosCalledWithSortedByVkPlaylistPositionAudios() {
    List<Audio> fromRepository = audiosGenerator.generateList(100);
    for(int i = 0, length = fromRepository.size(); i < length; i++) {
      fromRepository.get(i).setVkPlaylistPosition(i);
    }
    Collections.shuffle(fromRepository);
    when(audiosRepository.getAllByVkUserId(anyLong())).thenReturn(fromRepository);
    final AtomicBoolean showAudiosCalled = new AtomicBoolean();
    doAnswer(new Answer() {
      @Override
      public Object answer(InvocationOnMock invocation) throws Throwable {
        showAudiosCalled.set(true);
        assertThat((List<Audio>)invocation.getArguments()[0])
                .usingElementComparator(MyAudiosPresenterImpl.audioByPositionComparator)
                .isSorted();
        return null;
      }
    }).when(view).showAudios(fromRepository);
    presenter.onViewAttached(view);
    assertThat(showAudiosCalled.get())
            .isTrue();
  }

  @Test public void onAudioSelectedCalled_showAudioSelectedCalled() {
    presenter.onViewAttached(view);
    final AtomicBoolean showAudioSelectedCalled = new AtomicBoolean();
    Audio audio = audiosGenerator.generateOne();
    doAnswer(new Answer() {
      @Override
      public Object answer(InvocationOnMock invocation) throws Throwable {
        showAudioSelectedCalled.set(true);
        return null;
      }
    }).when(view).showAudioSelected(audio);
    presenter.onAudioSelected(audio);
    assertThat(showAudioSelectedCalled.get())
            .isTrue();
  }

  @Test public void onAudioSelectedCalledTwiceWithSameAudio_cancelAudioSelectCalled() {
    presenter.onViewAttached(view);
    final AtomicBoolean cancelAudioSelectCalled = new AtomicBoolean();
    Audio audio = audiosGenerator.generateOne();
    doAnswer(new Answer() {
      @Override
      public Object answer(InvocationOnMock invocation) throws Throwable {
        cancelAudioSelectCalled.set(true);
        return null;
      }
    }).when(view).cancelAudioSelect(audio);
    presenter.onAudioSelected(audio);
    presenter.onAudioSelected(audio);
    assertThat(cancelAudioSelectCalled.get())
            .isTrue();
  }

  @Test public void audiosSelectedDownloadSelectedAudiosCalled_audiosPostedToDownloader() {
    final List<Audio> postedToDownloader = new ArrayList<>();
    doAnswer(new Answer() {
      @Override
      public Object answer(InvocationOnMock invocation) throws Throwable {
        postedToDownloader.add((Audio)invocation.getArguments()[0]);
        return null;
      }
    }).when(downloader).post(any(Audio.class));
    List<Audio> audios = audiosGenerator.generateList(100);
    for(Audio audio : audios) {
      presenter.onAudioSelected(audio);
    }
    presenter.onDownloadSelectedAudiosRequest();
    assertThat(postedToDownloader)
            .hasSize(100)
            .containsAll(audios);
  }

  @Test public void downloadSelectedAudiosCalled_cancelAudioSelectOnDownloadableAudiosCalled() {
    presenter.onViewAttached(view);
    final List<Audio> calledFor = new ArrayList<>();
    doAnswer(new Answer() {
      @Override
      public Object answer(InvocationOnMock invocation) throws Throwable {
        calledFor.add((Audio)invocation.getArguments()[0]);
        return null;
      }
    }).when(view).cancelAudioSelect(any(Audio.class));
    List<Audio> selected = audiosGenerator.generateList(100);
    for(Audio audio : selected) {
      presenter.onAudioSelected(audio);
    }
    presenter.onDownloadSelectedAudiosRequest();
    assertThat(calledFor)
            .hasSize(100)
            .containsAll(selected);
  }

  @Test public void someAudiosSelected_viewReattachedShowAudioSelectedOnSelectedAudiosCalled() {
    List<Audio> selected = audiosGenerator.generateList(100);
    for(Audio audio : selected) {
      presenter.onAudioSelected(audio);
    }
    final List<Audio> calledFor = new ArrayList<>();
    doAnswer(new Answer() {
      @Override
      public Object answer(InvocationOnMock invocation) throws Throwable {
        calledFor.add((Audio)invocation.getArguments()[0]);
        return null;
      }
    }).when(view).showAudioSelected(any(Audio.class));
    presenter.onViewAttached(view);
    assertThat(calledFor)
            .hasSize(100)
            .containsAll(selected);
  }
}

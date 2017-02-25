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
import org.mockito.stubbing.Stubber;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import timber.log.Timber;

import static com.caco3.mvk.Stubbers.appendTo;
import static com.caco3.mvk.Stubbers.setArg;
import static com.caco3.mvk.Stubbers.setTrue;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.assertj.core.api.Java6Assertions.in;
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
  private MyAudiosPresenterImpl presenter;
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
    setTrue(audiosFromRepositoryShown).when(view).showAudios(audios);
    presenter.onViewAttached(view);

    assertTrue(audiosFromRepositoryShown.get());
  }

  @Test
  public void viewAttached_showGlobalProgressCalled() {
    final AtomicBoolean showGlobalProgressCalled = new AtomicBoolean(false);
    setTrue(showGlobalProgressCalled).when(view).showGlobalProgress();
    presenter.onViewAttached(view);

    assertTrue(showGlobalProgressCalled.get());
  }

  @Test
  public void viewAttached_afterShowProgressHideProgressCalled() {
    final AtomicBoolean showGlobalProgressCalled = new AtomicBoolean(false);
    final AtomicBoolean hideProgressCalledAfterShowProgress = new AtomicBoolean(false);
    setTrue(showGlobalProgressCalled).when(view).showGlobalProgress();
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
    setTrue(showRefreshLayoutCalled).when(view).showRefreshLayout();
    presenter.onViewAttached(view);
    presenter.onRefreshRequest();

    assertTrue(showRefreshLayoutCalled.get());
  }

  @Test
  public void onRefreshRequestCalled_hideRefreshLayoutCalledAfterShowRefreshLayout() {
    final AtomicBoolean showRefreshLayoutCalled = new AtomicBoolean(false);
    setTrue(showRefreshLayoutCalled).when(view).showRefreshLayout();
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
    setTrue(showNetworkErrorOccurredErrorCalled).when(view).showNetworkErrorOccurredError();
    presenter.onViewAttached(view);
    presenter.onRefreshRequest();

    assertTrue(showNetworkErrorOccurredErrorCalled.get());
  }

  @Test
  public void errorOccurred_hideRefreshLayoutCalled() throws Exception {
    when(audiosService.get()).thenThrow(IOException.class);
    final AtomicBoolean hideRefreshLayoutCalled = new AtomicBoolean(false);
    setTrue(hideRefreshLayoutCalled).when(view).hideRefreshLayout();
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
    setArg(actual).when(view).showAudios(ArgumentMatchers.<Audio>anyList());
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
    setArg(actual).when(view).showAudios(ArgumentMatchers.<Audio>anyList());
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
    setArg(actuallyShown).when(view).showAudios(any(List.class));
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
    final AtomicReference<List<Audio>> calledWith = new AtomicReference<>();
    setArg(calledWith, 1).when(audiosRepository)
            .replaceAllByVkUserId(anyLong(), ArgumentMatchers.<Audio>anyList());
    presenter.onRefreshRequest();
    assertThat(calledWith.get())
            .usingElementComparator(MyAudiosPresenterImpl.audioByPositionComparator)
            .isSorted();
  }

  @Test
  public void viewAttached_showAudiosCalledWithSortedByVkPlaylistPositionAudios() {
    List<Audio> fromRepository = audiosGenerator.generateList(100);
    for(int i = 0, length = fromRepository.size(); i < length; i++) {
      fromRepository.get(i).setVkPlaylistPosition(i);
    }
    Collections.shuffle(fromRepository);
    when(audiosRepository.getAllByVkUserId(anyLong())).thenReturn(fromRepository);
    AtomicReference<List<Audio>> calledWith = new AtomicReference<>();
    setArg(calledWith).when(view).showAudios(fromRepository);
    presenter.onViewAttached(view);
    assertThat(calledWith.get())
            .isNotNull()
            .usingElementComparator(MyAudiosPresenterImpl.audioByPositionComparator)
            .isSorted();
  }

  @Test public void onAudioClickCalledInSelectMode_showAudioSelectedCalled() {
    presenter.onViewAttached(view);
    final AtomicBoolean showAudioSelectedCalled = new AtomicBoolean();
    Audio audio = audiosGenerator.generateOne();
    setTrue(showAudioSelectedCalled).when(view).showAudioSelected(audio);
    presenter.mode = presenter.selectMode;
    presenter.onAudioClicked(audio);
    assertThat(showAudioSelectedCalled.get())
            .isTrue();
  }

  @Test public void onAudioClickCalledTwiceWithSameAudioInSelectMode_cancelAudioSelectCalled() {
    presenter.onViewAttached(view);
    final AtomicBoolean cancelAudioSelectCalled = new AtomicBoolean();
    Audio audio = audiosGenerator.generateOne();
    setTrue(cancelAudioSelectCalled).when(view).cancelAudioSelect(audio);
    presenter.mode = presenter.selectMode;
    presenter.onAudioClicked(audio);
    presenter.onAudioClicked(audio);
    assertThat(cancelAudioSelectCalled.get())
            .isTrue();
  }

  @Test public void audiosClickedInSelectModeAndDownloadSelectedAudiosCalled_audiosPostedToDownloader() {
    presenter.mode = presenter.selectMode;
    final List<Audio> postedToDownloader = new ArrayList<>();
    appendTo(postedToDownloader).when(downloader).post(any(Audio.class));
    List<Audio> audios = audiosGenerator.generateList(100);
    for(Audio audio : audios) {
      presenter.onAudioClicked(audio);
    }
    presenter.onDownloadSelectedAudiosRequest();
    assertThat(postedToDownloader)
            .hasSize(100)
            .containsAll(audios);
  }

  @Test public void downloadSelectedAudiosCalled_cancelAudioSelectOnDownloadableAudiosCalled() {
    presenter.onViewAttached(view);
    final List<Audio> calledFor = new ArrayList<>();
    appendTo(calledFor).when(view).cancelAudioSelect(any(Audio.class));
    List<Audio> selected = audiosGenerator.generateList(100);
    presenter.mode = presenter.selectMode;
    for(Audio audio : selected) {
      presenter.onAudioClicked(audio);
    }
    presenter.onDownloadSelectedAudiosRequest();
    assertThat(calledFor)
            .hasSize(100)
            .containsAll(selected);
  }

  @Test public void someAudiosClickedInSelectMode_viewReattachedShowAudioSelectedOnSelectedAudiosCalled() {
    List<Audio> selected = audiosGenerator.generateList(100);
    presenter.mode = presenter.selectMode;
    for(Audio audio : selected) {
      presenter.onAudioClicked(audio);
    }
    final List<Audio> calledFor = new ArrayList<>();
    appendTo(calledFor).when(view).showAudioSelected(any(Audio.class));
    presenter.onViewAttached(view);
    assertThat(calledFor)
            .hasSize(100)
            .containsAll(selected);
  }

  @Test public void someAudiosSelectedAndThenSomeCanceledDownloadCalled_selectedPostedToDownloader() {
    presenter.mode = presenter.selectMode;
    Audio selected1 = audiosGenerator.generateOne();
    Audio selected2 = audiosGenerator.generateOne();
    Audio canceled = audiosGenerator.generateOne();
    presenter.onAudioClicked(selected1);
    presenter.onAudioClicked(selected2);
    presenter.onAudioClicked(canceled);
    presenter.onAudioClicked(canceled);
    final List<Audio> postedToDownloader = new ArrayList<>();
    appendTo(postedToDownloader).when(downloader).post(any(Audio.class));
    presenter.onDownloadSelectedAudiosRequest();
    assertThat(postedToDownloader)
            .hasSize(2)
            .contains(selected1, selected2)
            .doesNotContain(canceled);
  }

  @Test public void audioLongClickedCalledInNormalMode_modeChangedToSelect() {
    presenter.onAudioLongClicked(new Audio());
    assertThat(presenter.mode)
            .isSameAs(presenter.selectMode);
  }

  @Test public void audioLongClickedCalledInNormalMode_startSelectModeCalled() {
    presenter.onViewAttached(view);
    final AtomicBoolean startSelectModeCalled = new AtomicBoolean();
    setTrue(startSelectModeCalled).when(view).startSelectMode();
    presenter.onAudioLongClicked(new Audio());
    assertThat(startSelectModeCalled.get())
            .isTrue();
  }

  @Test public void audioLongClickedCalledInNormalMode_showAudioSelectedCalled() {
    presenter.onViewAttached(view);
    Audio audio = audiosGenerator.generateOne();
    AtomicBoolean showAudioSelectedCalled = new AtomicBoolean();
    setTrue(showAudioSelectedCalled).when(view).showAudioSelected(audio);
    presenter.onAudioLongClicked(audio);
    assertThat(showAudioSelectedCalled.get())
            .isTrue();
  }

  @Test public void audioClickedCalledInNormalMode_showActionsForAudioCalled() {
    presenter.onViewAttached(view);
    Audio audio = audiosGenerator.generateOne();
    AtomicBoolean showActionsForAudioCalled = new AtomicBoolean();
    setTrue(showActionsForAudioCalled).when(view).showActionsFor(audio);
    presenter.onAudioClicked(audio);
    assertThat(showActionsForAudioCalled.get())
            .isTrue();
  }

  @Test public void someAudiosSelectedAndThenDownloadSelectedCalled_cancelAudioSelectCalledForAll() {
    presenter.onViewAttached(view);
    List<Audio> selected = audiosGenerator.generateList(100);
    List<Audio> calledFor = new ArrayList<>();
    appendTo(calledFor).when(view).cancelAudioSelect(any(Audio.class));
    for(Audio audio : selected) {
      presenter.onAudioLongClicked(audio);
    }
    presenter.onDownloadSelectedAudiosRequest();
    assertThat(calledFor)
            .hasSize(100)
            .containsAll(selected);
  }

  @Test public void onSelectModeFinishedCalled_cancelSelectCalledForAllSelectedAudios() {
    presenter.onViewAttached(view);
    List<Audio> selected = audiosGenerator.generateList(100);
    presenter.selectedInActionMode.addAll(selected);
    List<Audio> calledFor = new ArrayList<>();
    appendTo(calledFor).when(view).cancelAudioSelect(any(Audio.class));
    presenter.onSelectModeFinished();
    assertThat(calledFor)
            .hasSize(100)
            .containsAll(selected);
  }

  @Test public void someAudioSelectedViewReattached_showAudioSelectedForAllSelectedAudios() {
    List<Audio> calledFor = new ArrayList<>();
    appendTo(calledFor).when(view).showAudioSelected(any(Audio.class));
    List<Audio> selected = audiosGenerator.generateList(100);
    presenter.selectedInActionMode.addAll(selected);
    presenter.onViewAttached(view);
    assertThat(calledFor)
            .hasSize(100)
            .containsAll(selected);
  }

  @Test public void modeSetToSelectViewAttached_startSelectModeCalled() {
    AtomicBoolean startSelectModeCalled = new AtomicBoolean();
    setTrue(startSelectModeCalled).when(view).startSelectMode();
    presenter.mode = presenter.selectMode;
    presenter.onViewAttached(view);
    assertThat(startSelectModeCalled.get())
            .isTrue();
  }

  @Test public void audioClickedInSelectMode_setSelectModeTitleCalled() {
    AtomicBoolean setSelectModeTitleCalled = new AtomicBoolean();
    setTrue(setSelectModeTitleCalled).when(view)
            .setSelectModeTitle(ArgumentMatchers.<Audio>anyList());
    presenter.onViewAttached(view);
    presenter.onAudioLongClicked(new Audio());
    assertThat(setSelectModeTitleCalled.get())
            .isTrue();
  }

  @Test public void onAudioClickedCalledWithLastSelectedItemInSelectMode_finishSelectModeCalled() {
    Audio audio = audiosGenerator.generateOne();
    presenter.onViewAttached(view);
    presenter.mode = presenter.selectMode;
    presenter.selectedInActionMode.add(audio);
    AtomicBoolean finishSelectModeCalled = new AtomicBoolean();
    setTrue(finishSelectModeCalled).when(view).finishSelectMode();
    presenter.onAudioClicked(audio);
    assertThat(finishSelectModeCalled.get())
            .isTrue();
  }
}

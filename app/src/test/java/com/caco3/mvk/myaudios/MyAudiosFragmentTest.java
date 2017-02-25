package com.caco3.mvk.myaudios;

import android.Manifest;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.view.MenuItem;
import android.view.View;

import com.caco3.mvk.BuildConfig;
import com.caco3.mvk.SupportFragmentStarter;
import com.caco3.mvk.R;
import com.caco3.mvk.vk.audio.Audio;
import com.caco3.mvk.vk.audio.AudiosGenerator;

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
import org.robolectric.shadows.ShadowAlertDialog;
import org.robolectric.shadows.ShadowApplication;
import org.robolectric.shadows.ShadowToast;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.caco3.mvk.Stubbers.setTrue;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.robolectric.Shadows.shadowOf;

@RunWith(RobolectricTestRunner.class)
@Config(
        constants = BuildConfig.class,
        sdk = 22
)
public class MyAudiosFragmentTest {
  @Mock
  private MyAudiosPresenter presenter;
  private MyAudiosFragment fragment;
  private AudiosGenerator audiosGenerator = new AudiosGenerator();
  private ShadowApplication shadowApplication;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    fragment = new MyAudiosFragment();
    fragment.presenter = presenter;
    shadowApplication = shadowOf(RuntimeEnvironment.application);
    SupportFragmentStarter.startFragment(fragment);
  }

  @Test
  public void showGlobalProgressCalled_progressBarShownAudiosContentHidden() {
    fragment.showGlobalProgress();
    assertTrue(fragment.progressBar.getVisibility() == View.VISIBLE);
    assertTrue(fragment.audiosContentView.getVisibility() == View.GONE);
  }

  @Test
  public void hideGlobalProgressCalled_progressBarHiddenAudiosContentShown() {
    fragment.hideGlobalProgress();
    assertTrue(fragment.progressBar.getVisibility() == View.GONE);
    assertTrue(fragment.audiosContentView.getVisibility() == View.VISIBLE);
  }

  @Test
  public void hideGlobalProgressCalledAfterShowGlobalProgress_progressBarHiddenAudiosContentShown() {
    fragment.showGlobalProgress();
    fragment.hideGlobalProgress();
    assertTrue(fragment.progressBar.getVisibility() == View.GONE);
    assertTrue(fragment.audiosContentView.getVisibility() == View.VISIBLE);
  }

  @Test
  public void showRefreshLayoutCalled_refreshLayoutIsRefreshing() {
    fragment.showRefreshLayout();
    assertTrue(fragment.swipeRefreshLayout.isRefreshing());
  }

  @Test
  public void hideRefreshLayoutCalled_refreshLayoutIsNotRefreshing() {
    fragment.hideRefreshLayout();
    assertTrue(!fragment.swipeRefreshLayout.isRefreshing());
  }

  @Test
  public void showAudiosCalled_adapterItemCountSameAsProvidedAudios() {
    List<Audio> audios = audiosGenerator.generateList(10);
    fragment.showAudios(audios);
    int expected = audios.size();
    int actual = fragment.recyclerView.getAdapter().getItemCount();
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
  public void listScrolledFabClicked_listScrolledTo0Position() {
    fragment.showAudios(audiosGenerator.generateList(100));
    fragment.recyclerView.measure(0,0);
    fragment.recyclerView.layout(0,0,100,100);
    fragment.recyclerView.scrollToPosition(50);
    fragment.onFabClick();
    LinearLayoutManager layoutManager = (LinearLayoutManager)
            fragment.recyclerView.getLayoutManager();
    assertThat(layoutManager.findFirstCompletelyVisibleItemPosition())
            .isEqualTo(0);
  }

  @Test public void downloadInContextMenuClicked_onDownloadSelectedAudiosRequestCalled() {
    final AtomicBoolean downloadSelectedAudiosCalled = new AtomicBoolean();
    setTrue(downloadSelectedAudiosCalled).when(presenter).onDownloadSelectedAudiosRequest();
    fragment.startSelectMode();
    MenuItem menuItem = fragment.actionMode.getMenu().findItem(R.id.audios_context_menu_download);
    fragment.onActionItemClicked(fragment.actionMode, menuItem);
    assertThat(downloadSelectedAudiosCalled.get())
            .isTrue();
  }

  @Test public void downloadInContextMenuClicked_actionModeIsNull() {
    fragment.startSelectMode();
    MenuItem menuItem = fragment.actionMode.getMenu().findItem(R.id.audios_context_menu_download);
    fragment.onActionItemClicked(fragment.actionMode, menuItem);
    assertThat(fragment.actionMode)
            .isNull();
  }

  @Test public void onAudioLongClickCalled_onAudioLongClickCalledOnPresenter() {
    final AtomicBoolean onAudioLongClickedCalled = new AtomicBoolean();
    setTrue(onAudioLongClickedCalled).when(presenter).onAudioLongClicked(any(Audio.class));
    fragment.onAudioLongClick(new Audio());
    assertThat(onAudioLongClickedCalled.get())
            .isTrue();
  }

  @Test public void onAudioClickCalled_onAudioClickedCalledOnPresenter() {
    final AtomicBoolean onAudioClickedCalled = new AtomicBoolean();
    setTrue(onAudioClickedCalled).when(presenter).onAudioClicked(any(Audio.class));
    fragment.onAudioItemClicked(new Audio(), null);
    assertThat(onAudioClickedCalled.get())
            .isTrue();
  }

  @Test public void showAudioSelectedCalled_showSelectedOnAdapterCalled() {
    MyAudiosAdapter adapter = mock(MyAudiosAdapter.class);
    fragment.audiosAdapter = adapter;
    final AtomicBoolean showSelectedCalled = new AtomicBoolean();
    setTrue(showSelectedCalled).when(adapter).selectAudio(any(Audio.class));
    fragment.showAudioSelected(new Audio());
    assertThat(showSelectedCalled.get())
            .isTrue();
  }

  @Test public void cancelAudioSelectCalled_cancelSelectOnAdapterCalled() {
    MyAudiosAdapter adapter = mock(MyAudiosAdapter.class);
    fragment.audiosAdapter = adapter;
    final AtomicBoolean showSelectedCalled = new AtomicBoolean();
    setTrue(showSelectedCalled).when(adapter).cancelSelect(any(Audio.class));
    Audio audio = new Audio();
    fragment.showAudioSelected(audio);
    fragment.cancelAudioSelect(new Audio());
    assertThat(showSelectedCalled.get())
            .isTrue();
  }

  @Test public void actionModeIsNotNullAndOnDestroyViewCalled_actionModeIsNull() {
    fragment.actionMode = ((AppCompatActivity)fragment.getActivity()).startSupportActionMode(fragment);
    try {
      fragment.onDestroyView();
    } catch (NullPointerException ignore) {
      /**
       * Robolectric issues
       * Npe thrown when {@link android.support.v4.view.MenuItemCompat#setOnActionExpandListener(MenuItem, MenuItemCompat.OnActionExpandListener)}
       * called.
       */
    }
    assertThat(fragment.actionMode)
            .isNull();
  }

  @Test public void startSelectModeCalled_actionModeIsNotNull() {
    fragment.startSelectMode();
    assertThat(fragment.actionMode)
            .isNotNull();
  }

  @Test public void selectModeStartedAndSetSelectModeTitleCalled_actionModeTitleIsChanged() {
    fragment.startSelectMode();
    fragment.setSelectModeTitle(audiosGenerator.generateList(10));
    assertThat(fragment.actionMode.getTitle())
            .isEqualTo("10");
  }

  @Test public void selectModeStartedAndThenFinishSelectModeCalled_actionModeIsNull() {
    fragment.startSelectMode();
    fragment.finishSelectMode();
    assertThat(fragment.actionMode)
            .isNull();
  }

  @Test public void showActionsForAudioCalled_dialogShown() {
    fragment.showActionsFor(new Audio());
    ShadowAlertDialog alertDialog = shadowApplication.getLatestAlertDialog();
    /*assertThat(alertDialog)
            .isNotNull();*/
    assertThat(alertDialog.getTitle())
            .isEqualTo(fragment.getString(R.string.audio_actions_dialog_title));
    assertThat(alertDialog.getItems())
            .contains(fragment.getString(R.string.audio_download));
  }

  @Test public void showActionsForAudioCalledAndDownloadClicked_presentersMethodCalled() {
    shadowApplication.grantPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE);
    final AtomicBoolean onDownloadRequestCalled = new AtomicBoolean();
    setTrue(onDownloadRequestCalled).when(presenter).onDownloadRequest(any(Audio.class));
    fragment.showActionsFor(new Audio());
    ShadowAlertDialog alertDialog = shadowApplication.getLatestAlertDialog();
    alertDialog.clickOnItem(0);
    assertThat(onDownloadRequestCalled.get())
            .isTrue();
  }

  @Test public void onDestroyActionViewCalled_onSelectModeFinishedOnPresenterCalled() {
    final AtomicBoolean selectModeFinishedCalled = new AtomicBoolean();
    setTrue(selectModeFinishedCalled).when(presenter).onSelectModeFinished();
    fragment.onDestroyActionMode(mock(ActionMode.class));
    assertThat(selectModeFinishedCalled.get()).isTrue();
  }
}

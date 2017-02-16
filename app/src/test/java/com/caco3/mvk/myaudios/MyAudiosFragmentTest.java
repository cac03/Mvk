package com.caco3.mvk.myaudios;

import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.caco3.mvk.BuildConfig;
import com.caco3.mvk.R;
import com.caco3.mvk.vk.audio.Audio;
import com.caco3.mvk.vk.audio.AudiosGenerator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowToast;

import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.robolectric.shadows.support.v4.SupportFragmentTestUtil.startFragment;

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

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    fragment = new MyAudiosFragment();
    fragment.presenter = presenter;
    startFragment(fragment);
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
}

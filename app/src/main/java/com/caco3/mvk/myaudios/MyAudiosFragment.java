package com.caco3.mvk.myaudios;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.caco3.mvk.R;
import com.caco3.mvk.dagger.DaggerComponentsHolder;
import com.caco3.mvk.permission.PermissionRequest;
import com.caco3.mvk.ui.BaseFragment;
import com.caco3.mvk.ui.recyclerview.decorator.MarginItemDecorator;
import com.caco3.mvk.util.Intents;
import com.caco3.mvk.util.function.Action0;
import com.caco3.mvk.util.function.Action1;
import com.caco3.mvk.vk.audio.Audio;

import java.util.Comparator;
import java.util.List;


import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.recyclerview.animators.OvershootInLeftAnimator;

public class MyAudiosFragment extends BaseFragment implements MyAudiosView,
        SwipeRefreshLayout.OnRefreshListener, MyAudiosAdapter.UiEventsListener,
        SearchView.OnQueryTextListener {
  @Inject
  MyAudiosPresenter presenter;
  @BindView(R.id.audios_frag_refresh_layout)
  SwipeRefreshLayout swipeRefreshLayout;
  @BindView(R.id.audios_frag_recycler_view)
  RecyclerView recyclerView;
  @BindView(R.id.audios_frag_progress_bar)
  ProgressBar progressBar;
  @BindView(R.id.audios_frag_fab)
  FloatingActionButton floatingActionButton;
  View audiosContentView;
  private MyAudiosAdapter audiosAdapter = new MyAudiosAdapter(this);
  /**State of Search view is not saved when orientation changed.
   * So we have to save and restore it manually*/
  private String lastSearchQuery = null;
  private boolean isSearchViewExpanded = false;
  private MenuItemCompat.OnActionExpandListener searchViewOnExpandListener
          = new MenuItemCompat.OnActionExpandListener() {
    @Override
    public boolean onMenuItemActionExpand(MenuItem item) {
      isSearchViewExpanded = true;
      return true;
    }

    @Override
    public boolean onMenuItemActionCollapse(MenuItem item) {
      isSearchViewExpanded = false;
      return true;
    }
  };
  private Audio pendingAudio = null;

  @Override
  public View onCreateView(LayoutInflater inflater,
                           @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState) {
    View root = inflater.inflate(R.layout.audios_fragment, container, false);
    ButterKnife.bind(this, root);
    initViews();
    setHasOptionsMenu(true);

    return root;
  }

  private void initViews() {
    // refresh layout wraps all content relative to audios
    audiosContentView = swipeRefreshLayout;
    initRecyclerView();
    initSwipeRefreshLayout();
  }

  private void initRecyclerView() {
    int margin = (int)getResources().getDimension(R.dimen.default_padding) / 2;
    recyclerView.addItemDecoration(new MarginItemDecorator(margin, margin));
    recyclerView.setAdapter(audiosAdapter);
    recyclerView.setItemAnimator(new OvershootInLeftAnimator());
  }

  private void initSwipeRefreshLayout() {
    swipeRefreshLayout.setOnRefreshListener(this);
  }

  @OnClick(R.id.audios_frag_fab)
  /*package*/ void onFabClick() {
    recyclerView.smoothScrollToPosition(0);
  }


  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    if (presenter == null) {
      injectPresenter();
    }
    presenter.onViewAttached(this);
  }

  private void injectPresenter() {
    DaggerComponentsHolder componentsHolder = DaggerComponentsHolder.getInstance();
    componentsHolder.getMyAudiosComponent().inject(this);
  }

  @Override
  public void onDestroyView() {
    presenter.onViewDetached(this);
    super.onDestroyView();
  }

  @Override
  public void onRefresh() {
    presenter.onRefreshRequest();
  }

  @Override
  public void hideRefreshLayout() {
    swipeRefreshLayout.setRefreshing(false);
  }

  @Override
  public void showRefreshLayout() {
    swipeRefreshLayout.setRefreshing(true);
  }

  @Override
  public void showGlobalProgress() {
    audiosContentView.setVisibility(View.GONE);
    progressBar.setVisibility(View.VISIBLE);
  }

  @Override
  public void hideGlobalProgress() {
    audiosContentView.setVisibility(View.VISIBLE);
    progressBar.setVisibility(View.GONE);
  }

  @Override
  public void showAudios(List<Audio> audios) {
    audiosAdapter.setItems(audios);
  }

  @Override
  public void showNetworkIsUnavailableError() {
    Toast.makeText(getContext(), R.string.network_is_unavailable, Toast.LENGTH_SHORT).show();
  }

  @Override
  public void showNetworkErrorOccurredError() {
    Toast.makeText(getContext(), R.string.network_error_occurred, Toast.LENGTH_SHORT).show();
  }

  @Override
  public void onAudioItemClicked(Audio audio, View clickedView) {
    showAudioPopupMenu(audio, clickedView);
  }

  private void showAudioPopupMenu(final Audio audio, View anchor) {
    PopupMenu popupMenu = new PopupMenu(getContext(), anchor);
    popupMenu.getMenuInflater().inflate(R.menu.audio_popup_menu, popupMenu.getMenu());
    if (!audio.isAvailableForDownload()) {
      popupMenu.getMenu().findItem(R.id.audio_item_menu_download).setEnabled(false);
    }
    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
      @Override
      public boolean onMenuItemClick(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.audio_item_menu_download) {
          if (isWriteExternalStoragePermissionGranted()) {
            downloadAudio(audio);
          } else {
            pendingAudio = audio;
            requestWriteExternalStoragePermission();
          }
          return true;
        } else {
          return false;
        }
      }
    });
    popupMenu.show();
  }

  private boolean isWriteExternalStoragePermissionGranted() {
    return permissionManager.isPermissionGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE);
  }

  private void requestWriteExternalStoragePermission() {
    final View.OnClickListener requestAgain = new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        requestWriteExternalStoragePermission();
      }
    };
    PermissionRequest permissionRequest
            = PermissionRequest.builder()
            .addPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .onAllGranted(new Action0() {
              @Override
              public void call() {
                downloadPendingAudio();
              }
            })
            .onCancel(new Action0() {
              @Override
              public void call() {
                clearPendingAudio();
                showCannotDownloadWithoutPermissionSnackbar(R.string.action_grant_permission,
                        requestAgain);
              }
            })
            .onAnyDenied(new Action1<List<String>>() {
              @Override
              public void call(List<String> arg) {
                clearPendingAudio();
                showCannotDownloadWithoutPermissionSnackbar(R.string.action_grant_permission,
                        requestAgain);
              }
            })
            .onAnyNeverAskAgainDenied(new Action1<List<String>>() {
              @Override
              public void call(List<String> arg) {
                clearPendingAudio();
                showCannotDownloadWithoutPermissionSnackbar(R.string.settings, new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                    openAppSettings();
                  }
                });
              }
            }).build();
    permissionManager.newRequest(permissionRequest);
  }

  private void showCannotDownloadWithoutPermissionSnackbar(@StringRes int actionBtnText,
                                                           View.OnClickListener listener) {
    Snackbar.make(getView(), R.string.write_external_storage_rational, Snackbar.LENGTH_LONG)
            .setAction(actionBtnText, listener)
            .show();
  }

  private void downloadPendingAudio() {
    if (pendingAudio != null) {
      downloadAudio(pendingAudio);
    }
    clearPendingAudio();
  }

  private void clearPendingAudio() {
    pendingAudio = null;
  }

  private void openAppSettings() {
    Intents.openApplicationSettings(getContext());
  }

  private void downloadAudio(Audio audio) {
    presenter.onDownloadRequest(audio);
  }

  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    inflater.inflate(R.menu.my_audios_menu, menu);
    initSearchView(menu);
  }

  private void initSearchView(Menu menu) {
    MenuItem searchMenuItem = menu.findItem(R.id.my_audios_action_search);
    MenuItemCompat.setOnActionExpandListener(searchMenuItem, searchViewOnExpandListener);
    SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchMenuItem);
    searchView.setQueryHint(getString(R.string.search_audios_hint));
    searchView.setOnQueryTextListener(this);
    final String searchQuery = lastSearchQuery;
    if (isSearchViewExpanded) {
      searchMenuItem.expandActionView();
      searchView.setQuery(searchQuery, false);
    }
  }

  @Override
  public boolean onQueryTextSubmit(String query) {
    return false;
  }

  @Override
  public boolean onQueryTextChange(String newText) {
    lastSearchQuery = newText;
    if (isSearching()) {
      presenter.onSearch(newText);
    } else {
      presenter.onSearchCanceled();
    }
    recyclerView.scrollToPosition(0);
    return true;
  }

  private boolean isSearching() {
    return lastSearchQuery != null && !lastSearchQuery.isEmpty();
  }
}

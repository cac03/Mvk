package com.caco3.mvk.myaudios;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import com.caco3.mvk.vk.audio.Audio;

import java.util.Comparator;
import java.util.List;


import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.recyclerview.animators.OvershootInLeftAnimator;

public class MyAudiosFragment extends Fragment implements MyAudiosView,
        SwipeRefreshLayout.OnRefreshListener, MyAudiosAdapter.UiEventsListener,
        SearchView.OnQueryTextListener {
  private static final Comparator<Audio> audioByIdComparator = new Comparator<Audio>() {
    @Override
    public int compare(Audio o1, Audio o2) {
      return (int)(o1.getId() - o2.getId());
    }
  };

  @Inject
  MyAudiosPresenter presenter;
  @BindView(R.id.audios_frag_refresh_layout)
  SwipeRefreshLayout swipeRefreshLayout;
  @BindView(R.id.audios_frag_recycler_view)
  RecyclerView recyclerView;
  @BindView(R.id.audios_frag_progress_bar)
  ProgressBar progressBar;
  View audiosContentView;
  private MyAudiosAdapter audiosAdapter = new MyAudiosAdapter(this, audioByIdComparator);
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
    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
    recyclerView.setLayoutManager(linearLayoutManager);
    recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),
            linearLayoutManager.getOrientation()));
    recyclerView.setAdapter(audiosAdapter);
    recyclerView.setItemAnimator(new OvershootInLeftAnimator());
  }

  private void initSwipeRefreshLayout() {
    swipeRefreshLayout.setOnRefreshListener(this);
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
    if (!componentsHolder.hasMyAudiosComponent()) {
      componentsHolder.createMyAudiosComponent();
    }
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
    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
      @Override
      public boolean onMenuItemClick(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.audio_item_menu_download) {
          downloadAudio(audio);
          return true;
        } else {
          return false;
        }
      }
    });
    popupMenu.show();
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
    }
    if (isSearching()) {
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
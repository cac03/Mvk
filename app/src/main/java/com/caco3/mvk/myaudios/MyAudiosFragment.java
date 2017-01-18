package com.caco3.mvk.myaudios;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.caco3.mvk.R;
import com.caco3.mvk.dagger.DaggerComponentsHolder;
import com.caco3.mvk.vk.audio.Audio;

import java.util.List;


import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyAudiosFragment extends Fragment implements MyAudiosView,
        SwipeRefreshLayout.OnRefreshListener, MyAudiosAdapter.UiEventsListener {
  @Inject
  MyAudiosPresenter presenter;
  @BindView(R.id.audios_frag_refresh_layout)
  SwipeRefreshLayout swipeRefreshLayout;
  @BindView(R.id.audios_frag_recycler_view)
  RecyclerView recyclerView;
  @BindView(R.id.audios_frag_progress_bar)
  ProgressBar progressBar;
  View audiosContentView;
  private MyAudiosAdapter audiosAdapter = new MyAudiosAdapter(this);

  @Override
  public View onCreateView(LayoutInflater inflater,
                           @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState) {
    View root = inflater.inflate(R.layout.audios_fragment, container, false);
    ButterKnife.bind(this, root);
    initViews();

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
}

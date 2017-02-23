package com.caco3.mvk.ui;

import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.view.MenuItem;

import static com.caco3.mvk.util.Preconditions.checkNotNull;

public class SearchViewStateKeeper implements MenuItemCompat.OnActionExpandListener,
        SearchView.OnQueryTextListener {
  private MenuItem menuItem;
  private SearchView searchView;

  private MenuItemCompat.OnActionExpandListener realOnActionExpandListener;
  private SearchView.OnQueryTextListener realOnQueryTextListener;

  private boolean expanded;
  private String searchQuery = "";

  public void attach(MenuItem menuItem, SearchView searchView) {
    this.menuItem = checkNotNull(menuItem, "menuItem == null");
    this.searchView = checkNotNull(searchView, "searchView == null");
    if (expanded) {
      menuItem.expandActionView();
    }
    if (!searchQuery.isEmpty()) {
      searchView.setQuery(searchQuery, false);
    }
    MenuItemCompat.setOnActionExpandListener(menuItem, this);
    searchView.setOnQueryTextListener(this);
  }

  public void detach() {
    MenuItemCompat.setOnActionExpandListener(menuItem, null);
    searchView.setOnQueryTextListener(null);
    realOnActionExpandListener = null;
    realOnQueryTextListener = null;
  }

  public void setOnActionExpandListener(MenuItemCompat.OnActionExpandListener listener) {
    realOnActionExpandListener = checkNotNull(listener, "listener == null");
  }

  public void setOnQueryTextListener(SearchView.OnQueryTextListener onQueryTextListener) {
    realOnQueryTextListener = checkNotNull(onQueryTextListener, "onQueryTextListener == null");
  }

  @Override public boolean onMenuItemActionExpand(MenuItem item) {
    expanded = true;
    if (realOnActionExpandListener != null) {
      realOnActionExpandListener.onMenuItemActionExpand(item);
    }
    return true;
  }

  @Override public boolean onMenuItemActionCollapse(MenuItem item) {
    expanded = false;
    if (realOnActionExpandListener != null) {
      realOnActionExpandListener.onMenuItemActionCollapse(item);
    }
    return true;
  }

  @Override public boolean onQueryTextChange(String newText) {
    searchQuery = newText;
    return realOnQueryTextListener == null
            || realOnQueryTextListener.onQueryTextChange(newText);
  }

  @Override public boolean onQueryTextSubmit(String query) {
    searchQuery = query;
    return realOnQueryTextListener == null
            || realOnQueryTextListener.onQueryTextSubmit(query);
  }
}

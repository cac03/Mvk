package com.caco3.mvk.permission;

import android.support.v4.app.Fragment;

public class FragmentPermissionManager extends PermissionManager {
  private final Fragment fragment;

  public FragmentPermissionManager(Fragment fragment) {
    super(fragment.getContext());
    this.fragment = fragment;
  }

  @Override
  public boolean needToShowRationale(Permission permission) {
    return fragment.shouldShowRequestPermissionRationale(permission.permission);
  }

  @Override
  protected void requestPermissions(int requestId, String[] permissions) {
    fragment.requestPermissions(permissions, requestId);
  }
}

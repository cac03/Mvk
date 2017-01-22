package com.caco3.mvk.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.caco3.mvk.permission.FragmentPermissionManager;
import com.caco3.mvk.permission.PermissionManager;
import com.caco3.mvk.permission.PermissionManagerDelegate;


public abstract class BaseFragment extends Fragment {
  protected PermissionManager permissionManager;
  private PermissionManagerDelegate permissionManagerDelegate;

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    permissionManager = new FragmentPermissionManager(this);
    permissionManagerDelegate = permissionManager;
  }

  @Override
  public final void onRequestPermissionsResult(int requestCode,
                                         @NonNull String[] permissions,
                                         @NonNull int[] grantResults) {
    permissionManagerDelegate
            .onPermissionRequestResult(requestCode, permissions, grantResults);
  }
}

package com.caco3.mvk.permission;

import android.app.Activity;
import android.support.v4.app.ActivityCompat;

import java.util.List;


public class ActivityPermissionManager extends PermissionManager {
  private final Activity activity;

  public ActivityPermissionManager(Activity activity) {
    super(activity);
    this.activity = activity;
  }

  @Override
  public boolean needToShowRationale(Permission permission) {
    return ActivityCompat.shouldShowRequestPermissionRationale(activity, permission.permission);
  }

  @Override
  protected void requestPermissions(int requestId, String[] permissions) {
    ActivityCompat.requestPermissions(activity, permissions, requestId);
  }
}

package com.caco3.mvk.permission;


public interface PermissionManagerDelegate {
  void onPermissionRequestResult(int requestCode, String[] permissions, int[] grantResults);
}

package com.caco3.mvk.permission;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.caco3.mvk.util.Preconditions.checkState;

public abstract class PermissionManager implements PermissionManagerDelegate {
  private final Context context;
  private final PermissionSharedPreferences permissionSharedPreferences;
  @SuppressLint("UseSparseArrays")
  private final Map<Integer, PermissionRequest> ongoingRequests
          = new HashMap<>();
  private int requestCodeCounter = 0;

  /*package*/ PermissionManager(Context context) {
    this.context = context;
    permissionSharedPreferences = new PermissionSharedPreferences(context);
  }

  public boolean isPermissionGranted(Permission permission) {
    return ContextCompat.checkSelfPermission(context, permission.permission)
            == PackageManager.PERMISSION_GRANTED;
  }

  protected abstract boolean needToShowRationale(Permission permission);

  protected abstract void requestPermissions(int requestId, String[] permissions);

  @Override
  public void onPermissionRequestResult(int requestCode, String[] permissions, int[] grantResults) {
    checkState(ongoingRequests.containsKey(requestCode),
            "Unknown request code. Did you requested permissions using PermissionManager?");

    PermissionRequest request = ongoingRequests.get(requestCode);
    if (isRequestCanceled(permissions, grantResults)) {
      request.getOnRequestCanceledAction().call();
    } else {
      List<Permission> denied = extractDeniedPermissions(permissions, grantResults);
      List<Permission> granted = extractGrantedPermissions(permissions, grantResults);
      for(Permission permission : denied) {
        permissionSharedPreferences.setDenied(permission);
      }
      for(Permission permission : granted) {
        permissionSharedPreferences.setGranted(permission);
      }
      if (denied.isEmpty()) {
        request.getOnAllPermissionsGrantedAction().call();
      } else if (isAnyDeniedForever(denied)) {
        request.getOnAnyNeverAskAgainDeniedAction().call(denied);
      } else {
        request.getOnAnyPermissionDeniedAction().call(denied);
      }
    }
    ongoingRequests.remove(requestCode);
  }

  private boolean isRequestCanceled(String[] permissions, int[] grantResults) {
    return grantResults.length == 0;
  }

  private List<Permission> extractDeniedPermissions(String[] permissions, int[] grantResults) {
    List<Permission> res = new ArrayList<>(1);
    for(int i = 0; i < permissions.length; i++) {
      if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
        res.add(Permission.fromManifestString(permissions[i]));
      }
    }

    return res;
  }

  private List<Permission> extractGrantedPermissions(String[] permissions, int[] grantResults) {
    List<Permission> res = new ArrayList<>(1);
    for(int i = 0; i < permissions.length; i++) {
      if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
        res.add(Permission.fromManifestString(permissions[i]));
      }
    }

    return res;
  }

  private boolean isAnyDeniedForever(List<Permission> denied) {
    for(Permission permission : denied) {
      if (isDeniedForever(permission)) {
        return true;
      }
    }

    return false;
  }

  private boolean isDeniedForever(Permission permission) {
    return permissionSharedPreferences.isDenied(permission)
            && !needToShowRationale(permission);
  }

  public void newRequest(PermissionRequest request) {
    requestPermissions(++requestCodeCounter, permissionsToStringArray(request.getPermissions()));
    ongoingRequests.put(requestCodeCounter, request);
  }

  private String[] permissionsToStringArray(Collection<Permission> permissions) {
    final int length = permissions.size();
    String[] res = new String[length];
    int i = 0;
    for(Permission permission : permissions) {
      res[i++] = permission.permission;
    }

    return res;
  }
}

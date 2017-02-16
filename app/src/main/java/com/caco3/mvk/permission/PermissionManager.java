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

  public boolean isPermissionGranted(String permission) {
    return ContextCompat.checkSelfPermission(context, permission)
            == PackageManager.PERMISSION_GRANTED;
  }

  protected abstract boolean needToShowRationale(String permission);

  protected abstract void requestPermissions(int requestId, String[] permissions);

  @Override
  public void onPermissionRequestResult(int requestCode, String[] permissions, int[] grantResults) {
    checkState(ongoingRequests.containsKey(requestCode),
            "Unknown request code. Did you requested permissions using PermissionManager?");

    PermissionRequest request = ongoingRequests.get(requestCode);
    if (isRequestCanceled(permissions, grantResults)) {
      request.getOnRequestCanceledAction().call();
    } else {
      List<String> denied = extractDeniedPermissions(permissions, grantResults);
      List<String> granted = extractGrantedPermissions(permissions, grantResults);
      for(String permission : denied) {
        permissionSharedPreferences.setDenied(permission);
      }
      for(String permission : granted) {
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

  private List<String> extractDeniedPermissions(String[] permissions, int[] grantResults) {
    List<String> res = new ArrayList<>(1);
    for(int i = 0; i < permissions.length; i++) {
      if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
        res.add(permissions[i]);
      }
    }

    return res;
  }

  private List<String> extractGrantedPermissions(String[] permissions, int[] grantResults) {
    List<String> res = new ArrayList<>(1);
    for(int i = 0; i < permissions.length; i++) {
      if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
        res.add(permissions[i]);
      }
    }

    return res;
  }

  private boolean isAnyDeniedForever(List<String> denied) {
    for(String permission : denied) {
      if (isDeniedForever(permission)) {
        return true;
      }
    }

    return false;
  }

  private boolean isDeniedForever(String permission) {
    return permissionSharedPreferences.isDenied(permission)
            && !needToShowRationale(permission);
  }

  public void newRequest(PermissionRequest request) {
    requestPermissions(++requestCodeCounter, permissionsToStringArray(request.getPermissions()));
    ongoingRequests.put(requestCodeCounter, request);
  }

  private String[] permissionsToStringArray(Collection<String> permissions) {
    final int length = permissions.size();
    String[] res = new String[length];
    int i = 0;
    for(String permission : permissions) {
      res[i++] = permission;
    }

    return res;
  }
}

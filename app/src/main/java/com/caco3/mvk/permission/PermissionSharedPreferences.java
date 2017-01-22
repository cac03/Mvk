package com.caco3.mvk.permission;

import android.content.Context;
import android.content.SharedPreferences;

import static com.caco3.mvk.util.Preconditions.checkNotNull;

/*package*/ class PermissionSharedPreferences {
  private static final String PREFERENCES_FILENAME = "permissions";
  private static final String DENIED_KEY_PREFIX = "denied_";

  private SharedPreferences sharedPreferences;

  /*package*/ PermissionSharedPreferences(Context context) {
    this.sharedPreferences = checkNotNull(context, "context == null")
            .getSharedPreferences(PREFERENCES_FILENAME, Context.MODE_PRIVATE);
  }

  /*package*/ boolean isDenied(Permission permission) {
    return sharedPreferences.getBoolean(DENIED_KEY_PREFIX + permission.permission, false);
  }

  /*package*/ boolean isGranted(Permission permission) {
    return !isDenied(permission);
  }

  /*package*/ void setDenied(Permission permission) {
    sharedPreferences.edit().putBoolean(DENIED_KEY_PREFIX + permission.permission, true).apply();
  }

  /*package*/ void setGranted(Permission permission) {
    sharedPreferences.edit().remove(DENIED_KEY_PREFIX + permission.permission).apply();
  }
}

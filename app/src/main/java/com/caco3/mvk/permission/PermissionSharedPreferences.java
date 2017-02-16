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

  /*package*/ boolean isDenied(String permission) {
    return sharedPreferences.getBoolean(DENIED_KEY_PREFIX + permission, false);
  }

  /*package*/ boolean isGranted(String permission) {
    return !isDenied(permission);
  }

  /*package*/ void setDenied(String permission) {
    sharedPreferences.edit().putBoolean(DENIED_KEY_PREFIX + permission, true).apply();
  }

  /*package*/ void setGranted(String permission) {
    sharedPreferences.edit().remove(DENIED_KEY_PREFIX + permission).apply();
  }
}

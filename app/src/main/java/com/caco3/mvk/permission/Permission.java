package com.caco3.mvk.permission;


import android.Manifest;
import android.annotation.SuppressLint;
import android.support.annotation.StringRes;

import com.caco3.mvk.R;

public enum Permission {
  WRITE_EXTERNAL_STORAGE(Manifest.permission.WRITE_EXTERNAL_STORAGE,
          R.string.write_external_storage_rational);


  public final String permission;
  @StringRes
  public final int rationale;


  Permission(String permission, @StringRes int rationale) {
    this.permission = permission;
    this.rationale = rationale;
  }

  public static Permission fromManifestString(String manifestString) {
    for (Permission permission : Permission.values()) {
      if (permission.permission.equals(manifestString)) {
        return permission;
      }
    }

    throw new IllegalArgumentException(
            String.format("Unknown manifest permission string (%s)", manifestString));
  }
}

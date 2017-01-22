package com.caco3.mvk.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import static com.caco3.mvk.util.Preconditions.checkNotNull;

public class Intents {
  private Intents(){
  }

  public static void openApplicationSettings(Context context) {
    checkNotNull(context, "context == null");
    Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
    intent.setData(Uri.parse("package:" + context.getPackageName()));
    context.startActivity(intent);
  }
}

package com.caco3.mvk.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/*package*/ class NetworkManagerImpl implements NetworkManager {
  private final Context context;

  /*package*/ NetworkManagerImpl(Context context) {
    this.context = context;
  }

  @Override
  public boolean isNetworkAvailable() {
    return getActiveNetworkInfo().isConnectedOrConnecting();
  }

  @Override
  public boolean isConnectedWithWiFi() {
    return getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_WIFI;
  }

  private NetworkInfo getActiveNetworkInfo() {
    return getConnectivityManager().getActiveNetworkInfo();
  }

  private ConnectivityManager getConnectivityManager(){
    return (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
  }
}

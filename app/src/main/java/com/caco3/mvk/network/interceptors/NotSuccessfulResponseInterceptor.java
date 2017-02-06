package com.caco3.mvk.network.interceptors;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

public class NotSuccessfulResponseInterceptor implements Interceptor {

  @Override
  public Response intercept(Chain chain) throws IOException {
    Response response = chain.proceed(chain.request());
    if (!response.isSuccessful()) {
      throw new IOException(String.format("Unexpected code: %s", response));
    } else {
      return response;
    }
  }
}

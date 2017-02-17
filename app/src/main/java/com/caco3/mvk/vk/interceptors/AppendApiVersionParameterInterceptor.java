package com.caco3.mvk.vk.interceptors;


import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import static com.caco3.mvk.util.Preconditions.checkNotNull;

public class AppendApiVersionParameterInterceptor implements Interceptor {
  static final String API_VERSION_PARAM_KEY = "v";

  private final String apiVersion;

  public AppendApiVersionParameterInterceptor(String apiVersion) {
    this.apiVersion = checkNotNull(apiVersion, "apiVersion == null");
  }

  @Override
  public Response intercept(Chain chain) throws IOException {
    Request original = chain.request();
    HttpUrl url = original.url().newBuilder().addQueryParameter(API_VERSION_PARAM_KEY,
            apiVersion).build();
    Request newRequest = original.newBuilder().url(url).build();

    return chain.proceed(newRequest);
  }
}

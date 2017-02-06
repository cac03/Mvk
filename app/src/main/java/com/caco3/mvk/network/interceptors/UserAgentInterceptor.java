package com.caco3.mvk.network.interceptors;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import static com.caco3.mvk.util.Preconditions.checkNotNull;


public class UserAgentInterceptor implements Interceptor {
  /*package*/ static final String USER_AGENT_KEY = "User-Agent";
  private final String userAgentValue;

  public UserAgentInterceptor(String userAgentValue) {
    this.userAgentValue = checkNotNull(userAgentValue, "userAgentValue == null");
  }

  @Override
  public Response intercept(Chain chain) throws IOException {
    Request original = chain.request();
    Request newRequest = original.newBuilder()
            .addHeader(USER_AGENT_KEY, userAgentValue).build();
    return chain.proceed(newRequest);
  }
}

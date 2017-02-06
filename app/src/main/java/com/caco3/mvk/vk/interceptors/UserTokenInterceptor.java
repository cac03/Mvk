package com.caco3.mvk.vk.interceptors;

import com.caco3.mvk.vk.auth.UserToken;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import static com.caco3.mvk.util.Preconditions.checkNotNull;


public class UserTokenInterceptor implements Interceptor {
  /*package*/ static final String ACCESS_TOKEN_PARAM_KEY = "access_token";
  private final UserToken userToken;

  public UserTokenInterceptor(UserToken userToken) {
    this.userToken = checkNotNull(userToken, "userToken == null");
  }

  @Override
  public Response intercept(Chain chain) throws IOException {
    Request original = chain.request();
    HttpUrl url = original.url().newBuilder().addQueryParameter(ACCESS_TOKEN_PARAM_KEY,
            userToken.getAccessToken()).build();
    Request newRequest = original.newBuilder().url(url).build();
    return chain.proceed(newRequest);
  }
}

package com.caco3.mvk.vk.interceptors;


import com.caco3.mvk.vk.auth.UserToken;

import org.junit.Rule;
import org.junit.Test;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class UserTokenInterceptorTest {
  @Rule
  public MockWebServer mockWebServer = new MockWebServer();
  private HttpUrl url = mockWebServer.url("dummy");
  private UserToken userToken = new UserToken("dummyUserToken");
  private OkHttpClient okHttpClient = new OkHttpClient.Builder()
          .addInterceptor(new UserTokenInterceptor(userToken)).build();

  @Test
  public void requestSent_urlHasAccessTokenParameter() throws Exception {
    mockWebServer.enqueue(new MockResponse());
    Response response = okHttpClient.newCall(new Request.Builder().url(url).build()).execute();
    HttpUrl url = response.request().url();
    assertThat(url.queryParameterNames())
            .contains(UserTokenInterceptor.ACCESS_TOKEN_PARAM_KEY);
    assertThat(url.queryParameter(UserTokenInterceptor.ACCESS_TOKEN_PARAM_KEY))
            .isEqualTo(userToken.getAccessToken());
  }
}

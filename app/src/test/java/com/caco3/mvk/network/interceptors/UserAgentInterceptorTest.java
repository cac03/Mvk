package com.caco3.mvk.network.interceptors;


import org.junit.Rule;
import org.junit.Test;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class UserAgentInterceptorTest {
  private static final String DUMMY_USER_AGENT_VALUE = "Mz";
  @Rule
  public MockWebServer mockWebServer = new MockWebServer();
  private HttpUrl url = mockWebServer.url("dummyUrl");
  private final OkHttpClient okHttpClient = new OkHttpClient.Builder()
          .addInterceptor(new UserAgentInterceptor(DUMMY_USER_AGENT_VALUE))
          .build();

  @Test
  public void requestSent_userAgentChanged() throws Exception {
    mockWebServer.enqueue(new MockResponse());
    Response response = okHttpClient.newCall(new Request.Builder().url(url).build())
            .execute();
    Request request = response.request();
    assertThat(request.header(UserAgentInterceptor.USER_AGENT_KEY))
            .isEqualTo(DUMMY_USER_AGENT_VALUE);
  }
}

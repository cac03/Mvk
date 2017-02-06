package com.caco3.mvk.network.interceptors;


import com.caco3.mvk.network.interceptors.NotSuccessfulResponseInterceptor;

import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

import static junit.framework.Assert.fail;

public class NotSuccessfulResponseInterceptorTest {
  @Rule
  public MockWebServer mockWebServer = new MockWebServer();
  private final OkHttpClient okHttpClient = new OkHttpClient.Builder()
          .addInterceptor(new NotSuccessfulResponseInterceptor()).build();
  private final HttpUrl url = mockWebServer.url("dummyUrl");

  @Test(expected = IOException.class)
  public void responseNotSuccessful_ioExceptionThrown() throws Exception {
    mockWebServer.enqueue(new MockResponse().setResponseCode(404));
    okHttpClient.newCall(new Request.Builder().url(url).build()).execute();
    fail();
  }

  @Test
  public void responseSuccessful_noExceptionThrown() throws Exception {
    mockWebServer.enqueue(new MockResponse());
    okHttpClient.newCall(new Request.Builder().url(url).build()).execute();
  }
}

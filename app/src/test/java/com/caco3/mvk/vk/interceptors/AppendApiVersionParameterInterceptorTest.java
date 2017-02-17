package com.caco3.mvk.vk.interceptors;

import org.junit.Test;

import java.io.IOException;

import okhttp3.Connection;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;

import static junit.framework.Assert.fail;
import static org.assertj.core.api.Java6Assertions.assertThat;

public class AppendApiVersionParameterInterceptorTest {

  @Test
  public void nullPassedAsApiVersionString_npeThrown() {
    try {
      new AppendApiVersionParameterInterceptor(null);
      fail("Npe was not thrown");
    } catch (NullPointerException expected) {
      assertThat(expected.getMessage())
              .isEqualTo("apiVersion == null");
    }
  }

  @Test
  public void requestIntercepted_versionParameterAppended() throws Exception {
    Interceptor interceptor = new AppendApiVersionParameterInterceptor("6");
    Response response = interceptor.intercept(new Interceptor.Chain() {
      @Override
      public Request request() {
        return new Request.Builder().url("https://google.com").build();
      }

      @Override
      public Response proceed(Request request) throws IOException {
        return new Response.Builder().protocol(Protocol.HTTP_1_1)
                .code(200).request(request).build();
      }

      @Override
      public Connection connection() {
        return null;
      }
    });

    Request request = response.request();
    HttpUrl url = request.url();
    assertThat(url.queryParameterNames())
            .contains(AppendApiVersionParameterInterceptor.API_VERSION_PARAM_KEY);
    assertThat(url.queryParameter(AppendApiVersionParameterInterceptor.API_VERSION_PARAM_KEY))
            .isEqualTo("6");
  }
}

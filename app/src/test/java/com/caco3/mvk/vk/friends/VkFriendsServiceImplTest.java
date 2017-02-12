package com.caco3.mvk.vk.friends;

import com.caco3.mvk.vk.Vk;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.junit.Rule;
import org.junit.Test;

import java.util.List;

import edu.emory.mathcs.backport.java.util.Arrays;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class VkFriendsServiceImplTest {
  @Rule
  public MockWebServer mockWebServer = new MockWebServer();
  private HttpUrl url = mockWebServer.url("dummyFriends/");
  private final Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation()
          .create();
  private final VkFriendsService vkFriendsService
          = new VkFriendsServiceImpl(new Retrofit.Builder()
          .baseUrl(url)
          .client(new OkHttpClient())
          .addConverterFactory(GsonConverterFactory.create(gson))
          .build());

  @Test
  public void getCalled_correctListOfVkUserIdsReturned() throws Exception {
    mockWebServer.enqueue(new MockResponse().setBody("{\n" +
            "\"response\": [9101748, 15439101, 2453384, 1576215, 8506688]\n" +
            "}"));
    Long[] expected = new Long[]{Long.valueOf(9101748L),
            Long.valueOf(15439101L), Long.valueOf(2453384L), Long.valueOf(1576215L), Long.valueOf(8506688L)};
    List<Long> ids = vkFriendsService.get();
    assertThat(ids)
            .hasSize(5)
            .contains(expected);
  }
}

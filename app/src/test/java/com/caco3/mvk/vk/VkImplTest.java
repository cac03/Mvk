package com.caco3.mvk.vk;

import com.caco3.mvk.vk.auth.UserToken;
import com.google.gson.Gson;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import okhttp3.OkHttpClient;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class VkImplTest {
  private OkHttpClient okHttpClient = new OkHttpClient();
  @Mock
  private UserToken userToken;
  private Gson gson = new Gson();

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
  }

  @Test(expected = NullPointerException.class)
  public void userTokenIsNull_npeThrown() {
    new VkImpl(null, okHttpClient, gson);
  }

  @Test(expected = NullPointerException.class)
  public void okHttpClientIsNull_npeThrown() {
    new VkImpl(userToken, null, gson);
  }

  @Test(expected = NullPointerException.class)
  public void gsonIsNull_npeThrown() {
    new VkImpl(userToken, okHttpClient, null);
  }

  @Test
  public void argsOk_audiosServiceIsNotNull() {
    assertThat(new VkImpl(userToken, okHttpClient, gson).audios())
            .isNotNull();
  }

  @Test
  public void argsOk_usersServiceIsNotNull() {
    assertThat(new VkImpl(userToken, okHttpClient, gson).users())
            .isNotNull();
  }
}

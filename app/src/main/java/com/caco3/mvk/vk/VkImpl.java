package com.caco3.mvk.vk;

import com.caco3.mvk.vk.audio.VkAudioServiceImpl;
import com.caco3.mvk.vk.audio.VkAudiosService;
import com.caco3.mvk.vk.auth.UserToken;
import com.caco3.mvk.vk.interceptors.UserTokenInterceptor;
import com.caco3.mvk.vk.users.VkUsersService;
import com.caco3.mvk.vk.users.VkUsersServiceImpl;
import com.google.gson.Gson;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.caco3.mvk.util.Preconditions.checkNotNull;


/*package*/ class VkImpl implements Vk {
  /*package*/ static final String VK_API_BASE_URL = "https://api.vk.com/";
  private final VkAudiosService vkAudiosService;
  private final VkUsersService vkUsersService;

  public VkImpl(UserToken userToken, OkHttpClient baseOkHttpClient, Gson gson) {
    checkNotNull(userToken, "userToken == null");
    checkNotNull(baseOkHttpClient, "baseOkHttpClient == null");
    checkNotNull(gson, "gson == null");
    OkHttpClient okHttpClient = createOkHttpClient(baseOkHttpClient, userToken);
    Retrofit retrofit = createRetrofit(okHttpClient, gson);
    this.vkUsersService = new VkUsersServiceImpl(retrofit);
    this.vkAudiosService = new VkAudioServiceImpl(retrofit);
  }

  private OkHttpClient createOkHttpClient(OkHttpClient base, UserToken userToken) {
    return base.newBuilder()
            .addInterceptor(new UserTokenInterceptor(userToken))
            .build();
  }

  private Retrofit createRetrofit(OkHttpClient okHttpClient, Gson gson) {
    return new Retrofit.Builder()
            .baseUrl(VK_API_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();
  }

  @Override
  public VkAudiosService audios() {
    return vkAudiosService;
  }

  @Override
  public VkUsersService users() {
    return vkUsersService;
  }
}

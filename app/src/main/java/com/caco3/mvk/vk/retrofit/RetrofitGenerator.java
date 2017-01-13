package com.caco3.mvk.vk.retrofit;


import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitGenerator {
  private static final String VK_API_BASE_URL = "https://api.vk.com/";
  private static final Retrofit retrofit
          = new Retrofit.Builder()
          .client(new OkHttpClient())
          .baseUrl(VK_API_BASE_URL)
          .addConverterFactory(GsonConverterFactory.create())
          .build();

  public static <S> S generateService(Class<S> klass) {
    return retrofit.create(klass);
  }

}

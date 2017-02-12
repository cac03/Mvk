package com.caco3.mvk.vk.friends;


import com.caco3.mvk.vk.VkResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.http.GET;

import static com.caco3.mvk.util.Preconditions.checkNotNull;

public class VkFriendsServiceImpl implements VkFriendsService {
  private final FriendsRetrofitService retrofitService;

  public VkFriendsServiceImpl(Retrofit retrofit) {
    checkNotNull(retrofit, "retrofit == null");
    this.retrofitService = retrofit.create(FriendsRetrofitService.class);
  }

  @Override
  public List<Long> get() throws IOException {
    return retrofitService.get().execute().body().getResponseOrThrowIfNotSuccessful();
  }

  /*package*/ interface FriendsRetrofitService {
    @GET("/method/friends.get")
    Call<VkResponse<List<Long>>> get();
  }
}

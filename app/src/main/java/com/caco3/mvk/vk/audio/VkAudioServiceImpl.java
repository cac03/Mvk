package com.caco3.mvk.vk.audio;

import com.caco3.mvk.vk.VkResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.http.GET;

import static com.caco3.mvk.util.Preconditions.checkNotNull;

public class VkAudioServiceImpl implements VkAudiosService {
  private final RetrofitAudiosService retrofitAudiosService;

  public VkAudioServiceImpl(Retrofit retrofit) {
    this.retrofitAudiosService = checkNotNull(retrofit, "retrofit == null")
            .create(RetrofitAudiosService.class);
  }

  @Override
  public List<Audio> get() throws IOException {
    return retrofitAudiosService.getAll().execute()
            .body().getResponseOrThrowIfNotSuccessful().audios;
  }

  /*package*/ static class AudiosResponse {
    @SerializedName("items")
    @Expose
    List<Audio> audios;
  }

  /*package*/ interface RetrofitAudiosService {
    @GET("/method/audio.get")
    Call<VkResponse<AudiosResponse>> getAll();
  }
}

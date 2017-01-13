package com.caco3.mvk.vk.audio;

import com.caco3.mvk.vk.UnknownVkErrorException;
import com.caco3.mvk.vk.VkMethodResponse;
import com.caco3.mvk.vk.auth.UserToken;
import com.caco3.mvk.vk.method.PrivateVkMethod;
import com.caco3.mvk.vk.retrofit.RetrofitGenerator;
import com.google.gson.annotations.SerializedName;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;

/*package*/ class AudioGetMethod extends PrivateVkMethod<List<Audio>> {
  private final RetrofitService retrofitService;

  /*package*/ AudioGetMethod(UserToken userToken) {
    super(userToken);
    retrofitService = RetrofitGenerator.generateService(RetrofitService.class);
  }

  @Override
  public List<Audio> call() throws IOException {
    Response<AudioGetResponse> response = retrofitService.getAll(userToken.getAccessToken()).execute();
    if (response.isSuccessful()) {
      AudioGetResponse audioGetResponse = response.body();
      if (audioGetResponse.isSuccessful()) {
        return audioGetResponse.audios;
      } else {
        throw audioGetResponse.getException();
      }
    } else {
      throw new UnknownVkErrorException("Unable to get audios. response: " + response.toString());
    }
  }

  private static class AudioGetResponse extends VkMethodResponse {
    @SerializedName("response") List<Audio> audios;

    @Override
    public String toString() {
      return "AudioResponse{" +
              "audios=" + audios +
              "} " + super.toString();
    }
  }

  private interface RetrofitService {
    @GET("/method/audio.get")
    Call<AudioGetResponse> getAll(@Query("access_token") String accessToken);
  }
}

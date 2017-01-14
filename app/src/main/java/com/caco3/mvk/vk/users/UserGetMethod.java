package com.caco3.mvk.vk.users;

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


/*package*/ class UserGetMethod extends PrivateVkMethod<VkUser> {
  private static final String FIELDS = "photo_200_orig";
  private final RetrofitService retrofitService;

  /*package*/ UserGetMethod(UserToken userToken) {
    super(userToken);
    retrofitService = RetrofitGenerator.generateService(RetrofitService.class);
  }

  @Override
  public VkUser call() throws IOException {
    Call<UsersGetResponse> call = retrofitService.get(FIELDS, userToken.getAccessToken());
    Response<UsersGetResponse> response = call.execute();
    if (response.isSuccessful()) {
      UsersGetResponse usersGetResponse = response.body();
      if (usersGetResponse.isSuccessful()) {
        return usersGetResponse.users.get(0);
      } else {
        throw usersGetResponse.getException();
      }
    } else {
      throw new UnknownVkErrorException("Unable to get user. response: " + response.toString());
    }
  }

  private static class UsersGetResponse extends VkMethodResponse {
    @SerializedName("response")
    List<VkUser> users;

    @Override
    public String toString() {
      return "UsersGetResponse{" +
              "users=" + users +
              "} " + super.toString();
    }
  }

  private interface RetrofitService {
    @GET("/method/users.get")
    Call<UsersGetResponse> get(@Query("fields") String fields,
                               @Query("access_token") String accessToken);
  }
}

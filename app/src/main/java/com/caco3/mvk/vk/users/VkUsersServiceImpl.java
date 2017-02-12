package com.caco3.mvk.vk.users;

import com.caco3.mvk.util.Strings;
import com.caco3.mvk.vk.VkResponse;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Query;

import static com.caco3.mvk.util.Preconditions.checkNotNull;


public class VkUsersServiceImpl implements VkUsersService {
  private static final String FIELDS = "photo_200_orig";

  private final RetrofitUsersService retrofitUsersService;

  public VkUsersServiceImpl(Retrofit retrofit) {
    this.retrofitUsersService = checkNotNull(retrofit, "retrofit == null")
            .create(RetrofitUsersService.class);
  }

  @Override
  public VkUser get() throws IOException {
    return retrofitUsersService.get(FIELDS).execute().body()
            .getResponseOrThrowIfNotSuccessful().get(0);
  }

  @Override
  public List<VkUser> get(long... ids) throws IOException {
    return retrofitUsersService.get(FIELDS, Strings.join(",", ids))
            .execute().body().getResponseOrThrowIfNotSuccessful();
  }

  /*package*/ interface RetrofitUsersService {
    @GET("/method/users.get")
    Call<VkResponse<List<VkUser>>> get(@Query("fields") String fields);
    @GET("/method/users.get")
    Call<VkResponse<List<VkUser>>> get(@Query("fields") String fields,
                                       @Query("user_ids") String userIds);
  }
}

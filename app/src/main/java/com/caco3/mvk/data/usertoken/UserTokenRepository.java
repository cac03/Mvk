package com.caco3.mvk.data.usertoken;


import com.caco3.mvk.vk.auth.UserToken;

import java.util.List;

public interface UserTokenRepository {
  void save(UserToken userToken);
  List<UserToken> getAll();
  void update(UserToken userToken);
  void remove(UserToken userToken);
  void removeAll();
}

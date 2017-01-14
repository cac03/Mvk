package com.caco3.mvk.data.vkuser;

import com.caco3.mvk.vk.users.VkUser;

import java.util.List;

public interface VkUsersRepository {
  void save(VkUser vkUser);
  void saveAll(Iterable<VkUser> vkUsers);
  List<VkUser> getAll();
  void update(VkUser vkUser);
  void updateAll(Iterable<VkUser> vkUsers);
  void delete(VkUser vkUser);
  void deleteAll();
}

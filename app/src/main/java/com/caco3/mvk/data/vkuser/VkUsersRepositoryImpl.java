package com.caco3.mvk.data.vkuser;


import com.caco3.mvk.vk.users.VkUser;
import com.caco3.mvk.vk.users.VkUserDao;

import java.util.List;

public class VkUsersRepositoryImpl implements VkUsersRepository {
  private final VkUserDao dao;

  public VkUsersRepositoryImpl(VkUserDao dao) {
    this.dao = dao;
  }

  @Override
  public void save(VkUser vkUser) {
    dao.save(vkUser);
  }

  @Override
  public void saveAll(Iterable<VkUser> vkUsers) {
    dao.saveInTx(vkUsers);
  }

  @Override
  public List<VkUser> getAll() {
    return dao.loadAll();
  }

  @Override
  public void update(VkUser vkUser) {
    dao.update(vkUser);
  }

  @Override
  public void updateAll(Iterable<VkUser> vkUsers) {
    dao.updateInTx(vkUsers);
  }

  @Override
  public void delete(VkUser vkUser) {
    dao.delete(vkUser);
  }

  @Override
  public void deleteAll() {
    dao.deleteAll();
  }
}

package com.caco3.mvk.data.vkuser;

import com.caco3.mvk.vk.users.VkUser;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import static com.caco3.mvk.util.Preconditions.checkState;

public class InMemoryVkUsersRepository implements VkUsersRepository {
  private final List<VkUser> users = new ArrayList<>();
  private final AtomicLong idsCounter = new AtomicLong();

  @Override
  public void save(VkUser vkUser) {
    vkUser.setId(idsCounter.incrementAndGet());
    users.add(vkUser.clone());
  }

  @Override
  public void saveAll(Iterable<VkUser> vkUsers) {
    for(VkUser vkUser : vkUsers) {
      save(vkUser);
    }
  }

  @Override
  public List<VkUser> getAll() {
    return new ArrayList<>(users);
  }

  @Override
  public void update(VkUser vkUser) {
    int index = indexOfById(vkUser);
    checkState(index != -1, "vkUser(" + vkUser + ") is not saved.");
    users.set(index, vkUser);
  }

  private int indexOfById(VkUser vkUser) {
    long searchableId = vkUser.getId();
    for(int i = 0, length = users.size(); i < length; i++) {
      if (users.get(i).getId().equals(searchableId)) {
        return i;
      }
    }
    // not  found
    return -1;
  }

  @Override
  public void updateAll(Iterable<VkUser> vkUsers) {
    for(VkUser vkUser : vkUsers) {
      update(vkUser);
    }
  }

  @Override
  public void delete(VkUser vkUser) {
    if (!users.remove(vkUser)) {
      throw new IllegalStateException("vkUser( " + vkUser + " ) was not saved");
    }
  }

  @Override
  public void deleteAll() {
    users.clear();
  }
}

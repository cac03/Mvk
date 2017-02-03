package com.caco3.mvk.vk.users;


import com.caco3.mvk.AbstractPojoGenerator;
import com.caco3.mvk.vk.users.VkUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class VkUserGenerator extends AbstractPojoGenerator<VkUser> {
  private static final String[] dummyFirstNames = {
          "dummyName1", "dummyName2", "dummyName3"
  };
  private static final String[] dummyLastNames = {
          "dummyLastName1", "dummyLastName2"
  };
  private static final String[] dummyPhotoUrls = {
          "https://cdn.pixabay.com/photo/2012/04/11/11/32/letter-a-27580_960_720.png"
  };


  private final Random random = new Random();

  @Override
  public VkUser generateOne() {
    VkUser vkUser = new VkUser();
    vkUser.setFirstName(dummyFirstNames[random.nextInt(dummyFirstNames.length)]);
    vkUser.setLastName(dummyLastNames[random.nextInt(dummyLastNames.length)]);
    vkUser.setPhotoUrl(dummyPhotoUrls[random.nextInt(dummyPhotoUrls.length)]);

    return vkUser;
  }
}

package com.caco3.mvk.navdrawer;

import com.caco3.mvk.mvp.BaseView;
import com.caco3.mvk.vk.users.VkUser;


public interface NavDrawerView extends BaseView<NavDrawerPresenter> {
  void showVkUser(VkUser vkUser);
}

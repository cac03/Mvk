package com.caco3.mvk.vk.friends;

import java.io.IOException;
import java.util.List;

public interface VkFriendsService {
  List<Long> get() throws IOException;
}

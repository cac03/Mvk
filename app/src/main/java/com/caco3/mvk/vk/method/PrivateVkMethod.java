package com.caco3.mvk.vk.method;

import com.caco3.mvk.vk.auth.UserToken;

import static com.caco3.mvk.util.Preconditions.checkNotNull;

/**
 * A vk method that requires {@link com.caco3.mvk.vk.auth.UserToken} to be called
 */
public abstract class PrivateVkMethod<R> extends VkMethod<R> {
  protected final UserToken userToken;

  protected PrivateVkMethod(UserToken token) {
    this.userToken = checkNotNull(token);
  }
}

package com.caco3.mvk.vk.method;

import java.io.IOException;

/**
 * An abstraction over vk methods
 * @see <a href="https://vk.com/dev/methods">Vk methods</a>
 * @param <R> result that this method returns
 */
public abstract class VkMethod<R> {
  public abstract R call() throws IOException;
}

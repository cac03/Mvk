package com.caco3.mvk.storage;

import com.caco3.mvk.storage.dir.DirectoryProvider;
import com.caco3.mvk.storage.dir.ExternalStorageDirectoryProvider;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class StorageModule {
  @Provides
  @Singleton
  public DirectoryProvider provideExternalStorageDirectoryProvider() {
    return new ExternalStorageDirectoryProvider();
  }
}

package com.caco3.mvk;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

import static com.caco3.mvk.util.Preconditions.checkNotNull;


@Module
public class ApplicationModule {
  private final Context context;

  public ApplicationModule(Context context) {
    this.context = checkNotNull(context);
  }

  @Provides
  @Singleton
  public Context provideContext() {
    return context;
  }
}

package com.caco3.mvk.splash;

import dagger.Subcomponent;

@SplashScope
@Subcomponent(
        modules = {
                SplashModule.class
        }
)
public interface SplashComponent {
  void inject(SplashActivity splashActivity);
}

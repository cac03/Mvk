package com.caco3.mvk.navdrawer;

import com.caco3.mvk.ui.UserLoggedInBaseActivity;

import dagger.Subcomponent;

@NavDrawerScope
@Subcomponent(
        modules = {
                NavDrawerModule.class
        }
)
public interface NavDrawerComponent {
  void inject(UserLoggedInBaseActivity activity);
}

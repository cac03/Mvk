package com.caco3.mvk.login;

import dagger.Subcomponent;

@LogInScope
@Subcomponent(
        modules = {
                LogInModule.class
        }
)
public interface LogInComponent {
  void inject(LogInFragment fragment);
}

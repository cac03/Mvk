package com.caco3.mvk.myaudios;

import dagger.Subcomponent;

@MyAudiosScope
@Subcomponent(
        modules = {
                MyAudiosModule.class
        }
)
public interface MyAudiosComponent {
  void inject(MyAudiosFragment audiosFragment);
}

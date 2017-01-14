package com.caco3.mvk.audios;

import dagger.Subcomponent;

@AudiosScope
@Subcomponent(
        modules = {
                AudiosModule.class
        }
)
public interface AudiosComponent {
  void inject(AudiosFragment audiosFragment);
}

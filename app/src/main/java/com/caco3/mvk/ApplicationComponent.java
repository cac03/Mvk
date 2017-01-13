package com.caco3.mvk;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(
        modules = {
                ApplicationModule.class
        }
)
public interface ApplicationComponent {
}

package com.caco3.mvk;

import com.caco3.mvk.data.DataModule;
import com.caco3.mvk.login.LogInComponent;
import com.caco3.mvk.login.LogInModule;
import com.caco3.mvk.vk.VkModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(
        modules = {
                ApplicationModule.class,
                DataModule.class,
                VkModule.class
        }
)
public interface ApplicationComponent {
  LogInComponent plus(LogInModule logInModule);
}

package com.caco3.mvk.loggedin;

import com.caco3.mvk.audiodownload.AudioDownloadService;
import com.caco3.mvk.audios.AudiosModule;
import com.caco3.mvk.myaudios.MyAudiosComponent;
import com.caco3.mvk.myaudios.MyAudiosModule;
import com.caco3.mvk.navdrawer.NavDrawerComponent;
import com.caco3.mvk.navdrawer.NavDrawerModule;
import com.caco3.mvk.vk.VkModule;

import dagger.Subcomponent;

/**
 * Has an active {@link com.caco3.mvk.data.appuser.AppUser}
 */
@LoggedInScope
@Subcomponent(
        modules = {
                AudiosModule.class,
                LoggedInModule.class,
                VkModule.class
        }
)
public interface LoggedInComponent {
  MyAudiosComponent plus(MyAudiosModule myAudiosModule);
  NavDrawerComponent plus(NavDrawerModule navDrawerModule);
  void inject(AudioDownloadService service);
}

package com.caco3.mvk.loggedin;

import com.caco3.mvk.audios.MyAudiosComponent;
import com.caco3.mvk.audios.MyAudiosModule;
import com.caco3.mvk.navdrawer.NavDrawerComponent;
import com.caco3.mvk.navdrawer.NavDrawerModule;

import dagger.Subcomponent;

/**
 * Has an active {@link com.caco3.mvk.data.appuser.AppUser}
 */
@LoggedInScope
@Subcomponent(
        modules = {
                LoggedInModule.class
        }
)
public interface LoggedInComponent {
  MyAudiosComponent plus(MyAudiosModule myAudiosModule);
  NavDrawerComponent plus(NavDrawerModule navDrawerModule);
}

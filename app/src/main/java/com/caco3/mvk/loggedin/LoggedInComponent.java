package com.caco3.mvk.loggedin;

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
}

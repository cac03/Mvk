package com.caco3.mvk.ui;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.caco3.mvk.R;
import com.caco3.mvk.dagger.DaggerComponentsHolder;
import com.caco3.mvk.navdrawer.NavDrawerPresenter;
import com.caco3.mvk.navdrawer.NavDrawerView;
import com.caco3.mvk.vk.users.VkUser;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Base activity for all activities in {@link com.caco3.mvk.loggedin.LoggedInScope}.
 * It will automatically set navigation drawer header with {@link VkUser} associated
 * with current {@link com.caco3.mvk.data.appuser.AppUser}
 */
public abstract class UserLoggedInBaseActivity extends BaseActivity implements NavDrawerView {
  @Inject
  NavDrawerPresenter navDrawerPresenter;

  private boolean hasNavDrawer() {
    return navigationView != null;
  }

  private void injectNavDrawerPresenter() {
    DaggerComponentsHolder componentsHolder = DaggerComponentsHolder.getInstance();
    componentsHolder.getNavDrawerComponent().inject(this);
  }

  @Override
  public void onResume() {
    super.onResume();
    if (hasNavDrawer()) {
      if (navDrawerPresenter == null) {
        injectNavDrawerPresenter();
      }
      navDrawerPresenter.onViewAttached(this);
    }
  }

  @Override
  public void onPause() {
    if (navDrawerPresenter != null) {
      navDrawerPresenter.onViewDetached(this);
    }
    super.onPause();
  }

  @Override
  public void showVkUser(VkUser vkUser) {
    if (navigationView != null) {
      View view = navigationView.getHeaderView(0);
      new NavDrawerHeaderViewHolder(view).bind(vkUser);
    }
  }

  /*package*/ class NavDrawerHeaderViewHolder {
    @BindView(R.id.nav_drawer_header_user_name)
    TextView userName;
    @BindView(R.id.nav_drawer_header_image)
    ImageView userImage;

    /*package*/ NavDrawerHeaderViewHolder(View header) {
      ButterKnife.bind(this, header);
    }

    /*package*/ void bind(VkUser vkUser) {
      userName.setText(vkUser.getFirstName() + " " + vkUser.getLastName());
      Picasso.with(UserLoggedInBaseActivity.this)
              .load(vkUser.getPhotoUrl())
              .into(userImage);
    }
  }
}

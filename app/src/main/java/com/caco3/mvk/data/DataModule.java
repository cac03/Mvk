package com.caco3.mvk.data;

import android.content.Context;

import com.caco3.mvk.data.appuser.AppUsersRepository;
import com.caco3.mvk.data.appuser.AppUsersRepositoryImpl;
import com.caco3.mvk.data.audio.AudiosRepository;
import com.caco3.mvk.data.audio.AudiosRepositoryImpl;
import com.caco3.mvk.data.usertoken.UserTokenRepository;
import com.caco3.mvk.data.usertoken.UserTokenRepositoryImpl;
import com.caco3.mvk.vk.auth.DaoMaster;
import com.caco3.mvk.vk.auth.DaoSession;

import org.greenrobot.greendao.database.Database;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class DataModule {
  private static final String DATABASE_NAME = "mvk.db";
  private final AudiosRepository audiosRepository;
  private final AppUsersRepository appUsersRepository;
  private final UserTokenRepository userTokenRepository;

  public DataModule(Context context) {
    DaoSession daoSession = createDaoSession(context);
    this.audiosRepository = new AudiosRepositoryImpl(daoSession.getAudioDao());
    this.appUsersRepository = new AppUsersRepositoryImpl(context, daoSession.getAppUserDao());
    this.userTokenRepository = new UserTokenRepositoryImpl(daoSession.getUserTokenDao());
  }

  private DaoSession createDaoSession(Context context) {
    DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, DATABASE_NAME);
    Database db = helper.getWritableDb();
    return new DaoMaster(db).newSession();
  }

  @Provides
  @Singleton
  public AudiosRepository provideAudiosRepository() {
    return audiosRepository;
  }

  @Provides
  @Singleton
  public AppUsersRepository provideAppUsersRepository() {
    return appUsersRepository;
  }

  @Provides
  @Singleton
  public UserTokenRepository provideUserTokenRepository() {
    return userTokenRepository;
  }
}

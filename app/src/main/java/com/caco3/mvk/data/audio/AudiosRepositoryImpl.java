package com.caco3.mvk.data.audio;

import com.caco3.mvk.data.appuser.AppUser;
import com.caco3.mvk.vk.audio.Audio;
import com.caco3.mvk.vk.audio.AudioDao;

import java.util.List;

import static com.caco3.mvk.util.Preconditions.checkNotNull;


public class AudiosRepositoryImpl implements AudiosRepository {
  private final AudioDao dao;

  public AudiosRepositoryImpl(AudioDao audioDao) {
    this.dao = checkNotNull(audioDao);
  }

  @Override
  public void save(Audio audio) {
    dao.save(audio);
  }

  @Override
  public void saveAll(Iterable<Audio> audios) {
    dao.saveInTx(audios);
  }

  @Override
  public List<Audio> getAllByAppUser(AppUser appUser) {
    return dao.queryBuilder().where(AudioDao.Properties.AppUser.eq(appUser)).build().list();
  }

  @Override
  public void update(Audio audio) {
    dao.update(audio);
  }

  @Override
  public void updateAll(Iterable<Audio> audios) {
    dao.updateInTx(audios);
  }

  @Override
  public void delete(Audio audio) {
    dao.delete(audio);
  }

  @Override
  public void deleteAllByAppUser(AppUser appUser) {
    Iterable<Audio> usersAudios = getAllByAppUser(appUser);
    dao.deleteInTx(usersAudios);
  }

  @Override
  public void deleteAll() {
    dao.deleteAll();
  }
}

package com.caco3.mvk.data.audio;

import com.caco3.mvk.util.Iterables;
import com.caco3.mvk.util.Longs;
import com.caco3.mvk.vk.audio.Audio;
import com.caco3.mvk.vk.audio.AudioDao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.caco3.mvk.util.Preconditions.checkNotNull;


public class AudiosRepositoryImpl implements AudiosRepository {
  /*package*/ static final Comparator<Audio> audioByIdComparator = new Comparator<Audio>() {
    @Override
    public int compare(Audio o1, Audio o2) {
      return Longs.compare(o1.getId(), o2.getId());
    }
  };

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
  public List<Audio> getAllByVkUserId(long vkUserId) {
    return dao.queryBuilder().where(AudioDao.Properties.OwnerId.eq(vkUserId)).build().list();
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
  public void deleteAllByVkUserId(long vkUserId) {
    Iterable<Audio> usersAudios = getAllByVkUserId(vkUserId);
    dao.deleteInTx(usersAudios);
  }

  @Override
  public void deleteAll() {
    dao.deleteAll();
  }

  @Override
  public List<Audio> getAll() {
    return dao.loadAll();
  }

  @Override
  public void replaceAllByVkUserId(long vkUserId, Iterable<Audio> audios) {
    List<Audio> newAudios = Iterables.toNewArrayList(audios);
    List<Audio> oldAudios = getAllByVkUserId(vkUserId);
    Collections.sort(newAudios, audioByIdComparator);
    Collections.sort(oldAudios, audioByIdComparator);
    dao.deleteInTx(extractToDelete(oldAudios, newAudios));
    updateAll(extractToUpdate(oldAudios, newAudios));
    saveAll(extractToSave(oldAudios, newAudios));
  }

  /*package*/ List<Audio> extractToDelete(List<Audio> oldAudios, List<Audio> newAudios) {
    List<Audio> res = new ArrayList<>();
    for(Audio audio : oldAudios) {
      if (Collections.binarySearch(newAudios, audio, audioByIdComparator) < 0) {
        res.add(audio);
      }
    }

    return res;
  }

  /*package*/ List<Audio> extractToUpdate(List<Audio> oldAudios, List<Audio> newAudios) {
    List<Audio> res = new ArrayList<>();
    for(Audio audio : oldAudios) {
      int index = Collections.binarySearch(newAudios, audio, audioByIdComparator);
      boolean found = index >= 0;
      if (found) {
        res.add(prepareNewEntityForUpdate(audio, newAudios.get(index)));
      }
    }

    return res;
  }

  /*package*/ Audio prepareNewEntityForUpdate(Audio oldEntity, Audio newEntity) {
    newEntity.setFile(oldEntity.getFile());
    newEntity.setEntityId(oldEntity.getEntityId());
    return newEntity;
  }

  /*package*/ List<Audio> extractToSave(List<Audio> oldAudios, List<Audio> newAudios) {
    List<Audio> res = new ArrayList<>();
    for(Audio newAudio : newAudios) {
      boolean found = Collections.binarySearch(oldAudios, newAudio, audioByIdComparator) >= 0;
      if (!found) {
        res.add(newAudio);
      }
    }

    return res;
  }
}

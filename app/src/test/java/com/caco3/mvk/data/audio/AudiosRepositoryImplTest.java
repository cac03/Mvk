package com.caco3.mvk.data.audio;


import com.caco3.mvk.vk.audio.Audio;
import com.caco3.mvk.vk.audio.AudioDao;
import com.caco3.mvk.vk.audio.AudiosGenerator;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class AudiosRepositoryImplTest {
  @Mock
  private AudioDao audioDao;
  private AudiosRepositoryImpl audiosRepository;
  private final AudiosGenerator audiosGenerator = new AudiosGenerator();

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    audiosRepository = new AudiosRepositoryImpl(audioDao);
  }

  @Test
  public void extractingAudiosToDeleteIsCorrect() {
    List<Audio> oldAudios = audiosGenerator.generateList(20);
    List<Audio> newAudios = new ArrayList<>();
    newAudios.add(oldAudios.get(10));
    newAudios.add(oldAudios.get(12));
    newAudios.add(oldAudios.get(2));

    List<Audio> expected = new ArrayList<>(oldAudios);
    expected.remove(oldAudios.get(2));
    expected.remove(oldAudios.get(10));
    expected.remove(oldAudios.get(12));

    Collections.sort(oldAudios, AudiosRepositoryImpl.audioByIdComparator);
    Collections.sort(newAudios, AudiosRepositoryImpl.audioByIdComparator);

    List<Audio> actual = audiosRepository.extractToDelete(oldAudios, newAudios);
    assertThat(actual)
            .containsAll(expected);
  }

  @Test
  public void extractingAudiosToSaveIsCorrect() {
    List<Audio> oldAudios = audiosGenerator.generateList(20);
    List<Audio> expected = audiosGenerator.generateList(10);
    List<Audio> newAudios = new ArrayList<>(oldAudios);
    newAudios.addAll(expected);

    Collections.sort(oldAudios, AudiosRepositoryImpl.audioByIdComparator);
    Collections.sort(newAudios, AudiosRepositoryImpl.audioByIdComparator);

    List<Audio> actual = audiosRepository.extractToSave(oldAudios, newAudios);
    assertThat(actual)
            .containsAll(expected);
  }

  @Test
  public void extractingAudiosToUpdateIsCorrect() {
    List<Audio> oldAudios = audiosGenerator.generateList(10);
    List<Audio> expected = new ArrayList<>(oldAudios);
    List<Audio> newAudios = new ArrayList<>(oldAudios);

    Collections.sort(oldAudios, AudiosRepositoryImpl.audioByIdComparator);
    Collections.sort(newAudios, AudiosRepositoryImpl.audioByIdComparator);

    List<Audio> actual = audiosRepository.extractToUpdate(oldAudios, newAudios);
    assertThat(actual)
            .containsAll(expected);
  }


  @Test
  public void entityMustBeUpdated_downloadedAndEntityIdFieldsAreCopiedToNewEntity() {
    Audio oldEntity = audiosGenerator.generateOne();
    File file = new File("/some/path");
    oldEntity.setFile(file);
    oldEntity.setEntityId(8201L);
    Audio newEntity = audiosGenerator.generateOne();
    Audio updatedEntity = audiosRepository.prepareNewEntityForUpdate(oldEntity, newEntity);
    assertThat(newEntity.getEntityId())
            .isEqualTo(oldEntity.getEntityId());
    assertThat(newEntity.getFile())
            .isEqualTo(oldEntity.getFile());
    assertThat(updatedEntity)
            .isSameAs(newEntity);

  }
}

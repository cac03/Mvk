package com.caco3.mvk.vk.audio;


import com.caco3.mvk.AbstractPojoGenerator;

import java.util.List;
import java.util.Random;

public class AudiosGenerator extends AbstractPojoGenerator<Audio> {
  private static final String[] artists = {
          "asdafs", "gwetwoei", "kjhasdhgkjand", "asfnadksdnvjka", "gadgasdvavxvc"
  };
  private static final String[] titles = {
          "asfasgasd", "nbmnbmn,bwer", "kuhwetoihawegohasdg", "asfasdasd"
  };
  private final Random random = new Random();

  @Override
  public Audio generateOne() {
    Audio audio = new Audio();
    audio.setArtist(artists[random.nextInt(artists.length)]);
    audio.setTitle(titles[random.nextInt(titles.length)]);
    audio.setDurationSeconds(random.nextInt());
    audio.setDownloadUrl(artists[random.nextInt(artists.length)]
            + titles[random.nextInt(titles.length)] + ".mp3");
    audio.setId(random.nextLong());
    return audio;
  }

  @Override
  public List<Audio> generateList(int n) {
    List<Audio> audios = super.generateList(n);
    for(int i = 0, length = audios.size(); i < length; i++) {
      audios.get(i).setVkPlaylistPosition(i);
    }

    return audios;
  }
}

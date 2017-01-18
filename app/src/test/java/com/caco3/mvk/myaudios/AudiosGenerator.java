package com.caco3.mvk.myaudios;


import com.caco3.mvk.vk.audio.Audio;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AudiosGenerator {
  private static final String[] artists = {
          "asdafs", "gwetwoei", "kjhasdhgkjand", "asfnadksdnvjka", "gadgasdvavxvc"
  };
  private static final String[] titles = {
          "asfasgasd", "nbmnbmn,bwer", "kuhwetoihawegohasdg", "asfasdasd"
  };
  private final Random random = new Random();

  public List<Audio> generateList(int numToGenerate) {
    List<Audio> audios = new ArrayList<>();
    for(int i = 0; i < numToGenerate; i++) {
      audios.add(generateAudio());
    }

    return audios;
  }

  public Audio generateAudio() {
    Audio audio = new Audio();
    audio.setArtist(artists[random.nextInt(artists.length)]);
    audio.setTitle(titles[random.nextInt(titles.length)]);
    audio.setDurationSeconds(random.nextInt());
    audio.setDownloadUrl(artists[random.nextInt(artists.length)]
            + titles[random.nextInt(titles.length)]);
    return audio;
  }
}

package com.caco3.mvk.myaudios;

import com.caco3.mvk.search.DataSetFilter;
import com.caco3.mvk.vk.audio.Audio;

import java.util.ArrayList;
import java.util.List;


public class AudiosFilter extends DataSetFilter<Audio> {

  public AudiosFilter(List<Audio> dataSet) {
    super(dataSet);
  }

  @Override
  public List<Audio> filter(String query) {
    List<Audio> filtered = new ArrayList<>();
    for(Audio audio : dataSet) {
      if (audio.getTitle().toLowerCase().contains(query)
              || audio.getArtist().toLowerCase().contains(query)) {
        filtered.add(audio);
      }
    }

    return filtered;
  }
}

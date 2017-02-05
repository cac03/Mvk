package com.caco3.mvk.myaudios;

import com.caco3.mvk.search.DataSetFilter;
import com.caco3.mvk.vk.audio.Audio;

import java.util.ArrayList;
import java.util.List;


public class AudiosFilter implements DataSetFilter<Audio> {

  @Override
  public List<Audio> filter(List<Audio> all, String query) {
    query = query.toLowerCase();
    List<Audio> filtered = new ArrayList<>();
    for(Audio audio : all) {
      if (audio.getTitle().toLowerCase().contains(query)
              || audio.getArtist().toLowerCase().contains(query)) {
        filtered.add(audio);
      }
    }

    return filtered;
  }
}

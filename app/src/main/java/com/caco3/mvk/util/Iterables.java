package com.caco3.mvk.util;


import java.util.ArrayList;

import static com.caco3.mvk.util.Preconditions.checkNotNull;

public class Iterables {
  private Iterables(){
  }

  public static <T> ArrayList<T> toNewArrayList(Iterable<T> iterable) {
    checkNotNull(iterable, "iterable == null");
    ArrayList<T> arrayList = new ArrayList<>();
    for(T t : iterable) {
      arrayList.add(t);
    }

    return arrayList;
  }
}

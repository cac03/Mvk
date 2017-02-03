package com.caco3.mvk;


import java.util.ArrayList;
import java.util.List;

public abstract class AbstractPojoGenerator<T> {

  public abstract T generateOne();

  public List<T> generateList(int n) {
    List<T> list = new ArrayList<>(n);
    for(int i = 0; i < n; i++) {
      list.add(generateOne());
    }

    return list;
  }
}

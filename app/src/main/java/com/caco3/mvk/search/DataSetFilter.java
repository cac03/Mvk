package com.caco3.mvk.search;

import java.util.ArrayList;
import java.util.List;

import static com.caco3.mvk.util.Preconditions.checkNotNull;


public interface DataSetFilter<T> {
  List<T> filter(List<T> all, String query);
}

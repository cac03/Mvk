package com.caco3.mvk.search;

import java.util.ArrayList;
import java.util.List;

import static com.caco3.mvk.util.Preconditions.checkNotNull;


public abstract class DataSetFilter<T> {
  protected List<T> dataSet = new ArrayList<>();

  protected DataSetFilter(List<T> dataSet) {
    checkNotNull(dataSet, "dataSet == null");
    this.dataSet.clear();
    this.dataSet.addAll(dataSet);
  }

  public abstract List<T> filter(String query);

  public void resetWith(List<T> newDataSet) {
    dataSet.clear();
    dataSet.addAll(newDataSet);
  }
}

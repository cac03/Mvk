package com.caco3.mvk.data;

import java.util.List;

public interface BaseRepository<T> {
  void save(T entity);
  void saveAll(Iterable<T> entities);
  List<T> getAll();
  void update(T entity);
  void updateAll(Iterable<T> entities);
  void delete(T entity);
  void deleteAll();
}

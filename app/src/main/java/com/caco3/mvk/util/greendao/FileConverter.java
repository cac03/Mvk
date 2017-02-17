package com.caco3.mvk.util.greendao;

import org.greenrobot.greendao.converter.PropertyConverter;

import java.io.File;

public class FileConverter implements PropertyConverter<File, String> {
  @Override
  public File convertToEntityProperty(String databaseValue) {
    if (databaseValue == null) {
      return null;
    } else {
      return new File(databaseValue);
    }
  }

  @Override
  public String convertToDatabaseValue(File entityProperty) {
    return entityProperty.getPath();
  }
}

package com.caco3.mvk.util;

import org.junit.Test;


import static org.assertj.core.api.Java6Assertions.assertThat;


public class StringsTest {

  @Test
  public void longsJoinTest() {
    long[] longs = {1,2,4};
    String delimiter = ",";
    assertThat(Strings.join(delimiter, longs))
            .isEqualTo("1,2,4");
  }

  @Test
  public void toJoinArrayIsEmpty_emptyStringReturned() {
    long[] toJoin = new long[0];
    String delimiter = ",";
    assertThat(Strings.join(delimiter, toJoin))
            .isEmpty();
  }
}

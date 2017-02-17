package com.caco3.mvk.vk.audio;

import com.caco3.mvk.vk.VkResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.junit.Test;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class VkAudioServiceImplTest {
  private static final String AUDIO1_JSON = "{\"id\":41,\"owner_id\":1,\"artist\":\"Astronomyy\",\"title\":\"Rest in Paradise\",\"duration\":230,\"date\":1487274500,\"url\":\"\"}";
  private static final String AUDIO2_JSON = "{\"id\":25,\"owner_id\":1,\"artist\":\"ATTLAS\",\"title\":\"Frost\",\"duration\":277,\"date\":1487274055,\"url\":\"\"}";
  private static final String VALID_JSON = "{\"response\":{\"count\":2,\"items\":" +
          "[" + AUDIO1_JSON + "," + AUDIO2_JSON + "]}}";

  private final Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

  @Test
  public void audiosResponseParsedFromJson_jsonParsedCorrectly() {
    Type type = new ParameterizedType() {
      @Override
      public Type[] getActualTypeArguments() {
        return new Type[]{VkAudioServiceImpl.AudiosResponse.class};
      }

      @Override
      public Type getRawType() {
        return VkResponse.class;
      }

      @Override
      public Type getOwnerType() {
        return null;
      }
    };
    VkResponse<VkAudioServiceImpl.AudiosResponse> response = gson.fromJson(VALID_JSON, type);

    Audio audio1 = gson.fromJson(AUDIO1_JSON, Audio.class);
    Audio audio2 = gson.fromJson(AUDIO2_JSON, Audio.class);

    assertThat(response.getResponseOrThrowIfNotSuccessful().audios)
            .hasSize(2)
            .contains(audio1, audio2);
  }
}

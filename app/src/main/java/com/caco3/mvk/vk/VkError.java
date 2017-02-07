package com.caco3.mvk.vk;

import com.google.gson.annotations.SerializedName;

public class VkError {
  @SerializedName("error")
  public String errorType;
  @SerializedName("error_description")
  public String errorDescription;
  @SerializedName("error_code")
  public int errorCode;
  @SerializedName("error_msg")
  public String errorMessage;
  @SerializedName("captcha_img")
  public String captchaImageUrl;
  @SerializedName("captcha_sid")
  public int captchaId;

  public RuntimeException propagate() {
    throw VkException.byVkError(this);
  }

  @Override
  public String toString() {
    return "VkError{" +
            "errorType='" + errorType + '\'' +
            ", errorDescription='" + errorDescription + '\'' +
            ", errorCode=" + errorCode +
            ", errorMessage='" + errorMessage + '\'' +
            ", captchaImageUrl='" + captchaImageUrl + '\'' +
            ", captchaId=" + captchaId +
            '}';
  }
}
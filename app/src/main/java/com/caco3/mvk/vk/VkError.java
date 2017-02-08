package com.caco3.mvk.vk;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VkError {
  @SerializedName("error")
  @Expose
  public String errorType;
  @SerializedName("error_description")
  @Expose
  public String errorDescription;
  @SerializedName("error_code")
  @Expose
  public int errorCode;
  @SerializedName("error_msg")
  @Expose
  public String errorMessage;
  @SerializedName("captcha_img")
  @Expose
  public String captchaImageUrl;
  @SerializedName("captcha_sid")
  @Expose
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
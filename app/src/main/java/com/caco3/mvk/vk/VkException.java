package com.caco3.mvk.vk;

import com.caco3.mvk.vk.exceptions.AuthFailedException;
import com.caco3.mvk.vk.exceptions.CaptchaNeededException;

import static com.caco3.mvk.util.Preconditions.checkNotNull;

public abstract class VkException extends RuntimeException {
  /*package*/ static final int UNKNOWN_ERROR_CODE = 1;
  /*package*/ static final int APP_DISABLED_ERROR_CODE = 2;
  /*package*/ static final int UNKNOWN_METHOD_ERROR_CODE = 3;
  /*package*/ static final int INCORRECT_SIGNATURE_METHOD_ERROR_CODE = 4;
  /*package*/ static final int USER_AUTHORIZATION_FAILED_ERROR_CODE = 5;
  /*package*/ static final int TOO_MANY_REQUESTS_ERROR_CODE = 6;
  /*package*/ static final int PERMISSION_DENIEED_ERROR_CODE = 7;
  /*package*/ static final int INVALID_REQUEST_ERROR_CODE = 8;
  /*package*/ static final int FLOOD_CONTROL_ERROR_CODE = 9;
  /*package*/ static final int INTERAL_SERVER_ERROR_CODE = 10;
  /*package*/ static final int IN_TEST_MODE_APP_SHOULD_DISABLED_OR_USER_SHOULD_BE_AUTHORIZED_ERROR_CODE = 11;
  /*package*/ static final int CAPTCHA_NEEDED_ERROR_CODE = 14;
  /*package*/ static final int ACCESS_DENIED_ERROR_CODE = 15;
  /*package*/ static final int HTTP_AUTHORIZATION_FAILED = 16;
  /*package*/ static final int VALIDATION_REQUIRED_ERROR = 17;
  /*package*/ static final int USER_WAS_DELETED_OR_BANNED_ERROR_CODE = 18;
  /*package*/ static final int PERMISSION_DENIED_FOR_NON_STANDALONE_APPS_ERROR_CODE = 20;
  /*package*/ static final int PERMISSION_TO_PERFORM_THIS_ACTION_ALLOWED_ONLY_FOR_STANDALONE_AND_OPENAPI_APPS_ERROR_CODE = 21;
  /*package*/ static final int THIS_METHOD_WAS_DISABLED_ERROR_CODE = 23;
  /*package*/ static final int CONFIRMATION_REQUIRED_ERROR_CODE = 24;

  private VkError vkError;

  public VkException(VkError vkError) {
    this.vkError = vkError;
  }

  public VkException(String message) {
    super(message);
  }

  public VkException(VkError vkError, String message) {
    super(message);
    this.vkError = vkError;
  }

  public VkException(String message, Throwable cause) {
    super(message, cause);
  }

  public VkException(Throwable cause) {
    super(cause);
  }

  public VkError getVkError() {
    return vkError;
  }

  // this class has to know about its all subclasses...
  public static VkException byVkError(VkError vkError) {
    checkNotNull(vkError, "vkError == null");
    int errorCode = vkError.errorCode;
    if (errorCode == UNKNOWN_ERROR_CODE) {
      return new UnknownVkErrorException(vkError);
    } else if (errorCode == CAPTCHA_NEEDED_ERROR_CODE) {
      return new CaptchaNeededException(vkError);
    } else if (errorCode == USER_AUTHORIZATION_FAILED_ERROR_CODE) {
      return new AuthFailedException(vkError);
    } else {
      // TODO: 2/7/17 one exception to one error
      return new VkException(vkError) {
      };
    }
  }
}

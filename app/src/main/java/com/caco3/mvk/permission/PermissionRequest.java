package com.caco3.mvk.permission;


import com.caco3.mvk.util.function.Action0;
import com.caco3.mvk.util.function.Action1;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.caco3.mvk.util.Preconditions.checkState;

public class PermissionRequest {
  private final List<Permission> permissions;
  private final Action0 onAllPermissionsGranted;
  private final Action0 onRequestCanceled;
  private final Action1<List<Permission>> onAnyPermissionDenied;
  private final Action1<List<Permission>> onAnyNeverAskAgainDenied;

  private PermissionRequest(Builder builder) {
    this.permissions = builder.permissions;
    this.onAnyPermissionDenied = builder.onAnyPermissionDenied;
    this.onRequestCanceled = builder.onRequestCanceled;
    this.onAllPermissionsGranted = builder.onAllPermissionsGranted;
    this.onAnyNeverAskAgainDenied = builder.onAnyNeverAskAgainDenied;
  }

  public static Builder builder() {
    return new Builder();
  }

  public List<Permission> getPermissions() {
    return permissions;
  }

  /*package*/ Action0 getOnAllPermissionsGrantedAction() {
    return onAllPermissionsGranted;
  }

  /*package*/ Action0 getOnRequestCanceledAction() {
    return onRequestCanceled;
  }

  /*package*/ Action1<List<Permission>> getOnAnyPermissionDeniedAction() {
    return onAnyPermissionDenied;
  }

  /*package*/ Action1<List<Permission>> getOnAnyNeverAskAgainDeniedAction() {
    return onAnyNeverAskAgainDenied;
  }

  public static class Builder {
    private List<Permission> permissions = new ArrayList<>();
    private Action0 onAllPermissionsGranted;
    private Action0 onRequestCanceled;
    private Action1<List<Permission>> onAnyPermissionDenied;
    private Action1<List<Permission>> onAnyNeverAskAgainDenied;

    public Builder addPermission(Permission permission) {
      this.permissions.add(permission);
      return this;
    }

    public Builder addPermissions(Collection<Permission> collection) {
      this.permissions.addAll(collection);
      return this;
    }

    public Builder onAllGranted(Action0 onAllPermissionGranted) {
      this.onAllPermissionsGranted = onAllPermissionGranted;
      return this;
    }

    public Builder onCancel(Action0 onRequestCanceled) {
      this.onRequestCanceled = onRequestCanceled;
      return this;
    }

    public Builder onAnyDenied(Action1<List<Permission>> onPermissionDenied) {
      this.onAnyPermissionDenied = onPermissionDenied;
      return this;
    }

    public Builder onAnyNeverAskAgainDenied(Action1<List<Permission>> onAnyNeverAskAgainDenied) {
      this.onAnyNeverAskAgainDenied = onAnyNeverAskAgainDenied;
      return this;
    }

    public PermissionRequest build() {
      checkState(!permissions.isEmpty(), "No permissions to request");
      checkState(onAllPermissionsGranted != null, "onAllPermissionsGranted action is not set");
      checkState(onAnyPermissionDenied != null, "onAnyPermissionDenied action is not set");
      checkState(onRequestCanceled != null, "onRequestCanceled action is not set");
      checkState(onAnyNeverAskAgainDenied != null, "onAnyNeverAskAgainDenied action is not set");
      return new PermissionRequest(this);
    }
  }
}

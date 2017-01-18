package com.caco3.mvk.login;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.caco3.mvk.R;
import com.caco3.mvk.myaudios.MyAudiosActivity;
import com.caco3.mvk.dagger.DaggerComponentsHolder;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.caco3.mvk.util.Preconditions.checkState;


public class LogInFragment extends Fragment implements LogInView {
  @Inject
  LogInPresenter presenter;
  @BindView(R.id.log_in_frag_login)
  EditText usernameEditText;
  @BindView(R.id.log_in_frag_password)
  EditText passwordEditText;
  ProgressDialog loggingInProgressDialog = null;

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater,
                           @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState) {
    View root = inflater.inflate(R.layout.log_in_fragment, container, false);
    ButterKnife.bind(this, root);

    return root;
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    if (presenter == null) {
      injectPresenter();
    }
    presenter.onViewAttached(this);
  }

  private void injectPresenter() {
    DaggerComponentsHolder componentsHolder = DaggerComponentsHolder.getInstance();
    if (!componentsHolder.hasLogInComponent()) {
      componentsHolder.createLogInComponent();
    }
    componentsHolder.getLogInComponent().inject(this);
  }

  @Override
  public void onDestroyView() {
    presenter.onViewDetached(this);
    if (loggingInProgressShown()) {
      // presenter shall show it again if necessary when we reattach to it
      hideLogInProgress();
    }
    super.onDestroyView();
  }

  private boolean loggingInProgressShown() {
    return loggingInProgressDialog != null;
  }

  @Override
  public void showUsernameIsEmptyStringError() {
    usernameEditText.setError(getString(R.string.this_field_required));
  }

  @Override
  public void showPasswordIsEmptyStringError() {
    passwordEditText.setError(getString(R.string.this_field_required));
  }

  @Override
  public void showUsernameOrPasswordIncorrectError() {
    Toast.makeText(getContext(), R.string.username_or_password_incorrect, Toast.LENGTH_LONG).show();
  }

  @Override
  public void showNetworkIsUnavailableError() {
    Toast.makeText(getContext(), R.string.network_is_unavailable, Toast.LENGTH_SHORT).show();
  }

  @Override
  public void showNetworkErrorOccurredError() {
    Toast.makeText(getContext(), R.string.network_error_occurred, Toast.LENGTH_SHORT).show();
  }

  @Override
  public void navigateToMyAudiosActivity() {
    Activity activity = getActivity();
    Intent intent = new Intent(activity, MyAudiosActivity.class);
    activity.startActivity(intent);
    activity.finish();
  }

  @Override
  public void showLogInProgress() {
    checkState(loggingInProgressDialog == null,
            "Attempt to show log in progress dialog, but previous was not hidden.");
    loggingInProgressDialog = prepareLoggingInProgressDialog();
    loggingInProgressDialog.show();
  }

  private ProgressDialog prepareLoggingInProgressDialog() {
    ProgressDialog progressDialog = new ProgressDialog(getContext());
    progressDialog.setMessage(getString(R.string.logging_in));

    progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
      @Override
      public void onCancel(DialogInterface dialog) {
        presenter.cancelLoggingIn();
        loggingInProgressDialog = null;
      }
    });

    return progressDialog;
  }

  @Override
  public void hideLogInProgress() {
    checkState(loggingInProgressDialog != null, "Attempt to hide a dialog that is not shown.");
    loggingInProgressDialog.dismiss();
    loggingInProgressDialog = null;
  }

  @OnClick(R.id.log_in_frag_log_in_btn)
  /*package*/ void attemptToLogIn() {
    String username = usernameEditText.getText().toString();
    String password = passwordEditText.getText().toString();

    presenter.attemptToLogIn(username, password);
  }
}

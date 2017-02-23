package com.caco3.mvk;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;

import org.robolectric.Robolectric;

public class SupportFragmentStarter {

  public static void startFragment(Fragment fragment) {
    AppCompatActivity appCompatActivity = Robolectric.setupActivity(AppCompatFragmentActivity.class);
    appCompatActivity.getSupportFragmentManager().beginTransaction()
            .add(fragment, null).commitNow();
  }

  private static class AppCompatFragmentActivity extends AppCompatActivity {
    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      LinearLayout linearLayout = new LinearLayout(this);
      setContentView(linearLayout);
    }
  }
}

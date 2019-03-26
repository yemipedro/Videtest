package com.alexandriacity.videtest.ui.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.alexandriacity.videtest.R;
import com.alexandriacity.videtest.utilities.InjectorUtils;
import com.alexandriacity.videtest.utilities.NetworkUtils;
import com.alexandriacity.videtest.utilities.WorkUtils;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {

    private ViewGroup mRoot;
    private Snackbar connectivitySnack;
    BroadcastReceiver connectivityBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRoot = findViewById(R.id.root);
        connectivitySnack = Snackbar.make(mRoot, "", Snackbar.LENGTH_INDEFINITE);

        RecyclerView rvCourseList = findViewById(R.id.rv_course_list);
        ProgressBar pbLoading = findViewById(R.id.pb_loading);
        CourseAdapter adapter = new CourseAdapter(this);
        rvCourseList.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        rvCourseList.setHasFixedSize(true);
        rvCourseList.setAdapter(adapter);

        MainActivityViewModelFactory factory = InjectorUtils.provideMainActivityViewModelFactory(this);
        MainActivityViewModel mViewModel = ViewModelProviders.of(this, factory).get(MainActivityViewModel.class);

        mViewModel.getCourseListItems().observe(this, courseListItems -> {
            if (courseListItems == null || courseListItems.size() <= 0) {
                pbLoading.setVisibility(View.VISIBLE);
                rvCourseList.setVisibility(View.GONE);
            } else {
                adapter.setCourseList(courseListItems);
                pbLoading.setVisibility(View.GONE);
                rvCourseList.setVisibility(View.VISIBLE);
            }
        });

        WorkUtils.scheduleCourseSyncWork();

        connectivityBroadcastReceiver = new ConnectivityBroadcastReceiver();
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        this.registerReceiver(connectivityBroadcastReceiver, filter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            this.unregisterReceiver(connectivityBroadcastReceiver);
        } catch (IllegalArgumentException exception) {
            exception.printStackTrace();
        }
    }

    private class ConnectivityBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (NetworkUtils.isConnected(MainActivity.this)) {
                if (connectivitySnack.isShown())
                    connectivitySnack.dismiss();
            } else {
                if (!connectivitySnack.isShown()) {
                    connectivitySnack.setText(getString(R.string.status_not_connected));
                    connectivitySnack.show();
                }
            }
        }
    }
}

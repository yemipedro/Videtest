package com.alexandriacity.videtest.ui.coursedetail;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alexandriacity.videtest.R;
import com.alexandriacity.videtest.utilities.InjectorUtils;
import com.bumptech.glide.Glide;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class CourseDetailActivity extends AppCompatActivity {

    public static final String EXTRA_ID = "id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);

        int id = getIntent().getIntExtra(EXTRA_ID, -1);
        if (id == -1) {
            throw new IllegalArgumentException("Invalid course id");
        }

        setupActionBar();

        CourseDetailActivityViewModelFactory factory = InjectorUtils.provideCourseDetailActivityViewModelFactory(this, id);
        CourseDetailActivityViewModel mViewModel = ViewModelProviders.of(this, factory).get(CourseDetailActivityViewModel.class);

        mViewModel.getCourseLiveData().observe(this, courseEntry -> {
            if (courseEntry != null) {
                ImageView imgCourseThumb = findViewById(R.id.img_course_thumb);
                Glide.with(this)
                        .load(courseEntry.getImageUrl())
                        .into(imgCourseThumb);
                setToolbarTitle(courseEntry.getName());
                TextView tvName = findViewById(R.id.tv_course_title);
                tvName.setText(courseEntry.getName());
                TextView tvNumViews = findViewById(R.id.tv_num_views);
                tvNumViews.setText(getString(R.string.course_num_views, courseEntry.getNumberOfViews()));
                TextView tvChannelName = findViewById(R.id.tv_channel_name);
                tvChannelName.setText(courseEntry.getChannelName());
                TextView tvNumSubs = findViewById(R.id.tv_channel_subs);
                tvNumSubs.setText(getString(R.string.course_num_subs, courseEntry.getNumberOfSubscribers()));
                CircleImageView imgChannelProfile = findViewById(R.id.img_channel_thumb);
                Glide.with(this)
                        .load(courseEntry.getProfileImageUrl())
                        .into(imgChannelProfile);

                FrameLayout flCourse = findViewById(R.id.fl_course);
                flCourse.setOnClickListener(view -> {
                    String courseLink = courseEntry.getLink();
                    if (!TextUtils.isEmpty(courseLink))
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(courseLink)));
                });
            }
        });

        RecyclerView rvVideosList = findViewById(R.id.rv_videos_list);
        rvVideosList.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        rvVideosList.setHasFixedSize(true);
        VideoAdapter adapter = new VideoAdapter(this);
        rvVideosList.setAdapter(adapter);

        ProgressBar pbLoading = findViewById(R.id.pb_loading);
        mViewModel.getVideos(id).observe(this, videoEntries -> {
            if (videoEntries == null || videoEntries.size() <= 0) {
                pbLoading.setVisibility(View.VISIBLE);
                rvVideosList.setVisibility(View.GONE);
            } else {
                adapter.setCourseList(videoEntries);
                pbLoading.setVisibility(View.GONE);
                rvVideosList.setVisibility(View.VISIBLE);
            }
        });
    }

    private void setupActionBar() {
        setSupportActionBar(findViewById(R.id.toolbar));
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.back_arrow_icon);
            actionBar.setTitle(R.string.label_course);
        }
    }

    private void setToolbarTitle(String title) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.setTitle(title);
    }
}

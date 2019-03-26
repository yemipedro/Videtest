package com.alexandriacity.videtest.data.network;

import android.content.Context;

import com.alexandriacity.videtest.AppExecutors;
import com.alexandriacity.videtest.data.VidetestRepository;
import com.alexandriacity.videtest.data.database.VideoEntry;
import com.alexandriacity.videtest.utilities.InjectorUtils;
import com.google.common.util.concurrent.ListenableFuture;

import androidx.annotation.NonNull;
import androidx.concurrent.futures.ResolvableFuture;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.work.ListenableWorker;
import androidx.work.WorkerParameters;

public class VideosFetchWorker extends ListenableWorker {

    public static final String COURSE_ID = "course_id";

    public VideosFetchWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public ListenableFuture<Result> startWork() {
        ResolvableFuture<Result> result = ResolvableFuture.create();
        int courseId = getInputData().getInt(COURSE_ID, -1);
        if (courseId == -1) result.set(Result.failure());
        VidetestRepository repository = InjectorUtils.provideRepository(getApplicationContext());
        AppExecutors executors = AppExecutors.getInstance();
        executors.mainThread().execute(() -> {
            LiveData<VideoEntry> videoEntryLiveData = repository.fetchVideos(getApplicationContext(), courseId);
            videoEntryLiveData.observeForever(new Observer<VideoEntry>() {
                boolean isDataReceived = false;

                @Override
                public void onChanged(VideoEntry videoEntry) {
                    if (videoEntry != null) {
                        isDataReceived = true;
                        repository.insertVideo(videoEntry);
                        result.set(Result.success());
                    } else {
                        videoEntryLiveData.removeObserver(this);
                        result.set(isDataReceived ? Result.success() : Result.retry());
                    }
                }
            });
        });
        return result;
    }
}

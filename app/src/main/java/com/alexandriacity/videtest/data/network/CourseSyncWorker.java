package com.alexandriacity.videtest.data.network;

import android.content.Context;

import com.alexandriacity.videtest.AppExecutors;
import com.alexandriacity.videtest.data.VidetestRepository;
import com.alexandriacity.videtest.data.database.CourseEntry;
import com.alexandriacity.videtest.utilities.InjectorUtils;
import com.google.common.util.concurrent.ListenableFuture;

import androidx.annotation.NonNull;
import androidx.concurrent.futures.ResolvableFuture;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.work.ListenableWorker;
import androidx.work.WorkerParameters;

public class CourseSyncWorker extends ListenableWorker {

    public CourseSyncWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public ListenableFuture<Result> startWork() {
        VidetestRepository repository = InjectorUtils.provideRepository(getApplicationContext());
        ResolvableFuture<Result> result = ResolvableFuture.create();
        AppExecutors executors = AppExecutors.getInstance();
        executors.diskIO().execute(() -> {
            if (repository.isCourseFetchNeeded()) {
                executors.mainThread().execute(() -> {
                    LiveData<CourseEntry> courseEntryLiveData = repository.fetchCourses(getApplicationContext());
                    courseEntryLiveData.observeForever(new Observer<CourseEntry>() {
                        boolean isDataReceived = false;

                        @Override
                        public void onChanged(CourseEntry courseEntry) {
                            if (courseEntry != null) {
                                isDataReceived = true;
                                repository.insertCourse(courseEntry);
                                result.set(Result.success());
                            } else {
                                courseEntryLiveData.removeObserver(this);
                                result.set(isDataReceived ? Result.success() : Result.retry());
                            }
                        }
                    });
                });
            }
        });
        return result;
    }
}

package com.alexandriacity.videtest.utilities;

import com.alexandriacity.videtest.data.network.CourseSyncWorker;
import com.alexandriacity.videtest.data.network.VideosFetchWorker;

import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.ExistingWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

public class
WorkUtils {

    private static final String LOG_TAG = WorkUtils.class.getSimpleName();

    public static void scheduleCourseSyncWork() {
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();
        OneTimeWorkRequest request = new OneTimeWorkRequest.Builder(CourseSyncWorker.class)
                .setConstraints(constraints)
                .build();
        WorkManager.getInstance().enqueueUniqueWork("course_sync", ExistingWorkPolicy.KEEP, request);
    }

    public static void scheduleVideosFetchWork(int courseId) {
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();
        Data data = new Data.Builder().putInt(VideosFetchWorker.COURSE_ID, courseId).build();
        OneTimeWorkRequest request = new OneTimeWorkRequest.Builder(VideosFetchWorker.class)
                .setConstraints(constraints)
                .setInputData(data)
                .build();
        WorkManager.getInstance().enqueue(request);
    }
}

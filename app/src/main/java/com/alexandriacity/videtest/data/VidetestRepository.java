package com.alexandriacity.videtest.data;
/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import android.content.Context;

import com.alexandriacity.videtest.AppExecutors;
import com.alexandriacity.videtest.data.database.CourseDao;
import com.alexandriacity.videtest.data.database.CourseEntry;
import com.alexandriacity.videtest.data.database.CourseListItem;
import com.alexandriacity.videtest.data.database.VideoDao;
import com.alexandriacity.videtest.data.database.VideoEntry;
import com.alexandriacity.videtest.data.network.RetrofitOperations;
import com.alexandriacity.videtest.utilities.WorkUtils;

import java.util.List;

import androidx.lifecycle.LiveData;


public class VidetestRepository {

    // For Singleton instantiation
    private static final Object LOCK = new Object();
    private static VidetestRepository sInstance;
    private final CourseDao mCourseDao;
    private final VideoDao mVideoDao;
    private final AppExecutors mExecutors;

    private VidetestRepository(CourseDao courseDao,
                               VideoDao videoDao,
                               AppExecutors executors) {
        this.mCourseDao = courseDao;
        this.mVideoDao = videoDao;
        mExecutors = executors;
    }

    public synchronized static VidetestRepository getInstance(CourseDao courseDao,
                                                              VideoDao videoDao,
                                                              AppExecutors executors) {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new VidetestRepository(courseDao, videoDao, executors);

            }
        }
        return sInstance;
    }

    public LiveData<List<CourseListItem>> getCourseListItems() {
        return mCourseDao.getCourseListItems();
    }

    /**
     * Checks if course data is existing in database.
     *
     * @return Whether a fetch is needed
     */
    public boolean isCourseFetchNeeded() {
        int count = mCourseDao.countCourses();
        return (count <= 0);
    }

    public LiveData<CourseEntry> fetchCourses(Context context) {
        return RetrofitOperations.fetchCourses(context);
    }

    public LiveData<VideoEntry> fetchVideos(Context context, int courseId) {
        return RetrofitOperations.fetchVideos(context, courseId);
    }

    public void insertCourse(CourseEntry courseEntry) {
        mExecutors.diskIO().execute(() -> mCourseDao.insertCourse(courseEntry));
    }

    public void insertVideo(VideoEntry videoEntry) {
        mExecutors.diskIO().execute(() -> mVideoDao.insertVideo(videoEntry));
    }

    public LiveData<CourseEntry> getCourse(int id) {
        return mCourseDao.getCourse(id);
    }

    public LiveData<List<VideoEntry>> getVideos(int courseId) {
        mExecutors.diskIO().execute(() -> {
            if (isVideosFetchNeeded(courseId))
                WorkUtils.scheduleVideosFetchWork(courseId);
        });
        return mVideoDao.getVideos(courseId);
    }

    public boolean isVideosFetchNeeded(int id) {
        int count = mVideoDao.countVideos(id);
        return (count <= 0);
    }
}
package com.alexandriacity.videtest.utilities;/*
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
import com.alexandriacity.videtest.data.VidetestRepository;
import com.alexandriacity.videtest.data.database.VidetestDatabase;
import com.alexandriacity.videtest.ui.coursedetail.CourseDetailActivityViewModelFactory;
import com.alexandriacity.videtest.ui.main.MainActivityViewModelFactory;


/**
 * Provides static methods to inject the various classes needed for Videtest
 */
public class InjectorUtils {

    public static VidetestRepository provideRepository(Context context) {
        VidetestDatabase database = VidetestDatabase.getInstance(context.getApplicationContext());
        AppExecutors executors = AppExecutors.getInstance();

        return VidetestRepository.getInstance(database.courseDao(), database.videoDao(), executors);
    }

    public static MainActivityViewModelFactory provideMainActivityViewModelFactory(Context context) {
        VidetestRepository videtestRepository = provideRepository(context);
        return new MainActivityViewModelFactory(videtestRepository);
    }

    public static CourseDetailActivityViewModelFactory provideCourseDetailActivityViewModelFactory(Context context, int id) {
        VidetestRepository videtestRepository = provideRepository(context);
        return new CourseDetailActivityViewModelFactory(videtestRepository, id);
    }
}

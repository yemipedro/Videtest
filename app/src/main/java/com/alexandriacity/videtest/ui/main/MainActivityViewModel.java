package com.alexandriacity.videtest.ui.main;

import com.alexandriacity.videtest.data.VidetestRepository;
import com.alexandriacity.videtest.data.database.CourseListItem;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

class MainActivityViewModel extends ViewModel {
    private final VidetestRepository mRepository;

    MainActivityViewModel(VidetestRepository repository) {
        mRepository = repository;
    }

    LiveData<List<CourseListItem>> getCourseListItems() {
        return mRepository.getCourseListItems();
    }
}

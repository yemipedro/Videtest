package com.alexandriacity.videtest.ui.coursedetail;

import com.alexandriacity.videtest.data.VidetestRepository;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;


public class CourseDetailActivityViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private final VidetestRepository mRepository;
    private final int id;

    public CourseDetailActivityViewModelFactory(VidetestRepository repository, int id) {
        this.mRepository = repository;
        this.id = id;
    }

    @Override
    @NonNull
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new CourseDetailActivityViewModel(mRepository, id);
    }
}

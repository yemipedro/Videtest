package com.alexandriacity.videtest.ui.main;

import com.alexandriacity.videtest.data.VidetestRepository;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;


public class MainActivityViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private final VidetestRepository mRepository;

    public MainActivityViewModelFactory(VidetestRepository repository) {
        this.mRepository = repository;
    }

    @Override
    @NonNull
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new MainActivityViewModel(mRepository);
    }
}

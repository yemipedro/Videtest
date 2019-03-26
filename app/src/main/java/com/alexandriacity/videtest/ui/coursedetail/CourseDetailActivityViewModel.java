package com.alexandriacity.videtest.ui.coursedetail;

import com.alexandriacity.videtest.data.VidetestRepository;
import com.alexandriacity.videtest.data.database.CourseEntry;
import com.alexandriacity.videtest.data.database.VideoEntry;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

class CourseDetailActivityViewModel extends ViewModel {
    private final VidetestRepository mRepository;
    private final LiveData<CourseEntry> mCourseEntryLiveData;

    CourseDetailActivityViewModel(VidetestRepository repository, int id) {
        mRepository = repository;
        mCourseEntryLiveData = mRepository.getCourse(id);
    }

    LiveData<CourseEntry> getCourseLiveData() {
        return mCourseEntryLiveData;
    }

    LiveData<List<VideoEntry>> getVideos(int courseId) {
        return mRepository.getVideos(courseId);
    }
}

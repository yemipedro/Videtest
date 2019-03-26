package com.alexandriacity.videtest.data.database;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface CourseDao {

    @Query("SELECT id, name, imageUrl, numberOfViews FROM courses")
    LiveData<List<CourseListItem>> getCourseListItems();

    @Query("SELECT * FROM courses WHERE id = :id")
    LiveData<CourseEntry> getCourse(int id);

    @Query("SELECT COUNT(*) FROM courses")
    int countCourses();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCourse(CourseEntry courseEntry);

}

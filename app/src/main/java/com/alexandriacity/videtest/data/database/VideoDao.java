package com.alexandriacity.videtest.data.database;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface VideoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertVideo(VideoEntry videoEntry);

    @Query("SELECT * FROM videos WHERE courseId = :courseId")
    LiveData<List<VideoEntry>> getVideos(int courseId);

    @Query("SELECT COUNT(*) FROM videos WHERE courseId = :courseId")
    int countVideos(int courseId);
}

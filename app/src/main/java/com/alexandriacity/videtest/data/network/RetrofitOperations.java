package com.alexandriacity.videtest.data.network;

import android.content.Context;

import com.alexandriacity.videtest.data.database.CourseEntry;
import com.alexandriacity.videtest.data.database.VideoEntry;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class RetrofitOperations {

    public static LiveData<CourseEntry> fetchCourses(Context context) {
        MutableLiveData<CourseEntry> liveData = new MutableLiveData<>();
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = "https://api.letsbuildthatapp.com/youtube/home_feed";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        JSONArray videosArray = response.getJSONArray("videos");
                        JSONObject user = response.getJSONObject("user");
                        String userName = user.getString("name");
                        int size = 0;
                        for (int i = 0; i < videosArray.length(); i++) {
                            size++;
                            JSONObject courseJson = videosArray.getJSONObject(i);
                            try {
                                CourseEntry courseEntry = getCourseEntry(courseJson, userName);
                                liveData.setValue(courseEntry);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            if (size == videosArray.length()) liveData.setValue(null);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                Throwable::printStackTrace);
        queue.add(request);
        return liveData;
    }

    public static LiveData<VideoEntry> fetchVideos(Context context, int courseId) {
        MutableLiveData<VideoEntry> liveData = new MutableLiveData<>();
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = "https://api.letsbuildthatapp.com/youtube/course_detail?id=" + courseId;
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    int size = 0;
                    for (int i = 0; i < response.length(); i++) {
                        size++;
                        try {
                            JSONObject videoJson = response.getJSONObject(i);
                            VideoEntry videoEntry = getVideoEntry(videoJson, courseId);
                            liveData.setValue(videoEntry);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (size == response.length()) liveData.setValue(null);
                    }
                },
                Throwable::printStackTrace);
        queue.add(request);
        return liveData;
    }

    private static VideoEntry getVideoEntry(JSONObject videoJson, int courseId) throws JSONException {
        String name = videoJson.getString("name");
        String duration = videoJson.getString("duration");
        String imageUrl = videoJson.getString("imageUrl");
        String link = videoJson.getString("link");
        int number = videoJson.getInt("number");
        return new VideoEntry(number, courseId, name, duration, imageUrl, link);
    }

    private static CourseEntry getCourseEntry(JSONObject jsonCourse, String userName) throws JSONException {
        int id = jsonCourse.getInt("id");
        String name = jsonCourse.getString("name");
        String link = jsonCourse.getString("link");
        String imageUrl = jsonCourse.getString("imageUrl");
        long numberOfViews = jsonCourse.getLong("numberOfViews");
        JSONObject channel = jsonCourse.getJSONObject("channel");
        String channelName = channel.getString("name");
        String profileImageUrl = channel.getString("profileImageUrl");
        long numberOfSubscribers = channel.getLong("numberOfSubscribers");
        return new CourseEntry(id, name, userName, link, imageUrl, numberOfViews, channelName, profileImageUrl, numberOfSubscribers);
    }
}

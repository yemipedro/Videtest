package com.alexandriacity.videtest.ui.coursedetail;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alexandriacity.videtest.R;
import com.alexandriacity.videtest.data.database.VideoEntry;
import com.alexandriacity.videtest.ui.main.MainActivity;
import com.bumptech.glide.Glide;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHoler> {

    private final Context mContext;
    private List<VideoEntry> mVideosList;

    VideoAdapter(@NonNull Context context) {
        mContext = context;
    }

    @NonNull
    @Override
    public VideoViewHoler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_video, parent, false);
        return new VideoViewHoler(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHoler holder, int position) {
        holder.bindData(position);
    }

    @Override
    public int getItemCount() {
        return mVideosList == null ? 0 : mVideosList.size();
    }

    /**
     * Swaps the list used by the CourseAdapter for its course list. This method is called by
     * {@link MainActivity} after a load has finished. When this method is called, we assume we have
     * a new set of data, so we call notifyDataSetChanged to tell the RecyclerView to update.
     *
     * @param newVideosList the new list of courses to use as CourseAdapter's data source
     */
    void setCourseList(final List<VideoEntry> newVideosList) {
        // If there was no course data, then recreate all of the list
        if (mVideosList == null) {
            mVideosList = newVideosList;
            notifyDataSetChanged();
        } else {
            /*
             * Otherwise we use DiffUtil to calculate the changes and update accordingly. This
             * shows the four methods you need to override to return a DiffUtil callback. The
             * old list is the current list stored in the course list, where the new list is the new
             * values passed in from the observing the database.
             */

            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return mVideosList.size();
                }

                @Override
                public int getNewListSize() {
                    return newVideosList.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return mVideosList.get(oldItemPosition).getNumber()
                            == newVideosList.get(newItemPosition).getNumber();
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    VideoEntry newVideo = newVideosList.get(newItemPosition);
                    VideoEntry oldVideo = mVideosList.get(oldItemPosition);
                    return newVideo.getName().equals(oldVideo.getName())
                            && newVideo.getImageUrl().equals(oldVideo.getImageUrl())
                            && newVideo.getLink().equals(oldVideo.getLink())
                            && newVideo.getDuration().equals(oldVideo.getDuration());
                }
            });
            mVideosList = newVideosList;
            result.dispatchUpdatesTo(this);
        }
    }

    class VideoViewHoler extends RecyclerView.ViewHolder implements View.OnClickListener {

        final ImageView imgVideoThumb;
        final TextView tvNumber;
        final TextView tvVideoTitle;
        final TextView tvDuration;

        VideoViewHoler(View itemView) {
            super(itemView);

            imgVideoThumb = itemView.findViewById(R.id.img_video_thumb);
            tvNumber = itemView.findViewById(R.id.tv_number);
            tvVideoTitle = itemView.findViewById(R.id.tv_video_title);
            tvDuration = itemView.findViewById(R.id.tv_duration);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            VideoEntry videoEntry = mVideosList.get(getAdapterPosition());
            String videoLink = videoEntry.getLink();
            if (!TextUtils.isEmpty(videoLink))
                mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(videoLink)));
        }

        private void bindData(int position) {
            bindVideoThumb(position);
            bindNumber(position);
            bindVideoTitle(position);
            bindDuration(position);
        }

        private void bindDuration(int position) {
            VideoEntry videoEntry = mVideosList.get(position);
            this.tvDuration.setText(videoEntry.getDuration());
        }

        private void bindVideoTitle(int position) {
            VideoEntry videoEntry = mVideosList.get(position);
            this.tvVideoTitle.setText(videoEntry.getName());
        }

        private void bindVideoThumb(int position) {
            VideoEntry videoEntry = mVideosList.get(position);
            Glide.with(mContext)
                    .load(videoEntry.getImageUrl())
                    .dontAnimate()
                    .into(this.imgVideoThumb);
        }

        private void bindNumber(int position) {
            VideoEntry videoEntry = mVideosList.get(position);
            this.tvNumber.setText(String.valueOf(videoEntry.getNumber()));
        }

    }
}
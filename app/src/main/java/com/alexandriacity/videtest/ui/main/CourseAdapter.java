package com.alexandriacity.videtest.ui.main;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alexandriacity.videtest.R;
import com.alexandriacity.videtest.data.database.CourseListItem;
import com.alexandriacity.videtest.ui.coursedetail.CourseDetailActivity;
import com.bumptech.glide.Glide;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHoler> {

    private final Context mContext;
    private List<CourseListItem> mCourseList;

    CourseAdapter(@NonNull Context context) {
        mContext = context;
    }

    @NonNull
    @Override
    public CourseViewHoler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_course, parent, false);
        return new CourseViewHoler(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewHoler holder, int position) {
        holder.bindData(position);
    }

    @Override
    public int getItemCount() {
        return mCourseList == null ? 0 : mCourseList.size();
    }

    /**
     * Swaps the list used by the CourseAdapter for its course list. This method is called by
     * {@link MainActivity} after a load has finished. When this method is called, we assume we have
     * a new set of data, so we call notifyDataSetChanged to tell the RecyclerView to update.
     *
     * @param newCourseList the new list of courses to use as CourseAdapter's data source
     */
    void setCourseList(final List<CourseListItem> newCourseList) {
        // If there was no course data, then recreate all of the list
        if (mCourseList == null) {
            mCourseList = newCourseList;
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
                    return mCourseList.size();
                }

                @Override
                public int getNewListSize() {
                    return newCourseList.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return mCourseList.get(oldItemPosition).getId()
                            == newCourseList.get(newItemPosition).getId();
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    CourseListItem newCourse = newCourseList.get(newItemPosition);
                    CourseListItem oldCourse = mCourseList.get(oldItemPosition);
                    return newCourse.getName().equals(oldCourse.getName())
                            && newCourse.getImageUrl().equals(oldCourse.getImageUrl())
                            && newCourse.getNumberOfViews() == oldCourse.getNumberOfViews();
                }
            });
            mCourseList = newCourseList;
            result.dispatchUpdatesTo(this);
        }
    }

    class CourseViewHoler extends RecyclerView.ViewHolder implements View.OnClickListener {

        final ImageView imgCourseThumb;
        final CircleImageView imgProfileThumb;
        final TextView tvCourseTitle;
        final TextView tvNumViews;

        CourseViewHoler(View itemView) {
            super(itemView);

            imgCourseThumb = itemView.findViewById(R.id.img_course_thumb);
            imgProfileThumb = itemView.findViewById(R.id.img_profile_thumb);
            tvCourseTitle = itemView.findViewById(R.id.tv_course_title);
            tvNumViews = itemView.findViewById(R.id.tv_num_views);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int courseId = mCourseList.get(getAdapterPosition()).getId();
            Intent intent = new Intent(mContext, CourseDetailActivity.class);
            intent.putExtra(CourseDetailActivity.EXTRA_ID, courseId);
            mContext.startActivity(intent);
        }

        private void bindData(int position) {
            bindCourseThumb(position);
            bindProfileThumb(position);
            bindCourseTitle(position);
            bindNumViews(position);
        }

        private void bindNumViews(int position) {
            CourseListItem courseListItem = mCourseList.get(position);
            this.tvNumViews.setText(mContext.getString(R.string.course_num_views, courseListItem.getNumberOfViews()));
        }

        private void bindCourseTitle(int position) {
            CourseListItem courseListItem = mCourseList.get(position);
            this.tvCourseTitle.setText(courseListItem.getName());
        }

        private void bindCourseThumb(int position) {
            CourseListItem courseListItem = mCourseList.get(position);
            Glide.with(mContext)
                    .load(courseListItem.getImageUrl())
                    .dontAnimate()
                    .into(this.imgCourseThumb);
        }

        private void bindProfileThumb(int position) {
            CourseListItem courseListItem = mCourseList.get(position);
            Glide.with(mContext)
                    .load(courseListItem.getImageUrl())
                    .dontAnimate()
                    .into(this.imgProfileThumb);
        }

    }
}
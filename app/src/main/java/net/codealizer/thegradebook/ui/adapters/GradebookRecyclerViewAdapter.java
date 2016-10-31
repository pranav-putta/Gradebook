package net.codealizer.thegradebook.ui.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.codealizer.thegradebook.R;
import net.codealizer.thegradebook.apis.ic.classbook.CourseDetail;

import java.util.ArrayList;

/**
 * Created by Pranav on 10/9/16.
 */

public class GradebookRecyclerViewAdapter extends RecyclerView.Adapter<GradebookRecyclerViewAdapter.ViewHolder> {

    private String mDataSet[];

    private Context mContext;
    private ArrayList<CourseDetail> mCourses;

    public GradebookRecyclerViewAdapter(Context ctx, ArrayList<CourseDetail> mCourses) {
        mContext = ctx;
        this.mCourses = mCourses;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_class, parent, false);

        return new ViewHolder(item);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CourseDetail course = mCourses.get(position);

        holder.course.setText(course.getName());
        holder.courseNumber.setText(course.getCourseNumber());
        holder.teacher.setText(course.getTeacherName());
        holder.card.setCardElevation(10);

        if (!course.isActive()) {
            holder.card.setEnabled(false);
            holder.card.setCardElevation(0);
            holder.course.setTextColor(mContext.getResources().getColor(R.color.md_grey_400));
            holder.courseNumber.setTextColor(mContext.getResources().getColor(R.color.md_grey_400));
            holder.teacher.setTextColor(mContext.getResources().getColor(R.color.md_grey_400));
        } else {
            holder.card.setEnabled(true);
            holder.card.setCardElevation(10);
            holder.course.setTextColor(Color.parseColor("#222222"));
            holder.courseNumber.setTextColor(Color.parseColor("#1565C0"));
            holder.teacher.setTextColor(mContext.getResources().getColor(R.color.md_grey_600));
        }
    }

    @Override
    public int getItemCount() {
        return mCourses.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        CardView card;
        TextView course, courseNumber, teacher;

        public ViewHolder(View itemView) {
            super(itemView);

            course = (TextView) itemView.findViewById(R.id.card_class_title);
            courseNumber = (TextView) itemView.findViewById(R.id.card_class_course_number);
            teacher = (TextView) itemView.findViewById(R.id.card_class_course_teacher);

            card = (CardView) itemView.findViewById(R.id.gradebook_card);

        }
    }

}

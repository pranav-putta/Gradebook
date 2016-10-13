package net.codealizer.thegradebook.ui.gradebook.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.codealizer.thegradebook.R;
import net.codealizer.thegradebook.apis.ic.calendar.Term;
import net.codealizer.thegradebook.apis.ic.classbook.Classbook;
import net.codealizer.thegradebook.apis.ic.classbook.ClassbookActivity;
import net.codealizer.thegradebook.apis.ic.classbook.ClassbookGroup;
import net.codealizer.thegradebook.apis.ic.classbook.ClassbookTask;
import net.codealizer.thegradebook.listeners.OnClassbookClickListener;
import net.codealizer.thegradebook.ui.dialogs.Alert;
import net.codealizer.thegradebook.ui.gradebook.cards.BasicGradeDetail;
import net.codealizer.thegradebook.ui.gradebook.cards.BasicTermDetail;

import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by Pranav on 10/10/16.
 */

public class GradesEbrRecyclerViewAdapter extends RecyclerView.Adapter<GradesEbrRecyclerViewAdapter.ViewHolder> {

    Context mContext;
    private ArrayList<ClassbookTask> task;
    private boolean isEBR;

    private OnClassbookClickListener mOnClickListener;

    public GradesEbrRecyclerViewAdapter(Context mContext, ArrayList<ClassbookTask> task, OnClassbookClickListener clickListener) {
        this.mContext = mContext;
        this.task = task;
        this.mOnClickListener = clickListener;
    }

    @Override
    public GradesEbrRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_simple, parent, false);

        return new GradesEbrRecyclerViewAdapter.ViewHolder(item);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final ClassbookTask group = task.get(position);

        holder.title.setText(group.name);

        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnClickListener.onClassbookClicked(group);
            }
        });
    }

    @Override
    public int getItemCount() {
        return task.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout card;
        TextView title;

        public ViewHolder(View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.card_simple_title);

            card = (RelativeLayout) itemView.findViewById(R.id.card_simple);
        }
    }
}

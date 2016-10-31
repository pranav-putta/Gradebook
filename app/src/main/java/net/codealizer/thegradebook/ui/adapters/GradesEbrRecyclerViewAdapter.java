package net.codealizer.thegradebook.ui.adapters;

import android.content.Context;
import android.os.Vibrator;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.codealizer.thegradebook.R;
import net.codealizer.thegradebook.apis.ic.classbook.ClassbookTask;
import net.codealizer.thegradebook.listeners.OnClassbookClickListener;

import java.util.ArrayList;

/**
 * Created by Pranav on 10/10/16.
 */

public class GradesEbrRecyclerViewAdapter extends RecyclerView.Adapter<GradesEbrRecyclerViewAdapter.ViewHolder>
{

	Context mContext;
	private ArrayList<ClassbookTask> task;
	private boolean isEBR;

	private OnClassbookClickListener mOnClickListener;

	public GradesEbrRecyclerViewAdapter(Context mContext, ArrayList<ClassbookTask> task, OnClassbookClickListener clickListener)
	{
		this.mContext = mContext;
		this.task = task;
		this.mOnClickListener = clickListener;
	}

	@Override
	public GradesEbrRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
	{

		View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_simple, parent, false);

		return new GradesEbrRecyclerViewAdapter.ViewHolder(item);
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position)
	{
		final ClassbookTask group = task.get(position);
		final Vibrator v = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);

		holder.title.setText(group.name);

		holder.card.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				mOnClickListener.onClassbookClicked(group);
				v.vibrate(100);
			}
		});
	}

	@Override
	public int getItemCount()
	{
		return task.size();
	}

	public static class ViewHolder extends RecyclerView.ViewHolder
	{

		RelativeLayout card;
		TextView title;

		public ViewHolder(View itemView)
		{
			super(itemView);

			title = (TextView) itemView.findViewById(R.id.card_simple_title);

			card = (RelativeLayout) itemView.findViewById(R.id.card_simple);
		}
	}
}

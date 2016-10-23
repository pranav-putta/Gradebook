package net.codealizer.thegradebook.ui.dialogs;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.codealizer.thegradebook.R;

import java.util.List;

/**
 * Created by Pranav on 10/10/16.
 */

public class ProgressInformationDialogAdapter extends RecyclerView.Adapter<ProgressInformationDialogAdapter.ViewHolder>
{

	List<String> headers;
	List<String> values;

	private Context mContext;

	public ProgressInformationDialogAdapter(Context ctx, List<String> header, List<String> values)
	{
		this.headers = header;
		this.values = values;
		this.mContext = ctx;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
	{
		View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_dialog_progress_information, parent, false);
		return new ViewHolder(item);
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position)
	{
		holder.header.setText(headers.get(position));
		holder.value.setText(values.get(position));
	}

	@Override
	public int getItemCount()
	{
		return headers.size();
	}

	public static class ViewHolder extends RecyclerView.ViewHolder
	{

		TextView header, value;

		public ViewHolder(View itemView)
		{
			super(itemView);

			header = (TextView) itemView.findViewById(R.id.card_dialog_progress_information_header);
			value = (TextView) itemView.findViewById(R.id.card_dialog_progress_information_value);
		}
	}

}

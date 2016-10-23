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

public class ThemeDialogAdapter extends RecyclerView.Adapter<ThemeDialogAdapter.ViewHolder>
{

	private Context mContext;

	private List<Integer> mColors;
	private List<String> colorNames;

	public ThemeDialogAdapter(Context ctx, List<Integer> colors, List<String> colorNames)
	{
		this.mColors = colors;
		this.colorNames = colorNames;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
	{
		View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.color_list_item, parent, false);
		return new ViewHolder(item);
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position)
	{
		holder.header.setBackgroundColor(mColors.get(position));
		holder.value.setText(colorNames.get(position));
	}

	@Override
	public int getItemCount()
	{
		return mColors.size();
	}

	public static class ViewHolder extends RecyclerView.ViewHolder
	{

		TextView value;
		View header;

		public ViewHolder(View itemView)
		{
			super(itemView);

			header = (View) itemView.findViewById(R.id.color);
			value = (TextView) itemView.findViewById(R.id.color_title);
		}
	}

}

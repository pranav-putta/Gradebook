package net.codealizer.thegradebook.ui.adapters;

import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ViewHolder.ChildViewHolder;

import net.codealizer.thegradebook.R;
import net.codealizer.thegradebook.assets.BasicTermDetail;
import net.codealizer.thegradebook.ui.dialogs.Alert;

/**
 * Created by Pranav on 10/10/16.
 */

public class TermCardChildViewHolder extends ChildViewHolder
{

	public TextView title;
	public TextView score;

	public RelativeLayout card;

	public TermCardChildViewHolder(View itemView)
	{
		super(itemView);

		title = (TextView) itemView.findViewById(R.id.card_term_child_title);
		score = (TextView) itemView.findViewById(R.id.card_term_child_score);
		card = (RelativeLayout) itemView.findViewById(R.id.card_term_child);
	}

	public void bind(final BasicTermDetail detail, final Context context)
	{
		title.setText(detail.getTitle());
		score.setText(detail.getScore());

		card.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				Alert.showProgressInformation(detail, context);

			}
		});
	}
}

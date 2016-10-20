package net.codealizer.thegradebook.ui.gradebook.cards;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ViewHolder.ParentViewHolder;

import net.codealizer.thegradebook.R;
import net.codealizer.thegradebook.listeners.OnTermSelectedListener;
import net.codealizer.thegradebook.ui.gradebook.GradesActivity;

/**
 * Created by Pranav on 10/10/16.
 */

public class TermCardParentViewHolder extends ParentViewHolder {

    public LinearLayout card;
    public TextView title, date, grade;
    public Button moreInfo, assignments;
    public OnTermSelectedListener c;


    public TermCardParentViewHolder(View itemView, OnTermSelectedListener c) {
        super(itemView);

        this.c = c;
        title = (TextView) itemView.findViewById(R.id.card_term_title);
        date = (TextView) itemView.findViewById(R.id.card_term_date_range);
        grade = (TextView) itemView.findViewById(R.id.card_term_grade);
        moreInfo = (Button) itemView.findViewById(R.id.card_term_more_info);
        assignments = (Button) itemView.findViewById(R.id.card_term_assignments);
        card = (LinearLayout) itemView.findViewById(R.id.term_card);

        moreInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isExpanded()) {
                    collapseView();
                } else {
                    expandView();
                }
            }
        });

        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               assignments.callOnClick();
            }
        });


    }

    @Override
    public boolean shouldItemViewClickToggleExpansion() {
        return false;
    }

    public void bind(final BasicTerm term) {
        title.setText(term.getTitle());
        date.setText(term.getDates());
        grade.setText(term.getGrade());

        assignments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                c.onTermSelected(term.getTerm());
            }
        });
    }
}

package net.codealizer.thegradebook.ui.adapters;

import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ViewHolder.ParentViewHolder;

import net.codealizer.thegradebook.R;
import net.codealizer.thegradebook.assets.BasicTerm;
import net.codealizer.thegradebook.listeners.OnTermSelectedListener;

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
        bind(term, true);
    }

    public void bind(final BasicTerm term, boolean enabled) {
        title.setText(term.getTitle());
        date.setText(term.getDates());
        grade.setText(term.getGrade());

        assignments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                c.onTermSelected(term.getTask());
            }
        });

        if (term.getTask().groups.size() > 0) {
            assignments.setEnabled(true);
            assignments.setTextColor(Color.parseColor("#0D47A1"));
        } else {
            assignments.setTextColor(Color.parseColor("#616161"));

            assignments.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    c.onTermUnavailable();
                }
            });
        }

        if (term.getChildItemList() != null && term.getChildItemList().size() > 1) {
            moreInfo.setEnabled(true);
            moreInfo.setTextColor(Color.parseColor("#1565C0"));
        } else {
            moreInfo.setEnabled(false);
            moreInfo.setTextColor(Color.parseColor("#616161"));
        }
    }
}

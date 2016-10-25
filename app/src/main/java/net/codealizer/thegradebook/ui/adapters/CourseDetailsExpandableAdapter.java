package net.codealizer.thegradebook.ui.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;

import net.codealizer.thegradebook.R;
import net.codealizer.thegradebook.assets.BasicTerm;
import net.codealizer.thegradebook.assets.BasicTermDetail;
import net.codealizer.thegradebook.listeners.OnTermSelectedListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pranav on 10/10/16.
 */

public class CourseDetailsExpandableAdapter extends ExpandableRecyclerAdapter<TermCardParentViewHolder, TermCardChildViewHolder> {

    private LayoutInflater mInflater;
    private Context mContext;
    private OnTermSelectedListener mListener;
    private ArrayList<BasicTerm> parentListItems;

    /**
     * Primary constructor. Sets up {@link #mParentItemList} and {@link #mItemList}.
     * <p>
     * Changes to {@link #mParentItemList} should be made through add/remove methods in
     * {@link ExpandableRecyclerAdapter}
     *
     * @param parentItemList List of all {@link ParentListItem} objects to be
     *                       displayed in the RecyclerView that this
     *                       adapter is linked to
     */
    public CourseDetailsExpandableAdapter(Context context, @NonNull ArrayList<BasicTerm> parentItemList, OnTermSelectedListener c) {
        super(parentItemList);
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mListener = c;
        this.parentListItems = parentItemList;
    }

    @Override
    public TermCardParentViewHolder onCreateParentViewHolder(ViewGroup parentViewGroup) {
        View v = mInflater.inflate(R.layout.card_term_parent, parentViewGroup, false);
        return new TermCardParentViewHolder(v, mListener);
    }

    @Override
    public TermCardChildViewHolder onCreateChildViewHolder(ViewGroup childViewGroup) {
        View v = mInflater.inflate(R.layout.card_term_child_item, childViewGroup, false);
        return new TermCardChildViewHolder(v);
    }

    @Override
    public void onBindParentViewHolder(TermCardParentViewHolder parentViewHolder, int position, ParentListItem parentListItem) {
        BasicTerm term = (BasicTerm) parentListItem;
        if (parentListItems.get(position).getChildItemList().size() > 0) {
            parentViewHolder.bind(term);
        } else {
            parentViewHolder.bind(term, false);
        }
    }

    @Override
    public void onBindChildViewHolder(TermCardChildViewHolder childViewHolder, int position, Object childListItem) {
        BasicTermDetail term = (BasicTermDetail) childListItem;
        childViewHolder.bind(term, mContext);
    }

}
package net.codealizer.thegradebook.ui.gradebook.cards;

import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;

import net.codealizer.thegradebook.apis.ic.calendar.Term;

import java.util.List;

/**
 * Created by Pranav on 10/10/16.
 */

public class BasicTerm implements ParentListItem {

    private List mChildrenList;

    private String title;
    private String dates;
    private String grade;

    private Term mTerm;

    public BasicTerm(String title, String dates, String grade, List mChildrenList, Term t) {
        this.title = title;
        this.dates = dates;
        this.grade = grade;
        this.mChildrenList = mChildrenList;
        this.mTerm = t;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDates() {
        return dates;
    }

    public void setDates(String dates) {
        this.dates = dates;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }


    @Override
    public List<?> getChildItemList() {
        return mChildrenList;
    }

    public Term getTerm() {
        return mTerm;
    }

    public void setTerm(Term mTerm) {
        this.mTerm = mTerm;
    }

    @Override
    public boolean isInitiallyExpanded() {
        return false;
    }
}

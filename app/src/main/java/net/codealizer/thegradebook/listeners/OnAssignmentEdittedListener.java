package net.codealizer.thegradebook.listeners;

import net.codealizer.thegradebook.assets.BasicClassbookActivity;

/**
 * Created by Pranav on 10/12/16.
 */

public interface OnAssignmentEdittedListener
{

	void onAssignmentEditted(BasicClassbookActivity activity, int index, int masterIndex);

	void onAssignmentDeleted(BasicClassbookActivity activity, int index);
}

package net.codealizer.thegradebook.listeners;

import net.codealizer.thegradebook.apis.ic.student.Student;

/**
 * Created by Pranav on 10/9/16.
 */

public interface OnStudentInformationRetrievedListener extends OnICActionListener
{

	void onStudentInformationRetrieved(Student student);

}

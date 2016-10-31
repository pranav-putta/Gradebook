package net.codealizer.thegradebook.ui.classbook.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.codealizer.thegradebook.R;

public class SimpleFragment extends Fragment
{
	public static final String ID_BUNDLE = "bundle";
	public static final String KEY_DATE = "net.codealizer.thegradebook.ui.gradebook.cards.KEY_DATE";
	public static final String KEY_TASK_CLASS = "net.codealizer.thegradebook.ui.gradebook.cards.KEY_TASK_CLASS";
	int id;
	String text;
	String date;

	@SuppressLint("ValidFragment")
	public SimpleFragment()
	{
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{

		// Inflate the layout for this fragment
		View v = inflater.inflate(R.layout.dialog_update_step, container, false);
		FragmentActivity mAct = getActivity();

		TextView img = (TextView) v.findViewById(R.id.dialog_updates_message);

		text = getArguments().getString(KEY_TASK_CLASS);
		img.setText(text);

		return v;
	}

}
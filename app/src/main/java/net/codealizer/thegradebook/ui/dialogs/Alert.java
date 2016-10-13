package net.codealizer.thegradebook.ui.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import net.codealizer.thegradebook.R;
import net.codealizer.thegradebook.apis.ic.calendar.Term;
import net.codealizer.thegradebook.apis.ic.classbook.ClassbookGroup;
import net.codealizer.thegradebook.apis.ic.classbook.ClassbookTask;
import net.codealizer.thegradebook.data.Data;
import net.codealizer.thegradebook.listeners.OnAssignmentAddedListener;
import net.codealizer.thegradebook.listeners.OnAssignmentEdittedListener;
import net.codealizer.thegradebook.ui.gradebook.cards.BasicClassbookActivity;
import net.codealizer.thegradebook.ui.gradebook.cards.BasicGradeDetail;
import net.codealizer.thegradebook.ui.gradebook.cards.BasicTerm;
import net.codealizer.thegradebook.ui.gradebook.cards.BasicTermDetail;

import org.apache.commons.lang3.text.WordUtils;

import java.util.ArrayList;

/**
 * Created by Pranav on 10/8/16.
 */

public class Alert {

    public static void showNetworkErrorDialog(final Context ctx) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        builder.setTitle("Network Error");
        builder.setMessage("Make sure that you are connected to the Internet");
        builder.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ctx.startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
            }
        });
        builder.setNegativeButton("OK", null);
        builder.create().show();
    }

    public static void showInvalidPasswordDialog(Context ctx) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        builder.setTitle("Invalid Password");
        builder.setMessage("Your username and password do not seem to be valid, please contact" +
                "your school's administration.");
        builder.setPositiveButton("Ok", null);
        builder.create().show();
    }

    public static void showInvalidNameDialog(Context ctx) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        builder.setTitle("Invalid Name");
        builder.setMessage("This assignment name has already been used!");
        builder.setPositiveButton("Ok", null);
        builder.create().show();
    }

    public static void showLogoutConfirmationDialog(final Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Logout");
        builder.setMessage("Are you sure you want to logout?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Data.logout(activity);
            }
        });
        builder.setNegativeButton("No", null);
        builder.create().show();
    }

    public static void showEmptyClassDialog(final Context ctx) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        builder.setTitle("Empty Class");
        builder.setMessage("Your teacher has not posted any grades for this class.");
        builder.setPositiveButton("OK", null);
        builder.setNegativeButton("Cancel", null);
        builder.create().show();
    }

    public static void showGradeInactiveDialog(final Context ctx) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        builder.setTitle("Inactive");
        builder.setMessage("Your teacher has not marked this grade active yet.");
        builder.setPositiveButton("OK", null);
        builder.setNegativeButton("Cancel", null);
        builder.create().show();
    }

    public static void showProgressInformation(BasicTermDetail detail, Context ctx) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(ctx);
        dialog.setTitle(detail.getTitle());
        View v = LayoutInflater.from(ctx).inflate(R.layout.dialog_progress_information, null);
        dialog.setPositiveButton("Ok", null);

        if (!detail.getScore().equals("-")) {
            dialog.setView(v);

            RecyclerView r = (RecyclerView) v.findViewById(R.id.dialog_progress_information_list);
            r.setHasFixedSize(true);
            r.setLayoutManager(new LinearLayoutManager(ctx));
            r.setAdapter(new ProgressInformationDialogAdapter(ctx, detail.getHeaders(), detail.getValues()));

        } else {
            dialog.setMessage("No grades have been posted here");
        }

        dialog.create().show();
    }

    public static void showEditAssignmentScoreDialog(final ClassbookTask task, final BasicClassbookActivity a, final Context context, final OnAssignmentEdittedListener listener, final int index, final int masterIndex) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Custom Assignment");

        View v = LayoutInflater.from(context).inflate(R.layout.dialog_edit_assignment, null);

        builder.setView(v);
        final TextView score = (TextView) v.findViewById(R.id.card_edit_assignment_score);
        final TextView title = (TextView) v.findViewById(R.id.card_edit_assignment_title);
        final SeekBar progress = (SeekBar) v.findViewById(R.id.card_edit_assignment_seek_bar);

        score.setText(a.getPoints() + "/" + a.getTotalPoints());
        title.setText(a.getTitle());

        progress.setMax(a.getTotalPoints());
        progress.setProgress(a.getPoints());

        progress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int p, boolean b) {
                a.setPoints(p);
                score.setText(a.getPoints() + "/" + a.getTotalPoints());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                listener.onAssignmentEditted(a, index, masterIndex);
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.setNeutralButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                listener.onAssignmentDeleted(a, index);
            }
        });

        builder.create().show();
    }

    public static void showEditAssignmentDialog(final ClassbookTask t, final BasicClassbookActivity a, Context context, final OnAssignmentEdittedListener listener, final int index, final int masterIndex) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Edit Assignment");

        View v = LayoutInflater.from(context).inflate(R.layout.dialog_new_assignment, null);

        builder.setView(v);
        builder.setView(v);
        final TextInputLayout titleEditText = (TextInputLayout) v.findViewById(R.id.dialog_new_assignment_title);
        final TextInputLayout pointsEditText = (TextInputLayout) v.findViewById(R.id.dialog_new_assignment_points);
        final RadioGroup categories = (RadioGroup) v.findViewById(R.id.dialog_new_assignment_cateogry);

        final ArrayList<Integer> ids = new ArrayList<>();
        for (ClassbookGroup group : t.groups) {
            RadioButton button = new RadioButton(context);
            button.setText(group.name + " (Weight: " + group.weight + ")");
            ids.add(button.getId());
            categories.addView(button);
        }

        if (t.groups.size() > 0) {
            ((RadioButton) categories.getChildAt(a.getGroupID())).setChecked(true);
        }

        titleEditText.getEditText().setText(a.getTitle());
        pointsEditText.getEditText().setText(String.valueOf(a.getTotalPoints()));

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                a.setTitle(titleEditText.getEditText().getText().toString());
                int points = a.getPoints();
                int newPoints = Integer.parseInt(pointsEditText.getEditText().getText().toString());

                if (points > newPoints) {
                    points = newPoints;
                }
                a.setPoints(points);
                a.setGroupID(categories.indexOfChild(categories.findViewById(categories.getCheckedRadioButtonId())));
                a.setTotalPoints(Integer.parseInt(pointsEditText.getEditText().getText().toString()));
                listener.onAssignmentEditted(a, index, masterIndex);
            }
        });
        builder.setNeutralButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                listener.onAssignmentDeleted(a, index);
            }
        });
        builder.setNegativeButton("Cancel", null);

        builder.create().show();
    }

    public static void showNewAssignmentDialog(Term term, Context ctx, OnAssignmentAddedListener a) {
        showNewAssignmentDialog(term.getTask(), ctx, a);
    }

    public static void showNewAssignmentDialog(ClassbookTask t, Context ctx, final OnAssignmentAddedListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        builder.setTitle("New Assignment");

        View v = LayoutInflater.from(ctx).inflate(R.layout.dialog_new_assignment, null);

        builder.setView(v);
        final TextInputLayout titleEditText = (TextInputLayout) v.findViewById(R.id.dialog_new_assignment_title);
        final TextInputLayout pointsEditText = (TextInputLayout) v.findViewById(R.id.dialog_new_assignment_points);
        final RadioGroup categories = (RadioGroup) v.findViewById(R.id.dialog_new_assignment_cateogry);

        final ArrayList<Integer> ids = new ArrayList<>();
        for (ClassbookGroup group : t.groups) {
            RadioButton button = new RadioButton(ctx);
            button.setText(group.name + " (Weight: " + group.weight + ")");
            ids.add(button.getId());
            categories.addView(button);
        }

        if (t.groups.size() > 0) {
            ((RadioButton) categories.getChildAt(0)).setChecked(true);
        }

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String title = WordUtils.capitalize(titleEditText.getEditText().getText().toString());
                int points = Integer.parseInt(pointsEditText.getEditText().getText().toString());
                int groupID = categories.indexOfChild(categories.findViewById(categories.getCheckedRadioButtonId()));
                BasicClassbookActivity a = new BasicClassbookActivity(title, points, points, groupID);
                listener.onAssignmentAdded(a);
            }
        });
        builder.setNegativeButton("Cancel", null);

        builder.create().show();
    }


    public static void showProgressInformation(BasicGradeDetail detail, Context ctx) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(ctx);
        dialog.setTitle(detail.getName());
        View v = LayoutInflater.from(ctx).inflate(R.layout.dialog_progress_information, null);
        dialog.setPositiveButton("Ok", null);

        if (!detail.getScore().equals("-")) {
            dialog.setView(v);

            RecyclerView r = (RecyclerView) v.findViewById(R.id.dialog_progress_information_list);
            r.setHasFixedSize(true);
            r.setLayoutManager(new LinearLayoutManager(ctx));
            r.setAdapter(new ProgressInformationDialogAdapter(ctx, detail.getHeaders(), detail.getValues()));

        } else {
            dialog.setMessage("No grades have been posted here");
        }

        dialog.create().show();
    }
}

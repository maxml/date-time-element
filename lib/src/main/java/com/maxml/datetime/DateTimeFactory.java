package com.maxml.datetime;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.maxml.datetime.dialog.TimePickerDialog;
import com.maxml.datetime.fragment.DatePickerFragment;
import com.maxml.datetime.fragment.TimePickerFragment;
import com.maxml.datetime.util.DateFormatter;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class DateTimeFactory {

    private static final int DIALOG_DATE = 0;
    private static final int DIALOG_TIME = 1;
    private static final int DIALOG_EXIT = 2;

    private AppCompatActivity activity;
    // TODO: refactor
    private TextView dateView;
    private Calendar calendar;

    public DateTimeFactory(AppCompatActivity activity, TextView dateView) {
        this.activity = activity;
        this.dateView = dateView;

        if (activity.getString(R.string.main_no_date).equals(dateView.getText())) {
            calendar = GregorianCalendar.getInstance(TimeZone.getTimeZone("UTC"));
        } else {
            calendar = DateFormatter.toUiCalendar(dateView.getText().toString());
        }
    }

    public void start() {
        chooseDialog();
    }

    // TODO: delete lib -> move to other flavour
    public void chooseDialog() {
        new MaterialDialog.Builder(activity)
                .title(R.string.dialog_datetime_title)
                .items(R.array.dialog_array_exit)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                        dialog.dismiss();

                        switch (position) {
                            case DIALOG_DATE:
//                                getDateFragment();
                                getDateDialog();
                                break;
                            case DIALOG_TIME:
//                                getTimeFragment();
//                                getTimeDialog();
                                getCustomTimeDialog();
                                break;
                            case DIALOG_EXIT:
                                dialog.dismiss();
                                break;
                        }
                    }
                }).show();
    }

    private void getTimeFragment() {
        TimePickerFragment newFragment = new TimePickerFragment();
        newFragment.setInfo(this, dateView, calendar);
        newFragment.show(activity.getSupportFragmentManager(), "time-picker");
    }

    private void getTimeDialog() {
        TimePickerDialog tpd = new TimePickerDialog(activity, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePickerDialog.TimePicker view, int hourOfDay, int minute, int seconds) {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                calendar.set(Calendar.SECOND, seconds);

                dateView.setText(DateFormatter.toUiString(calendar.getTime()));

                chooseDialog();
            }
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND), true);
        tpd.show();
    }

    private void getDateDialog() {
        DatePickerDialog tpd = new DatePickerDialog(activity, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {

                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                dateView.setText(DateFormatter.toUiString(calendar.getTime()));

                chooseDialog();
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        tpd.show();
    }

    private void getDateFragment() {
        DatePickerFragment newFragment = new DatePickerFragment();
        newFragment.setInfo(this, dateView, calendar);
        newFragment.show(activity.getSupportFragmentManager(), "date-picker");
    }

    private void getCustomTimeDialog() {
        Calendar now = Calendar.getInstance();
        TimePickerDialog mTimePicker = new TimePickerDialog(activity, new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePickerDialog.TimePicker view, int hourOfDay,
                                  int minute, int seconds) {

                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                calendar.set(Calendar.SECOND, seconds);

                dateView.setText(DateFormatter.toUiString(calendar.getTime()));

                chooseDialog();
            }
        }, now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE), now.get(Calendar.SECOND), true);
        mTimePicker.show();
    }
}

package com.maxml.datetime.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.TextView;

import com.maxml.datetime.DateTimeFactory;
import com.maxml.datetime.dialog.TimePickerDialog;
import com.maxml.datetime.util.DateFormatter;

import java.util.Calendar;

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    private DateTimeFactory dialog;
    private TextView dateView;
    private Calendar calendar;

    public void setInfo(DateTimeFactory dialog, TextView dateView, Calendar calendar) {
        this.dialog = dialog;
        this.dateView = dateView;
        this.calendar = calendar;

        // TODO: Fragment#getBundle
//        // Creating a bundle object to pass currently set time to the fragment
//        Bundle b = getArguments();
//
//        mHour = b.getInt("set_hour");
//
//        mMinute = b.getInt("set_minute");
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int hours = c.get(Calendar.HOUR_OF_DAY);
        int minutes = c.get(Calendar.MINUTE);
        int seconds = c.get(Calendar.SECOND);

        return new TimePickerDialog(getActivity(), this, hours, minutes, seconds, true);
    }

    @Override
    public void onTimeSet(TimePickerDialog.TimePicker view, int hourOfDay, int minute, int seconds) {
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, seconds);

        dateView.setText(DateFormatter.toUiString(calendar.getTime()));

        // TODO: wtf public method
        dialog.chooseDialog();
    }
}

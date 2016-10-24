package com.maxml.datetime.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;
import android.widget.TextView;

import com.maxml.datetime.DateTimeFactory;
import com.maxml.datetime.util.DateFormatter;

import java.util.Calendar;

// TODO: in wiki
public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    private DateTimeFactory dialog;
    private TextView dateView;
    private Calendar calendar;

    // TODO: Fragment#getBundle
    public void setInfo(DateTimeFactory dialog, TextView dateView, Calendar calendar) {
        this.dialog = dialog;
        this.dateView = dateView;
        this.calendar = calendar;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        // Do something with the date chosen by the user
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);

        dateView.setText(DateFormatter.toUiString(calendar.getTime()));

        // TODO: wtf public method
        dialog.chooseDialog();
    }


}

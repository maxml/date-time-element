package com.maxml.datetime.dialog;
/*
 * Copyright (C) 2007 The Android Open Source Project
 * Copyright (C) 2013 Ivan Kovac  navratnanos@gmail.com
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.NumberPicker;

import com.maxml.datetime.R;

import java.text.DateFormatSymbols;
import java.util.Calendar;


/**
 * A dialog that prompts the user for the time of day using a {@link TimePicker}.
 */
public class TimePickerDialog extends AlertDialog implements OnClickListener {

    /**
     * The callback interface used to indicate the user is done filling in
     * the time (they clicked on the 'Set' button).
     */
    public interface OnTimeSetListener {

        /**
         * @param view      The view associated with this listener.
         * @param hourOfDay The hour that was set.
         * @param minute    The minute that was set.
         */
        void onTimeSet(TimePicker view, int hourOfDay, int minute, int seconds);
    }

    /**
     * The callback interface used to indicate the time has been adjusted.
     */
    public interface OnTimeChangedListener {

        /**
         * @param view      The view associated with this listener.
         * @param hourOfDay The current hour.
         * @param minute    The current minute.
         * @param seconds   The current second.
         */
        void onTimeChanged(TimePicker view, int hourOfDay, int minute, int seconds);
    }

    private static final String HOUR = "hour";
    private static final String MINUTE = "minute";
    private static final String SECONDS = "seconds";
    private static final String IS_24_HOUR = "is24hour";

    private final TimePicker mTimePicker;
    private final OnTimeSetListener mCallback;
    private final Calendar mCalendar;
    private final java.text.DateFormat mDateFormat;

    private int mInitialHourOfDay;
    private int mInitialMinute;
    private int mInitialSeconds;
    private boolean mIs24HourView;

    /**
     * A no-op callback used in the constructor to avoid null checks
     * later in the code.
     */
    private static OnTimeChangedListener NO_OP_CHANGE_LISTENER = new OnTimeChangedListener() {
        public void onTimeChanged(TimePicker view, int hourOfDay, int minute, int seconds) {
        }
    };

    /**
     * @param context      Parent.
     * @param callBack     How parent is notified.
     * @param hourOfDay    The initial hour.
     * @param minute       The initial minute.
     * @param is24HourView Whether this is a 24 hour view, or AM/PM.
     */
    public TimePickerDialog(Context context,
                            OnTimeSetListener callBack,
                            int hourOfDay, int minute, int seconds, boolean is24HourView) {

        this(context, 0,
                callBack, hourOfDay, minute, seconds, is24HourView);
    }

    /**
     * @param context      Parent.
     * @param theme        the theme to apply to this dialog
     * @param callBack     How parent is notified.
     * @param hourOfDay    The initial hour.
     * @param minute       The initial minute.
     * @param is24HourView Whether this is a 24 hour view, or AM/PM.
     */
    public TimePickerDialog(Context context,
                            int theme,
                            OnTimeSetListener callBack,
                            int hourOfDay, int minute, int seconds, boolean is24HourView) {
        super(context, theme);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        mCallback = callBack;
        mInitialHourOfDay = hourOfDay;
        mInitialMinute = minute;
        mInitialSeconds = seconds;
        mIs24HourView = is24HourView;

        mDateFormat = DateFormat.getTimeFormat(context);
        mCalendar = Calendar.getInstance();
        updateTitle(mInitialHourOfDay, mInitialMinute, mInitialSeconds);

        setButton(context.getText(R.string.time_set), this);
        setButton2(context.getText(R.string.cancel), (OnClickListener) null);
        //setIcon(android.R.drawable.ic_dialog_time);

        LayoutInflater inflater =
                (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.time_picker_dialog, null);
        setView(view);
        mTimePicker = (TimePicker) view.findViewById(R.id.timePicker);

        // initialize state
        mTimePicker.setCurrentHour(mInitialHourOfDay);
        mTimePicker.setCurrentMinute(mInitialMinute);
        mTimePicker.setCurrentSecond(mInitialSeconds);
        mTimePicker.setIs24HourView(mIs24HourView);
        mTimePicker.setOnTimeChangedListener(NO_OP_CHANGE_LISTENER);
    }

    public void onClick(DialogInterface dialog, int which) {
        if (mCallback != null) {
            mTimePicker.clearFocus();
            mCallback.onTimeSet(mTimePicker, mTimePicker.getCurrentHour(),
                    mTimePicker.getCurrentMinute(), mTimePicker.getCurrentSeconds());
        }
    }

    public void onTimeChanged(TimePicker view, int hourOfDay, int minute, int seconds) {
        updateTitle(hourOfDay, minute, seconds);
    }

    public void updateTime(int hourOfDay, int minutOfHour, int seconds) {
        mTimePicker.setCurrentHour(hourOfDay);
        mTimePicker.setCurrentMinute(minutOfHour);
        mTimePicker.setCurrentSecond(seconds);
    }

    private void updateTitle(int hour, int minute, int seconds) {
        mCalendar.set(Calendar.HOUR_OF_DAY, hour);
        mCalendar.set(Calendar.MINUTE, minute);
        mCalendar.set(Calendar.SECOND, seconds);
        setTitle(mDateFormat.format(mCalendar.getTime()) + ":" + String.format("%02d", seconds));
    }

    @Override
    public Bundle onSaveInstanceState() {
        Bundle state = super.onSaveInstanceState();
        state.putInt(HOUR, mTimePicker.getCurrentHour());
        state.putInt(MINUTE, mTimePicker.getCurrentMinute());
        state.putInt(SECONDS, mTimePicker.getCurrentSeconds());
        state.putBoolean(IS_24_HOUR, mTimePicker.is24HourView());
        return state;
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        int hour = savedInstanceState.getInt(HOUR);
        int minute = savedInstanceState.getInt(MINUTE);
        int seconds = savedInstanceState.getInt(SECONDS);
        mTimePicker.setCurrentHour(hour);
        mTimePicker.setCurrentMinute(minute);
        mTimePicker.setCurrentSecond(seconds);
        mTimePicker.setIs24HourView(savedInstanceState.getBoolean(IS_24_HOUR));
        mTimePicker.setOnTimeChangedListener(NO_OP_CHANGE_LISTENER);
        updateTitle(hour, minute, seconds);
    }

    /**
     * A view for selecting the time of day, in either 24 hour or AM/PM mode.
     * <p>
     * The hour, each minute digit, each seconds digit, and AM/PM (if applicable) can be conrolled by
     * vertical spinners.
     * <p>
     * The hour can be entered by keyboard input.  Entering in two digit hours
     * can be accomplished by hitting two digits within a timeout of about a
     * second (e.g. '1' then '2' to select 12).
     * <p>
     * The minutes can be entered by entering single digits.
     * The seconds can be entered by entering single digits.
     * <p>
     * Under AM/PM mode, the user can hit 'a', 'A", 'p' or 'P' to pick.
     * <p>
     * For a dialog using this view, see {@link android.app.TimePickerDialog}.
     */
    public static class TimePicker extends FrameLayout {

        public NumberPicker.Formatter TWO_DIGIT_FORMATTER =
                new NumberPicker.Formatter() {

                    @Override
                    public String format(int value) {
                        return String.format("%02d", value);
                    }
                };

        // state
        private int mCurrentHour = 0; // 0-23
        private int mCurrentMinute = 0; // 0-59
        private int mCurrentSeconds = 0; // 0-59
        private Boolean mIs24HourView = false;
        private boolean mIsAm;

        // ui components
        private final NumberPicker mHourPicker;
        private final NumberPicker mMinutePicker;
        private final NumberPicker mSecondPicker;
        private final Button mAmPmButton;
        private final String mAmText;
        private final String mPmText;

        // callbacks
        private OnTimeChangedListener mOnTimeChangedListener;

        public TimePicker(Context context) {
            this(context, null);
        }

        public TimePicker(Context context, AttributeSet attrs) {
            this(context, attrs, 0);
        }

        public TimePicker(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);

            LayoutInflater inflater =
                    (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            inflater.inflate(R.layout.time_picker_widget,
                    this, // we are the parent
                    true);

            // hour
            mHourPicker = (NumberPicker) findViewById(R.id.hour);
            mHourPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {

                @Override
                public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                    mCurrentHour = newVal;
                    if (!mIs24HourView) {
                        // adjust from [1-12] to [0-11] internally, with the times
                        // written "12:xx" being the start of the half-day
                        if (mCurrentHour == 12) {
                            mCurrentHour = 0;
                        }
                        if (!mIsAm) {
                            // PM means 12 hours later than nominal
                            mCurrentHour += 12;
                        }
                    }
                    onTimeChanged();
                }
            });

            // digits of minute
            mMinutePicker = (NumberPicker) findViewById(R.id.minute);
            mMinutePicker.setMinValue(0);
            mMinutePicker.setMaxValue(59);
            mMinutePicker.setFormatter(TWO_DIGIT_FORMATTER);
            mMinutePicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                @Override
                public void onValueChange(NumberPicker spinner, int oldVal, int newVal) {
                    mCurrentMinute = newVal;
                    onTimeChanged();
                }
            });

            // digits of seconds
            mSecondPicker = (NumberPicker) findViewById(R.id.seconds);
            mSecondPicker.setMinValue(0);
            mSecondPicker.setMaxValue(59);
            mSecondPicker.setFormatter(TWO_DIGIT_FORMATTER);
            mSecondPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {

                @Override
                public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                    mCurrentSeconds = newVal;
                    onTimeChanged();

                }
            });

            // am/pm
            mAmPmButton = (Button) findViewById(R.id.amPm);

            // now that the hour/minute picker objects have been initialized, set
            // the hour range properly based on the 12/24 hour display mode.
            configurePickerRanges();

            // initialize to current time
            Calendar cal = Calendar.getInstance();
            setOnTimeChangedListener(NO_OP_CHANGE_LISTENER);

            // by default we're not in 24 hour mode
            setCurrentHour(cal.get(Calendar.HOUR_OF_DAY));
            setCurrentMinute(cal.get(Calendar.MINUTE));
            setCurrentSecond(cal.get(Calendar.SECOND));

            mIsAm = (mCurrentHour < 12);

        /* Get the localized am/pm strings and use them in the spinner */
            DateFormatSymbols dfs = new DateFormatSymbols();
            String[] dfsAmPm = dfs.getAmPmStrings();
            mAmText = dfsAmPm[Calendar.AM];
            mPmText = dfsAmPm[Calendar.PM];
            mAmPmButton.setText(mIsAm ? mAmText : mPmText);
            mAmPmButton.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    requestFocus();
                    if (mIsAm) {

                        // Currently AM switching to PM
                        if (mCurrentHour < 12) {
                            mCurrentHour += 12;
                        }
                    } else {

                        // Currently PM switching to AM
                        if (mCurrentHour >= 12) {
                            mCurrentHour -= 12;
                        }
                    }
                    mIsAm = !mIsAm;
                    mAmPmButton.setText(mIsAm ? mAmText : mPmText);
                    onTimeChanged();
                }
            });

            if (!isEnabled()) {
                setEnabled(false);
            }
        }

        @Override
        public void setEnabled(boolean enabled) {
            super.setEnabled(enabled);
            mMinutePicker.setEnabled(enabled);
            mHourPicker.setEnabled(enabled);
            mAmPmButton.setEnabled(enabled);
        }

        /**
         * Used to save / restore state of time picker
         */
        private class SavedState extends BaseSavedState {

            private final int mHour;
            private final int mMinute;

            private SavedState(Parcelable superState, int hour, int minute) {
                super(superState);
                mHour = hour;
                mMinute = minute;
            }

            private SavedState(Parcel in) {
                super(in);
                mHour = in.readInt();
                mMinute = in.readInt();
            }

            public int getHour() {
                return mHour;
            }

            public int getMinute() {
                return mMinute;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                super.writeToParcel(dest, flags);
                dest.writeInt(mHour);
                dest.writeInt(mMinute);
            }

            public Creator<SavedState> CREATOR
                    = new Creator<SavedState>() {
                public SavedState createFromParcel(Parcel in) {
                    return new SavedState(in);
                }

                public SavedState[] newArray(int size) {
                    return new SavedState[size];
                }
            };
        }

        @Override
        protected Parcelable onSaveInstanceState() {
            Parcelable superState = super.onSaveInstanceState();
            return new SavedState(superState, mCurrentHour, mCurrentMinute);
        }

        @Override
        protected void onRestoreInstanceState(Parcelable state) {
            SavedState ss = (SavedState) state;
            super.onRestoreInstanceState(ss.getSuperState());
            setCurrentHour(ss.getHour());
            setCurrentMinute(ss.getMinute());
        }

        /**
         * Set the callback that indicates the time has been adjusted by the user.
         *
         * @param onTimeChangedListener the callback, should not be null.
         */
        public void setOnTimeChangedListener(OnTimeChangedListener onTimeChangedListener) {
            mOnTimeChangedListener = onTimeChangedListener;
        }

        /**
         * @return The current hour (0-23).
         */
        public Integer getCurrentHour() {
            return mCurrentHour;
        }

        /**
         * Set the current hour.
         */
        public void setCurrentHour(Integer currentHour) {
            this.mCurrentHour = currentHour;
            updateHourDisplay();
        }

        /**
         * Set whether in 24 hour or AM/PM mode.
         *
         * @param is24HourView True = 24 hour mode. False = AM/PM.
         */
        public void setIs24HourView(Boolean is24HourView) {
            if (mIs24HourView != is24HourView) {
                mIs24HourView = is24HourView;
                configurePickerRanges();
                updateHourDisplay();
            }
        }

        /**
         * @return true if this is in 24 hour view else false.
         */
        public boolean is24HourView() {
            return mIs24HourView;
        }

        /**
         * @return The current minute.
         */
        public Integer getCurrentMinute() {
            return mCurrentMinute;
        }

        /**
         * Set the current minute (0-59).
         */
        public void setCurrentMinute(Integer currentMinute) {
            this.mCurrentMinute = currentMinute;
            updateMinuteDisplay();
        }

        /**
         * @return The current minute.
         */
        public Integer getCurrentSeconds() {
            return mCurrentSeconds;
        }

        /**
         * Set the current second (0-59).
         */
        public void setCurrentSecond(Integer currentSecond) {
            this.mCurrentSeconds = currentSecond;
            updateSecondsDisplay();
        }

        @Override
        public int getBaseline() {
            return mHourPicker.getBaseline();
        }

        /**
         * Set the state of the spinners appropriate to the current hour.
         */
        private void updateHourDisplay() {
            int currentHour = mCurrentHour;
            if (!mIs24HourView) {
                // convert [0,23] ordinal to wall clock display
                if (currentHour > 12) currentHour -= 12;
                else if (currentHour == 0) currentHour = 12;
            }
            mHourPicker.setValue(currentHour);
            mIsAm = mCurrentHour < 12;
            mAmPmButton.setText(mIsAm ? mAmText : mPmText);
            onTimeChanged();
        }

        private void configurePickerRanges() {
            if (mIs24HourView) {
                mHourPicker.setMinValue(0);
                mHourPicker.setMaxValue(23);
                mHourPicker.setFormatter(TWO_DIGIT_FORMATTER);
                mAmPmButton.setVisibility(View.GONE);
            } else {
                mHourPicker.setMinValue(1);
                mHourPicker.setMaxValue(12);
                mHourPicker.setFormatter(null);
                mAmPmButton.setVisibility(View.VISIBLE);
            }
        }

        private void onTimeChanged() {
            mOnTimeChangedListener.onTimeChanged(this, getCurrentHour(), getCurrentMinute(), getCurrentSeconds());
        }

        /**
         * Set the state of the spinners appropriate to the current minute.
         */
        private void updateMinuteDisplay() {
            mMinutePicker.setValue(mCurrentMinute);
            mOnTimeChangedListener.onTimeChanged(this, getCurrentHour(), getCurrentMinute(), getCurrentSeconds());
        }

        /**
         * Set the state of the spinners appropriate to the current second.
         */
        private void updateSecondsDisplay() {
            mSecondPicker.setValue(mCurrentSeconds);
            mOnTimeChangedListener.onTimeChanged(this, getCurrentHour(), getCurrentMinute(), getCurrentSeconds());
        }
    }
}

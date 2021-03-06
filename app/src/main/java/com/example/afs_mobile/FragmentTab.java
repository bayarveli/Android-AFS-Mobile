package com.example.afs_mobile;

import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.material.button.MaterialButton;

import java.util.Calendar;


public class FragmentTab extends Fragment implements TimePickerDialog.OnTimeSetListener {

    public static final String ARG_OBJECT = "object";
    TextView time;
    Calendar calendar;
    int hour, minute;
    TimePickerDialog timePickerDialog ;

    NumberPicker mPond1DriveTime;
    NumberPicker mPond1FeedTime;

    CheckBox mIsEnabled;

    EditText mLabel1;
    EditText mLabel2;
    EditText mLabel3;
    EditText mLabel4;
    EditText mLabel5;
    EditText mLabel6;
    EditText mLabel7;
    EditText mLabel8;
    EditText mLabel9;
    EditText mLabel10;

    TabDataStructure mTabData = new TabDataStructure();;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab, container, false);
        // Inflate the layout for this fragment
        mLabel1 = (EditText) view.findViewById(R.id.lblPond1);
        mLabel2 = (EditText) view.findViewById(R.id.lblPond2);
        mLabel3 = (EditText) view.findViewById(R.id.lblPond3);
        mLabel4 = (EditText) view.findViewById(R.id.lblPond4);
        mLabel5 = (EditText) view.findViewById(R.id.lblPond5);
        mLabel6 = (EditText) view.findViewById(R.id.lblPond6);
        mLabel7 = (EditText) view.findViewById(R.id.lblPond7);
        mLabel8 = (EditText) view.findViewById(R.id.lblPond8);
        mLabel9 = (EditText) view.findViewById(R.id.lblPond9);
        mLabel10 = (EditText) view.findViewById(R.id.lblPond10);

        mLabel1.setText("1");
        mLabel2.setText("2");
        mLabel3.setText("3");
        mLabel4.setText("4");
        mLabel5.setText("5");
        mLabel6.setText("6");
        mLabel7.setText("7");
        mLabel8.setText("8");
        mLabel9.setText("9");
        mLabel10.setText("10");

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle saveInstanceState)
    {


        mIsEnabled = (CheckBox) view.findViewById(R.id.chk_taskActive);

        mIsEnabled.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTabData.mIsActivated = mIsEnabled.isChecked();
                Log.d("TASK", "Check: " + mTabData.mIsActivated);
            }
        });

        mPond1DriveTime = (NumberPicker) view.findViewById(R.id.npDriveTime1);
        mPond1DriveTime.setMaxValue(59);
        mPond1DriveTime.setMinValue(0);
        mPond1DriveTime.setValue(30);

        mPond1DriveTime.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                mTabData.mPondTime = newVal;
                Log.d("TASK", "NumPick Drive: " + mTabData.mPondTime);
            }
        });

        mPond1FeedTime = (NumberPicker) view.findViewById(R.id.npPondTime1);
        mPond1FeedTime.setMaxValue(59);
        mPond1FeedTime.setMinValue(0);
        mPond1FeedTime.setValue(30);

        mPond1FeedTime.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                mTabData.mFeedingTime = newVal;
                Log.d("TASK", "NumPick Feed: " + mTabData.mFeedingTime);
            }
        });

        NumberPicker numberPicker3 = (NumberPicker) view.findViewById(R.id.npDriveTime2);
        numberPicker3.setMaxValue(59);
        numberPicker3.setMinValue(0);
        numberPicker3.setValue(30);
        NumberPicker numberPicker4 = (NumberPicker) view.findViewById(R.id.npPondTime2);
        numberPicker4.setMaxValue(59);
        numberPicker4.setMinValue(0);
        numberPicker4.setValue(30);

        calendar = Calendar.getInstance();
        time = (TextView) view.findViewById(R.id.etxt_taskTime);
        time.setText(calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE));

        mTabData.mTaskTimeMinute = calendar.get(Calendar.MINUTE);
        mTabData.mTaskTimeHour = calendar.get(Calendar.HOUR_OF_DAY);

        ((TextView) view.findViewById(R.id.etxt_taskTime)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePicker();
            }
        });
    }

    public TabDataStructure getTabData()
    {
        return mTabData;
    }

    //display time picker dialog
    public void timePicker() {

        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);
        timePickerDialog = new TimePickerDialog(getActivity(), this, hour, minute, true);
        timePickerDialog.show();
    }
    //display selected time
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        time.setText(hourOfDay + ":" + minute);

        mTabData.mTaskTimeHour = hourOfDay;
        mTabData.mTaskTimeMinute = minute;
    }
}
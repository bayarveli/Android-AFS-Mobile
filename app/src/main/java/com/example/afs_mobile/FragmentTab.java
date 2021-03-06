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

    // TODO: There is two way to select pond choice (First is selected for now)
    // First is that user can order operable ponds with drive time
    // Second is that checkboxes can be added to each pond selected (16 ponds)
    public static final String ARG_OBJECT = "object";
    TextView time;
    Calendar calendar;
    int hour, minute;
    TimePickerDialog timePickerDialog ;
    CheckBox mIsEnabled;
    TabDataStructure mTabData = new TabDataStructure();;

    int[] mLabelIdList = new int[]
            {
                    R.id.lblPond1, R.id.lblPond2, R.id.lblPond3, R.id.lblPond4,
                    R.id.lblPond5, R.id.lblPond6, R.id.lblPond7, R.id.lblPond8,
                    R.id.lblPond9, R.id.lblPond10, R.id.lblPond11, R.id.lblPond12,
                    R.id.lblPond13, R.id.lblPond14, R.id.lblPond15, R.id.lblPond16
            };

    int[] mNumPickDriveIdList = new int[]
            {
                    R.id.npDriveTime1, R.id.npDriveTime2, R.id.npDriveTime3, R.id.npDriveTime4,
                    R.id.npDriveTime5, R.id.npDriveTime6, R.id.npDriveTime7, R.id.npDriveTime8,
                    R.id.npDriveTime9, R.id.npDriveTime10, R.id.npDriveTime11, R.id.npDriveTime12,
                    R.id.npDriveTime13, R.id.npDriveTime14, R.id.npDriveTime15, R.id.npDriveTime16
            };


    int[] mNumPickPondIdList = new int[]
            {
                    R.id.npPondTime1, R.id.npPondTime2, R.id.npPondTime3, R.id.npPondTime4,
                    R.id.npPondTime5, R.id.npPondTime6, R.id.npPondTime7, R.id.npPondTime8,
                    R.id.npPondTime9, R.id.npPondTime10, R.id.npPondTime11, R.id.npPondTime12,
                    R.id.npPondTime13, R.id.npPondTime14, R.id.npPondTime15, R.id.npPondTime16
            };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab, container, false);
        // Inflate the layout for this fragment

        for (int idx = 0; idx < mLabelIdList.length; idx++)
        {
            EditText t = (EditText) view.findViewById(mLabelIdList[idx]);
            t.setText(Integer.toString(idx + 1));
        }

        for (int idx = 0; idx < mNumPickDriveIdList.length; idx++)
        {
            NumberPicker t = (NumberPicker) view.findViewById(mNumPickDriveIdList[idx]);
            t.setMinValue(0);
            t.setMaxValue(59);
            t.setValue(0);
            mTabData.mPondTime[idx] = 0;
        }

        for (int idx = 0; idx < mNumPickPondIdList.length; idx++)
        {
            NumberPicker t = (NumberPicker) view.findViewById(mNumPickPondIdList[idx]);
            t.setMinValue(0);
            t.setMaxValue(59);
            t.setValue(0);
            mTabData.mFeedingTime[idx] = 0;
        }

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle saveInstanceState)
    {
        //
        mIsEnabled = (CheckBox) view.findViewById(R.id.chk_taskActive);

        mIsEnabled.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTabData.mIsActivated = mIsEnabled.isChecked();
                // TODO: Enable or disable all controls according to isActivated checkbox
            }
        });

        //
        for (int idx = 0; idx < mNumPickDriveIdList.length; idx++)
        {
            NumberPicker npDrive = (NumberPicker) view.findViewById(mNumPickDriveIdList[idx]);

            int finalIdx = idx;
            npDrive.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                @Override
                public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                    mTabData.mPondTime[finalIdx] = newVal;
                    // TODO: Enable the following table row sequentially.
                }
            });


            NumberPicker npFeed = (NumberPicker) view.findViewById(mNumPickPondIdList[idx]);

            npFeed.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                @Override
                public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                    mTabData.mFeedingTime[finalIdx] = newVal;
                }
            });
        }

        //
        calendar = Calendar.getInstance();
        time = (TextView) view.findViewById(R.id.etxt_taskTime);
        time.setText(calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE));

        mTabData.mTaskTimeYear = calendar.get(Calendar.YEAR);
        mTabData.mTaskTimeMonth = calendar.get(Calendar.MONTH);
        mTabData.mTaskTimeDay = calendar.get(Calendar.DAY_OF_MONTH);
        mTabData.mTaskTimeDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        mTabData.mTaskTimeMinute = calendar.get(Calendar.MINUTE);
        mTabData.mTaskTimeHour = calendar.get(Calendar.HOUR_OF_DAY);
        mTabData.mTaskTimeSecond = 0;

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

        mTabData.mTaskTimeYear = calendar.get(Calendar.YEAR);
        mTabData.mTaskTimeMonth = calendar.get(Calendar.MONTH);
        mTabData.mTaskTimeDay = calendar.get(Calendar.DAY_OF_MONTH);
        mTabData.mTaskTimeDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        mTabData.mTaskTimeHour = hourOfDay;
        mTabData.mTaskTimeMinute = minute;
        mTabData.mTaskTimeSecond = 0;
    }
}
package com.example.afs_mobile;


import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;


public class FragmentTab extends Fragment implements TimePickerDialog.OnTimeSetListener {

    public static final String ARG_OBJECT = "object";
    TextView time;
    Calendar calendar;
    int hour, minute;
    TimePickerDialog timePickerDialog ;

    NumberPicker numPickOne;

    public static int numpickVal = 30;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab, container, false);

//        NumberPicker numberPicker1 = (NumberPicker) view.findViewById(R.id.numPick1);
//        numberPicker1.setMaxValue(59);
//        numberPicker1.setMinValue(0);
//        numberPicker1.setValue(30);
//        NumberPicker numberPicker2 = (NumberPicker) view.findViewById(R.id.numPick2);
//        numberPicker2.setMaxValue(59);
//        numberPicker2.setMinValue(0);
//        numberPicker2.setValue(30);
//        NumberPicker numberPicker3 = (NumberPicker) view.findViewById(R.id.numPick3);
//        numberPicker3.setMaxValue(59);
//        numberPicker3.setMinValue(0);
//        numberPicker3.setValue(30);
//        NumberPicker numberPicker4 = (NumberPicker) view.findViewById(R.id.numPick4);
//        numberPicker4.setMaxValue(59);
//        numberPicker4.setMinValue(0);
//        numberPicker4.setValue(30);

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle saveInstanceState)
    {

        numPickOne = (NumberPicker) view.findViewById(R.id.numPick1);
        numPickOne.setMaxValue(59);
        numPickOne.setMinValue(0);
        numPickOne.setValue(30);

        numPickOne.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                numpickVal = newVal;
                Log.d("TASK", "Set value change: " + numpickVal);
            }
        });

        NumberPicker numberPicker2 = (NumberPicker) view.findViewById(R.id.numPick2);
        numberPicker2.setMaxValue(59);
        numberPicker2.setMinValue(0);
        numberPicker2.setValue(30);
        NumberPicker numberPicker3 = (NumberPicker) view.findViewById(R.id.numPick3);
        numberPicker3.setMaxValue(59);
        numberPicker3.setMinValue(0);
        numberPicker3.setValue(30);
        NumberPicker numberPicker4 = (NumberPicker) view.findViewById(R.id.numPick4);
        numberPicker4.setMaxValue(59);
        numberPicker4.setMinValue(0);
        numberPicker4.setValue(30);

        Bundle args = getArguments();
//        ((TextView) view.findViewById(R.id.textView)).setText(Integer.toString(args.getInt(ARG_OBJECT)));

        calendar = Calendar.getInstance();
        time = (TextView) view.findViewById(R.id.etxt_taskTime);
        time.setText(calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE));

        ((TextView) view.findViewById(R.id.etxt_taskTime)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePicker();
            }
        });
    }


    public int getNumPickData()
    {
        Log.d("TASK", "Number Picker Val: " + numpickVal);
        return numpickVal;
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
    }
}
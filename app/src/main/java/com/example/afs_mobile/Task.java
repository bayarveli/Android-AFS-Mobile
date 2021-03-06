package com.example.afs_mobile;

import android.graphics.pdf.PdfDocument;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.os.StrictMode;
import android.text.NoCopySpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;

import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import com.example.afs_mobile.TabDataStructure;


public class Task extends Fragment {

    private Socket mClient;
    PagerAdapter pagerAdapter;
    ViewPager viewPager;
    Button sendButton;

    int currentPage = 0;
    TabDataStructure mTabDataList[] = new TabDataStructure[AppParamConfig.SYSTEM_POND_COUNT];

    byte[] messageBuffer = new byte[600];

    @Override
    public void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        super.onCreate(savedInstanceState);

        mClient = SocketHandler.getSocket();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_task, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        pagerAdapter = new PagerAdapter(getActivity().getSupportFragmentManager());
        viewPager = view.findViewById(R.id.pager);
        TabLayout tabLayout = view.findViewById(R.id.tab_layout);

        //! Page count not to be destroyed.
        viewPager.setOffscreenPageLimit(AppParamConfig.SYSTEM_POND_COUNT);
        viewPager.setAdapter(pagerAdapter);

        tabLayout.setupWithViewPager(viewPager);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentPage = position;

                List<Fragment> listOfFragments = getActivity().getSupportFragmentManager().getFragments();
                //! First fragment is the navigation host fragment
                FragmentTab tabNow = (FragmentTab) listOfFragments.get(currentPage + 1);

                if (null != tabNow) {
                    // Each message has 60 bytes => 2 bytes address + AppParamConfig.SYSTEM_POND_CONFIG_SIZE_IN_EEPROM
                    int messageIdx = currentPage * (AppParamConfig.SYSTEM_POND_CONFIG_SIZE_IN_EEPROM + 2);
                    int eepromAddressStart = currentPage * AppParamConfig.SYSTEM_POND_CONFIG_SIZE_IN_EEPROM;
                    byte[] eepromAddressStartByte = ByteBuffer.allocate(4).putInt(eepromAddressStart).array();

                    messageBuffer[messageIdx] = eepromAddressStartByte[2];
                    messageBuffer[messageIdx + 1] = eepromAddressStartByte[3];
                    if (true == tabNow.mTabData.mIsActivated)
                    {
                        messageBuffer[messageIdx + 2] = (byte) 1;
                    }
                    else
                    {
                        messageBuffer[messageIdx + 2] = (byte) 0;
                    }

                    byte[] taskTimeYearByte = ByteBuffer.allocate(4).putInt(tabNow.mTabData.mTaskTimeYear).array();
                    messageBuffer[messageIdx + 3] = taskTimeYearByte[2];
                    messageBuffer[messageIdx + 4] = taskTimeYearByte[3];
                    messageBuffer[messageIdx + 5] = (byte) tabNow.mTabData.mTaskTimeMonth;
                    messageBuffer[messageIdx + 6] = (byte) tabNow.mTabData.mTaskTimeDay;
                    messageBuffer[messageIdx + 7] = (byte) tabNow.mTabData.mTaskTimeDayOfWeek;
                    messageBuffer[messageIdx + 8] = (byte) tabNow.mTabData.mTaskTimeHour;
                    messageBuffer[messageIdx + 9] = (byte) tabNow.mTabData.mTaskTimeMinute;
                    messageBuffer[messageIdx + 10] = (byte) tabNow.mTabData.mTaskTimeSecond;
                    // TODO: Total pond count must be calculated from events in Fragment tab
                    messageBuffer[messageIdx + 11] = (byte) 0;

                    int offset = 0;
                    for (int idx = 0; idx < tabNow.mTabData.mFeedingTime.length; idx++)
                    {
                        messageBuffer[messageIdx + 12 + offset] = (byte) tabNow.mTabData.mPondTime[idx];
                        byte[] feedingTimeByte = ByteBuffer.allocate(4).putInt(tabNow.mTabData.mFeedingTime[idx]).array();
                        messageBuffer[messageIdx + 12 + offset + 1] = feedingTimeByte[2];
                        messageBuffer[messageIdx + 12 + offset + 2] = feedingTimeByte[3];
                        offset += 3;
                    }

//                    Log.d("TASK", "Tab Index: " + currentPage);
//                    Log.d("TASK", "isActivated: " + tabNow.mTabData.mIsActivated);
//                    Log.d("TASK", "Hour: " + tabNow.mTabData.mTaskTimeHour);
//                    Log.d("TASK", "Minute: " + tabNow.mTabData.mTaskTimeMinute);
//                    for (int i = 0; i < 16; i++) {
//                        Log.d("TASK", "Pond: " + i);
//                        Log.d("TASK", "Drive Time: " + tabNow.mTabData.mPondTime[i]);
//                        Log.d("TASK", "Feed Time: " + tabNow.mTabData.mFeedingTime[i]);
//                    }
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        sendButton = (Button) view.findViewById(R.id.btnSendToDevice);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                List<Fragment> listOfFragments = getActivity().getSupportFragmentManager().getFragments();
                //! First fragment is the navigation host fragment
                FragmentTab tabNow = (FragmentTab) listOfFragments.get(currentPage + 1);

                if (null != tabNow) {

                    // Each message has 60 bytes => 2 bytes address + AppParamConfig.SYSTEM_POND_CONFIG_SIZE_IN_EEPROM
                    int messageIdx = currentPage * (AppParamConfig.SYSTEM_POND_CONFIG_SIZE_IN_EEPROM + 2);
                    int eepromAddressStart = currentPage * AppParamConfig.SYSTEM_POND_CONFIG_SIZE_IN_EEPROM;
                    byte[] eepromAddressStartByte;
                    eepromAddressStartByte = ByteBuffer.allocate(4).putInt(eepromAddressStart).array();

                    messageBuffer[messageIdx] = eepromAddressStartByte[2];
                    messageBuffer[messageIdx + 1] = eepromAddressStartByte[3];
                    if (true == tabNow.mTabData.mIsActivated)
                    {
                        messageBuffer[messageIdx + 2] = (byte) 1;
                    }
                    else
                    {
                        messageBuffer[messageIdx + 2] = (byte) 0;
                    }

                    byte[] taskTimeYearByte = ByteBuffer.allocate(4).putInt(tabNow.mTabData.mTaskTimeYear).array();
                    messageBuffer[messageIdx + 3] = taskTimeYearByte[2];
                    messageBuffer[messageIdx + 4] = taskTimeYearByte[3];
                    messageBuffer[messageIdx + 5] = (byte) tabNow.mTabData.mTaskTimeMonth;
                    messageBuffer[messageIdx + 6] = (byte) tabNow.mTabData.mTaskTimeDay;
                    messageBuffer[messageIdx + 7] = (byte) tabNow.mTabData.mTaskTimeDayOfWeek;
                    messageBuffer[messageIdx + 8] = (byte) tabNow.mTabData.mTaskTimeHour;
                    messageBuffer[messageIdx + 9] = (byte) tabNow.mTabData.mTaskTimeMinute;
                    messageBuffer[messageIdx + 10] = (byte) tabNow.mTabData.mTaskTimeSecond;
                    // TODO: Total pond count must be calculated from events in Fragment tab
                    messageBuffer[messageIdx + 11] = (byte) 0;

                    int offset = 0;
                    for (int idx = 0; idx < tabNow.mTabData.mFeedingTime.length; idx++)
                    {
                        messageBuffer[messageIdx + 12 + offset] = (byte) tabNow.mTabData.mPondTime[idx];
                        byte[] feedingTimeByte = ByteBuffer.allocate(4).putInt(tabNow.mTabData.mFeedingTime[idx]).array();
                        messageBuffer[messageIdx + 12 + offset + 1] = feedingTimeByte[2];
                        messageBuffer[messageIdx + 12 + offset + 2] = feedingTimeByte[3];
                        offset += 3;
                    }

                    Log.d("TASK", byteToHexString(messageBuffer));

//                    Log.d("TASK", "Tab Index: " + currentPage);
//                    Log.d("TASK", "isActivated: " + tabNow.mTabData.mIsActivated);
//                    Log.d("TASK", "Hour: " + tabNow.mTabData.mTaskTimeHour);
//                    Log.d("TASK", "Minute: " + tabNow.mTabData.mTaskTimeMinute);
//                    for (int i = 0; i < 16; i++) {
//                        Log.d("TASK", "Pond: " + i);
//                        Log.d("TASK", "Drive Time: " + tabNow.mTabData.mPondTime[i]);
//                        Log.d("TASK", "Feed Time: " + tabNow.mTabData.mFeedingTime[i]);
//                    }

                    // TODO: run a thread for send config
                    // TODO: send config for each task
                }
            }
        });



    }

    private String byteToHexString(byte[] payload) {
        if (payload == null) return "<empty>";
        StringBuilder stringBuilder = new StringBuilder(payload.length);
        stringBuilder.append("\n");
        int idx = 0;
        for (byte byteChar : payload)
        {
            idx++;
            stringBuilder.append(String.format("%02X ", byteChar));
            int remain = idx % 60;
            if (0 == remain)
            {
                stringBuilder.append("\n");
            }

        }

        return stringBuilder.toString();
    }
}


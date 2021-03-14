package com.example.afs_mobile;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.pdf.PdfDocument;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.os.StrictMode;
import android.text.NoCopySpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
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
    private Context mAppContext;

    int currentPage = 0;
    TabDataStructure mTabDataList[] = new TabDataStructure[AppParamConfig.SYSTEM_POND_COUNT];

    byte[] messageBuffer = new byte[600];
    byte[] configMessageBuffer = new byte[8];
    byte[] testMsg = new byte[61];
    int configMsgBufferLen = 0;

    SendConfigTask sendData = null;

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

        mAppContext = getActivity().getApplicationContext();

        // TODO: send a message to device that changes mode to config

        configMessageBuffer[0] = AppParamConfig.SYSTEM_MSD_ID_START_CONFIG;
        configMsgBufferLen = 1;

        try {
            OutputStream outStream = mClient.getOutputStream();
            DataOutputStream dos = new DataOutputStream(outStream);

            if (configMsgBufferLen > 0)
            {
                dos.write(configMessageBuffer, 0, configMsgBufferLen);
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }

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

        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK)
                {
                    configMessageBuffer[0] = AppParamConfig.SYSTEM_MSD_ID_END_CONFIG;
                    configMsgBufferLen = 1;

                    try {
                        OutputStream outStream = mClient.getOutputStream();
                        DataOutputStream dos = new DataOutputStream(outStream);

                        if (configMsgBufferLen > 0)
                        {
                            dos.write(configMessageBuffer, 0, configMsgBufferLen);
                        }
                    } catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
                return false;
            }
        });

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

                    Log.d("TASK", byteToHexString(messageBuffer));

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
                    sendData = new SendConfigTask(getActivity());
                    sendData.execute();

                }
            }
        });
    }

    public class SendConfigTask extends AsyncTask<Void, Integer, Void>
    {
        private ProgressDialog dialog;

        public SendConfigTask(FragmentActivity activity)
        {
            dialog = new ProgressDialog(activity);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if (mClient.isConnected())
            {
                DataOutputStream dos = null;
                DataInputStream dis = null;

                try {
                    dos = new DataOutputStream(mClient.getOutputStream());
                    dis = new DataInputStream(mClient.getInputStream());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                for (int i = 0; i < AppParamConfig.SYSTEM_POND_COUNT; i++) {
                    try {
                        byte[] sendResponse = new byte[2];
                        // If task is activated then send this data
                        if (messageBuffer[(i * 60) + 2] == 0x01) {
                            testMsg[0] = AppParamConfig.SYSTEM_MSG_ID_TASK_INFO;
                            for (int j = 0; j < 60; j++)
                            {
                                testMsg[j + 1] = messageBuffer[(i * 60) + j];
                            }
                            dos.write(testMsg, 0, 61);

                            int result = dis.read(sendResponse);

                            Thread.sleep(1000);

                            if (result > 0)
                            {
                                if (AppParamConfig.SYSTEM_MSG_ID_TASK_INFO_RESP == sendResponse[0])
                                {
                                    if (0x11 == sendResponse[1])
                                    {
                                        // TODO: Handle config send case is success
                                        String toastMessage = "Task Config " + i + " is sent.";
                                        Log.d("TASK", toastMessage);

                                    }
                                    else if (0xFF == sendResponse[1])
                                    {
                                        // TODO: Handle config send fail case
                                        String toastMessage = "Task Config " + i + " is not sent.";
                                        Log.d("TASK", toastMessage);
                                    }
                                    else
                                    {
                                        Log.d("TASK", "Invalid response format.");
                                    }

                                }
                            }
                        }
                        else
                        {
                            Log.d("TASK", "Task " + i + "is not activated.");
                        }
                        publishProgress((i + 1));

                    } catch (IOException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            else
            {
                Log.d("TASK", "Socket is not connected.");

            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog.setMessage("Konfigürasyon yükleme:");
            dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            dialog.setProgress(0);
            dialog.setMax(10);
            dialog.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if (dialog.isShowing())
            {
                dialog.dismiss();
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            dialog.setProgress(values[0]);
        }
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


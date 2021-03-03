package com.example.afs_mobile;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.StrictMode;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Test#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Test extends Fragment implements View.OnClickListener {

    private Socket mClient;
    private int mMoveSpeedCounter = 130;
    private int mFeedSpeedCounter = 130;
    private EditText mMoveSpeedText;
    private EditText mFeedSpeedText;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Test() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Test.
     */
    // TODO: Rename and change types and number of parameters
    public static Test newInstance(String param1, String param2) {
        Test fragment = new Test();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        mClient = SocketHandler.getSocket();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_test, container, false);

        mMoveSpeedText = view.findViewById(R.id.etxtMoveSpeed);
        mMoveSpeedText.setGravity(Gravity.CENTER_HORIZONTAL);
        mMoveSpeedText.setText(Integer.toString(mMoveSpeedCounter));

        mFeedSpeedText = view.findViewById(R.id.feedSpeed);
        mFeedSpeedText.setGravity(Gravity.CENTER_HORIZONTAL);
        mFeedSpeedText.setText(Integer.toString(mFeedSpeedCounter));

        Button leftButton = (Button) view.findViewById(R.id.btnMoveLeft);
        Button rightButton = (Button) view.findViewById(R.id.btnMoveRight);
        Button stopButton = (Button) view.findViewById(R.id.btnMoveStop);
        Button speedDecreaseButton = (Button) view.findViewById(R.id.btnMoveDecreaseSpeed);
        Button speedIncreaseButton = (Button) view.findViewById(R.id.btnMoveIncreaseSpeed);

        Button btnFeedDecrease = (Button) view.findViewById(R.id.btnFeedDecreaseSpeed);
        Button btnFeedIncrease = (Button) view.findViewById(R.id.btnFeedIncreaseSpeed);
        Button btnFeedStop = (Button) view.findViewById(R.id.btnFeedStop);
        Button btnFeedStart = (Button) view.findViewById(R.id.btnFeedStart);

        btnFeedDecrease.setOnClickListener(this);
        btnFeedIncrease.setOnClickListener(this);
        btnFeedStart.setOnClickListener(this);
        btnFeedStop.setOnClickListener(this);

        leftButton.setOnClickListener(this);
        rightButton.setOnClickListener(this);
        stopButton.setOnClickListener(this);
        speedDecreaseButton.setOnClickListener(this);
        speedIncreaseButton.setOnClickListener(this);

        try {
            PrintWriter out = new PrintWriter(new BufferedWriter(
                    new OutputStreamWriter(mClient.getOutputStream())),
                    true);
            out.println("Come to Test");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onClick(View v) {

        String test = null;
        byte[] messageBuffer = new byte[8];
        int messageLen = 0;

        switch (v.getId()) {
            case R.id.btnMoveLeft:
                messageBuffer[0] = 0x08;
                messageLen = 1;
                break;
            case R.id.btnMoveRight:
                messageBuffer[0] = 0x07;
                messageLen = 1;
                break;
            case R.id.btnMoveStop:
                messageBuffer[0] = 0x09;
                messageLen = 1;
                break;
            case R.id.btnMoveDecreaseSpeed:
                mMoveSpeedCounter -= 5;
                if (mMoveSpeedCounter < 0)
                {
                    mMoveSpeedCounter = 0;
                }
                mMoveSpeedText.setText(Integer.toString(mMoveSpeedCounter));
                messageBuffer[0] = 0x0A;
                messageBuffer[1] = (byte) mMoveSpeedCounter;
                messageLen = 2;
                break;
            case R.id.btnMoveIncreaseSpeed:
                mMoveSpeedCounter += 5;
                if (mMoveSpeedCounter > 255)
                {
                    mMoveSpeedCounter = 255;
                }
                mMoveSpeedText.setText(Integer.toString(mMoveSpeedCounter));
                messageBuffer[0] = 0x0A;
                messageBuffer[1] = (byte) mMoveSpeedCounter;
                messageLen = 2;
                break;
            case R.id.btnFeedStart:
                messageBuffer[0] = 0x0B;
                messageLen = 1;
                break;
            case R.id.btnFeedStop:
                messageBuffer[0] = 0x0C;
                messageLen = 1;
                break;
            case R.id.btnFeedDecreaseSpeed:
                mFeedSpeedCounter -= 5;
                if (mFeedSpeedCounter < 0)
                {
                    mFeedSpeedCounter = 0;
                }
                mFeedSpeedText.setText(Integer.toString(mFeedSpeedCounter));
                messageBuffer[0] = 0x0D;
                messageBuffer[1] = (byte) mFeedSpeedCounter;
                messageLen = 2;
                break;
            case R.id.btnFeedIncreaseSpeed:
                mFeedSpeedCounter += 5;
                if (mFeedSpeedCounter > 255)
                {
                    mFeedSpeedCounter = 255;
                }
                mFeedSpeedText.setText(Integer.toString(mFeedSpeedCounter));
                messageBuffer[0] = 0x0D;
                messageBuffer[1] = (byte) mFeedSpeedCounter;
                messageLen = 2;
                break;
            default:
                break;
        }

        try {
 //           PrintWriter out = new PrintWriter(new BufferedWriter(
 //                   new OutputStreamWriter(mClient.getOutputStream())),
 //                   true);
 //           out.println(test);

            OutputStream outStream = mClient.getOutputStream();
            DataOutputStream dos = new DataOutputStream(outStream);

            if (messageLen > 0)
            {
                dos.write(messageBuffer, 0, messageLen);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
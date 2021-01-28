package com.example.afs_mobile;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.IOException;
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

        Button leftButton = (Button) view.findViewById(R.id.button);
        Button rightButton = (Button) view.findViewById(R.id.button3);
        Button stopButton = (Button) view.findViewById(R.id.button4);

        leftButton.setOnClickListener(this);
        rightButton.setOnClickListener(this);
        stopButton.setOnClickListener(this);

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

        switch (v.getId()) {
            case R.id.button:
                test = "Left";
                break;
            case R.id.button3:
                test = "Right";
                break;
            case R.id.button4:
                test = "Stop";
                break;
            default:
                break;
        }

        try {
            PrintWriter out = new PrintWriter(new BufferedWriter(
                    new OutputStreamWriter(mClient.getOutputStream())),
                    true);
            out.println(test);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
package com.example.afs_mobile;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Home#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Home extends Fragment implements View.OnClickListener {

    private Socket mClient;

    private static final int SERVER_PORT = 13001;
    private static final String SERVER_IP = "192.168.1.25";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Home() {
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
    public static Home newInstance(String param1, String param2) {
        Home fragment = new Home();
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
        // run socket thread
        new Thread(new ClientThread()).start();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);


        Button taskButton = (Button) view.findViewById(R.id.button1);
        Button testButton = (Button) view.findViewById(R.id.button2);

        if (mClient == null)
        {
            taskButton.setEnabled(false);
            testButton.setEnabled(false);
        }

        taskButton.setOnClickListener(this);
        testButton.setOnClickListener(this);
        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onClick(View view) {
        Fragment fragment = null;
        switch (view.getId()) {
            case R.id.button1:
                fragment = new Task();
                replaceFragment(fragment);
                break;
            case R.id.button2:
                fragment = new Test();
                replaceFragment(fragment);
                break;
        }
    }

    public void replaceFragment(Fragment someFragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_home, someFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    class ClientThread implements Runnable {
        @Override
        public void run() {
            try {
                InetAddress serverAddr = InetAddress.getByName(SERVER_IP);
                mClient = new Socket(serverAddr, SERVER_PORT);
                SocketHandler.setSocket(mClient);
            } catch (UnknownHostException e1) {
                e1.printStackTrace();
            } catch (IOException e1){
                e1.printStackTrace();
            }
        }
    }

//    public class ConnectTask extends AsyncTask<String, String, TcpClient> {
//
//        @Override
//        protected TcpClient doInBackground(String... message) {
//
//            mTcpClient = new TcpClient(new TcpClient.OnMessageReceived() {
//                @Override
//                public void messageReceived(String message) {
//                    publishProgress(message);
//                }
//            });
//
//            mTcpClient.run();
//
//            return null;
//        }
//
//
//    }
//
//    public class SendMessageTask extends AsyncTask<String, Void, Void> {
//
//        @Override
//        protected Void doInBackground(String... params) {
//            Log.e("MSG:", params[0]);
//            mTcpClient.sendMessage(params[0]);
//            return null;
//        }
//    }

}
package com.example.afs_mobile;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.StrictMode;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Logger;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Home#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Home extends Fragment implements View.OnClickListener {

    private Socket mClient;
    private EditText mIpAddress;
    private EditText mPortNumber;
    private Context mAppContext;

    private static int SERVER_PORT = 13001;
    private static String SERVER_IP = "192.168.1.25";

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
 //       new Thread(new ClientThread()).start();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        mAppContext = getActivity().getApplicationContext();

        mPortNumber = (EditText) view.findViewById(R.id.etxtPort);
        mIpAddress = (EditText) view.findViewById(R.id.etxtIpAddress);

        mPortNumber.setText("13001");
        mIpAddress.setText("192.168.1.25");

        InputFilter[] filters = new InputFilter[1];

        filters[0] = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if (end > start)
                {
                    String destTxt = dest.toString();
                    String resultingTxt = destTxt.substring(0, dstart) + source.subSequence(start, end) + destTxt.substring(dend);
                    if (!resultingTxt.matches("^\\d{1,3}(\\.(\\d{1,3}(\\.(\\d{1,3}(\\.(\\d{1,3})?)?)?)?)?)?")) {
                        Toast.makeText(mAppContext, "Invalid IP format", Toast.LENGTH_SHORT).show();
                        return "";
                    }
                    else
                    {
                        String[] splits = resultingTxt.split("\\.");
                        for (int i = 0; i < splits.length; i++)
                        {
                            if (Integer.valueOf(splits[i]) > 255)
                            {
                                Toast.makeText(mAppContext, "Invalid number in IP.", Toast.LENGTH_SHORT).show();
                                return "";
                            }
                        }
                        return source;
                    }
                }
                else
                {
                    return null;
                }

            }
        };

        mIpAddress.setFilters(filters);

        Button taskButton = (Button) view.findViewById(R.id.button1);
        Button testButton = (Button) view.findViewById(R.id.button2);
        Button connectButton = (Button) view.findViewById(R.id.btnConnect);

        taskButton.setOnClickListener(this);
        testButton.setOnClickListener(this);
        connectButton.setOnClickListener(this);
        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onClick(View view) {
        Fragment fragment = null;
        Button taskButton = (Button) view.findViewById(R.id.button1);
        Button testButton = (Button) view.findViewById(R.id.button2);
        //

        switch (view.getId()) {
            case R.id.button1:

//                if (mClient == null)
//                {
//                    taskButton.setEnabled(false);
//                }
//                else
                {
                    fragment = new Task();
                    replaceFragment(fragment);
                }
                break;
            case R.id.button2:

//                if (mClient == null)
//                {
//                    testButton.setEnabled(false);
//                    return;
//                }
//                else
                {
                    fragment = new Test();
                    replaceFragment(fragment);
                }
                break;
            case R.id.btnConnect:

                if (!mIpAddress.getText().toString().matches(""))
                {
                    if (!mPortNumber.getText().toString().matches("")) {
                        SERVER_IP = mIpAddress.getText().toString();
                        SERVER_PORT = Integer.parseInt(mPortNumber.getText().toString());

                        if ((SERVER_PORT > 0) && (SERVER_PORT < 65354))
                        {
                            Toast.makeText(mAppContext, "Bağlanıyor.", Toast.LENGTH_LONG).show();

                            // run socket thread
                            new Thread(new ClientThread()).start();
                        }
                        else
                        {
                            Toast.makeText(mAppContext, "Geçersiz port numarası", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(mAppContext, "Port numarası giriniz.", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(mAppContext, "IP giriniz.", Toast.LENGTH_SHORT).show();
                }

                break;
            default:
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

                if (mClient.isBound())
                {
                    // Toast.makeText(mAppContext, "Baglandı.", Toast.LENGTH_SHORT).show();
                    Log.i("MyTag", "is Bounded.");
                }

                SocketHandler.setSocket(mClient);
            } catch (UnknownHostException e1)
            {
                Log.d("MyTag", "Unexpected host.");
                e1.printStackTrace();
            } catch (IOException e1)
            {
                Log.d("MyTag", "IO exception");
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
package com.example.afs_mobile;

import android.graphics.pdf.PdfDocument;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

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
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;



public class Task extends Fragment {

    private Socket mClient;
    PagerAdapter pagerAdapter;
    ViewPager viewPager;
    Button sendButton;

    int currentPage = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_task, container, false);



        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        pagerAdapter = new PagerAdapter(getFragmentManager());
        viewPager = view.findViewById(R.id.pager);
        TabLayout tabLayout = view.findViewById(R.id.tab_layout);

        //! Page count not to be destroyed.
        viewPager.setOffscreenPageLimit(5);
        viewPager.setAdapter(pagerAdapter);

        tabLayout.setupWithViewPager(viewPager);


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
//                Toast.makeText(getActivity().getApplicationContext(),
//                        "Selected page position: " + position, Toast.LENGTH_SHORT).show();
                currentPage = position;
                Log.d("TASK", "Selected:" + position + " - " + currentPage);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        sendButton = (Button) view.findViewById(R.id.btnSendToDevice);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentPage == 0)
                {
                    FragmentTab tabNow = (FragmentTab) pagerAdapter.getItem(currentPage);

                    if (null != tabNow)
                    {
                        Log.d("TASK", "Current Page: " + currentPage + " Selected Value: " + tabNow.getNumPickData());
                    }
                    else {
                        Log.d("TASK", "Fragment tab is NULL!");
                    }

                }
                else if (currentPage == 1)
                {
                    FragmentTab tabNow = (FragmentTab) pagerAdapter.getItem(currentPage);

                    if (null != tabNow)
                    {
                        Log.d("TASK", "Current Page: " + currentPage + " Selected Value: " + tabNow.getNumPickData());
                    }
                    else {
                        Log.d("TASK", "Fragment tab is NULL!");
                    }
                }
                else if (currentPage == 2)
                {
                    FragmentTab tabNow = (FragmentTab) pagerAdapter.getItem(currentPage);

                    if (null != tabNow)
                    {
                        Log.d("TASK", "Current Page: " + currentPage + " Selected Value: " + tabNow.getNumPickData());
                    }
                    else {
                        Log.d("TASK", "Fragment tab is NULL!");
                    }
                }
                else if (currentPage == 3)
                {
                    FragmentTab tabNow = (FragmentTab) pagerAdapter.getItem(currentPage);

                    if (null != tabNow)
                    {
                        Log.d("TASK", "Current Page: " + currentPage + " Selected Value: " + tabNow.getNumPickData());
                    }
                    else {
                        Log.d("TASK", "Fragment tab is NULL!");
                    }
                }
                else if (currentPage == 4)
                {
                    FragmentTab tabNow = (FragmentTab) pagerAdapter.getItem(currentPage);

                    if (null != tabNow)
                    {
                        Log.d("TASK", "Current Page: " + currentPage + " Selected Value: " + tabNow.getNumPickData());
                    }
                    else {
                        Log.d("TASK", "Fragment tab is NULL!");
                    }
                }
            }
        });

    }

    private static String makeFragmentName(int viewPagerId, int index)
    {
        return "android:switcher:" + viewPagerId + ":" + index;
    }
}


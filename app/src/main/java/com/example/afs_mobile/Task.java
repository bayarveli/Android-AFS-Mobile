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
    TabDataStructure mTabDataList[] = new TabDataStructure[5];

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
        viewPager.setOffscreenPageLimit(5);
        viewPager.setAdapter(pagerAdapter);

        tabLayout.setupWithViewPager(viewPager);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentPage = position;
                Log.d("TASK", "Selected >> " + currentPage);
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
                    Log.d("TASK", "Tab Index: " + currentPage);
                    Log.d("TASK", "isActivated: " + tabNow.mTabData.mIsActivated);
                    Log.d("TASK", "Hour: " + tabNow.mTabData.mTaskTimeHour);
                    Log.d("TASK", "Minute: " + tabNow.mTabData.mTaskTimeMinute);
                    Log.d("TASK", "Drive Time: " + tabNow.mTabData.mPondTime);
                    Log.d("TASK", "Feed Time: " + tabNow.mTabData.mFeedingTime);
                }
            }
        });

    }
}


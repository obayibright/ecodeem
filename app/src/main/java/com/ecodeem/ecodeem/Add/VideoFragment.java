package com.ecodeem.ecodeem.Add;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ecodeem.ecodeem.R;

public class VideoFragment extends Fragment {
    private static final String TAG = "VideoFragment";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video, container, false);
        Log.d(TAG, "onCreateView: started.");

        return view;
    }
}

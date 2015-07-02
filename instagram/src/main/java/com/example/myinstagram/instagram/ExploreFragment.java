package com.example.myinstagram.instagram;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.myinstagram.adapters.ExploreFragmentAdapter;
import com.example.myinstagram.adapters.GridImageAdapter;
import com.example.myinstagram.adapters.ImageAdapter;

import java.util.List;

public class ExploreFragment extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_explore, container, false);
        ListView listView = (ListView)rootView.findViewById(R.id.picture_list);
        listView.setAdapter(new ExploreFragmentAdapter(this.getActivity(), null));
        return rootView;
    }
}


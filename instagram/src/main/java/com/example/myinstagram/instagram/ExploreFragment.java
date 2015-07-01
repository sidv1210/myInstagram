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

import com.example.myinstagram.adapters.ImageAdapter;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ExploreFragment} interface
 * to handle interaction events.
 * Use the {@link ExploreFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExploreFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private ImageAdapter imageAdapter;

    ArrayAdapter<ImageView> picAdapter;

    // TODO: Rename and change types of parameters
    public static ExploreFragment newInstance(String param1, String param2) {
        ExploreFragment fragment = new ExploreFragment();
        return fragment;
    }


    public ExploreFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_explore, container, false);
        ListView listView = (ListView)rootView.findViewById(R.id.picture_list);
        listView.setAdapter(new ImageAdapter(this.getActivity(),null));
        return rootView;
    }



    private View  createViewDimension(){
        ImageView imageView = new ImageView(this.getActivity());
        imageView.setLayoutParams(new GridView.LayoutParams(GridView.AUTO_FIT, 320));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setPadding(8, 8, 8, 8);
        return imageView;
    }
}


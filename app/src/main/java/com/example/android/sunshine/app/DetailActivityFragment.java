package com.example.android.sunshine.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {

    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView =  inflater.inflate(R.layout.fragment_detail, container, false);
        TextView m;
        m = (TextView)rootView.findViewById(R.id.textView);

        String retour = getActivity().getIntent().getExtras().getString(Intent.EXTRA_TEXT);

        m.setText(retour);

        return rootView;

    }
}
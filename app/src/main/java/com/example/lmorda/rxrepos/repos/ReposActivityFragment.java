package com.example.lmorda.rxrepos.repos;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lmorda.rxrepos.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class ReposActivityFragment extends Fragment {

    public ReposActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_repos, container, false);
    }
}

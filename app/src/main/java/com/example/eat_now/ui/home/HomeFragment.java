package com.example.eat_now.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eat_now.R;
import com.example.eat_now.adapters.HomeHorAdapter;
import com.example.eat_now.databinding.FragmentHomeBinding;
import com.example.eat_now.models.HomeHorModel;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    RecyclerView homehorrec;
    List<HomeHorModel> homeHorModelList;
    HomeHorAdapter homeHorAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        //binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        homehorrec = root.findViewById(R.id.home_hor_rec);

        homeHorModelList = new ArrayList<>();

        homeHorModelList.add(new HomeHorModel(R.drawable.tastytreatlogo, "Tasty Treat"));
        homeHorModelList.add(new HomeHorModel(R.drawable.tastytreatlogo2, "Tasty Treat"));
        homeHorModelList.add(new HomeHorModel(R.drawable.tastytreatlogo2, "Tasty Treat"));
        homeHorModelList.add(new HomeHorModel(R.drawable.tastytreatlogo2, "Tasty Treat"));
        homeHorModelList.add(new HomeHorModel(R.drawable.tastytreatlogo2, "Tasty Treat"));

        homeHorAdapter = new HomeHorAdapter(getActivity(), homeHorModelList);
        homehorrec.setAdapter(homeHorAdapter);

        homehorrec.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        homehorrec.setHasFixedSize(true);
        homehorrec.setNestedScrollingEnabled(false);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
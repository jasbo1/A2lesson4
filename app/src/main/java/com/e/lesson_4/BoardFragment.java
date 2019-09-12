package com.e.lesson_4;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class BoardFragment extends Fragment {



    public BoardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_board,container,false);

        TextView textTitle=view.findViewById(R.id.textTitle);
        TextView textDesc=view.findViewById(R.id.textDesc);
        LinearLayout linearLayout = view.findViewById(R.id.liner);
        ImageView imageView=view.findViewById(R.id.imageView);
        Button button1=view.findViewById(R.id.button1);
        int pos=getArguments().getInt("pos");
        switch (pos){
            case 0:
                textTitle.setText("Привет");
                textDesc.setText("Друг");
                imageView.setImageResource(R.drawable.smaile1);
                button1.setVisibility(View.GONE);
               linearLayout.setBackgroundColor(getResources().getColor(R.color.colorYellow));


                break;
            case 1:
                textTitle.setText("Как дела");
                textDesc.setText("У тебя?");
                imageView.setImageResource(R.drawable.smaile2);
                button1.setVisibility(View.GONE);
              linearLayout.setBackgroundColor(getResources().getColor(R.color.colorGreen));
                break;
            case 2:
                textTitle.setText("Что делал");
                textDesc.setText("Сегодня");
                imageView.setImageResource(R.drawable.smaile3);
                button1.setVisibility(View.VISIBLE);
                linearLayout.setBackgroundColor(getResources().getColor(R.color.colorBlue));
                button1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SharedPreferences preferences=getActivity().getSharedPreferences("settings", Context.MODE_PRIVATE);
                        preferences.edit().putBoolean("isShown",true).apply();

                        startActivity(new Intent(getContext(),MainActivity.class));
                        getActivity().finish();
                    }
                });
                break;

        }
        return view;
    }

}

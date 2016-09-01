package com.example.maddi.fitness;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by sunny on 16-Apr-16.
 */
public class AccountFragment extends Fragment {



    public AccountFragment(){
        // Required empty public constructor
    }

    public static AccountFragment newInstance(){
        AccountFragment fragment = new AccountFragment();
        return fragment;
    }

    public interface OnChangeGoalListener{
        public void onChangeGoalClicked();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.account_fragment, container, false);

        ImageView changeGoalImg = (ImageView) rootView.findViewById(R.id.settingsIcon);
        TextView changeGoalsText = (TextView) rootView.findViewById(R.id.settingsText);

        final OnChangeGoalListener mGoalListener;
        try {
            mGoalListener = (OnChangeGoalListener) getContext();
            Log.d("mContext is ", getContext().toString());
        }catch (ClassCastException ex){
            throw new ClassCastException("The hosting activity of the fragment" +
                    "forgot to implement onFragmentInteractionListener");
        }

        changeGoalImg.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mGoalListener.onChangeGoalClicked();
            }
        });

        changeGoalsText.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mGoalListener.onChangeGoalClicked();
            }
        });


        return rootView;
    }


}

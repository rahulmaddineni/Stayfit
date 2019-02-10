package com.example.maddi.fitness;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

//import com.firebase.client.Firebase;

/**
 * Created by sunny on 16-Apr-16.
 */
public class BasicInfoFragment extends Fragment {

    int position;
    private String height = "";
    public String weight = "";

    public BasicInfoFragment(){
        // Required empty public constructor
    }

    public static BasicInfoFragment newInstance(){
        BasicInfoFragment fragment = new BasicInfoFragment();
        return fragment;
    }

    public interface OnFloatingButtonClickListener{
        public void onFloatingButtonClicked();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


//        final Firebase ref = new Firebase("https://healthkit.firebaseio.com/Users/"+LoginActivity.USER_ID);
        Log.d("UserID:",LoginActivity.USER_ID);

        final View rootView =  inflater.inflate(R.layout.fragment_basicinfo, container, false);

        final OnFloatingButtonClickListener mListener;
        try {
            mListener = (OnFloatingButtonClickListener) getContext();
            Log.d("mContext is ", getContext().toString());
        }catch (ClassCastException ex){
            throw new ClassCastException("The hosting activity of the fragment" +
                    "forgot to implement onFragmentInteractionListener");
        }

        final Button next = (Button) rootView.findViewById(R.id.nextbutton);

        final FloatingActionButton fab1=(FloatingActionButton)getActivity().findViewById(R.id.next);
        final EditText nameET = (EditText) rootView.findViewById(R.id.nameInput);
        final EditText phoneET = (EditText) rootView.findViewById(R.id.phoneInput);
        final EditText ageET = (EditText) rootView.findViewById(R.id.ageInput);
        final EditText weightET = (EditText) rootView.findViewById(R.id.weightInput);
        final EditText heightET = (EditText) rootView.findViewById(R.id.heightInput);

        final RadioGroup myRadioGroup = (RadioGroup) rootView.findViewById(R.id.genderGroup);
        myRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                position = myRadioGroup.indexOfChild(rootView.findViewById(checkedId));
                if (position == 0) {
                    Log.d("Gender is ", "Male");
//                    ref.child("gender").setValue("Male");
                } else {
                    Log.d("Gender is ", "Female");
//                    ref.child("gender").setValue("Female");
                }
            }
        });

        ImageView userPhoto = (ImageView) getActivity().findViewById(R.id.userPhoto);
        userPhoto.setImageResource(R.drawable.runimage);


        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nameET.getText().toString().length() == 0) {
                    nameET.setError("Name is required!");
                    return;
                }
                mListener.onFloatingButtonClicked();
//                Log.d("Name is ", nameET.getText().toString());
//                ref.child("name").setValue(nameET.getText().toString());
//                Log.d("Phone Number is ", phoneET.getText().toString());
//                ref.child("phone").setValue(phoneET.getText().toString());
//                Log.d("Age is ", ageET.getText().toString());
//                ref.child("age").setValue(ageET.getText().toString());
//                Log.d("User Height - cms is", heightET.getText().toString());
//                ref.child("height").setValue(heightET.getText().toString());
//                Log.d("User Weight is ", weightET.getText().toString());
//                ref.child("weight").setValue(weightET.getText().toString());

            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nameET.getText().toString().length() == 0) {
                    nameET.setError("Name is required!");
                    return;
                }
                mListener.onFloatingButtonClicked();
//                Log.d("Name is ", nameET.getText().toString());
//                ref.child("name").setValue(nameET.getText().toString());
//                Log.d("Phone Number is ", phoneET.getText().toString());
//                ref.child("phone").setValue(phoneET.getText().toString());
//                Log.d("Age is ", ageET.getText().toString());
//                ref.child("age").setValue(ageET.getText().toString());
//                Log.d("User Height - cms is", heightET.getText().toString());
//                ref.child("height").setValue(heightET.getText().toString());
//                Log.d("User Weight is ", weightET.getText().toString());
//                ref.child("weight").setValue(weightET.getText().toString());

            }
        });

        final TextView bmicalc = (TextView) rootView.findViewById(R.id.bmi);
        final TextView bmires = (TextView) rootView.findViewById(R.id.bmiresult);
        ImageView bmiImage = (ImageView) rootView.findViewById(R.id.bmiimage);
        bmiImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    if(weightET.getText().toString() != "" || heightET.getText().toString() != "") {
                        Log.d("In try Block","");
                        Float bmi = ((Float.parseFloat(weightET.getText().toString())) * 10000) / ((Float.parseFloat(heightET.getText().toString())) * (Float.parseFloat(heightET.getText().toString())));
                        bmi = Float.parseFloat(String.format("%.2f", bmi));
                        Log.d("BMI is", bmi.toString());
                        bmicalc.setText(bmi.toString());
                        if (bmi < 18.5) {
                            bmires.setText("Under Weight");
                        } else if (bmi > 18.5 && bmi < 24.99) {
                            bmires.setText("Normal Weight");
                        } else if (bmi > 25 && bmi < 29.99) {
                            bmires.setText("Over Weight");
                        } else {
                            bmires.setText("Obesity");
                        }
                    }
                }
                catch (Exception ex){
                    Log.d("In Catch Block","");
                    Toast toast = Toast.makeText(getActivity(), "Forgot to enter height or weight?", Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });


        return rootView;
    }


}


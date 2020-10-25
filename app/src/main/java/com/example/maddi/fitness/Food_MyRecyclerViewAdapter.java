package com.example.maddi.fitness;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;

import java.util.List;
import java.util.Map;

/**
 * Created by maddi on 3/21/2016.
 */
public class Food_MyRecyclerViewAdapter extends RecyclerView.Adapter<Food_MyRecyclerViewAdapter.ViewHolder> {
    public static float caloriecount = 0f;
    public static float totalfat = 0f;
    public static float totalcarbs = 0f;
    public static float totalprotein = 0f;
    public static int count = 0;
    private List<Map<String, ?>> mDataset;
    private Context mContext;

    // Constructor
    public Food_MyRecyclerViewAdapter(Context myContext, List<Map<String, ?>> myDataset) {
        mContext = myContext;
        mDataset = myDataset;
    }

    // Using View Holder
    @Override
    public Food_MyRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int ViewType) {
        View v;
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_food, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Filling Data into ViewHolder
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Map<String, ?> food = mDataset.get(position);
        holder.bindMovieData((food));
    }

    // No of items in dataset
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    // ViewHolder Class
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView vTitle;
        public TextView vType;
        public TextView vCal;
        public Button vAdd;

        // Set Popup Window
        public RelativeLayout mRelativeLayout;
        public PopupWindow mPopupWindow;

        private FirebaseAuth mAuth;
        private DatabaseReference mDatabase;

        public ViewHolder(View v) {
            super(v);
            vTitle = v.findViewById(R.id.title);
            vType = v.findViewById(R.id.type);
            vCal = v.findViewById(R.id.calories);
            vAdd = v.findViewById(R.id.addfood);
            //mRelativeLayout = (RelativeLayout) v.findViewById(R.id.recyclr_frag_pop);
            mAuth = FirebaseAuth.getInstance();
            mDatabase = FirebaseDatabase.getInstance().getReference();
        }

        private DatabaseReference getCaloriesRef(String ref) {
            FirebaseUser user = mAuth.getCurrentUser();
            String userId = user.getUid();
            return mDatabase.child("Calories").child(userId).child(ref);
        }

        public void bindMovieData(final Map<String, ?> fooditem) {
            vTitle.setText((String) fooditem.get(("iname")));
            vType.setText((String) fooditem.get("bname"));
            // vDesc.setText((String) fooditem.get("idesc"));
            vCal.setText((String) fooditem.get("ical"));
            caloriecount = Food_RecyclerFrag_Main.calRef1;
            totalcarbs = Food_RecyclerFrag_Main.user_carbs1;
            totalprotein = Food_RecyclerFrag_Main.user_protein1;
            totalfat = Food_RecyclerFrag_Main.user_fat1;
            vAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    count++;
                    Log.d("Before Adding", String.valueOf(caloriecount) + String.valueOf(totalcarbs) + String.valueOf(totalfat) + String.valueOf(totalprotein));
                    caloriecount = caloriecount + (Float.parseFloat(String.valueOf(fooditem.get("ical"))));
                    totalcarbs = totalcarbs + (Float.parseFloat((String.valueOf(fooditem.get("icarbs")))));
                    totalfat = totalfat + (Float.parseFloat((String.valueOf(fooditem.get("ifat")))));
                    totalprotein = totalprotein + (Float.parseFloat((String.valueOf(fooditem.get("iprotein")))));
                    Log.d("After Adding", String.valueOf(caloriecount) + String.valueOf(totalcarbs) + String.valueOf(totalfat) + String.valueOf(totalprotein));
                    Log.d("Adapter", (String.valueOf(Food_RecyclerFrag_Main.user_fat1)) + (String.valueOf(Food_RecyclerFrag_Main.user_carbs1)) + (String.valueOf(Food_RecyclerFrag_Main.user_protein1)) + (String.valueOf(Food_RecyclerFrag_Main.calRef1)));

                    getCaloriesRef("totalcalories").setValue(caloriecount);
                    getCaloriesRef("totalfat").setValue(totalfat);
                    getCaloriesRef("totalcarbs").setValue(totalcarbs);
                    getCaloriesRef("totalprotein").setValue(totalprotein);

                    if (count == 1) {
                        String toast1 = String.valueOf(count) + "item added";
                        Toast.makeText(mContext, toast1, Toast.LENGTH_SHORT).show();
                    } else if (count > 1) {
                        String toast2 = String.valueOf(count) + "items added";
                        Toast.makeText(mContext, toast2, Toast.LENGTH_SHORT).show();
                    }
                }
            });
            JSONArray j = null;
        }
    }
}



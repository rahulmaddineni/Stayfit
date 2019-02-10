package com.example.maddi.fitness;

import android.content.Intent;
import android.location.Address;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
//import com.google.android.gms.location.places.AutocompleteFilter;
//import com.google.android.gms.location.places.Place;
//import com.google.android.gms.location.places.ui.PlaceAutocomplete;
//import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
//import com.google.android.gms.location.places.ui.PlaceSelectionListener;

import java.util.Locale;

public class AskLocationActivity extends AppCompatActivity {

    AddressResultReceiver mResultReceiver;

    EditText addressEdit;

    TextView infoText;

    boolean fetchAddress;
    int fetchType = Constants.USE_ADDRESS_LOCATION;
    private static final String TAG = "MAIN_ACTIVITY";
    public static TextView placename = null;
    public static String place1;
    public Button invite;
    public static Address address1 = new Address(Locale.getDefault());
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
        final String TAG1 = AskLocationActivity.class.getSimpleName();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asklocation);
        invite = (Button) findViewById(R.id.invite);
        invite.setVisibility(View.INVISIBLE);
        // Go to Map Load page
        invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AskLocationActivity.this, LoadMapActivity.class);
                //Intent intent = new Intent(AskLocationActivity.this, LatLongActivity.class);
                startActivity(intent);
            }
        });

        // Send Data to LoadMapActivity Lat and Long
        /*Intent intent = null;
        intent.putExtra("Latitude",address1.getLatitude());
        intent.putExtra("Longitude",address1.getLongitude());
        startActivity(intent);*/
        // placename = (TextView) findViewById(R.id.place_name);

        /* ---- Auto Complete Search for Google Maps ---- */
//        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
//                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
//
//        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
//            @Override
//            public void onPlaceSelected(Place place) {
//                // TODO: Get info about the selected place.
//                Log.i(TAG1, "Place: " + place.getName());
//                // placename.setText(place.getName());
//                place1 = place.getName().toString();
//                addressEdit.setText(place.getName().toString());
//            }
//
//            @Override
//            public void onError(Status status) {
//                // TODO: Handle the error.
//                Log.i(TAG1, "An error occurred: " + status);
//            }
//
//            //  @Override
//            protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//                if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
//                    if (resultCode == RESULT_OK) {
//                        Place place = PlaceAutocomplete.getPlace(AskLocationActivity.this, data);
//                        Log.i(TAG1, "Place: " + place.getName());
//                    } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
//                        Status status = PlaceAutocomplete.getStatus(AskLocationActivity.this, data);
//                        // TODO: Handle the error.
//                        Log.i(TAG1, status.getStatusMessage());
//
//                    } else if (resultCode == RESULT_CANCELED) {
//                        // The user canceled the operation.
//                    }
//                }
//            }
//        });
//        AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
//                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_NONE)
//                        //.setTypeFilter(AutocompleteFilter.TYPE_FILTER_ADDRESS)
//                .build();
//
//        autocompleteFragment.setFilter(typeFilter);

/* ----- Get Lat and Long from Place Name ----- */
        addressEdit = (EditText) findViewById(R.id.addressEdit);
        infoText = (TextView) findViewById(R.id.infoText);
        mResultReceiver = new AddressResultReceiver(null);
        fetchAddress = false;
        fetchType = Constants.USE_ADDRESS_NAME;
        addressEdit.setEnabled(true);
        addressEdit.requestFocus();

    }
    public void onButtonClicked(View view) {
        Intent intent = new Intent(this, GeocodeAddressIntentService.class);
        intent.putExtra(Constants.RECEIVER, mResultReceiver);
        intent.putExtra(Constants.FETCH_TYPE_EXTRA, fetchType);
        if(fetchType == Constants.USE_ADDRESS_NAME) {
            if(addressEdit.getText().length() == 0) {
                Toast.makeText(this, "Please enter an address name", Toast.LENGTH_LONG).show();
                return;
            }
            invite.setVisibility(View.VISIBLE);
            intent.putExtra(Constants.LOCATION_NAME_DATA_EXTRA, addressEdit.getText().toString());
        }
        Log.e(TAG, "Starting Service");
        startService(intent);
    }

    class AddressResultReceiver extends ResultReceiver {
        public Address address;
        public AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, final Bundle resultData) {
            if (resultCode == Constants.SUCCESS_RESULT) {
                address = resultData.getParcelable(Constants.RESULT_ADDRESS);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        infoText.setVisibility(View.VISIBLE);
                        infoText.setText("Latitude: " + address.getLatitude() + "\n" +
                                "Longitude: " + address.getLongitude() + "\n" +
                                "Address: " + resultData.getString(Constants.RESULT_DATA_KEY));
                        infoText.setVisibility(View.INVISIBLE);
                        address1.setLatitude(address.getLatitude());
                        address1.setLongitude(address.getLongitude());
                    }
                });
            }
            else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        infoText.setVisibility(View.VISIBLE);
                        infoText.setText(resultData.getString(Constants.RESULT_DATA_KEY));
                    }
                });
            }
        }
    }
}


package com.example.maddi.fitness;

import android.content.Intent;
import android.database.Cursor;
import android.location.Address;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by maddi on 4/11/2016.
 */
public class LoadMapActivity extends AppCompatActivity {

    public final int PICK_CONTACT = 2015;
    private final String DEBUG_TAG = "SMS Manager";
    public String phoneno = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        int permissionCheck = ContextCompat.checkSelfPermission(LoadMapActivity.this,
//                Manifest.permission.LOCATION_HARDWARE);
//        ActivityCompat.requestPermissions(LoadMapActivity.this,
//                new String[]{Manifest.permission.LOCATION_HARDWARE},);
        setContentView(R.layout.activity_mapload);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, new MapLoadFragment().newInstance())
                    .commit();
        }

        ImageView contact = (ImageView) (findViewById(R.id.contact_picker));
        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                startActivityForResult(i, PICK_CONTACT);
            }
        });

        Button invite = (Button) findViewById(R.id.invite);
        invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendSMSMessage();
            }
        });

    }

    // Contacts picker
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_CONTACT && resultCode == RESULT_OK) {
            Uri contactUri = data.getData();
            Cursor cursor = getContentResolver().query(contactUri, null, null, null, null);
            cursor.moveToFirst();
            int column = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            Log.d("phone number", cursor.getString(column));
            TextView dummy = (TextView) findViewById(R.id.dummy);
            dummy.setText(cursor.getString(column));
            phoneno = cursor.getString(column);
        }
    }

    // Send SMS
    protected void sendSMSMessage() {
        Log.i("Send SMS", "");
        String phoneNo = phoneno;
        //String lat = getIntent().getExtras().getString("Latitude");
        //String lng = getIntent().getExtras().getString("Longitude");
        // Replace latitude and longitude values
        Address msgaddress = AskLocationActivity.address1;
        String message = "Shall we run together, Location:" + "http://maps.google.com/?q=" + msgaddress.getLatitude() + "," + msgaddress.getLongitude();

        Log.d("Message", message);
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, message, null, null);
            Toast.makeText(getApplicationContext(), "Invitation sent.", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "SMS faild, please try again.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
}

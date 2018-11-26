package com.example.rb25788.smartring;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApi;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    public static final int ERROR_DIALOG_REQUEST = 9001;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (isServiceOk()) {
            init();
        }
    }

    private void init() {
        Button btnMap = findViewById(R.id.btnMap);
        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MapActivity.class);
                startActivity(intent);
            }
        });
    }

    public boolean isServiceOk() {
        Log.d(TAG, "isServiceOk : checking google services version");
        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(MainActivity.this);
        if (available == ConnectionResult.SUCCESS) {
            //everything is fine and user can make map request
            Log.d(TAG, "isServicesOk: Google PLay Services is working");
            return true;
        }
        else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            // an error occured but it can be fixed
            Log.d(TAG, "isServicesOK: an error occured but we can fix it");
        }
        else {
            Toast.makeText(this, "Ther is nothing you can do", Toast.LENGTH_SHORT).show();
        }
        return false;
    }
}

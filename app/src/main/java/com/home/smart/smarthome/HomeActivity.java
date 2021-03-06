package com.home.smart.smarthome;

import android.content.Intent;
import android.os.Bundle;
import android.renderscript.Double2;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeActivity extends AppCompatActivity {
    private TextView temperature,humidity;
    private Switch kitchenLight,roomLight,roomFan;
    DatabaseReference mRef= FirebaseDatabase.getInstance().getReference();
    DatabaseReference mTemp=mRef.child("Temperature");
    DatabaseReference kitchenLightRef=mRef.child("kitchenLight");
    DatabaseReference roomLightRef=mRef.child("roomLight");
    DatabaseReference roomFanRef=mRef.child("roomFan");
    DatabaseReference humidityRef=mRef.child("Humidity");
    boolean flag[]=new boolean[3];
    void log(String s){
        System.out.println("Log Msg ="+s);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        flag[0]=flag[1]=flag[2]=false;
        setContentView(R.layout.activity_home);
        mRef.keepSynced(true);
        temperature=(TextView) findViewById(R.id.TemperatureDisplay);
        humidity=(TextView) findViewById(R.id.HumidityDisplay);
        kitchenLight=(Switch)findViewById(R.id.kitchenLight);
        roomLight=(Switch)findViewById(R.id.roomLight);
        roomFan=(Switch)findViewById(R.id.roomFan);
        try {
            String data = getIntent().getExtras().getString("QRCODE");
            checkForQR(data);
        }
        catch(Exception e){

        }
    }
    public void checkForQR(String data){
        Toast.makeText(getApplicationContext(),"Data : "+data,Toast.LENGTH_SHORT).show();
        if(data.equals("123")){
            Toast.makeText(getApplicationContext(),"in : "+flag[0],Toast.LENGTH_SHORT).show();
            kitchenLightRef.setValue(!flag[0]);
        }
        if(data == "roomLight")
        roomLightRef.setValue(roomLight.isChecked());
        if(data == "roomFan")
        roomFanRef.setValue(roomFan.isChecked());
    }
    public void switchListner(View v){
        System.out.println(" View "+v.getId()+" is "+kitchenLight.isChecked() );
        switch (v.getId()){
            case R.id.kitchenLight:
                    kitchenLightRef.setValue(kitchenLight.isChecked());
                break;
            case R.id.roomLight:
                roomLightRef.setValue(roomLight.isChecked());
                break;
            case R.id.roomFan:
                roomFanRef.setValue(roomFan.isChecked());
                break;
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        mTemp.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Double update=dataSnapshot.getValue(Double.class);
                temperature.setText(""+update);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        humidityRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Double update=dataSnapshot.getValue(Double.class);
                humidity.setText(""+update);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        kitchenLightRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Boolean update=dataSnapshot.getValue(Boolean.class);
                flag[0]=update;
                kitchenLight.setChecked(update);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        roomLightRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Boolean update=dataSnapshot.getValue(Boolean.class);
                flag[1]=update;
                roomLight.setChecked(update);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        roomFanRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Boolean update=dataSnapshot.getValue(Boolean.class);
                flag[2]=update;
                roomFan.setChecked(update);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void allOff(){
        kitchenLightRef.setValue(false);
        roomFanRef.setValue(false);
        roomFanRef.setValue(false);
        System.out.println("Call done");
    }
    @Override
    protected void onDestroy() {
        System.out.println("Destroy called");
        allOff();
        super.onDestroy();
    }
    public void QRButton(View v){
        Intent in=new Intent(this,QRScannerActivity.class);
        startActivity(in);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

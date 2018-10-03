package com.example.wycliffenyakemwa.mqttchatapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.example.wycliffenyakemwa.mqttchatapp.utilities.Constants;
import com.example.wycliffenyakemwa.mqttchatapp.R;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.UnsupportedEncodingException;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {




    String TAG = "MyMQTTApp";
    Button publish,connect;
    MainActivity mqttInstance;
    TextInputEditText topic, theMessage;

    //String clientId = MqttClient.generateClientId();

    Constants constants = new Constants();
    MqttConnectOptions options = new MqttConnectOptions();
    MqttAndroidClient client = new MqttAndroidClient(MainActivity.this, "tcp://broker.hivemq.com:1883",
            constants.getClientId());


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



        getSupportActionBar().setTitle("Publish here");

        connect = findViewById(R.id.buttonConnect1);
        publish = findViewById(R.id.buttonPublish);
        topic = findViewById( R.id.etTopic);
        theMessage = findViewById( R.id.etMessage);

        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                connectMQTT();
                Toast.makeText(MainActivity.this, constants.getClientId(),Toast.LENGTH_LONG).show();


            }
        });

        publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                Log.d(TAG , topic.getText().toString()  );
                Log.d(TAG , theMessage.getText().toString()  );

                if (client.isConnected()) {

                    publishRetainedMQTT(topic.getText().toString() , theMessage.getText().toString() );
                    Toast.makeText(MainActivity.this, "Message has been published, yeey ",Toast.LENGTH_LONG).show();
                }
                else{

                    Toast.makeText(MainActivity.this, "No longer connected to broker",Toast.LENGTH_LONG).show();
                }

            }

        });

    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_subscribe) {

            Intent myIntent = new Intent(MainActivity.this, Subscriber.class);
            startActivity(myIntent);

        } else if (id == R.id.nav_chat){

            Intent myIntent = new Intent(MainActivity.this, Chat.class);
            startActivity(myIntent);

       }
       else if (id == R.id.nav_chat) {

            Intent myIntent = new Intent(MainActivity.this, Chat.class);
            startActivity(myIntent);

        }
//        else if (id == R.id.nav_share) {
//
//        } else if (id == R.id.nav_send) {
//
//        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    public void connectMQTT(){

        try {
            IMqttToken token = client.connect(options);
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // We are connected
                    Log.d(TAG, "onSuccess");
                    Toast.makeText(MainActivity.this, "Connection Successfully Established",Toast.LENGTH_LONG).show();
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    // Something went wrong e.g. connection timeout or firewall problems
                    Log.d(TAG, "onFailure");
                    Toast.makeText(MainActivity.this, "Connection not established",Toast.LENGTH_LONG).show();
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }



    public void publishRetainedMQTT(String topic, String payload){

        byte[] encodedPayload = new byte[0];
        try {
            encodedPayload = payload.getBytes("UTF-8");
            MqttMessage message = new MqttMessage(encodedPayload);
            message.setRetained(true);
            client.publish(topic, message);


        } catch (UnsupportedEncodingException | MqttException e) {
            e.printStackTrace();
            Toast.makeText(MainActivity.this, e.getMessage(),Toast.LENGTH_LONG).show();

        }
    }// end publish


}

package com.example.wycliffenyakemwa.mqttchatapp;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class Subscriber extends AppCompatActivity {

    String TAG = "MyMQTTApp";
    Button subscribe, connect;
    TextInputEditText topic, theMessage;

    Constants constants = new Constants();
    MqttConnectOptions options = new MqttConnectOptions();
    MqttAndroidClient client = new MqttAndroidClient(Subscriber.this, "tcp://broker.hivemq.com:1883",
            constants.getClientId());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscriber);

        connect = (Button) findViewById(R.id.buttonConnect);
        subscribe = (Button) findViewById(R.id.buttonSubscribe);
        topic = (TextInputEditText) findViewById( R.id.etTopic);
        theMessage = (TextInputEditText) findViewById( R.id.etMessage);



        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Subscribe here");


        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                connectMQTT();

            }
        });

        subscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //connectMQTT();
                Log.d(TAG , topic.getText().toString());

                if (client.isConnected()) {

                    subscribeMQTT(topic.getText().toString() , 2 );
                    Toast.makeText(Subscriber.this, "Message has been published, ",Toast.LENGTH_LONG).show();

                    client.setCallback(new MqttCallback() {
                        @Override
                        public void connectionLost(Throwable cause) {

                        }

                        @Override
                        public void messageArrived(String topic, MqttMessage message) throws Exception {

                            String stringMessage =  new String(message.getPayload());
                            theMessage.setText(stringMessage);

                        }

                        @Override
                        public void deliveryComplete(IMqttDeliveryToken token) {

                        }
                    });
                }
                else{

                    Toast.makeText(Subscriber.this, "Connection broken",Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    public void connectMQTT(){

        try {
            IMqttToken token = client.connect(options);
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // We are connected
                    Log.d(TAG, "onSuccessConn");
                    Toast.makeText(Subscriber.this, "Connection Successfully Established",Toast.LENGTH_LONG).show();
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    // Something went wrong e.g. connection timeout or firewall problems

                    Log.d(TAG, "onFailureConn");
                    Toast.makeText(Subscriber.this, "Connection not established",Toast.LENGTH_LONG).show();

                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
            Log.d(TAG,e.getMessage());
        }
    }// end Connect



    public void subscribeMQTT(String topic, int qos){

        try {
            IMqttToken subToken = client.subscribe(topic, qos);
            subToken.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {

                    Log.d(TAG, "onSuccess");
                    Toast.makeText(Subscriber.this, " Message was published "  ,Toast.LENGTH_LONG).show();
                    // asyncActionToken.getResponse();
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken,
                                      Throwable exception) {
                    // The subscription could not be performed, maybe the user was not
                    // authorized to subscribe on the specified topic e.g. using wildcards

                    Log.d(TAG, "onFailure");
                    Toast.makeText(Subscriber.this, " user was not authorized to subscribe on the specified topic",Toast.LENGTH_LONG).show();
                    exception.getStackTrace();
                }
            });

        } catch (MqttException e) {

            e.printStackTrace();
            Toast.makeText(Subscriber.this, e.getMessage(),Toast.LENGTH_LONG).show();


        }

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:

                Intent intent = new Intent(Subscriber.this, MainActivity.class);
                startActivity(intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

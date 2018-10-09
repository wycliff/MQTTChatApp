package com.example.wycliffenyakemwa.mqttchatapp.activity;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.wycliffenyakemwa.mqttchatapp.R;
import com.example.wycliffenyakemwa.mqttchatapp.adapter.MessageAdapter;
import com.example.wycliffenyakemwa.mqttchatapp.model.Message;
import com.example.wycliffenyakemwa.mqttchatapp.utilities.Constants;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.UnsupportedEncodingException;

public class Chat extends AppCompatActivity {


    MainActivity mqttInstance;
    private EditText editText;
    private MessageAdapter messageAdapter;
    private ListView messagesView;
    Message messageObject;

    String TAG = "MyMQTTApp";
    String topic = "our/chat";

    //String clientId = MqttClient.generateClientId();
    Constants constants = new Constants();
    MqttConnectOptions options = new MqttConnectOptions();
    MqttAndroidClient client = new MqttAndroidClient(Chat.this, "tcp://broker.hivemq.com:1883",
            constants.getClientId());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Live Chat");

        editText = findViewById(R.id.editText);

        messageAdapter = new MessageAdapter(this);
        messagesView = findViewById(R.id.messages_view);
        messagesView.setAdapter(messageAdapter);

        connectMQTT();

        if(client.isConnected()){
            subscribeMQTT( topic, 2 );

            Toast.makeText(Chat.this, "Message has been published, ",Toast.LENGTH_LONG).show();

            client.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {
                    connectMQTT(); // to reconnect
                }
                @Override
                public void messageArrived(String topic, MqttMessage message) {
                    String stringMessage =  new String(message.getPayload());
                    messageObject = new Message(stringMessage , topic, 2, false);
                    messageAdapter.add(messageObject);
                    messagesView.setSelection(messagesView.getCount() - 1);
                    Toast.makeText(Chat.this, stringMessage ,Toast.LENGTH_LONG).show();
                }
                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {

                }
            });
        }

        else{

            connectMQTT();

            if(client.isConnected()){
                subscribeMQTT( topic, 2 );

                Toast.makeText(Chat.this, "Message has been published, ",Toast.LENGTH_LONG).show();

                client.setCallback(new MqttCallback() {
                    @Override
                    public void connectionLost(Throwable cause) {
                        connectMQTT(); // to reconnect
                    }

                    @Override
                    public void messageArrived(String topic, MqttMessage message) {
                        String stringMessage =  new String(message.getPayload());
                        messageObject = new Message(stringMessage , topic, 2, false);
                        messageAdapter.add(messageObject);
                        messagesView.setSelection(messagesView.getCount() - 1);


                        Toast.makeText(Chat.this, stringMessage ,Toast.LENGTH_LONG).show();
                    }
                    @Override
                    public void deliveryComplete(IMqttDeliveryToken token) {

                    }
                });
            }
        }
    }




    // send message (publish)
    public void sendMessage(View view) {
        connectMQTT();
        String  typed = editText.getText().toString();
        if (typed.length() > 0) {

            // mqtt stuff in here
            publishRetainedMQTT(topic , typed);

            messageObject = new Message(typed, "our/chat" , 2, true);
            messageAdapter.add(messageObject);
            messagesView.setSelection(messagesView.getCount() - 1);
            editText.getText().clear();
        }
        subscribeMQTT(topic,2);
        connectMQTT();
    }


    public void onReceiveMessage(){
        messageObject = new Message("", topic, 2, false);
        messageAdapter.add(messageObject);
        messagesView.setSelection(messagesView.getCount() - 1);
        editText.getText().clear();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(Chat.this, MainActivity.class);
                startActivity(intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    public void connectMQTT(){
        try {
            options.setCleanSession(true);
            options.setKeepAliveInterval(30);
            //options.setAutomaticReconnect(true);

            IMqttToken token = client.connect(options);
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // We are connected
                    Log.d(TAG, "onSuccess");
                    Toast.makeText(Chat.this, "Connection Successfully Established",Toast.LENGTH_LONG).show();
                }
                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    // Something went wrong e.g. connection timeout or firewall problems
                    Log.d(TAG, "onFailure");
                    Toast.makeText(Chat.this, "Connection not established",Toast.LENGTH_LONG).show();
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }


    public void subscribeMQTT(String topic, int qos){

        try {
            IMqttToken subToken = client.subscribe(topic, qos);
            subToken.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {

                    Log.d(TAG, "onSuccess");
                    Toast.makeText(Chat.this, " Message was published "  ,Toast.LENGTH_LONG).show();
                    // asyncActionToken.getResponse();
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken,
                                      Throwable exception) {
                    // The subscription could not be performed, maybe the user was not
                    // authorized to subscribe on the specified topic e.g. using wildcards

                    Log.d(TAG, "onFailure");
                    Toast.makeText(Chat.this, " user was not authorized to subscribe on the specified topic",Toast.LENGTH_LONG).show();
                    exception.getStackTrace();
                }
            });

        } catch (MqttException e) {

            e.printStackTrace();
            Toast.makeText(Chat.this, e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }



    public void publishRetainedMQTT(String topic, String payload){

        byte[] encodedPayload = new byte[0];
        try {
            encodedPayload = payload.getBytes("UTF-8");
            MqttMessage message = new MqttMessage(encodedPayload);
            message.setRetained(true);
            client.publish(topic, message);
            Toast.makeText(Chat.this, " Message was published "  ,Toast.LENGTH_LONG).show();


        } catch (UnsupportedEncodingException | MqttException e) {
            e.printStackTrace();
            Toast.makeText(Chat.this, e.getMessage(),Toast.LENGTH_LONG).show();

        }
    }// end publish


}




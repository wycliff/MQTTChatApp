package com.example.wycliffenyakemwa.mqttchatapp.utilities;

import org.eclipse.paho.client.mqttv3.MqttClient;

public class Constants {


//    private Context mAppContext;
    String clientId ;


//
//    try {
//
//        if (ActivityCompat.checkSelfPermission (mAppContext, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED ) {
//            Toast.makeText(mAppContext, "First enable PHONE STATE ACCESS ", Toast.LENGTH_LONG).show();
//        }
//
//        TelephonyManager tMgr = (TelephonyManager) mAppContext.getSystemService(Context.TELEPHONY_SERVICE);
//         clientId = tMgr.getLine1Number();
//
//    }
//    catch(SecurityException  e){
//
//       e.printStackTrace();
//    }


    public String getClientId(){
        clientId = MqttClient.generateClientId();
        return clientId;
    }
}

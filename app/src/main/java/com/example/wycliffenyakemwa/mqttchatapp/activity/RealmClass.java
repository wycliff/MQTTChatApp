package com.example.wycliffenyakemwa.mqttchatapp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.wycliffenyakemwa.mqttchatapp.R;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class RealmClass extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_realm_class);


        // Initialize realm
        Realm.init(this);

        // Get a Realm instance for this thread
        Realm realm = Realm.getDefaultInstance();

        //configuring a realm , default.
        RealmConfiguration config = new RealmConfiguration.Builder().build();
    }

    public void configureNewRealm() {

//        RealmConfiguration config = new RealmConfiguration.Builder()
//                .name("myrealm.realm")
////                .encryptionKey(getKey())
////                .schemaVersion(42)
////                .modules(new MySchemaModule())
////                .migration(new MyMigration())
////                .build();
    }
}

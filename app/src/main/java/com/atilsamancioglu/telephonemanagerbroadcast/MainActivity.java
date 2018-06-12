package com.atilsamancioglu.telephonemanagerbroadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    TextView callerText;
    private String caller;
    LocalBroadcastManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        callerText = findViewById(R.id.callerText);

        manager = LocalBroadcastManager.getInstance(this);

    }


    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("my.result.receiver");

        manager.registerReceiver(receiver,intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        manager.unregisterReceiver(receiver);
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String incomingNumber = intent.getStringExtra("incomingNumber");
            System.out.println("incomingNumber: " + incomingNumber);
            matchContact(incomingNumber);
        }
    };

    private void matchContact(String number) {

        ArrayList<String> namesFromDB = new ArrayList<String>();
        ArrayList<String> numbersFromDB = new ArrayList<String>();

        HashMap<String, String> contactInfo = new HashMap<String, String>();

        SQLiteDatabase sqLiteDatabase = this.openOrCreateDatabase("com.atilsamancioglu.telephonemanagerbroadcast",MODE_PRIVATE,null);
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS phonebook (name VARCHAR, number VARCHAR)");
        sqLiteDatabase.execSQL("INSERT INTO phonebook (name, number) VALUES ('James', '6505551212')");
        sqLiteDatabase.execSQL("INSERT INTO phonebook (name, number) VALUES ('Lars', '6505551213')");
        sqLiteDatabase.execSQL("INSERT INTO phonebook (name, number) VALUES ('Kirk', '6505551214')");

        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM phonebook",null);

        int nameIx = cursor.getColumnIndex("name");
        int numberIx = cursor.getColumnIndex("number");

        while (cursor.moveToNext()) {

            String nameFromDB = cursor.getString(nameIx);
            String numberFromDB = cursor.getString(numberIx);

            namesFromDB.add(nameFromDB);
            numbersFromDB.add(numberFromDB);

            contactInfo.put(numberFromDB,nameFromDB);


        }

        cursor.close();

        for (String s : numbersFromDB) {
            if (s.matches(number)) {
                caller = contactInfo.get(number);
                callerText.setText("Caller: " + caller);
            } else {
                caller = "";

            }
        }

    }



}

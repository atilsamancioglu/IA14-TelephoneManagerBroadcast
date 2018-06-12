package com.atilsamancioglu.telephonemanagerbroadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

public class MyPhoneReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(final Context context, Intent intent) {

        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        telephonyManager.listen(new PhoneStateListener() {

            @Override
            public void onCallStateChanged(int state, String incomingNumber) {
                super.onCallStateChanged(state, incomingNumber);


                System.out.println("incomingNumber: " + incomingNumber);

                LocalBroadcastManager manager = LocalBroadcastManager.getInstance(context);
                Intent intentToMain = new Intent("my.result.receiver");
                intentToMain.putExtra("incomingNumber",incomingNumber);
                manager.sendBroadcast(intentToMain);

            }
        },PhoneStateListener.LISTEN_CALL_STATE);
    }
}

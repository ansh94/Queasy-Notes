package com.anshdeep.simplenotes;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by ANSHDEEP on 05-04-2016.
 */
public class MyReceiver extends BroadcastReceiver {

    public MyReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String title = intent.getStringExtra("reminder_title");
        String desc = intent.getStringExtra("reminder_desc");
        Intent intent1 = new Intent(context, MyNewIntentService.class);
        intent1.putExtra("rem_title",title);
        intent1.putExtra("rem_desc",desc);
        context.startService(intent1);
    }
}

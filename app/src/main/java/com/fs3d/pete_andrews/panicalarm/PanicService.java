package com.fs3d.pete_andrews.panicalarm;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by peteb_000 on 12/03/2015.
 */
public class PanicService extends Service {

    public Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String arg = msg.getData().getString("arg");
            String content = msg.getData().getString("message");
            switch (arg) {
                case "toast_short":
                    notifyToast(content, Toast.LENGTH_SHORT);
                    break;
                case "toast_long":
                    notifyToast(content, Toast.LENGTH_LONG);
                    break;
                case "notification":
                    String title = "GPS";
                    notifyPanel(title, content, 0);
            }
        }
    };
    private Context ctxt = this.getApplicationContext();

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String[] args;
        try {
            args = intent.getStringArrayExtra("args");
        } catch (Exception e) {
            e.printStackTrace();
            args = new String[]{"NONE"};
        }
        processArguments(args);
        return super.onStartCommand(intent, flags, startId);
    }

    /* The two methods below handle Toast notification requests and Notification Bar information respectively. */

    public void processArguments(String... Args) {
        int size = Args.length;
        for (String a : Args) {
            Log.d("PanicService (Args)", "Processing each of " + String.valueOf(size) + ": " + a);
        }
    }

    public void notifyToast(String output, int length) {
        Toast.makeText(ctxt, output, length).show();
    }

    /* The methods below form the branching off points for the other services the app will need access to.
     * They will all spawn their own threads and broadcast their results back here via ActivityResults.
     */



    /* The following single method encapsulates the ActivityResultHandler for all called services.
     * We will then deal with each result that comes back via each request number in a series of
     * switch-case statements.
     */

    /* The following Handler will deal with any off-thread comms and hand the results back to the UI
     *so that we can present any information we need to the user.
     */

    @TargetApi(16)
    public void notifyPanel(String title, String output, int mode) {
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Intent intent = new Intent(ctxt, PrefsActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(ctxt, 0, intent, 0);
        Notification mNot = new Notification.Builder(ctxt)
                .setContentTitle(title)
                .setContentText(output)
                .setSound(soundUri)
                .addAction(0, "View", pIntent)
                .build();
        NotificationManager notMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notMgr.notify(0, mNot);
    }
}

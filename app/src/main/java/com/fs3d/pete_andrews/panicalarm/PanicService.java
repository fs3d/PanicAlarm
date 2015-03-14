package com.fs3d.pete_andrews.panicalarm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by peteb_000 on 12/03/2015.
 */
public class PanicService extends Service {

    NotificationManager nMgr;
    int notId = 106;
    private Context ctxt = this.getApplicationContext();
    private int notifierGPS = 106;
    private int notifierGeneric = 101;
    public Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String title, ticker;
            String bodytext;
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
                    title = msg.getData().getString("title");
                    ticker = msg.getData().getString("ticker");
                    bodytext = msg.getData().getString("text");
                    notifyPanel(notifierGeneric, new String[]{title, ticker, bodytext});
                    break;
                case "GPSDebug":
                    title = "GPS Coordinate Test";
                    ticker = "GPS Coordinates updated.";
                    bodytext = msg.getData().getString("coords");
                    String[] textArr = bodytext.split(":");
                    String[] composite = new String[textArr.length + 2];
                    composite[0] = title;
                    composite[1] = ticker;
                    for (int i = 0; i < textArr.length; i++) {
                        composite[i + 2] = textArr[i];
                    }
                    updatePanel(notifierGPS, composite);
                    break;
            }
        }
    };

    /* The two methods below handle Toast notification requests and Notification Bar information respectively. */

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

    public void processArguments(String... Args) {
        int size = Args.length;
        for (String a : Args) {
            Log.d("PanicService (Args)", "Processing each of " + String.valueOf(size) + ": " + a);
        }
    }

    public void notifyToast(String output, int length) {
        Toast.makeText(ctxt, output, length).show();
    }

    public void notifyPanel(int _ID, String... args) {
        // Process arguments to determine the type of notification information we will share.
        String title, ticker;
        String[] textArr;
        try {
            title = args[0];
            ticker = args[1];
            textArr = new String[args.length - 2];
            for (int i = 2; i < args.length; i++) {
                textArr[i - 2] = args[i];
            }
        } catch (IndexOutOfBoundsException e) {
            title = "ERROR";
            ticker = "PanicAlarm: Internal error";
            textArr = new String[]{e.toString()};
        }
        // Create a new notification panel.
        // Invoking the default notification service
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);

        mBuilder.setContentTitle(title);
        mBuilder.setContentText(textArr[0]);
        mBuilder.setTicker(ticker);
        mBuilder.setSmallIcon(R.drawable.ic_launcher);

        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();

        // Sets a title for the Inbox style big view
        inboxStyle.setBigContentTitle(title);
        // Moves events into the big view
        for (int i = 0; i < textArr.length; i++) {
            inboxStyle.addLine(textArr[i]);
        }
        mBuilder.setStyle(inboxStyle);

        // Increase notification number every time a new notification arrives
        mBuilder.setNumber(_ID);

        // When the user presses the notification, it is auto-removed
        mBuilder.setAutoCancel(true);

        // Creates an implicit intent
        Intent resultIntent = new Intent("com.fs3d.pete_andrews.TEL_INTENT",
                Uri.parse("tel:123456789"));
        resultIntent.putExtra("from", "FS3D");

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(DebugMode.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_ONE_SHOT);
        mBuilder.setContentIntent(resultPendingIntent);
        nMgr = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        nMgr.notify(_ID, mBuilder.build());
    }

    public void updatePanel(int _ID, String... args) {
        // Update an existing notification panel.
    }

    public void cancelNotification(int notId) {
        String ns = Context.NOTIFICATION_SERVICE;
        nMgr = (NotificationManager) getApplicationContext().getSystemService(ns);
        nMgr.cancel(notId);
    }
}

package com.fs3d.pete_andrews.panicalarm;

import android.content.Context;
import android.telephony.SmsManager;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by peteb_000 on 16/05/2015.
 * <p/>
 * All methods relating to SMS and Email go in here. It will expand to take on IM related tasks.
 * in a later release.
 */
public class MessageManager {

    Context ctxt;

    public MessageManager(Context ctxt) {
        // Empty constructor.
        this.ctxt = ctxt;
    }

    public void SendTestSMS(String phonenum) {
        // Send a single test SMS to the specified phone number.
        String xmsg = "S.O.S TEST: Please disregard for the purposes of an emergency.\n\nThis is a test message only.";
        SmsManager.getDefault().sendTextMessage(phonenum, null, xmsg, null, null);
    }

    public void SendPanic(String phonenum, String coords) {
        String xmsg = "S.O.S! My last known location is: " + coords + "\n\nPlease send help immediately.";
        SmsManager.getDefault().sendTextMessage(phonenum, null, xmsg, null, null);
    }

    public void EmailPanic(String mailto, String coords) {
        String subj = "S.O.S MESSAGE - PLEASE DO NOT IGNORE";
        String xmsg = "I'm in trouble. Please send help immediately. A Google Maps location has been included in this message.";
        // Email code goes in here.
    }

    public ArrayList<String> buildContactList() {
        ArrayList<String> pendingContacts = new ArrayList<>();
        pendingContacts.add("+447411806322"); // This is a debug line only.
        // This will build a Contact List of details using first, Phone numbers, then Mail addresses.
        // The list will include only those contacts currently active in the DB.
        // This list will then be used to send our messages.
        return pendingContacts;
    }

    public void broadcastPanic() {
        // This will be called when a distress condition is met and a broadcast setting exists.
        ArrayList<String> pendingCons = buildContactList();
        int last = pendingCons.size();
        int curr = 0;
        do {
            SendPanic(pendingCons.get(curr), "TEST");
            curr++;
        } while (curr < last);
    }

    public void broadcastTestMsg() {
        // This will only be called from the Debug menu.
        ArrayList<String> pendingCons = buildContactList();
        int last = pendingCons.size();
        int curr = 0;
        String toasty = "Test Message sent to:\n";
        do {
            SendTestSMS(pendingCons.get(curr));
            toasty += pendingCons.get(curr) + "\n";
            curr++;
        } while (curr < last);
        doToast(toasty);
    }

    public void doToast(String output) {
        Toast.makeText(ctxt, output, Toast.LENGTH_LONG).show();
    }
}

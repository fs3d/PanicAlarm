package com.fs3d.pete_andrews.panicalarm.com.fs3d.pete_andrews.panicalarm.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.fs3d.pete_andrews.panicalarm.R;
import com.fs3d.pete_andrews.panicalarm.com.fs3d.pete_andrews.panicalarm.dataops.ContactEntries;

import java.util.List;

/**
 * Created by FS3D on 11/02/2015.
 */
public class ContactDataAdapter extends BaseAdapter {
    private static final String TAG = "ContactDataAdapter";

    private List<ContactEntries> details;
    private LayoutInflater mInflater;

    public ContactDataAdapter(Context ctxt, List<ContactEntries> listContactDetails) {
        this.setItems(listContactDetails);

    }

    @Override
    public int getCount() {
        return (getItems() != null && !getItems().isEmpty()) ? getItems().size() : 0;
    }

    @Override
    public ContactEntries getItem(int pos) {
        return (getItems() != null && !getItems().isEmpty()) ? getItems().get(pos) : null;
    }

    @Override
    public long getItemId(int pos) {
        return (getItems() != null && !getItems().isEmpty()) ? getItems().get(pos).getId() : pos;
    }

    @Override
    public View getView(int pos, View conView, ViewGroup parent) {
        View v = conView;
        ViewHolder holder;
        if (v == null) {
            v = mInflater.inflate(R.layout.list_item_contactable, parent, false);
            holder = new ViewHolder();
            holder.txtContactData = (TextView) v.findViewById(R.id.txt_contact_data);
            holder.txtContactLabel = (TextView) v.findViewById(R.id.txt_contact_label);
            holder.imgConType = (ImageView) v.findViewById(R.id.img_contact_type);
            holder.chkActive = (CheckBox) v.findViewById(R.id.chk_activated);
            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }

        ContactEntries currItem = getItem(pos);
        if (currItem != null) {
            holder.txtContactData.setText(currItem.getData());
            holder.txtContactLabel.setText(currItem.getLabel());
        }
        return v;
    }

    public List<ContactEntries> getItems() {
        return details;
    }

    public void setItems(List<ContactEntries> items) {
        this.details = items;
    }

    class ViewHolder {
        TextView txtContactData;
        TextView txtContactLabel;
        ImageView imgConType;
        CheckBox chkActive;
    }
}

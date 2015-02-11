package com.fs3d.pete_andrews.panicalarm.com.fs3d.pete_andrews.panicalarm.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fs3d.pete_andrews.panicalarm.R;
import com.fs3d.pete_andrews.panicalarm.com.fs3d.pete_andrews.panicalarm.dataops.Persons;

import java.util.List;

/**
 * Created by peteb_000 on 11/02/2015.
 */
public class PersonsAdapter extends BaseAdapter {

    public static final String TAG = "PersonsAdapter";

    private List<Persons> mItems;
    private LayoutInflater mInflater;

    public PersonsAdapter(Context ctxt, List<Persons> listPeople) {
        this.setItems(listPeople);
        this.mInflater = LayoutInflater.from(ctxt);
    }

    @Override
    public int getCount() {
        return (getItems() != null && !getItems().isEmpty()) ? getItems().size() : 0;
    }

    @Override
    public Persons getItem(int pos) {
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
            v = mInflater.inflate(R.layout.list_item_person, parent, false);
            holder = new ViewHolder();
            holder.txtPersonName = (TextView) v.findViewById(R.id.txt_display_name);
            holder.txtPhoneCount = (TextView) v.findViewById(R.id.txt_phone_count);
            holder.txtEmailCount = (TextView) v.findViewById(R.id.txt_email_count);
            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }

        Persons currItem = getItem(pos);
        if (currItem != null) {
            holder.txtPersonName.setText(currItem.getName());
            holder.txtPhoneCount.setText(currItem.getContactId());
            holder.txtEmailCount.setText(String.valueOf(currItem.getId()));
        }
        return v;
    }

    public List<Persons> getItems() {
        return mItems;
    }

    public void setItems(List<Persons> mItems) {
        this.mItems = mItems;
    }

    class ViewHolder {
        TextView txtPersonName;
        TextView txtPhoneCount;
        TextView txtEmailCount;
    }
}

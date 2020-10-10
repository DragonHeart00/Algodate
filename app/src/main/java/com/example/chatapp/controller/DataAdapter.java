package com.example.chatapp.controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chatapp.R;
import com.example.chatapp.data.Card;

import java.util.List;

public class DataAdapter extends ArrayAdapter<Card> {
    Context context;

    public DataAdapter(Context context, int resourceId, List<Card> cardList) {
        super(context, resourceId, cardList);
        this.context = context;
    }

    public View getView(int position, View convertView, ViewGroup viewGroup){
        Card card = getItem(position);

        if (convertView == null ) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item, viewGroup, false);
        }

        TextView name = convertView.findViewById(R.id.my_name);
        ImageView image= convertView.findViewById(R.id.image);

        name.setText(card.getName());
        image.setImageResource(R.drawable.photo1);

        return convertView;
    }
}

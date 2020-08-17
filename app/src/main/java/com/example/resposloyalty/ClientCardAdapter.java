package com.example.resposloyalty;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

public class ClientCardAdapter extends ArrayAdapter<ClientCard> {
    ArrayList<ClientCard> data;
    private Activity context;

    public ClientCardAdapter(Activity context, int resource, int textView, ArrayList<ClientCard> data) {
        super(context, resource, textView, data);
        this.context = context;
        this.data = data;
    }

    @Override
    @NonNull
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        return super.getView(position, convertView, parent);
    }

    //  press spinner method
    @Override
    public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            row = inflater.inflate(R.layout.spinner_cards, parent, false);
        }

        ClientCard item = data.get(position);

        //  set selected item background color
        if(position == ConfirmedClientActivity.posOfItemSpinnerSelected){
            row.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));
        }
        if (item != null) { // парсим данные с каждого объекта
            TextView card = row.findViewById(R.id.cardsCodeTextView);

            if (card != null)
                card.setText(item.getCardCode());
        }
        return row;
    }
}




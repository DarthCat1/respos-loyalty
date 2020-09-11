package com.example.resposloyalty;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.TimeZone;

public class ConfirmedClientActivity extends AppCompatActivity {

    Spinner cardsSpinner;
    public static int posOfItemSpinnerSelected;
    private Client client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = new Client((Authenticator) getIntent().getSerializableExtra("auth"));
        Thread thread = new Thread(client);
        thread.start();
        setContentView(R.layout.activity_confirmed_client);
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        updateClientInfo();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.confirmed_client_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.profile:
                Intent intentProfile = new Intent(ConfirmedClientActivity.this, ProfileActivity.class);
                intentProfile.putExtra("authConfirmedClient", client.getAuth());
                startActivity(intentProfile);
                return (true);
            case R.id.about:
                Toast.makeText(this, R.string.about_toast, Toast.LENGTH_LONG).show();
                return (true);
            case R.id.logout:
                Intent intentMain = new Intent(ConfirmedClientActivity.this, MainActivity.class);
                startActivity(intentMain);
                return (true);
            case R.id.exit:
                finishAffinity();
                return (true);
        }
        return (super.onOptionsItemSelected(item));
    }


    void updateClientInfo() {

        cardsSpinner = findViewById(R.id.cardsSpinner);

        ClientCardAdapter adapter = new ClientCardAdapter(this,
                R.layout.spinner_cards, R.id.cardsCodeTextView, (ArrayList) client.getClientCards());
        // call adapter
        cardsSpinner.setAdapter(adapter);
        final TextView cardCategory = findViewById(R.id.cardCategory);
        final TextView cardStatus = findViewById(R.id.cardStatus);
        final TextView cardActivateDate = findViewById(R.id.cardActivateDate);
        final TextView clientBonus = findViewById(R.id.clientBonus);

        cardsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ClientCard selectedCard;
                posOfItemSpinnerSelected = i;
                if (cardsSpinner.getSelectedItem() != null) {
                    selectedCard = (ClientCard) cardsSpinner.getSelectedItem();
                    cardCategory.setText(selectedCard.getCardCategory());
                    cardStatus.setText(selectedCard.getStatus());

                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                    dateFormat.setTimeZone(TimeZone.getDefault());

                    try {
                        cardActivateDate.setText(selectedCard.getActivateDate().equals(dateFormat.parse("01-01-1970 00:00")) ?
                                "Картка не активована" : dateFormat
                                .format(selectedCard.getActivateDate()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    clientBonus.setText(String.valueOf(client.getBonusCounts()));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

    }
}
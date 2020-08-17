package com.example.resposloyalty;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    EditText passwordEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //  hide password
        passwordEdit = findViewById(R.id.password_edit_text);
        passwordEdit.setTransformationMethod(new PasswordTransformationMethod());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.login_screen_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.about:
                Toast.makeText(this, R.string.about_toast, Toast.LENGTH_LONG).show();
                return (true);
            case R.id.exit:
                finishAffinity();
                return (true);
        }
        return (super.onOptionsItemSelected(item));
    }

    public void tryAuth(View view) {
        final EditText userName = findViewById(R.id.login_edit_text);
        final EditText password = findViewById(R.id.password_edit_text);
        final Authenticator auth = new Authenticator();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                auth.TransferAuth(userName.getText().toString(), password.getText().toString());
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
        TextView info = findViewById(R.id.info_text_view);
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (auth.getErrorMessage().equals("") & auth.getToken() != null) {
            Intent intent = new Intent(MainActivity.this, ConfirmedClientActivity.class);
            intent.putExtra("auth", auth);
            startActivity(intent);
        } else
            info.setText(auth.getErrorMessage());
    }

    public void showHidePass(View view) {
        Button hideShowPass = findViewById(R.id.show_hide_password_button);
        if (hideShowPass.getText().equals("show")) {
            passwordEdit.setTransformationMethod(null);
            hideShowPass.setText("hide");
        } else {
            passwordEdit.setTransformationMethod(new PasswordTransformationMethod());
            hideShowPass.setText("show");
        }
    }

    public void resetPassword(View view) {
        TextView info = findViewById(R.id.info_text_view);
        info.setText("");
    }
}
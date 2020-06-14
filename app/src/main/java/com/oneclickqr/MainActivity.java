package com.oneclickqr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    TextView textViewRe,textViewFo;
    Button buttonLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewRe = (TextView) findViewById(R.id.id_Tv_register);
        textViewFo = (TextView) findViewById(R.id.id_Tv_forgot);
        buttonLogin = (Button) findViewById(R.id.btn_SingIn);


        textViewRe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent rephone = new Intent (MainActivity.this, ContactsContract.CommonDataKinds.Phone.class);
                startActivity(rephone);
                Toast.makeText(getApplicationContext(), "does'nt works", Toast.LENGTH_SHORT).show();
            }
        });

        textViewFo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
                sendIntent.setType("text/plain");
                sendIntent.setPackage("com.whatsapp");
                startActivity(sendIntent);

            }
        });
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Login = new Intent(MainActivity.this, Into.class);
                startActivity(Login);
            }
        });
    }
}

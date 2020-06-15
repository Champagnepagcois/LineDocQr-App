package com.oneclickqr.actividades;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import com.oneclickqr.Into;
import com.oneclickqr.Login.Login;
import com.oneclickqr.MainActivity;
import com.oneclickqr.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Bloquea giro de pantalla
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        setContentView(R.layout.activity_splash);


        compruebaConexion();
    }

    private void compruebaConexion() {

        ConnectivityManager connectivity = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo info_wifi = connectivity.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        NetworkInfo info_datos = connectivity.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if (String.valueOf(info_wifi.getState()).equals("CONNECTED")) {

            Splash1();
        } else {
            if (String.valueOf(info_datos.getState()).equals("CONNECTED")) {

                Splash1();
            } else {

                fallo();

            }
        }

    }

    private void fallo() {
        AlertDialog.Builder dialogo1 = new AlertDialog.Builder(this);
        dialogo1.setTitle("Sin conexion a internet");
        dialogo1.setMessage("¿ Deseas volver a intentar conectar a internet ?");
        dialogo1.setCancelable(false);
        dialogo1.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                compruebaConexion();
            }
        });
        dialogo1.setNegativeButton("Cerrar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                cancelar();
            }
        });
        dialogo1.show();
    }

    public void cancelar() {
        System.exit(0);
    }

    private void Splash1() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent intent = new Intent(SplashActivity.this, Login.class);
                startActivity(intent);
                finish();

            }
        }, 2000);
    }
}
package com.oneclickqr;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.oneclickqr.Login.Login;
import com.oneclickqr.fragments.HomeFragment;
import com.oneclickqr.fragments.ProfileFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.util.Arrays;

public class Into extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener  {

    DrawerLayout drawerLayout;
    Toolbar toolbar;
    NavigationView navigationView;
    ActionBarDrawerToggle toogle;
    private RequestQueue requestQueue;


    private HomeFragment homeFrangment;
    private ProfileFragment profileFragment;
    private BottomNavigationView mMainNav;
    private FrameLayout mMainFrame;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*bloquea el giro de pantalla*/
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_into);

        Intent intent = getIntent();
        //String d = intent.getStringExtra("correo");
        //Toast.makeText(Into.this, "Iniciaste sesion como:" + intent.getStringExtra("correo"),Toast.LENGTH_LONG).show();

        String usern = intent.getStringExtra("correo");

        JSONObject user = new JSONObject();
        try{
            user.put("username",usern);
            }catch (JSONException e){
            e.printStackTrace();
        }
        Submit(user.toString());

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        BarraInferior();

        drawerLayout = findViewById(R.id.drawer);
        try {
            toolbar = findViewById(R.id.toolbar);
        } catch (Exception e) {
        }

        navigationView = findViewById(R.id.navigationView);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toogle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawerOpen, R.string.drawerClose);
        drawerLayout.addDrawerListener(toogle);
        toogle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }


    //Volley POST
    private  void Submit(String data){
        final String savedata = data;
        //Aqui se indica la ip de la computadora donde esta corriendo el servidor
        String URL = "http://192.168.0.3:3000/api/test";

        requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                    Log.e(Into.class.getSimpleName(),"Respuesta del servidor"+ response.toString());
                    String ress = response.toString();
                    String name = "";
                    String surnamep = "";
                    String surnamem = "";


                    if (ress !=null){


                        //Aqui se tiene que pasar el dato fer

                        String na = ress.substring(2,11);
                        Toast.makeText(getApplicationContext(),na,Toast.LENGTH_LONG).show();

                    }else {
                        Log.e(Into.class.getSimpleName(), "No tiene datos registrados");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(),
                                        "No se pudo recivir ningun dato del servidor",
                                        Toast.LENGTH_LONG)
                                        .show();
                            }
                        });

                    };


                    String res = response.toString();
                    Toast.makeText(getApplicationContext(),res,Toast.LENGTH_LONG).show();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();

                //Log.v("VOLLEY", error.toString());
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return savedata == null ? null : savedata.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    //Log.v("Unsupported Encoding while trying to get the bytes", data);
                    return null;
                }
            }

        };
        requestQueue.add(stringRequest);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment myfragment = null;
        Class fragmentClass;

        switch (menuItem.getItemId()) {
           case R.id.internet:

                Qr();

                break;

            case R.id.logout:
                Intent intentReg = new Intent(Into.this, Login.class);
                Into.this.startActivity(intentReg);
                Toast.makeText(getApplicationContext(), "Sesion cerrada", Toast.LENGTH_SHORT).show();
                Into.this.finish();
                break;

            case R.id.app_exit:
                Toast.makeText(getApplicationContext(), "Aplicacion cerrada", Toast.LENGTH_SHORT).show();
                Intent intentRe = new Intent(Into.this, MainActivity.class);
                Into.this.startActivity(intentRe);
                //Sirve para cerrar toda la aplicacion
                //finishAffinity();
                break;
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode,data);
        if (result != null)
            if (result.getContents() !=null){

                AlertDialog.Builder dialogo1 = new AlertDialog.Builder(this);
                dialogo1.setTitle("El codigo escaneado es");
                dialogo1.setMessage("El codigo es"+ result.getContents());
                dialogo1.setCancelable(false);
                dialogo1.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                    }
                });
                dialogo1.show();

            }else{
                AlertDialog.Builder dialogo1 = new AlertDialog.Builder(this);
                dialogo1.setTitle("Error");
                dialogo1.setMessage("¿ Deseas volver a escanear ?");
                dialogo1.setCancelable(false);
                dialogo1.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                    }
                });
                dialogo1.show();
            }
    }

    private void Qr() {

        new IntentIntegrator(Into.this).initiateScan();

    }

    private void BarraInferior() {
        mMainFrame = (FrameLayout) findViewById(R.id.f1content);
        mMainNav = (BottomNavigationView) findViewById(R.id.main_nav);


        homeFrangment = new HomeFragment();
        profileFragment = new ProfileFragment();

        setFragment(homeFrangment);


        mMainNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {

                    case R.id.nav_home:
                        mMainNav.setItemBackgroundResource(R.color.graylight);
                        setFragment(homeFrangment);
                        return true;

                    case R.id.nav_center:
                        mMainNav.setItemBackgroundResource(R.color.graylight);
                        setFragment(profileFragment);
                        return true;

                    default:
                        return false;
                }
            }


        });
    }

    private void setFragment(Fragment fragment) {

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.f1content, fragment);
        fragmentTransaction.commit();
    }
}

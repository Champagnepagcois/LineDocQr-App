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
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.oneclickqr.Login.Login;
import com.oneclickqr.fragments.HomeFragment;
import com.oneclickqr.fragments.ProfileFragment;

public class Into extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener  {

    DrawerLayout drawerLayout;
    Toolbar toolbar;
    NavigationView navigationView;
    ActionBarDrawerToggle toogle;


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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment myfragment = null;
        Class fragmentClass;

        switch (menuItem.getItemId()) {
           case R.id.search:
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://192.168.0.3:3000/"));
                startActivity(browserIntent);
                Toast.makeText(getApplicationContext(), "Abriendo pagina", Toast.LENGTH_SHORT).show();
                break;

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

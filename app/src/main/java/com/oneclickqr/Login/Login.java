package com.oneclickqr.Login;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.oneclickqr.Into;
import com.oneclickqr.R;
import com.oneclickqr.fragments.HomeFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class Login extends AppCompatActivity {
    private RequestQueue requestQueue;
    private FloatingActionButton btnflo;
    private Button btnInto;
    private EditText username,pass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*bloquea el giro de pantalla*/
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
       setContentView(R.layout.activity_login);
        username = (EditText) findViewById(R.id.ET_email);
        pass = (EditText) findViewById(R.id.ETpassword);

        btnInto = (Button) findViewById(R.id.btn_SingIn);
        btnInto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                        String usern = username.getText().toString();
                        String passuser = pass.getText().toString();
                        JSONObject user = new JSONObject();
                        try{
                            user.put("username",usern);
                            user.put("password", passuser);
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                        Submit(user.toString());
            }
        });

        btnflo = (FloatingActionButton) findViewById(R.id.btnQr);
        btnflo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new IntentIntegrator(Login.this).initiateScan();
            }
        });
    }

    //Volley POST
    private  void Submit(String data){
        final String savedata = data;
        String URL = "http://192.168.0.3:3000/api/login";

        requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {

                String suc = response.toString();
                String uno = "1";

                if (suc.equals(uno)){
                    AccFil();
                } else {
                    Toast.makeText(getApplicationContext(), "Fail",Toast.LENGTH_LONG).show();
                }
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

    public void AccFil(){

        Intent into = new Intent(Login.this, Into.class);
        into.putExtra("correo",username.getText().toString());
        startActivity(into);
        Login.this.finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null)
            if (result.getContents() != null) {

                AlertDialog.Builder dialogo1 = new AlertDialog.Builder(this);
                dialogo1.setTitle("Los datos del paciente son:");
                dialogo1.setMessage(result.getContents());
                dialogo1.setCancelable(false);
                dialogo1.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                    }
                });
                dialogo1.show();

            } else {
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
}

package com.oneclickqr.fragments;


import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.oneclickqr.R;

import net.glxn.qrgen.android.QRCode;

public class HomeFragment extends Fragment {
    private TextView tv,tvda;
    private Button btnFH;
    private View rootView;


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        String textoo = getArguments().getString("textFrom");
        tvda = (TextView) view.findViewById(R.id.tv_datoss);
        tvda.setText(textoo);
        // Inflate the layout for this fragment
        btnFH = (Button) view.findViewById(R.id.btnQr);


        String texto = textoo;
        Bitmap bitmap = QRCode.from(texto).bitmap();
        // Suponiendo que tienes un ImageView con el id ivCodigoGenerado
        ImageView imagenCodigo = view.findViewById(R.id.id_imageqr);
        imagenCodigo.setImageBitmap(bitmap);

        return view;
    }

}

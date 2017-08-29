package com.pujhones.bicita.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.davl3232.bicita.R;

public class MainActivity extends AppCompatActivity {

    Button amigosBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        amigosBtn = (Button) findViewById(R.id.amigosBtn);

        amigosBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(view.getContext(), AmigosActivity.class);

                startActivity(in);
            }
        });
    }
}

package com.example.xiaojinggong.yanzhengmatest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.xiaojinggong.yanzhengmatest.YanzhengmaView.VerificationCodeView;

public class MainActivity extends AppCompatActivity {
    private VerificationCodeView view;
    private TextView textView;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        view = (VerificationCodeView)findViewById(R.id.view);
        button = (Button)findViewById(R.id.button);
        textView = (TextView)findViewById(R.id.text);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String a = view.getmText();
                textView.setText(view.getmText());

            }
        });
    }
}

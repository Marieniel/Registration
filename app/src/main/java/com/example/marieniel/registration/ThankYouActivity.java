package com.example.marieniel.registration;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;

public class ThankYouActivity extends AppCompatActivity {
Timer timer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thank_you);

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Intent thankyouIntent = new Intent(ThankYouActivity.this,RegistrationActivity.class);
                startActivity(thankyouIntent);
                finish();
            }
        }, 3000);
    }
}

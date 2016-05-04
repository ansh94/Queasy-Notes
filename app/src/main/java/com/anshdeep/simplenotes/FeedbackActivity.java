package com.anshdeep.simplenotes;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class FeedbackActivity extends AppCompatActivity {

    private final String feedbackText = "If you have any complaints or feedback regarding the app feel free to send a email " +
            "to the developer by clicking on the email floating action button in the bottom right corner.Feedback can be anything" +
            " related to the bugs you are facing or if you want a new feature to be added.If you like the app, you can send" +
            " a positive feedback also :)";

    private TextView feedbackTv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        feedbackTv = (TextView) findViewById(R.id.feedback_html);
        feedbackTv.setText(feedbackText);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.mailFab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendEmail();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    protected void sendEmail() {
        Log.i("Send email", "");
        String[] TO = {"anshdeep.1494@gmail.com"};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("message/rfc822");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Feedback for Simple Notes App");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Enter your feedback here");

        try {
            startActivity(Intent.createChooser(emailIntent, "Send email using"));
            Log.i("Finished sending email", "");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(FeedbackActivity.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }

}

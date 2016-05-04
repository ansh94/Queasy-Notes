package com.anshdeep.simplenotes;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

public class HelpActivity extends AppCompatActivity {

    private final String htmlText = "<body><h3><b>1. Adding a note</h3></b>" + "<p>To add a new note click the + icon in the" +
            " bottom right of the notes section and enter your title and description." +
            "When you are done adding your note click on the the tick mark button and it will get saved in the database " +
            "and you can view it in the main notes screen.</p>" + "<br>" +
            "<h3><b>2. Modifying a note</h3></b>" + "<p>To modify a note click on the note you want to modify from the main notes screen " +
            "and when you are finished editing click the tick mark button and it will be modified in the database.</p>" + "<br>" +
            "<h3><b>3. Adding notes to archive</h3></b>" + "<p>To add a note to archive just swipe the note left or right and it will be" +
            " added to archives. You can view all the archived notes in the archive section of the app.</p>" +
            "<h3><b>4. Deleting single or multiple notes</h3></b>" + "<p>For deleting just long click on the notes you want to delete and press" +
            " the trash button that will appear on the top right when you long click it.</p>" +
            "<h3><b>5. Adding a reminder</h3></b>" + "<p>To add a new reminder note go to the reminder section from the navigation drawer and click on the + button in the bottom " +
            "right corner.After filling all the details in the add reminder screen click on the tick mark button to save your reminder.</p>" + "<br>" +
            "<h3><b>6. Modifying a reminder</h3></b>" + "<p>To modify a reminder follow the same procedure as modifying a note.</p>" +
            "<h3><b>7. Deleting a reminder</h3></b>" + "<p>To delete the reminder just swipe the reminder left or right.</p> </body>";

    private TextView helpBody;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        helpBody = (TextView) findViewById(R.id.text_html);
        helpBody.setMovementMethod(new ScrollingMovementMethod());
        helpBody.setText(Html.fromHtml(htmlText));
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

}

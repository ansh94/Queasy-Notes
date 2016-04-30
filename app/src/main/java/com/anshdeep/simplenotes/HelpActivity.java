package com.anshdeep.simplenotes;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

public class HelpActivity extends AppCompatActivity {

    private final String htmlText = "<body><h3><b>1. Adding a note</h3></b>" +"<p>To add a new note click the + icon in the" +
            " bottom right of the notes section and enter your title and description." +
            "When you are done adding your note click on the the tick mark button and it will get saved in the database " +
            "and you can view it in the main notes screen.</p>"+ "<br>" +
            "<h3><b>2. Modifying a note</h3></b>" + "<p>To modify a note click on the note you want to modify from the main notes screen " +
            "and when you are finished editing click the tick mark button and it will be modified in the database.</p>"+ "<br>"+
            "<h3><b>3. Adding a reminder</h3></b>" +"<p>To add a new reminder note go to the reminder section from the navigation drawer and click on the + button in the bottom "+
            "right corner.After filling all the details in the add reminder screen click on the tick mark button to save your reminder.</p>"+"<br>"+
            "<h3><b>4. Modifying a reminder</h3></b>"+"<p>To modify a reminder follow the same procedure as modifying a note.</p>"+
            "</body>";

//    private TextView helpTitle;
    private TextView helpBody;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        helpTitle = (TextView) findViewById(R.id.helpTitle);
        helpBody = (TextView) findViewById(R.id.text_html);
        helpBody.setMovementMethod(new ScrollingMovementMethod());
        helpBody.setText(Html.fromHtml(htmlText));
    }
    @Override
    protected void onResume() {
        super.onResume();
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case android.R.id.home:
//              NavUtils.navigateUpFromSameTask(this);
//                return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
}

package me.eddiep.android.voting;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;

import me.eddiep.android.voting.data.CandidateListAdapter;
import me.eddiep.android.voting.data.Election;

public class ElectionMoreActivity extends AppCompatActivity {

    private Election election;
    private static final SimpleDateFormat format = new SimpleDateFormat("MMMM dd, yyyy");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getIntent().getExtras() == null || (election = getIntent().getExtras().getParcelable("election")) == null) {
            Intent intent = new Intent(this, ElectionActivity.class);
            startActivity(intent);
            return;
        }

        setContentView(R.layout.election_more);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Candidates");
        setSupportActionBar(toolbar);

        Typeface bold = Typeface.createFromAsset(getAssets(), "GOTHAM-BOLD.TTF");
        Typeface light = Typeface.createFromAsset(getAssets(), "GOTHAM-LIGHT.TTF");

        TextView title = (TextView)findViewById(R.id.electionTitle);
        TextView date = (TextView)findViewById(R.id.electionDate);

        if (title != null) {
            title.setText(election.getName());
            title.setTypeface(bold);
        }

        if (date != null) {
            String formattedDate = format.format(election.getDate().toDate());
            String daysLeftText;
            int daysLeft = election.getDaysRemaining();
            if (daysLeft == 0) {
                daysLeftText = "Today";
            } else if (daysLeft == 1) {
                daysLeftText = "Tomorrow";
            } else {
                daysLeftText = daysLeft + " days away";
            }
            date.setTypeface(light);
            date.setText(formattedDate + "   |   " + daysLeftText);
        }

        CandidateListAdapter adapter = new CandidateListAdapter(this, election.getCandidates(), this);

        GridView list = (GridView) findViewById(R.id.electionGrid);
        if (list != null) {
            list.setAdapter(adapter);
            list.setClickable(true);
            list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(ElectionMoreActivity.this, CandidateProfileActivity.class);
                    intent.putParcelableArrayListExtra("candidates", election.getCandidates());
                    intent.putExtra("startPos", position);

                    startActivity(intent);
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_candidate, menu);
        return true;
    }

    boolean toggleFav;
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_expand) {
            Intent intent = new Intent(this, CandidateProfileActivity.class);
            intent.putParcelableArrayListExtra("candidates", election.getCandidates());

            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

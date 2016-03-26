package me.eddiep.android.voting;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import me.eddiep.android.voting.data.Candidate;
import me.eddiep.android.voting.data.Election;
import me.eddiep.android.voting.data.ElectionListAdapter;

public class ElectionActivity extends AppCompatActivity {

    private Election[] elections;
    private ListView list;
    private ElectionListAdapter normalView;

    private void createElections() {
        ArrayList<Candidate> candidates = new ArrayList<>();
        candidates.add(new Candidate("Hillary Clinton", "Democrat", R.drawable.ic_hillary, R.drawable.ic_hillary_profile, "Hillary Clinton first rose to elective office in 2001 and Stepped down from an elected position in 2009. Clinton has served as an elected official for a total of 8 years."));
        candidates.add(new Candidate("Ted Cruz", "Republican", R.drawable.ic_ted, R.drawable.ic_ted_profile, "Ted Cruz first rose to elective office in 2013 and holds an elected position to this day. Cruz has served as an elected official for a total of 2 years and counting."));
        candidates.add(new Candidate("Donald Trump", "Republican", R.drawable.ic_donald, R.drawable.ic_donald_profile, "Donald John Trump, Sr. is an American businessman, politician, television personality, and candidate for the Republican nomination for President of the United States in 2016 Election."));
        candidates.add(new Candidate("Bernie Sanders", "Democrat", R.drawable.ic_bernie, R.drawable.ic_bernie_profile, "Bernie Sanders first rose to elective office in 1981 and holds an elected position to this day. Sanders has served as an elected official for a total of 34 years and counting"));
        candidates.add(new Candidate("John Kasich", "Republican", R.drawable.ic_john, R.drawable.ic_john_profile, "John Kasich first rose to elective office in 1979 and holds an elected position to this day. Kasich has served as an elected official for a total of 26 years and counting."));
        candidates.add(new Candidate("Gary Johnson", "Libertarian", R.drawable.ic_gary, R.drawable.ic_gary_profile, "Gary Johnson first rose to elective office in 1995 and stepped down from an elected position in 2003. Johnson has served as an elected official for a total of 8 years"));

        elections = new Election[5];

        Calendar calendar = Calendar.getInstance();

        calendar.set(2016, 3, 26);
        elections[0] = new Election("Rhode Island Primary", calendar.getTime(), R.drawable.ic_rhode_island);
        elections[0].setDescription("Description of the event can go here. Hey look at me I'm a multiline textview that is doing great things like going on multiple lines and the such");
        elections[0].setCandidates(candidates);

        calendar.set(2016, 3, 5);
        elections[1] = new Election("Wisconsin Primary", calendar.getTime(), R.drawable.ic_wisconsin);
        elections[1].setCandidates(candidates);

        calendar.set(2016, 3, 9);
        elections[2] = new Election("Wyoming Caucus", calendar.getTime(), R.drawable.ic_wyomings);
        elections[2].setCandidates(candidates);

        calendar.set(2016, 3, 19);
        elections[3] = new Election("New York Primary", calendar.getTime(), R.drawable.ic_new_york);
        elections[3].setCandidates(candidates);

        calendar.set(2016, 10, 11);
        elections[4] = new Election("US Presidential Election", calendar.getTime(), R.drawable.ic_us_general);
        elections[4].setCandidates(candidates);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.election_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("LIST OF ELECTIONS 2016");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            toolbar.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        }
        setSupportActionBar(toolbar);

        createElections();

        final List<Election> temp = Arrays.asList(elections);

        normalView = new ElectionListAdapter(this, temp, this);

        list = (ListView) findViewById(R.id.electionList);
        if (list != null) {
            list.setAdapter(normalView);
            list.setClickable(true);
            list.setDivider(null);
            list.setDividerHeight(0);
            list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Election election = temp.get(position);
                    Intent intent = new Intent(ElectionActivity.this, ElectionDetailActivity.class);
                    intent.putExtra("election", election);
                    startActivity(intent);
                }
            });
        }
    }

    private Menu menu;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
        if (id == R.id.action_fav) {
            toggleFav = !toggleFav;
            if (toggleFav) {
                final List<Election> temp = new ArrayList<>(Arrays.asList(elections));
                Iterator<Election> iterator = temp.iterator();
                while (iterator.hasNext()) {
                    Election eitem = iterator.next();
                    if (!eitem.isPinned())
                        iterator.remove();
                }
                ElectionListAdapter tempAdapter = new ElectionListAdapter(this, temp, this);
                list.setAdapter(tempAdapter);

                menu.getItem(0).setIcon(R.drawable.ic_folder_white_24dp);
            } else {
                list.setAdapter(normalView);

                menu.getItem(0).setIcon(R.drawable.ic_folder_special_white_24dp);
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

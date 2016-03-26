package me.eddiep.android.voting;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.SimpleDateFormat;
import java.util.List;

import me.eddiep.android.voting.data.Election;

public class ElectionDetailActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap map;
    private static final SimpleDateFormat format = new SimpleDateFormat("MMMM dd, yyyy");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Election election;
        if (getIntent().getExtras() == null || (election = getIntent().getExtras().getParcelable("election")) == null) {
            Intent intent = new Intent(this, ElectionActivity.class);
            startActivity(intent);
            return;
        }

        setContentView(R.layout.election_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setTitle("Polling Locations");
        }
        setSupportActionBar(toolbar);

        Typeface bold = Typeface.createFromAsset(getAssets(), "GOTHAM-BOLD.TTF");
        Typeface light = Typeface.createFromAsset(getAssets(), "GOTHAM-LIGHT.TTF");

        TextView title = (TextView)findViewById(R.id.electionTitle);
        TextView date = (TextView)findViewById(R.id.electionDate);
        TextView description = (TextView) findViewById(R.id.electionDescription);
        ImageView background = (ImageView) findViewById(R.id.electionBackground);

        String formattedDate = format.format(election.getDate().toDate());
        if (title != null) {
            title.setTypeface(bold);
            title.setText(election.getName());
        }
        String daysLeftText;
        int daysLeft = election.getDaysRemaining();
        if (daysLeft == 0) {
            daysLeftText = "Today";
        } else if (daysLeft == 1) {
            daysLeftText = "Tomorrow";
        } else {
            daysLeftText = daysLeft + " days away";
        }

        if (date != null) {
            date.setTypeface(light);
            date.setText(formattedDate + "   |   " + daysLeftText);
        }


        if (description != null) {
            description.setTypeface(light);
            description.setText(election.getDescription());
        }

        if (background != null) {
            Drawable d = _getDrawable(this, election.getBackground());
            background.setImageDrawable(d);
        }

        Button button = (Button) findViewById(R.id.electionNotify);
        if (button != null) {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO Set reminder
                    Snackbar.make(v, "Reminder set!", Snackbar.LENGTH_SHORT).show();
                }
            });
        }

        Button button2 = (Button) findViewById(R.id.electionMore);
        if (button2 != null) {
            button2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ElectionDetailActivity.this, ElectionMoreActivity.class);
                    intent.putExtra("election", election);
                    startActivity(intent);
                }
            });
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.map = googleMap;
        Geocoder coder = new Geocoder(this);
        List<Address> address;

        try {
            address = coder.getFromLocationName("1 PROSPECT ST PROVIDENCE, RI 02912", 1);
            LatLng pollingPlace = new LatLng(address.get(0).getLatitude(), address.get(0).getLongitude());
            map.addMarker(new MarkerOptions().position(pollingPlace).title("Polling Place"));
            map.moveCamera(CameraUpdateFactory.newLatLng(pollingPlace));
            googleMap.animateCamera(CameraUpdateFactory.zoomIn());
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    /**
     * Return a drawable object associated with a particular resource ID.
     * <p>
     * Starting in {@link android.os.Build.VERSION_CODES#LOLLIPOP}, the returned
     * drawable will be styled for the specified Context's theme.
     *
     * @param id The desired resource identifier, as generated by the aapt tool.
     *            This integer encodes the package, type, and resource entry.
     *            The value 0 is an invalid identifier.
     * @return Drawable An object that can be used to draw this resource.
     */
    public final Drawable _getDrawable(Context context, int id) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= 21) {
            return ContextCompat.getDrawable(context, id);
        } else {
            return context.getResources().getDrawable(id);
        }
    }
}

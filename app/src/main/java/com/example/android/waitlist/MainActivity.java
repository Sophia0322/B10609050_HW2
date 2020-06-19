package com.example.android.waitlist;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.waitlist.data.WaitlistContract;
import com.example.android.waitlist.data.WaitlistDbHelper;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener{

    private GuestListAdapter mAdapter;
    private SQLiteDatabase mDb;
    private EditText mNewGuestNameEditText;
    private EditText mNewPartySizeEditText;
    private final static String LOG_TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView waitlistRecyclerView;
        // Set local attributes to corresponding views
        waitlistRecyclerView = (RecyclerView) this.findViewById(R.id.all_guests_list_view);
        mNewGuestNameEditText = (EditText) this.findViewById(R.id.person_name_edit_text);
        mNewPartySizeEditText = (EditText) this.findViewById(R.id.party_count_edit_text);

        View mpartySizeView = this.findViewById(R.id.party_size_text_view);

        // Set layout for the RecyclerView, because it's a list we are using the linear layout
        waitlistRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Create a DB helper (this will create the DB if run for the first time)
        WaitlistDbHelper dbHelper = new WaitlistDbHelper(this);

        // Keep a reference to the mDb until paused or killed. Get a writable database
        // because you will be adding restaurant customers
        mDb = dbHelper.getWritableDatabase();

        // Get all guest info from the database and save in a cursor
        Cursor cursor = getAllGuests();

        final String color = new String();
        // Create an adapter for that cursor to display the data
        mAdapter = new GuestListAdapter(this, cursor, color);

        // Link the adapter to the RecyclerView
        waitlistRecyclerView.setAdapter(mAdapter);

        // COMPLETED (3) Create a new ItemTouchHelper with a SimpleCallback that handles both LEFT and RIGHT swipe directions
        // Create an item touch helper to handle swiping items off the list
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            // COMPLETED (4) Override onMove and simply return false inside
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                //do nothing, we only care about swiping
                return false;
            }

            // COMPLETED (5) Override onSwiped
            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {
                // COMPLETED (8) Inside, get the viewHolder's itemView's tag and store in a long variable id
                final RecyclerView.ViewHolder viewHoldert=viewHolder;
                AlertDialog a = new AlertDialog.Builder(MainActivity.this).create();
                a.setTitle("警告視窗");
                a.setMessage("是否確定要刪除？");
                a.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        //get the id of the item being swiped
                        long id = (long) viewHoldert.itemView.getTag();
                        // COMPLETED (9) call removeGuest and pass through that id
                        //remove from DB
                        removeGuest(id);
                        // COMPLETED (10) call swapCursor on mAdapter passing in getAllGuests() as the argument
                        //update the list
                        mAdapter.swapCursor(getAllGuests());
                    }
                });
                a.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mAdapter.swapCursor(getAllGuests());
                        dialog.dismiss();
                    }
                });

                a.show();
            }

            //COMPLETED (11) attach the ItemTouchHelper to the waitlistRecyclerView
        }).attachToRecyclerView(waitlistRecyclerView);


        //color
        /*mVisualizerView = (VisualizerView) findViewById(R.id.activity_visualizer);
        setupSharedPreferences();
        setupPermissions();*/
    }

    /**
     * This method is called when user clicks on the Add to waitlist button
     *
     * @param view The calling view (button)
     */
    public void addToWaitlist(View view) {
        if (mNewGuestNameEditText.getText().length() == 0 ||
                mNewPartySizeEditText.getText().length() == 0) {
            return;
        }
        //default party size to 1
        int partySize = 1;
        try {
            //mNewPartyCountEditText inputType="number", so this should always work
            partySize = Integer.parseInt(mNewPartySizeEditText.getText().toString());
        } catch (NumberFormatException ex) {
            Log.e(LOG_TAG, "Failed to parse party size text to number: " + ex.getMessage());
        }

        // Add guest info to mDb
        addNewGuest(mNewGuestNameEditText.getText().toString(), partySize);

        // Update the cursor in the adapter to trigger UI to display the new list
        mAdapter.swapCursor(getAllGuests());

        //clear UI text fields
        mNewPartySizeEditText.clearFocus();
        mNewGuestNameEditText.getText().clear();
        mNewPartySizeEditText.getText().clear();
    }

    /**
     * Query the mDb and get all guests from the waitlist table
     *
     * @return Cursor containing the list of guests
     */
    private Cursor getAllGuests() {
        return mDb.query(
                WaitlistContract.WaitlistEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                WaitlistContract.WaitlistEntry.COLUMN_TIMESTAMP
        );
    }

    /**
     * Adds a new guest to the mDb including the party count and the current timestamp
     *
     * @param name  Guest's name
     * @param partySize Number in party
     * @return id of new record added
     */
    private long addNewGuest(String name, int partySize) {
        ContentValues cv = new ContentValues();
        cv.put(WaitlistContract.WaitlistEntry.COLUMN_GUEST_NAME, name);
        cv.put(WaitlistContract.WaitlistEntry.COLUMN_PARTY_SIZE, partySize);
        return mDb.insert(WaitlistContract.WaitlistEntry.TABLE_NAME, null, cv);
    }

    // COMPLETED (1) Create a new function called removeGuest that takes long id as input and returns a boolean
    /**
     * Removes the record with the specified id
     *
     * @param id the DB id to be removed
     * @return True: if removed successfully, False: if failed
     */
    private boolean removeGuest(long id) {
        // COMPLETED (2) Inside, call mDb.delete to pass in the TABLE_NAME and the condition that WaitlistEntry._ID equals id
        return mDb.delete(WaitlistContract.WaitlistEntry.TABLE_NAME, WaitlistContract.WaitlistEntry._ID + "=" + id, null) > 0;
    }

    // 讓menu可以work
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_item, (Menu) menu);
        return super.onCreateOptionsMenu(menu);
    }

    // When the "Add" menu item is pressed, open Main2Activity; "Settings" menu item is pressed, open SettingsActivity
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings_add) {
            Intent intent = new Intent(this, Main2Activity.class);
            startActivity(intent);
            return true;
        }
        else if(id == R.id.action_settings){
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }else {
            return super.onOptionsItemSelected(item);
        }

    }

    // COMPLETED (4) Update setupSharedPreferences and onSharedPreferenceChanged to load the color
    // from shared preferences. Call setColor, passing in the color you got
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void setupSharedPreferences() {
        // Get all of the values from shared preferences to set it up
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        loadColorFromPreferences(sharedPreferences);
        // Register the listener
        sharedPreferences.registerOnSharedPreferenceChangeListener((SharedPreferences.OnSharedPreferenceChangeListener) this);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void loadColorFromPreferences(SharedPreferences sharedPreferences) {
        /*SharedPreferences.Editor editor = getSharedPreferences(String.valueOf(sharedPreferences),
                MODE_PRIVATE).edit();
        String color_change = null;
        editor.putString("color_change", String.valueOf(sharedPreferences));

       */

        /*SharedPreferences sharedPref = getSharedPreferences("colorPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        String color_change = null;
        editor.putString("color_change", String.valueOf(sharedPreferences));
        editor.commit();*/
       // editor.putInt("idName", 12);

        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(this);
        String option = pref.getString("pref_color_option_value", "1"); //取顏色的文字&顏色對應的值
        String[] optionText= getResources().getStringArray(R.array.pref_color_option_labels);
        //System.out.println(option);
        //System.out.println(optionText);
        //return optionText[Integer.parseInt(option)]; //傳回顏色對應的值

        Intent it=new Intent();
        Bundle bundle=new Bundle();
        bundle.putString("color",option);
        it.putExtras(bundle);


        //System.out.println(editor);
        //System.out.println(color_change);

    }

    public interface IMethodCaller {
        String getColorPreference();
    }

    public String getColorPreference(){
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(this);
        String option = pref.getString("pref_color_option_value", "1"); //取顏色的文字&顏色對應的值
        String[] optionText= getResources().getStringArray(R.array.pref_color_option_labels);
        //System.out.println(option);
        //System.out.println(optionText);
        //return optionText[Integer.parseInt(option)]; //傳回顏色對應的值
        System.out.println("hi");
        return optionText[Integer.parseInt(option)]; //傳回顏色對應的值
    }

    // Updates the screen if the shared preferences change. This method is required when you make a
    // class implement OnSharedPreferenceChangedListener
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.pref_color_key))) {
            loadColorFromPreferences(sharedPreferences);
        }
    }
}
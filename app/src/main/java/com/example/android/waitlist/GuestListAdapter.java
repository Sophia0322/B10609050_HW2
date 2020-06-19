package com.example.android.waitlist;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.waitlist.data.WaitlistContract;


public class GuestListAdapter extends RecyclerView.Adapter<GuestListAdapter.GuestViewHolder> {

    public static String color_chooser;
    // Holds on to the cursor to display the waitlist
    private Cursor mCursor;
    private Context mContext;
    //MainActivity m= new MainActivity();
    //String color = m.getColorPreference();
    final String color;

    /**
     * Constructor using the context and the db cursor
     * @param context the calling context/activity
     * @param cursor the db cursor with waitlist data to display
     * @param color
     */
    public GuestListAdapter(Context context, Cursor cursor, String color) {
        this.mContext = context;
        this.mCursor = cursor;
        this.color = color;
    }

    @Override
    public GuestViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Get the RecyclerView item layout
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.guest_list_item, parent, false);

        SharedPreferences sharedPref = view.getContext().getSharedPreferences("colorPref", Context.MODE_PRIVATE);
        String color_change=sharedPref.getString("option",null);

        return new GuestViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(GuestViewHolder holder, int position) {
        // Move the mCursor to the position of the item to be displayed
        if (!mCursor.moveToPosition(position))
            return; // bail if returned null

        // Update the view holder with the information needed to display
        String name = mCursor.getString(mCursor.getColumnIndex(WaitlistContract.WaitlistEntry.COLUMN_GUEST_NAME));
        int partySize = mCursor.getInt(mCursor.getColumnIndex(WaitlistContract.WaitlistEntry.COLUMN_PARTY_SIZE));
        // COMPLETED (6) Retrieve the id from the cursor and
        long id = mCursor.getLong(mCursor.getColumnIndex(WaitlistContract.WaitlistEntry._ID));

        Intent intent = ((MainActivity) mContext).getIntent();


        // Display the guest name
        holder.nameTextView.setText(name);
        // Display the party count
        holder.partySizeTextView.setText(String.valueOf(partySize));
        // COMPLETED (7) Set the tag of the itemview in the holder to the id
        holder.itemView.setTag(id);

        holder.partySizeTextView.setBackground(ContextCompat.getDrawable(mContext, R.drawable.circle));

        /*SharedPreferences sharedPref = mContext.getSharedPreferences("colorPref", Context.MODE_PRIVATE);
        String color_change=sharedPref.getString("color_change",null);
        System.out.println(color_change);
        SharedPreferences pref= mContext.getSharedPreferences(MainActivity.this);
        String option = pref.getString("pref_color_option_value", "1"); //取顏色的文字&顏色對應的值*/



        /*
        switch(color){
            case "Red":
                holder.partySizeTextView.setBackground(ContextCompat.getDrawable(mContext, R.drawable.circle_red));
                break;
            case "Blue":
                holder.partySizeTextView.setBackground(ContextCompat.getDrawable(mContext, R.drawable.circle));
                break;
            case "Green":
                holder.partySizeTextView.setBackground(ContextCompat.getDrawable(mContext, R.drawable.circle_green));
                break;
        }
*/
        /*if (partySize>40) {
            holder.partySizeTextView.setBackground(ContextCompat.getDrawable(mContext, R.drawable.circle_red));
        }*/

        /*if (mContext instanceof MainActivity) {
            String colorpref=((MainActivity)mContext).getColorPreference();
            switch(colorpref){
                case "Red":
                    holder.partySizeTextView.setBackground(ContextCompat.getDrawable(mContext, R.drawable.circle_red));
                    break;
                case "Blue":
                    holder.partySizeTextView.setBackground(ContextCompat.getDrawable(mContext, R.drawable.circle));
                    break;
                case "Green":
                    holder.partySizeTextView.setBackground(ContextCompat.getDrawable(mContext, R.drawable.circle_green));
                    break;
            }
        }
*/

        /*
        if (mContext instanceof MainActivity.IMethodCaller) {
            String colorpref=((MainActivity.IMethodCaller) mContext).getColorPreference();
            switch(colorpref){
                case "Red":
                    holder.partySizeTextView.setBackground(ContextCompat.getDrawable(mContext, R.drawable.circle_red));
                    break;
                case "Blue":
                    holder.partySizeTextView.setBackground(ContextCompat.getDrawable(mContext, R.drawable.circle));
                    break;
                case "Green":
                    holder.partySizeTextView.setBackground(ContextCompat.getDrawable(mContext, R.drawable.circle_green));
                    break;
            }
*/
        }




        /*
        SharedPreferences getPrefs = PreferenceManager
                .getDefaultSharedPreferences(getBaseContext());
        String background_chooser = getPrefs
                .getString("prefSetBackground", "1");
        if (background_chooser.equals("1")) {
            holder.partySizeTextView.setBackgroundColor(Color.RED);
        } else if (background_chooser.equals("2")) {
            holder.partySizeTextView.setBackgroundColor(Color.GREEN);
        } else if (background_chooser.equals("3")) {
            holder.partySizeTextView.setBackgroundColor(Color.BLUE);
        } else {
            holder.partySizeTextView.setBackgroundColor(Color.YELLOW);
        }
        /*if(partySize>40){
            holder.partySizeTextView.setBackgroundColor(Color.parseColor("RED"));
        }else{
            holder.partySizeTextView.setBackgroundColor(R.drawable.circle);
        }


    } */


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
   // @Override
    protected void onCreate(Bundle savedInstanceState) {
       /* super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mMainView = (LinearLayout) findViewById(R.id.activity_list);

        //defaultSetup();
        setupSharedPreferences();*/
    }

    // COMPLETED (4) Update setupSharedPreferences and onSharedPreferenceChanged to load the color
    // from shared preferences. Call setColor, passing in the color you got
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void setupSharedPreferences() {
        // Get all of the values from shared preferences to set it up
        /*SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        loadColorFromPreferences(sharedPreferences);
        // Register the listener
        sharedPreferences.registerOnSharedPreferenceChangeListener((SharedPreferences.OnSharedPreferenceChangeListener) this);

         */
    }


    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    /**
     * Swaps the Cursor currently held in the adapter with a new one
     * and triggers a UI refresh
     *
     * @param newCursor the new cursor that will replace the existing one
     */
    public void swapCursor(Cursor newCursor) {
        // Always close the previous mCursor first
        if (mCursor != null) mCursor.close();
        mCursor = newCursor;
        if (newCursor != null) {
            // Force the RecyclerView to refresh(重要！！攸關能不能改變顏色)
            this.notifyDataSetChanged();
        }
    }



    /**
     * Inner class to hold the views needed to display a single item in the recycler-view
     */
    class GuestViewHolder extends RecyclerView.ViewHolder {

        // Will display the guest name
        TextView nameTextView;
        // Will display the party size number
        TextView partySizeTextView;

        /**
         * Constructor for our ViewHolder. Within this constructor, we get a reference to our
         * TextViews
         *
         * @param itemView The View that you inflated in
         *                 {@link GuestListAdapter#onCreateViewHolder(ViewGroup, int)}
         */
        public GuestViewHolder(View itemView) {
            super(itemView);
            nameTextView = (TextView) itemView.findViewById(R.id.name_text_view);
            partySizeTextView = (TextView) itemView.findViewById(R.id.party_size_text_view);
        }

    }
}
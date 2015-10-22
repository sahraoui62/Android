package com.iagl.slimane.final_project.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.Toast;

import com.iagl.slimane.final_project.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by slimane on 30/09/2015.
 */
public class ProposeTransaction extends Activity implements View.OnClickListener{

    private Button b_transaction_before_validation;
    private Button b_add_image_transaction;
    private ImageView image_transaction;
    private EditText title_transaction;
    private EditText description_transaction;
    private EditText date_transaction;
    private SharedPreferences sharedPreferences;

    //private EditText transaction_from;
    //private EditText transaction_to;
    private AutoCompleteTextView autoCompAddress_to;
    private AutoCompleteTextView autoCompAddress_from;
    private static final String LOG_TAG = "Google Places Autocomplete";
    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String OUT_JSON = "/json";
    private static final String API_KEY = "AIzaSyAiRgAuYqnX8whxqXHVCWAI6UCzRKOwwRI";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_transaction);
        sharedPreferences = getSharedPreferences("connection", Context.MODE_PRIVATE);
        if(sharedPreferences.getString("user_id_connected","").isEmpty()){
            Intent intent = new Intent(ProposeTransaction.this, SignIn.class);
            startActivity(intent);
        }
        title_transaction = (EditText)findViewById(R.id.title_transaction);
        description_transaction = (EditText)findViewById(R.id.description_transaction);
        date_transaction = (EditText)findViewById(R.id.date_transaction);
        //transaction_from = (EditText)findViewById(R.id.transaction_from);
        //transaction_to = (EditText)findViewById(R.id.transaction_to);
        b_transaction_before_validation = (Button)findViewById(R.id.b_transaction_before_validation);
        b_add_image_transaction = (Button)findViewById(R.id.b_add_image_transaction);
        image_transaction = (ImageView)findViewById(R.id.image_transaction);
        b_transaction_before_validation.setOnClickListener(this);
        b_add_image_transaction.setOnClickListener(this);
        autoCompAddress_to = (AutoCompleteTextView) findViewById(R.id.transaction_to);
        autoCompAddress_from = (AutoCompleteTextView) findViewById(R.id.transaction_from);

        autoCompAddress_to.setAdapter(new GooglePlacesAutocompleteAdapter(this, R.layout.list_address));
        autoCompAddress_to.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String str = (String) parent.getItemAtPosition(position);
                Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
            }
        });
        autoCompAddress_from.setAdapter(new GooglePlacesAutocompleteAdapter(this, R.layout.list_address));
        autoCompAddress_from.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String str = (String) parent.getItemAtPosition(position);
                Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
            }
        });

        //autoCompAddress_from.setText("Fisherman's Wharf, San Francisco, CA, United States");
        //autoCompAddress_to.setText("The Moscone Center, Howard Street, San Francisco, CA, United States");

    }

    public void onItemClick(AdapterView adapterView, View view, int position, long id) {
        String str = (String) adapterView.getItemAtPosition(position);
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

    public static ArrayList autocomplete(String input) {
        ArrayList resultList = null;

        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
            sb.append("?key=" + API_KEY);
            sb.append("&components=country:gr");
            sb.append("&input=" + URLEncoder.encode(input, "utf8"));

            URL url = new URL(sb.toString());
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
        } catch (MalformedURLException e) {
            //Log.e(LOG_TAG, "Error processing Places API URL", e);
            return resultList;
        } catch (IOException e) {
            //Log.e(LOG_TAG, "Error connecting to Places API", e);
            return resultList;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        try {
            // Create a JSON object hierarchy from the results
            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");

            // Extract the Place descriptions from the results
            resultList = new ArrayList(predsJsonArray.length());
            for (int i = 0; i < predsJsonArray.length(); i++) {
                System.out.println(predsJsonArray.getJSONObject(i).getString("description"));
                System.out.println("============================================================");
                resultList.add(predsJsonArray.getJSONObject(i).getString("description"));
            }
        } catch (JSONException e) {
            //Log.e(LOG_TAG, "Cannot process JSON results", e);
        }

        return resultList;
    }

    class GooglePlacesAutocompleteAdapter extends ArrayAdapter implements Filterable {
        private ArrayList resultList;
        public GooglePlacesAutocompleteAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }
        public int getCount() {
            return resultList.size();
        }

        public String getItem(int index) {
            return (String)resultList.get(index);
        }

        public Filter getFilter() {
            Filter filter = new Filter() {
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults filterResults = new FilterResults();
                    if (constraint != null) {
                        // Retrieve the autocomplete results.
                        resultList = autocomplete(constraint.toString());
                        // Assign the data to the FilterResults
                        filterResults.values = resultList;
                        filterResults.count = resultList.size();
                    }
                    return filterResults;
                }

                 protected void publishResults(CharSequence constraint, FilterResults results) {
                    if (results != null && results.count > 0) {
                        notifyDataSetChanged();
                    } else {
                        notifyDataSetInvalidated();
                    }
                }
            };
            return filter;
        }
    }

    public void onClick(View v) {
        if(v == b_transaction_before_validation){
            Intent intent = new Intent(ProposeTransaction.this, SummaryTransaction.class);
            intent.putExtra("title_transaction", title_transaction.getText().toString());
            intent.putExtra("description_transaction",description_transaction.getText().toString());
            intent.putExtra("date_transaction",date_transaction.getText().toString());
            intent.putExtra("transaction_from",autoCompAddress_from.getText().toString());
            intent.putExtra("transaction_to",autoCompAddress_to.getText().toString());
            startActivity(intent);
        }
        if(v == b_add_image_transaction){
            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, 1);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Here we need to check if the activity that was triggers was the Image Gallery.
        // If it is the requestCode will match the LOAD_IMAGE_RESULTS value.
        // If the resultCode is RESULT_OK and there is some data we know that an image was picked.
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            // Let's read picked image data - its URI
            Uri pickedImage = data.getData();
            // Let's read picked image path using content resolver
            String[] filePath = { MediaStore.Images.Media.DATA };
            Cursor cursor = getContentResolver().query(pickedImage, filePath, null, null, null);
            cursor.moveToFirst();
            String imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]));

            // Now we need to set the GUI ImageView data with data read from the picked file.
            image_transaction.setImageBitmap(BitmapFactory.decodeFile(imagePath));

            // At the end remember to close the cursor or you will end with the RuntimeException!
            cursor.close();
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

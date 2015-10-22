package com.iagl.slimane.final_project.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.iagl.slimane.final_project.R;
import com.iagl.slimane.final_project.datasources.Person;
import com.iagl.slimane.final_project.datasources.Transaction;
import com.iagl.slimane.final_project.sqlhelpers.DatabaseHelper;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by slimane on 17/10/2015.
 */
public class ShowAdvert extends Activity {

    private DatabaseHelper databaseHelper;
    private Map<String,Long> map;
    private SharedPreferences sharedPreferences;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_page);
        sharedPreferences = getSharedPreferences("connection", Context.MODE_PRIVATE);

        if(sharedPreferences.getString("user_id_connected","").isEmpty()){
            Intent intent = new Intent(ShowAdvert.this, SignIn.class);
            startActivity(intent);
        }
        final ListView listview = (ListView) findViewById(R.id.show_list_proposition);
        try {
            final Dao<Transaction,Integer> transactionDao = getHelper().getTransactionDao();
            List<Transaction> transactions = transactionDao.queryBuilder().query();
            String[] titles = new String[transactions.size()];
            map = new HashMap<String,Long>();

            int compteur = 0;
            for(Transaction s  : transactions ) {
                titles[compteur++] = s.getTitle();
                map.put(s.getTitle(),s.getId());
            }
            final ArrayList<String> list = new ArrayList<String>();
            for (int i = 0; i < titles.length; ++i) {
                list.add(titles[i]);
            }
            final StableArrayAdapter adapter = new StableArrayAdapter(this,
                    android.R.layout.simple_list_item_1, list);
            listview.setAdapter(adapter);

            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                public void onItemClick(AdapterView<?> parent, final View view,
                                        int position, long id) {
                    final String item = (String) parent.getItemAtPosition(position);
                    AlertDialog.Builder dialog = new AlertDialog.Builder(ShowAdvert.this);
                    dialog.setTitle(item);
                    int id_item = map.get(item).intValue();
                    try {
                        final Dao<Transaction,Integer> transactiondao =  getHelper().getTransactionDao();
                        Transaction t = transactiondao.queryForId(id_item);
                        StringBuffer sb = new StringBuffer();
                        sb.append("Description :" + t.getDescription());
                        sb.append("\n");
                        sb.append("Départ :" + t.getAddress_from());
                        sb.append("\n");
                        sb.append("Arrivée :" + t.getAddress_to());
                        sb.append("\n");
                        sb.append("Téléphone " + t.getPerson().getPhone_number());
                        dialog.setMessage(sb.toString());

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    dialog.show();
                    /*
                    view.animate().setDuration(2000).alpha(0)
                            .withEndAction(new Runnable() {
                                @Override
                                public void run() {
                                    list.remove(item);
                                    adapter.notifyDataSetChanged();
                                    view.setAlpha(1);
                                }
                            });
                     */
                }

            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private DatabaseHelper getHelper() {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(this, DatabaseHelper.class);
        }
        return databaseHelper;
    }

    private class StableArrayAdapter extends ArrayAdapter<String> {

        HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

        public StableArrayAdapter(Context context, int textViewResourceId,
                                  List<String> objects) {
            super(context, textViewResourceId, objects);
            for (int i = 0; i < objects.size(); ++i) {
                mIdMap.put(objects.get(i), i);
            }
        }

        public long getItemId(int position) {
            String item = getItem(position);
            return mIdMap.get(item);
        }

        public boolean hasStableIds() {
            return true;
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

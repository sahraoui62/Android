package com.iagl.slimane.final_project.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

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
public class ShowMyAdvert extends Activity {

    private DatabaseHelper databaseHelper;
    private Map<String,Long> map;
    private SharedPreferences sharedPreferences;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_page);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        if(sharedPreferences.getString("user_id_connected","").isEmpty()){
            Intent intent = new Intent(ShowMyAdvert.this, SignIn.class);
            startActivity(intent);
        }
        final ListView listview = (ListView) findViewById(R.id.show_list_proposition);
        try {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ShowMyAdvert.this);
            final Dao<Transaction,Integer> transactionDao = getHelper().getTransactionDao();
            List<Transaction> transactions = transactionDao.queryBuilder().where().eq("person_id", sharedPreferences.getString("user_id_connected", null)).query();
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
                    AlertDialog.Builder dialog = new AlertDialog.Builder(ShowMyAdvert.this);
                    dialog.setTitle(item.toUpperCase());
                    int id_item = map.get(item).intValue();
                    try {
                        final Dao<Transaction,Integer> transactiondao =  getHelper().getTransactionDao();
                        final Transaction t = transactiondao.queryForId(id_item);
                        StringBuffer sb = new StringBuffer();
                        sb.append("Description :" + t.getDescription());
                        sb.append("\n");
                        sb.append("Départ :" + t.getAddress_from());
                        sb.append("\n");
                        sb.append("Arrivée :" + t.getAddress_to());
                        sb.append("\n");
                        sb.append("Téléphone " + t.getPerson().getPhone_number());
                        dialog.setMessage(sb.toString());
                        dialog.setPositiveButton("Supprimer", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                int id_item = map.get(item).intValue();
                                try {
                                    transactiondao.delete(t);
                                    Toast.makeText(ShowMyAdvert.this, "L'annonce a bien été supprimée.", Toast.LENGTH_SHORT).show();
                                    ((StableArrayAdapter) listview.getAdapter()).notifyDataSetChanged();
                                    Intent intent = new Intent(ShowMyAdvert.this, ShowMyAdvert.class);
                                    startActivity(intent);
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        dialog.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
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
        int id = item.getItemId();
        if (id == R.id.action_add_advert) {
            Intent intent = new Intent(ShowMyAdvert.this, ProposeTransaction.class);
            startActivity(intent);
        }
        if (id == R.id.action_show_advert) {
            Intent intent = new Intent(ShowMyAdvert.this, ShowAdvert.class);
            startActivity(intent);
        }
        if (id == R.id.action_signin) {
            Intent intent = new Intent(ShowMyAdvert.this, SignIn.class);
            startActivity(intent);
        }
        if (id == R.id.action_signup) {
            Intent intent = new Intent(ShowMyAdvert.this, SignUp.class);
            startActivity(intent);
        }
        if (id == R.id.action_home) {
            Intent intent = new Intent(ShowMyAdvert.this, Home.class);
            startActivity(intent);
        }
        if (id == R.id.action_signout) {
            SharedPreferences sharedpreferences = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.clear();
            editor.commit();
            Intent intent = new Intent(ShowMyAdvert.this, SignIn.class);
            startActivity(intent);
        }
        if (id == R.id.action_show_my_advert) {
            Intent intent = new Intent(ShowMyAdvert.this, ShowMyAdvert.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        if(sharedPreferences.getString("user_id_connected","").isEmpty()){
            menu.findItem(R.id.action_add_advert).setVisible(false);
            menu.findItem(R.id.action_signout).setVisible(false);
            menu.findItem(R.id.action_show_my_advert).setVisible(false);
            menu.findItem(R.id.action_signin).setVisible(true);
        }else{
            menu.findItem(R.id.action_signin).setVisible(false);
            menu.findItem(R.id.action_signup).setVisible(false);
        }
        menu.findItem(R.id.action_show_my_advert).setVisible(false);
        return true;
    }
}

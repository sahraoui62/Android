package com.iagl.slimane.final_project.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.iagl.slimane.final_project.R;

/**
 * Created by slimane on 17/10/2015.
 */
public class Home extends Activity implements View.OnClickListener{

    private Button b_home_add;
    private Button b_home_show;
    private Button b_home_signout;
    private Button b_home_show_my;
    private SharedPreferences sharedPreferences;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        if(sharedPreferences.getString("user_id_connected","").isEmpty()){
            Intent intent = new Intent(Home.this, SignIn.class);
            startActivity(intent);
        }
        b_home_add = (Button)findViewById(R.id.b_home_add);
        b_home_show = (Button)findViewById(R.id.b_home_show);
        b_home_signout = (Button)findViewById(R.id.b_home_signout);
        b_home_show_my = (Button)findViewById(R.id.b_home_show_my);

        b_home_signout.setOnClickListener(this);
        b_home_add.setOnClickListener(this);
        b_home_show.setOnClickListener(this);
        b_home_show_my.setOnClickListener(this);
    }

    public void onClick(View v) {
        if(v == b_home_add){
            Intent intent = new Intent(Home.this, ProposeTransaction.class);
            startActivity(intent);
        }
        if(v == b_home_show){
            Intent intent = new Intent(Home.this, ShowAdvert.class);
            startActivity(intent);
        }
        if(v == b_home_signout){
            SharedPreferences sharedpreferences = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.clear();
            editor.commit();
            Intent intent = new Intent(Home.this, SignIn.class);
            startActivity(intent);
        }
        if(v == b_home_show_my){
            Intent intent = new Intent(Home.this, ShowMyAdvert.class);
            startActivity(intent);
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
            Intent intent = new Intent(Home.this, ProposeTransaction.class);
            startActivity(intent);
        }
        if (id == R.id.action_show_advert) {
            Intent intent = new Intent(Home.this, ShowAdvert.class);
            startActivity(intent);
        }
        if (id == R.id.action_signin) {
            Intent intent = new Intent(Home.this, SignIn.class);
            startActivity(intent);
        }
        if (id == R.id.action_signup) {
            Intent intent = new Intent(Home.this, SignUp.class);
            startActivity(intent);
        }
        if (id == R.id.action_home) {
            Intent intent = new Intent(Home.this, Home.class);
            startActivity(intent);
        }
        if (id == R.id.action_signout) {
            SharedPreferences sharedpreferences = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.clear();
            editor.commit();
            Intent intent = new Intent(Home.this, SignIn.class);
            startActivity(intent);
        }
        if (id == R.id.action_show_my_advert) {
            Intent intent = new Intent(Home.this, ShowMyAdvert.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        if(sharedPreferences.getString("user_id_connected","").isEmpty()){
            menu.findItem(R.id.action_add_advert).setVisible(false);
            menu.findItem(R.id.action_signout).setVisible(false);
            menu.findItem(R.id.action_show_my_advert).setVisible(false);
            menu.findItem(R.id.action_signin).setVisible(true);
        }else{
            menu.findItem(R.id.action_signin).setVisible(false);
            menu.findItem(R.id.action_signup).setVisible(false);
        }
        menu.findItem(R.id.action_home).setVisible(false);
        return true;
    }
}
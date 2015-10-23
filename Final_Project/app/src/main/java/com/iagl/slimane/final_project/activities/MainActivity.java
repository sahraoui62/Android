package com.iagl.slimane.final_project.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.iagl.slimane.final_project.R;

public class MainActivity extends Activity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_page);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                final Intent mainIntent = new Intent(MainActivity.this, SignIn.class);
                MainActivity.this.startActivity(mainIntent);
                MainActivity.this.finish();
            }
        }, 2000);
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
        if (id == R.id.action_add_advert) {
            Intent intent = new Intent(MainActivity.this, ProposeTransaction.class);
            startActivity(intent);
        }
        if (id == R.id.action_show_advert) {
            Intent intent = new Intent(MainActivity.this, ShowAdvert.class);
            startActivity(intent);
        }
        if (id == R.id.action_signin) {
            Intent intent = new Intent(MainActivity.this, SignIn.class);
            startActivity(intent);
        }
        if (id == R.id.action_signup) {
            Intent intent = new Intent(MainActivity.this, SignUp.class);
            startActivity(intent);
        }
        if (id == R.id.action_home) {
            Intent intent = new Intent(MainActivity.this, Home.class);
            startActivity(intent);
        }
        if (id == R.id.action_signout) {
            SharedPreferences sharedpreferences = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.clear();
            editor.commit();
            Intent intent = new Intent(MainActivity.this, SignIn.class);
            startActivity(intent);
        }
        if (id == R.id.action_show_my_advert) {
            Intent intent = new Intent(MainActivity.this, ShowMyAdvert.class);
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
            menu.findItem(R.id.action_signin).setVisible(true);
            menu.findItem(R.id.action_show_my_advert).setVisible(false);
        }else{
            menu.findItem(R.id.action_signin).setVisible(false);
            menu.findItem(R.id.action_signup).setVisible(false);
        }
        return true;
    }
}

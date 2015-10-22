package com.iagl.slimane.final_project.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
    private SharedPreferences sharedPreferences;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);
        sharedPreferences = getSharedPreferences("connection", Context.MODE_PRIVATE);
        if(sharedPreferences.getString("user_id_connected","").isEmpty()){
            Intent intent = new Intent(Home.this, SignIn.class);
            startActivity(intent);
        }
        b_home_add = (Button)findViewById(R.id.b_home_add);
        b_home_show = (Button)findViewById(R.id.b_home_show);
        b_home_signout = (Button)findViewById(R.id.b_home_signout);
        b_home_signout.setOnClickListener(this);
        b_home_add.setOnClickListener(this);
        b_home_show.setOnClickListener(this);
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
            SharedPreferences sharedpreferences = getSharedPreferences("connection", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.clear();
            editor.commit();
            Intent intent = new Intent(Home.this, SignIn.class);
            startActivity(intent);
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
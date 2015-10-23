package com.iagl.slimane.final_project.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.iagl.slimane.final_project.R;
import com.iagl.slimane.final_project.datasources.Person;
import com.iagl.slimane.final_project.datasources.Transaction;
import com.iagl.slimane.final_project.sqlhelpers.DatabaseHelper;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

/**
 * Created by slimane on 17/10/2015.
 */
public class SignIn extends Activity implements View.OnClickListener {

    private Button b_signin_connection;
    private Button b_signin_signup;
    private EditText signin_password;
    private EditText signin_username;
    private DatabaseHelper databaseHelper;

    private SharedPreferences sharedPreferences;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin_page);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        if(!sharedPreferences.getString("user_id_connected","").isEmpty()){
            Intent intent = new Intent(SignIn.this, Home.class);
            startActivity(intent);
        }
        b_signin_connection = (Button)findViewById(R.id.b_signin_connection);
        b_signin_signup = (Button)findViewById(R.id.b_signin_signup);
        b_signin_connection.setOnClickListener(this);
        b_signin_signup.setOnClickListener(this);
        signin_password = (EditText)findViewById(R.id.signin_password);
        signin_username = (EditText)findViewById(R.id.signin_username);
    }

    public void onClick(View v) {
        if(v == b_signin_signup){
            Intent intent = new Intent(SignIn.this, SignUp.class);
            startActivity(intent);
        }
        if(v == b_signin_connection){
            final Dao<Person,Integer> personDao;
            try {
                personDao = getHelper().getPersonDao();
                List<Person> lp = personDao.queryBuilder().where().eq("username",signin_username.getText().toString()).query();
                if(lp.isEmpty()){
                    toast_a_message("Nom d'utilisateur inconnu. Réessayer",Toast.LENGTH_SHORT);
                }else {
                    Iterator<Person> it = lp.iterator();
                    Person p = it.next();
                    String pass_in_database = p.getPassword();

                    Log.e("Test","Comparaiton de " + pass_in_database + " avec " + signin_password.getText().toString());


                    if (signin_password.getText().toString().equals(pass_in_database)) {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("user_id_connected", p.getId() + "");
                        editor.commit();
                        Intent intent = new Intent(SignIn.this, Home.class);
                        startActivity(intent);
                    }else{
                        toast_a_message("Mauvais mot de passe. Réessayer",Toast.LENGTH_SHORT);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
    }

    public void toast_a_message(String message, int duration){
        Toast toast = Toast.makeText(getApplicationContext(), message, duration);
        toast.show();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }
    private DatabaseHelper getHelper() {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(this, DatabaseHelper.class);
        }
        return databaseHelper;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add_advert) {
            Intent intent = new Intent(SignIn.this, ProposeTransaction.class);
            startActivity(intent);
        }
        if (id == R.id.action_show_advert) {
            Intent intent = new Intent(SignIn.this, ShowAdvert.class);
            startActivity(intent);
        }
        if (id == R.id.action_signin) {
            Intent intent = new Intent(SignIn.this, SignIn.class);
            startActivity(intent);
        }
        if (id == R.id.action_signup) {
            Intent intent = new Intent(SignIn.this, SignUp.class);
            startActivity(intent);
        }
        if (id == R.id.action_home) {
            Intent intent = new Intent(SignIn.this, Home.class);
            startActivity(intent);
        }
        if (id == R.id.action_signout) {
            SharedPreferences sharedpreferences = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.clear();
            editor.commit();
            Intent intent = new Intent(SignIn.this, SignIn.class);
            startActivity(intent);
        }
        if (id == R.id.action_show_my_advert) {
            Intent intent = new Intent(SignIn.this, ShowMyAdvert.class);
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
        menu.findItem(R.id.action_signin).setVisible(false);
        return true;
    }
}

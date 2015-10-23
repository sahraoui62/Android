package com.iagl.slimane.final_project.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by slimane on 17/10/2015.
 */
public class SignUp extends Activity implements View.OnClickListener{

    private Button b_signup_validation;
    private EditText signup_username;
    private EditText signup_mail;
    private EditText signup_password;
    private EditText signup_phone_number;

    private DatabaseHelper databaseHelper;
    private SharedPreferences sharedPreferences;

    private DatabaseHelper getHelper() {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(this, DatabaseHelper.class);
        }
        return databaseHelper;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_page);
        signup_username = (EditText)findViewById(R.id.signup_username);
        signup_mail = (EditText)findViewById(R.id.signup_mail);
        signup_password = (EditText)findViewById(R.id.signup_password);
        signup_phone_number = (EditText)findViewById(R.id.signup_phone_number);
        b_signup_validation = (Button)findViewById(R.id.b_signup_validation);
        b_signup_validation.setOnClickListener(this);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
    }

    public boolean isEmailValid(String email)
    {
        String regExpn =
                "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                        +"((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                        +"([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                        +"([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(regExpn,Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);

        if(matcher.matches())
            return true;
        else
            return false;
    }

    public void onClick(View v) {
        if(v == b_signup_validation){
            if(signup_mail.getText().toString().isEmpty() || signup_username.getText().toString().isEmpty()
                    || signup_password.getText().toString().isEmpty() || signup_phone_number.getText().toString().isEmpty()){
                toast_a_message("Veuillez renseigner tous les champs", Toast.LENGTH_SHORT);
            }else if(!isEmailValid(signup_mail.getText().toString())){
                toast_a_message("Email invalide", Toast.LENGTH_SHORT);
            }else{
                try {
                    final Person person = new Person();
                    person.setUsername(signup_username.getText().toString());
                    person.setAddress(signup_mail.getText().toString());
                    person.setPassword(signup_password.getText().toString());
                    person.setPhone_number(signup_phone_number.getText().toString());
                    final Dao<Person,Integer> personDao = getHelper().getPersonDao();
                    personDao.create(person);

                    List<Person> lp = personDao.queryBuilder().where().eq("username", signup_username.getText().toString()).query();
                    Iterator<Person> it = lp.iterator();
                    //Long id = personDao.extractId(person).longValue();
                    Long id = it.next().getId();

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("user_id_connected", id + "");
                    editor.commit();
                    toast_a_message("Vous êtes enregistré", Toast.LENGTH_LONG);
                    Intent intent = new Intent(SignUp.this,Home.class);
                    startActivity(intent);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
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

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add_advert) {
            Intent intent = new Intent(SignUp.this, ProposeTransaction.class);
            startActivity(intent);
        }
        if (id == R.id.action_show_advert) {
            Intent intent = new Intent(SignUp.this, ShowAdvert.class);
            startActivity(intent);
        }
        if (id == R.id.action_signin) {
            Intent intent = new Intent(SignUp.this, SignIn.class);
            startActivity(intent);
        }
        if (id == R.id.action_signup) {
            Intent intent = new Intent(SignUp.this, SignUp.class);
            startActivity(intent);
        }
        if (id == R.id.action_home) {
            Intent intent = new Intent(SignUp.this, Home.class);
            startActivity(intent);
        }
        if (id == R.id.action_signout) {
            SharedPreferences sharedpreferences = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.clear();
            editor.commit();
            Intent intent = new Intent(SignUp.this, SignIn.class);
            startActivity(intent);
        }
        if (id == R.id.action_show_my_advert) {
            Intent intent = new Intent(SignUp.this, ShowMyAdvert.class);
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
        menu.findItem(R.id.action_signup).setVisible(false);
        return true;
    }
}
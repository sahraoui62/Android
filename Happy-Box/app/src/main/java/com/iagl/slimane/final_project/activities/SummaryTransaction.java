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
import android.widget.TextView;
import android.widget.Toast;

import com.iagl.slimane.final_project.R;
import com.iagl.slimane.final_project.datasources.Person;
import com.iagl.slimane.final_project.datasources.Transaction;
import com.iagl.slimane.final_project.datasources.Transaction_State;
import com.iagl.slimane.final_project.sqlhelpers.DatabaseHelper;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;


/**
 * Created by slimane on 30/09/2015.
 */
public class SummaryTransaction extends Activity implements View.OnClickListener {

    private DatabaseHelper databaseHelper = null;
    private TextView title_transaction_summary;
    private TextView description_transaction_summary;
    private TextView date_transaction_summary;
    private TextView transaction_from_summary;
    private TextView transaction_to_summary;
    private Button b_summary_new_transaction_return;
    private Button b_summary_validate;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_transaction_summary);
        Intent intent = getIntent();
        title_transaction_summary = (TextView) findViewById(R.id.title_transaction_summary);
        description_transaction_summary = (TextView) findViewById(R.id.description_transaction_summary);
        date_transaction_summary = (TextView) findViewById(R.id.date_transaction_summary);
        transaction_from_summary = (TextView) findViewById(R.id.transaction_from_summary);
        transaction_to_summary = (TextView) findViewById(R.id.transaction_to_summary);
        b_summary_new_transaction_return = (Button) findViewById(R.id.b_summary_new_transaction_return);
        b_summary_validate = (Button) findViewById(R.id.b_summary_validate);

        title_transaction_summary.setText(intent.getStringExtra("title_transaction"));
        description_transaction_summary.setText(intent.getStringExtra("description_transaction"));
        date_transaction_summary.setText(intent.getStringExtra("date_transaction"));
        transaction_from_summary.setText(intent.getStringExtra("transaction_from"));
        transaction_to_summary.setText(intent.getStringExtra("transaction_to"));

        b_summary_new_transaction_return.setOnClickListener(this);
        b_summary_validate.setOnClickListener(this);
    }


    public void onClick(View v) {
        if(v == b_summary_new_transaction_return){
            Intent intent = new Intent(SummaryTransaction.this, ProposeTransaction.class);
            startActivity(intent);
        }
        if(v == b_summary_validate){
            final Transaction transaction = new Transaction();
            transaction.setAddress_from(transaction_from_summary.getText().toString());
            transaction.setAddress_to(transaction_to_summary.getText().toString());
            transaction.setDescription(description_transaction_summary.getText().toString());
            transaction.setTitle(title_transaction_summary.getText().toString());
            SharedPreferences sharedpreferences = getSharedPreferences("connection", Context.MODE_PRIVATE);
            final Dao<Person,Integer> personDao;
            try {
                personDao = getHelper().getPersonDao();
                List<Person> lp = personDao.queryBuilder().where().eq("id",sharedpreferences.getString("user_id_connected", null)).query();
                Iterator<Person> it = lp.iterator();
                transaction.setPerson(it.next());
                transaction.setState(Transaction_State.NOT_STARTED);
                final Dao<Transaction,Integer> transactionDao = getHelper().getTransactionDao();
                transactionDao.create(transaction);
                toast_a_message("Votre annonce a été transmise, dès qu'une personne sera interessé par votre annonce, vous serez contacté.", Toast.LENGTH_LONG);
                Intent intent = new Intent(SummaryTransaction.this, ShowAdvert.class);
                startActivity(intent);
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
    }

    public void toast_a_message(String message, int duration){
        Toast toast = Toast.makeText(getApplicationContext(), message, duration);
        toast.show();
    }

    private DatabaseHelper getHelper() {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(this, DatabaseHelper.class);
        }
        return databaseHelper;
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

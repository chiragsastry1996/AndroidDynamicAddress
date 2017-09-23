package com.example.chira.address;


import android.os.AsyncTask;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.chira.check.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static android.widget.Toast.LENGTH_LONG;


public class MainActivity extends AppCompatActivity {
    RadioGroup rg;
    RadioButton rb;
    public String url;
    private static String adr1;
    private static String adr2;
    private static String adr3;
    private static final String REGISTER_URL = "http://192.168.2.90/android/adr/adr2.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        contactList = new ArrayList<>();

        url = "http://192.168.2.90/android/adr/adr.php";
        rg = (RadioGroup)findViewById(R.id.radiog);
    }

    public void rbclick(View v){

        int radiobuttonid = rg.getCheckedRadioButtonId();
        rb = (RadioButton)findViewById(radiobuttonid);
        String adr = String.valueOf(rb.getText());

        class RegisterUser extends AsyncTask<String, Void, String> {
            // ProgressDialog loading;
            RegisterUserClass ruc = new RegisterUserClass();


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                //Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();

            }

            @Override
            protected String doInBackground(String... params) {

                HashMap<String, String> data = new HashMap<>();
                data.put("adr",params[0]);

                String result = ruc.sendPostRequest(REGISTER_URL,data);
                return  result;
            }
        }

        RegisterUser ru = new RegisterUser();
        ru.execute(adr);

        Toast.makeText(getBaseContext(),rb.getText(),Toast.LENGTH_SHORT).show();
       // register(adr);
    }

    private String TAG = MainActivity.class.getSimpleName();

    // URL to get contacts JSON

    ArrayList<HashMap<String, String>> contactList;
    RadioButton rb1,rb2,rb3;


    protected void onPause(){
        super.onPause();

    }
    protected void onResume(){
        super.onResume();
        new GetContacts().execute();
    }
    private class GetContacts extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url);
            rb1=(RadioButton)findViewById(R.id.rb1);
            rb2=(RadioButton)findViewById(R.id.rb2);
            rb3=(RadioButton)findViewById(R.id.rb3);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    JSONArray contacts = jsonObj.getJSONArray("result");

                    JSONObject c = contacts.getJSONObject(0);
                    adr1 = c.getString("adr1");
                    adr2 = c.getString("adr2");
                    adr3 = c.getString("adr3");


                    HashMap<String, String> contact = new HashMap<>();

                    contact.put("adr1", adr1);
                    contact.put("adr2", adr2);
                    contact.put("adr3", adr3);

                    contactList.add(contact);

                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                LENGTH_LONG)
                                .show();
                    }
                });

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    rb1.setText(adr1);
                    rb2.setText(adr2);
                    rb3.setText(adr3);

                }
            });
        }


    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        finish();
    }

  /*  private void register(String adr) {
        class RegisterUser extends AsyncTask<String, Void, String> {
            // ProgressDialog loading;
            RegisterUserClass ruc = new RegisterUserClass();


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                //Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();

            }

            @Override
            protected String doInBackground(String... params) {

                HashMap<String, String> data = new HashMap<>();
                data.put("adr",params[0]);

                String result = ruc.sendPostRequest(REGISTER_URL,data);
                return  result;
            }
        }

        RegisterUser ru = new RegisterUser();
        ru.execute(adr);

    }*/

}

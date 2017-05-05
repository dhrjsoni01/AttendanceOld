package dks.attendance;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class ScrollingActivity extends AppCompatActivity {
    int br2,yr2;
    String roll,table,name;
    EditText enroll;
    Spinner sbranch,syear;
    ProgressDialog progressDialog;
    AlertDialog network;
    ArrayAdapter<CharSequence> abranch,ayear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);

        enroll = (EditText)findViewById(R.id.rollno);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("loading..");
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        network = new AlertDialog.Builder(this).create();
        network.setTitle("Error");
        network.setMessage("No Internet Connection");

        sbranch = (Spinner)findViewById(R.id.spinner_branch);
        syear = (Spinner) findViewById(R.id.spinner_year);

        abranch = ArrayAdapter.createFromResource(this,R.array.branch,android.R.layout.simple_spinner_item);
        ayear = ArrayAdapter.createFromResource(this,R.array.year,android.R.layout.simple_spinner_item);

        ayear.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        abranch.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);

        sbranch.setAdapter(abranch);
        syear.setAdapter(ayear);

        sbranch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                br2 = (int) parent.getItemIdAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(getBaseContext(),"nothing selected in branch",Toast.LENGTH_LONG).show();
            }
        });
        syear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                yr2 = (int) parent.getItemIdAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                Toast.makeText(getBaseContext(),"nothing selected in year",Toast.LENGTH_LONG).show();

            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

     /*   FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "App for JEC Attendance\nBy:- Dheeraj Soni", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        */
        if (isNetworkAvailable()){
            Message msg = new Message(this);
            msg.execute();

        }else{
            network.show();

        }


    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about) {
            Intent about = new Intent(this,About.class);
            startActivity(about);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void btncheck(View view){
        if (isNetworkAvailable()){

            roll = enroll.getText().toString().toUpperCase();
            if(roll.length()==12) {
                /*backend programing*/
                progressDialog.show();
                Btask btask = new Btask(this);
                btask.execute();
            }else{

                AlertDialog msg = new AlertDialog.Builder(this).create();
                msg.setMessage("Incorrect Roll no.");
                msg.show();
            }

        }else{
            network.show();

        }



    }
    public void btnlogin(View view){
        if (isNetworkAvailable()){
            Intent login = new Intent(this,loginpage.class);
            startActivity(login);
        }else{
            network.show();

        }
    }
    public void btnreg(View view){
        if (isNetworkAvailable()){
            Intent reg = new Intent(this,registerpage.class);
            startActivity(reg);
        }else{
            network.show();

        }
    }


    public class Btask extends AsyncTask<String,String,String>{
        Context ctx;
        AlertDialog alertDialog;
        Btask(Context ctx){
            this.ctx=ctx;

        }

        @Override
        protected void onPreExecute() {
            alertDialog = new AlertDialog.Builder(ctx).create();
            alertDialog.setTitle("Warning");
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            String check_url="http://jecattendance.host22.com/check.php";

            try {
                URL url = new URL(check_url);
                HttpURLConnection httpURLConnection =(HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String data = URLEncoder.encode("branch","UTF-8")+"="+URLEncoder.encode(String.valueOf(br2),"UTF-8")+"&"+
                        URLEncoder.encode("year","UTF-8")+"="+URLEncoder.encode(String.valueOf(yr2),"UTF-8")+"&"+
                        URLEncoder.encode("roll","UTF-8")+"="+URLEncoder.encode(roll,"UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuffer buffer = new StringBuffer();
                String line = "";
                while ((line=bufferedReader.readLine())!=null){
                    buffer.append(line);
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                String response = buffer.toString();
                if(!response.equals("error")) {
                    if(!response.equals("invalid")) {
                        JSONObject parentobject = new JSONObject(response);
                        JSONArray parentarray = parentobject.getJSONArray("sresponse");
                        JSONObject finalobj = parentarray.getJSONObject(0);
                        table = finalobj.getString("table");
                        name = finalobj.getString("name");
                    }
                }

                return response;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return "unknown";
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String s) {
            progressDialog.dismiss();
            if(s.equals("error")){
                alertDialog.setMessage("Connection error please report to developer");
                alertDialog.show();
            }
            else if(s.equals("invalid")){
                alertDialog.setMessage("Enrollment no. not found");
                alertDialog.show();
            }
            else{
                Toast.makeText(getApplicationContext(),"onpost",Toast.LENGTH_LONG).show();
                Intent check = new Intent(getApplicationContext(),Record2.class);
                check.putExtra("branch",br2);
                check.putExtra("year",yr2);
                check.putExtra("name",name);
                check.putExtra("table",table);
                check.putExtra("roll",roll);
                startActivity(check);

            }
        }
    }

    public class Message extends AsyncTask<String,String,String>{
        AlertDialog alertDialog;
        Context ctx;
        Message(Context ctx){
            this.ctx=ctx;
        }

        @Override
        protected void onPreExecute() {
            alertDialog = new AlertDialog.Builder(ctx).create();
            alertDialog.setTitle("Notification");
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            if(!s.equals("empty")){
                alertDialog.setMessage(s);
                alertDialog.show();
            }
        }

        @Override
        protected String doInBackground(String... params) {

            String check_url="http://jecattendance.host22.com/msg.php";


            try {
                URL url = new URL(check_url);
                HttpURLConnection httpURLConnection =(HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuffer buffer = new StringBuffer();
                String line = "";
                while ((line=bufferedReader.readLine())!=null){
                    buffer.append(line);
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                String response = buffer.toString();

                return response;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "error";
        }
    }

}

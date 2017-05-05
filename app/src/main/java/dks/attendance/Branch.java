package dks.attendance;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
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
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by DKS on 1/25/2017.
 */

public class Branch extends AppCompatActivity {
    ProgressDialog progressDialog;
    String name,table,ID;
    TextView tv_name;
    int br,yr;
    Spinner b_sbranch,b_year;
    AlertDialog network;
    ArrayAdapter<CharSequence> branch,year;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.branch);
        Bundle last = getIntent().getExtras();
        name = last.getString("name");
        tv_name = (TextView)findViewById(R.id.fname);
        tv_name.setText("Hello, "+name);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("loading...");
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        network = new AlertDialog.Builder(this).create();
        network.setMessage("No Internet Connection");
        network.setTitle("Error");

        b_sbranch=(Spinner)findViewById(R.id.spin_branch);
        b_year = (Spinner)findViewById(R.id.spin_year);
        branch = ArrayAdapter.createFromResource(this,R.array.branch,android.R.layout.simple_spinner_item);
        year = ArrayAdapter.createFromResource(this,R.array.year,android.R.layout.simple_spinner_item);

        year.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        branch.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);

        b_sbranch.setAdapter(branch);
        b_year.setAdapter(year);

        b_sbranch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                br = (int) parent.getItemIdAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(getBaseContext(),"nothing selected in branch",Toast.LENGTH_LONG).show();
            }
        });
        b_year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                yr = (int) parent.getItemIdAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                Toast.makeText(getBaseContext(), "nothing selected in year", Toast.LENGTH_LONG).show();
            }

        });

    }
    public void next(View view){
        if (isNetworkAvailable()) {
            progressDialog.show();

            btask task = new btask(this);
            task.execute(name);
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

    public class btask extends AsyncTask<String,String,String>{
        Context ctx;
        btask(Context ctx){
            this.ctx = ctx;
        }

        @Override
        protected void onPostExecute(String s) {
            progressDialog.dismiss();
           if(s.equals("error")){
                AlertDialog alertDialog = new AlertDialog.Builder(ctx).create();
                alertDialog.setTitle("notice");
                alertDialog.setMessage("Server error, please report us");
                alertDialog.show();
            }else{
                Intent sub = new Intent(getApplicationContext(),Subject.class);
                sub.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                sub.putExtra("branch",br);
                sub.putExtra("year",yr);
                sub.putExtra("table",table);
                sub.putExtra("id",ID);
                startActivity(sub);
               finish();
            }
        }

        @Override
        protected String doInBackground(String... params) {

            String by_url = "http://jecattendance.host22.com/byear.php";
            try {
                URL url = new URL(by_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String data = URLEncoder.encode("branch","UTF-8")+"="+URLEncoder.encode(String.valueOf(br),"UTF-8")+"&"+
                        URLEncoder.encode("year","UTF-8")+"="+URLEncoder.encode(String.valueOf(yr),"UTF-8")+"&"+
                        URLEncoder.encode("name","UTF-8")+"="+URLEncoder.encode(name,"UTF-8");

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
                    JSONObject parentobject = new JSONObject(response);
                    JSONArray parentarray = parentobject.getJSONArray("sresponse");
                    JSONObject finalobj = parentarray.getJSONObject(0);
                    table = finalobj.getString("table");
                    ID = String.valueOf(finalobj.getString("id"));
                }
                return response;


            } catch (MalformedURLException e) {
                e.printStackTrace();
            }catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }


            return null;
        }
    }
}

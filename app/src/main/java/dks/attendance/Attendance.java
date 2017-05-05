package dks.attendance;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
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
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by DKS on 1/25/2017.
 */

public class Attendance extends AppCompatActivity {
    TextView tv_name,tv_roll;
    String roll,name,x,ID,table;
    ProgressDialog progressDialog;
    AlertDialog alertDialog,network;
    int i=1;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.atteandace);
        Bundle values = getIntent().getExtras();
        roll = values.getString("roll");
        name = values.getString("name");
        table=values.getString("table");
        ID = values.getString("id");
        alertDialog = new AlertDialog.Builder(this).create();
        network = new AlertDialog.Builder(this).create();
        network.setTitle("Error");
        network.setMessage("No Internet Connection");

        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Wait..");

        tv_name = (TextView)findViewById(R.id.namea);
        tv_roll = (TextView)findViewById(R.id.aenroll);
        tv_roll.setText(roll);
        tv_name.setText(name);

    }
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Incomplete Attendance")
                .setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Attendance.this.finish();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    public void present(View view){
        if (isNetworkAvailable()) {
            progressDialog.show();
            Snackbar.make(view, roll + " is present", Snackbar.LENGTH_SHORT).show();

            i = i + 1;
            x = "1";
            Btask btask = new Btask(this);
            btask.execute(String.valueOf(i));
        }else{
            network.show();
        }



    }
    public void absent(View view){
        if (isNetworkAvailable()) {
            progressDialog.show();
            Snackbar.make(view, roll + " is absent", Snackbar.LENGTH_SHORT).show();

            i = i + 1;
            x = "0";
            Btask btask = new Btask(this);
            btask.execute(String.valueOf(i));
        }else{
            network.show();
        }

    }
    public void prev(View view){
        if (isNetworkAvailable()) {
            progressDialog.show();
            i = i - 1;
            if (i > 0) {
                x = "2";
                Btask btask = new Btask(this);
                btask.execute(String.valueOf(i));
            } else {
                progressDialog.dismiss();
                alertDialog.setTitle("Warning");
                alertDialog.setMessage("You are at 1st roll no");
                alertDialog.show();
                i = 1;
            }
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


    public class Btask extends AsyncTask<String,String,String>{

        Context ctx;
        Btask (Context ctx){
            this.ctx=ctx;
        }


        @Override
        protected String doInBackground(String... params) {

            String aurl="http://jecattendance.host22.com/atten.php";
            String no = params[0];

                try {
                    URL url = new URL(aurl);
                    HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                    String data = URLEncoder.encode("table","UTF-8")+"="+URLEncoder.encode(table,"UTF-8")+"&"+
                            URLEncoder.encode("data","UTF-8")+"="+URLEncoder.encode(x,"UTF-8")+"&"+
                            URLEncoder.encode("id","UTF-8")+"="+URLEncoder.encode(ID,"UTF-8")+"&"+
                            URLEncoder.encode("roll","UTF-8")+"="+URLEncoder.encode(roll,"UTF-8")+"&"+
                            URLEncoder.encode("sno","UTF-8")+"="+URLEncoder.encode(no,"UTF-8");
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
                        roll = finalobj.getString("roll");
                        name = finalobj.getString("name");
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
            return "error";
        }

        @Override
        protected void onPostExecute(String s) {
            progressDialog.dismiss();
            if(s.equals("error")){
                new AlertDialog.Builder(ctx)
                        .setTitle("Success")
                        .setMessage("Attendance completed")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                finish();
                            }
                        })
                        .show();
            }else {
                tv_name.setText(name);
                tv_roll.setText(roll);
                super.onPostExecute(s);
            }
        }


    }

}

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
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by DKS on 1/25/2017.
 */

public class Subject extends AppCompatActivity {
    Spinner spni_sub;
    String table,roll,name,ID;
    int br,yr,sub;
    TextView textView;
    AlertDialog network;
    ProgressDialog progressDialog;
    ArrayAdapter<CharSequence> cs1,cs2,cs3,cs4,it1,it2,it3,it4,ce1,ce2,ce3,ce4,me1,me2,me3,me4,ee1,ee2,ee3,ee4,ec1,ec2,ec3,ec4,ip1,ip2,ip3,ip4;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subject);


        Bundle values = getIntent().getExtras();
        textView = (TextView)findViewById(R.id.textView3);
        br = values.getInt("branch");
        yr = values.getInt("year");
        table = values.getString("table");
        ID = values.getString("id");
        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("loading");
        progressDialog.setCancelable(false);
        network = new AlertDialog.Builder(this).create();
        network.setMessage("No Internet Connection");
        network.setTitle("Error");

        spni_sub = (Spinner)findViewById(R.id.spin_subject);
        setspin();

        spni_sub.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sub = (int)parent.getItemIdAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }
    public void setspin(){
        cs1 = ArrayAdapter.createFromResource(this,R.array.cs1,android.R.layout.simple_spinner_item);
        cs2 = ArrayAdapter.createFromResource(this,R.array.cs2,android.R.layout.simple_spinner_item);
        cs3 = ArrayAdapter.createFromResource(this,R.array.cs3,android.R.layout.simple_spinner_item);
        cs4 = ArrayAdapter.createFromResource(this,R.array.cs4,android.R.layout.simple_spinner_item);
        it1 = ArrayAdapter.createFromResource(this,R.array.it1,android.R.layout.simple_spinner_item);
        it2 = ArrayAdapter.createFromResource(this,R.array.it2,android.R.layout.simple_spinner_item);
        it3 = ArrayAdapter.createFromResource(this,R.array.it3,android.R.layout.simple_spinner_item);
        it4 = ArrayAdapter.createFromResource(this,R.array.it4,android.R.layout.simple_spinner_item);
        me1 = ArrayAdapter.createFromResource(this,R.array.me1,android.R.layout.simple_spinner_item);
        me2 = ArrayAdapter.createFromResource(this,R.array.me2,android.R.layout.simple_spinner_item);
        me3 = ArrayAdapter.createFromResource(this,R.array.me3,android.R.layout.simple_spinner_item);
        me4 = ArrayAdapter.createFromResource(this,R.array.me4,android.R.layout.simple_spinner_item);
        ee1 = ArrayAdapter.createFromResource(this,R.array.ee1,android.R.layout.simple_spinner_item);
        ee2 = ArrayAdapter.createFromResource(this,R.array.ee2,android.R.layout.simple_spinner_item);
        ee3 = ArrayAdapter.createFromResource(this,R.array.ee3,android.R.layout.simple_spinner_item);
        ee4 = ArrayAdapter.createFromResource(this,R.array.ee4,android.R.layout.simple_spinner_item);
        ec1 = ArrayAdapter.createFromResource(this,R.array.ec1,android.R.layout.simple_spinner_item);
        ec2 = ArrayAdapter.createFromResource(this,R.array.ec2,android.R.layout.simple_spinner_item);
        ec3 = ArrayAdapter.createFromResource(this,R.array.ec3,android.R.layout.simple_spinner_item);
        ec4 = ArrayAdapter.createFromResource(this,R.array.ec4,android.R.layout.simple_spinner_item);
        ip1 = ArrayAdapter.createFromResource(this,R.array.ip1,android.R.layout.simple_spinner_item);
        ip2 = ArrayAdapter.createFromResource(this,R.array.ip2,android.R.layout.simple_spinner_item);
        ip3 = ArrayAdapter.createFromResource(this,R.array.ip3,android.R.layout.simple_spinner_item);
        ip4 = ArrayAdapter.createFromResource(this,R.array.ip4,android.R.layout.simple_spinner_item);
        ce1 = ArrayAdapter.createFromResource(this,R.array.ce1,android.R.layout.simple_spinner_item);
        ce2 = ArrayAdapter.createFromResource(this,R.array.ce2,android.R.layout.simple_spinner_item);
        ce3 = ArrayAdapter.createFromResource(this,R.array.ce3,android.R.layout.simple_spinner_item);
        ce4 = ArrayAdapter.createFromResource(this,R.array.ce4,android.R.layout.simple_spinner_item);

        cs1.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        cs2.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        cs3.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        cs4.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        it1.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        it2.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        it3.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        it4.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        me1.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        me2.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        me3.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        me4.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        ce1.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        ce2.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        ce3.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        ce4.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        ip1.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        ip2.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        ip3.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        ip4.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        ee1.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        ee2.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        ee3.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        ee4.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        ec1.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        ec2.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        ec3.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        ec4.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);


        if((br==0)&&(yr==0)){
            spni_sub.setAdapter(ce1);
        }
        if((br==0)&&(yr==1)){
            spni_sub.setAdapter(ce2);
        }
        if((br==0)&&(yr==2)){
            spni_sub.setAdapter(ce3);
        }
        if((br==0)&&(yr==3)){
            spni_sub.setAdapter(ce4);
        }
        if((br==1)&&(yr==0)){
            spni_sub.setAdapter(cs1);
        }
        if((br==1)&&(yr==1)){
            spni_sub.setAdapter(cs2);
        }
        if((br==1)&&(yr==2)){
            spni_sub.setAdapter(cs3);
        }
        if((br==1)&&(yr==3)){
            spni_sub.setAdapter(cs4);
        }
        if((br==2)&&(yr==0)){
            spni_sub.setAdapter(ec1);
        }
        if((br==2)&&(yr==1)){
            spni_sub.setAdapter(ec2);
        }
        if((br==2)&&(yr==2)){
            spni_sub.setAdapter(ec3);
        }
        if((br==2)&&(yr==3)){
            spni_sub.setAdapter(ec4);
        }
        if((br==3)&&(yr==0)){
            spni_sub.setAdapter(ee1);
        }
        if((br==3)&&(yr==1)){
            spni_sub.setAdapter(ee2);
        }
        if((br==3)&&(yr==2)){
            spni_sub.setAdapter(ee3);
        }
        if((br==3)&&(yr==3)){
            spni_sub.setAdapter(ee4);
        }
        if((br==4)&&(yr==0)){
            spni_sub.setAdapter(ip1);
        }
        if((br==4)&&(yr==1)){
            spni_sub.setAdapter(ip2);
        }
        if((br==4)&&(yr==2)){
            spni_sub.setAdapter(ip3);
        }
        if((br==4)&&(yr==3)){
            spni_sub.setAdapter(ip4);
        }
        if((br==5)&&(yr==0)){
            spni_sub.setAdapter(it1);
        }
        if((br==5)&&(yr==1)){
            spni_sub.setAdapter(it2);
        }
        if((br==5)&&(yr==2)){
            spni_sub.setAdapter(it3);
        }
        if((br==5)&&(yr==3)){
            spni_sub.setAdapter(it4);
        }
        if((br==6)&&(yr==0)){
            spni_sub.setAdapter(me1);
        }
        if((br==6)&&(yr==1)){
            spni_sub.setAdapter(me2);
        }
        if((br==6)&&(yr==2)){
            spni_sub.setAdapter(me3);
        }
        if((br==6)&&(yr==3)){
            spni_sub.setAdapter(me4);
        }

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void start(View view){
        if (isNetworkAvailable()) {

            progressDialog.show();

            btask task = new btask(this);
            task.execute(table, ID, String.valueOf(sub));
        }else{
            network.show();
        }

    }

    public class btask extends AsyncTask<String,String,String> {
        Context ctx;
        btask(Context ctx){
            this.ctx = ctx;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            progressDialog.dismiss();

            Intent atten = new Intent(getApplicationContext(),Attendance2.class);
            //atten.putExtra("roll",roll);
            //atten.putExtra("name",name);
            atten.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            atten.putExtra("table",table);
            atten.putExtra("id",ID);
            startActivity(atten);
            finish();
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(String... params) {

            String by_url = "http://jecattendance.host22.com/sub.php";

            String tbl = params[0];
            String id = params[1];
            String sb = params[2];
            try {
                URL url = new URL(by_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String data = URLEncoder.encode("table","UTF-8")+"="+URLEncoder.encode(tbl,"UTF-8")+"&"+
                        URLEncoder.encode("sub","UTF-8")+"="+URLEncoder.encode(sb,"UTF-8")+"&"+
                        URLEncoder.encode("id","UTF-8")+"="+URLEncoder.encode(id,"UTF-8");
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

            return null;
        }
    }
}

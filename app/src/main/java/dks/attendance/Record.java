package dks.attendance;

import android.app.ProgressDialog;
import android.content.Context;
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
import android.widget.ProgressBar;
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
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by DKS on 1/25/2017.
 */

public class Record extends AppCompatActivity {
    Spinner spin_sub_stu;
    TextView tv_name,tv_total,tv_atten,tv_percent;
    int br,yr,p,t,sub;
    String name,table,roll,sr;
    ProgressDialog progressDialog;
    AlertDialog network;
    ArrayAdapter<CharSequence> cs1,cs2,cs3,cs4,it1,it2,it3,it4,ce1,ce2,ce3,ce4,me1,me2,me3,me4,ee1,ee2,ee3,ee4,ec1,ec2,ec3,ec4,ip1,ip2,ip3,ip4;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.record);

        Bundle values = getIntent().getExtras();
        br = values.getInt("branch");
        yr = values.getInt("year");
        roll = values.getString("roll");
        name = values.getString("name");
        table = values.getString("table");

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("loading..");
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        network = new AlertDialog.Builder(this).create();
        network.setTitle("Error");
        network.setMessage("No Internet Connection");


        tv_atten=(TextView)findViewById(R.id.atten);
        tv_name =(TextView)findViewById(R.id.rec_name);
        tv_total =(TextView)findViewById(R.id.total);
        tv_percent= (TextView)findViewById(R.id.percent);

        tv_name.setText("Hello, "+name);




        spin_sub_stu = (Spinner)findViewById(R.id.spin_sub_stu);

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
            spin_sub_stu.setAdapter(ce1);
        }
        if((br==0)&&(yr==1)){
            spin_sub_stu.setAdapter(ce2);
        }
        if((br==0)&&(yr==2)){
            spin_sub_stu.setAdapter(ce3);
        }
        if((br==0)&&(yr==3)){
            spin_sub_stu.setAdapter(ce4);
        }
        if((br==1)&&(yr==0)){
            spin_sub_stu.setAdapter(cs1);
        }
        if((br==1)&&(yr==1)){
            spin_sub_stu.setAdapter(cs2);
        }
        if((br==1)&&(yr==2)){
            spin_sub_stu.setAdapter(cs3);
        }
        if((br==1)&&(yr==3)){
            spin_sub_stu.setAdapter(cs4);
        }
        if((br==2)&&(yr==0)){
            spin_sub_stu.setAdapter(ec1);
        }
        if((br==2)&&(yr==1)){
            spin_sub_stu.setAdapter(ec2);
        }
        if((br==2)&&(yr==2)){
            spin_sub_stu.setAdapter(ec3);
        }
        if((br==2)&&(yr==3)){
            spin_sub_stu.setAdapter(ec4);
        }
        if((br==3)&&(yr==0)){
            spin_sub_stu.setAdapter(ee1);
        }
        if((br==3)&&(yr==1)){
            spin_sub_stu.setAdapter(ee2);
        }
        if((br==3)&&(yr==2)){
            spin_sub_stu.setAdapter(ee3);
        }
        if((br==3)&&(yr==3)){
            spin_sub_stu.setAdapter(ee4);
        }
        if((br==4)&&(yr==0)){
            spin_sub_stu.setAdapter(ip1);
        }
        if((br==4)&&(yr==1)){
            spin_sub_stu.setAdapter(ip2);
        }
        if((br==4)&&(yr==2)){
            spin_sub_stu.setAdapter(ip3);
        }
        if((br==4)&&(yr==3)){
            spin_sub_stu.setAdapter(ip4);
        }
        if((br==5)&&(yr==0)){
            spin_sub_stu.setAdapter(it1);
        }
        if((br==5)&&(yr==1)){
            spin_sub_stu.setAdapter(it2);
        }
        if((br==5)&&(yr==2)){
            spin_sub_stu.setAdapter(it3);
        }
        if((br==5)&&(yr==3)){
            spin_sub_stu.setAdapter(it4);
        }
        if((br==6)&&(yr==0)){
            spin_sub_stu.setAdapter(me1);
        }
        if((br==6)&&(yr==1)){
            spin_sub_stu.setAdapter(me2);
        }
        if((br==6)&&(yr==2)){
            spin_sub_stu.setAdapter(me3);
        }
        if((br==6)&&(yr==3)){
            spin_sub_stu.setAdapter(me4);
        }

        spin_sub_stu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (isNetworkAvailable()) {
                    sub = (int) parent.getItemIdAtPosition(position);
                    progressDialog.show();
                    Btask btask = new Btask(getApplicationContext());
                    btask.execute();
                }else{
                    network.show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public class Btask extends AsyncTask<String,String,String>{
        Context ctx;
        Btask(Context ctx){
            this.ctx=ctx;
        }

        @Override
        protected void onPostExecute(String s) {
            if(s.equals("unknown")){
                Toast.makeText(getApplicationContext(),"Internal error..",Toast.LENGTH_LONG).show();
            }else {
                tv_atten.setText("Class Attend:  " + String.valueOf(p));
                tv_total.setText("Total Class:  " + String.valueOf(t));
                if (t != 0) {
                    float a =p;
                    float b= t;
                    float f =(a / b)*100;
                    tv_percent.setText(String.valueOf(f) + " %");
                } else {
                    tv_percent.setText("100 %");
                }
            }
            progressDialog.dismiss();
            super.onPostExecute(s);
        }

        @Override
        protected String doInBackground(String... params) {
            String check_url="http://jecattendance.host22.com/rec.php";

            try {
                URL url = new URL(check_url);
                HttpURLConnection httpURLConnection =(HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String data = URLEncoder.encode("table","UTF-8")+"="+URLEncoder.encode(table,"UTF-8")+"&"+
                        URLEncoder.encode("sub","UTF-8")+"="+URLEncoder.encode(String.valueOf(sub),"UTF-8")+"&"+
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
                sr=response;
                p=0;
                t=0;
                if(!response.equals("error")) {
                    if(!response.equals("invalid")) {
                        JSONObject parentobject = new JSONObject(response);
                        JSONArray parentarray = parentobject.getJSONArray("sresponse");
                        for(int z=0;z<parentarray.length();z++){
                            JSONObject finalobj = parentarray.getJSONObject(z);
                            String ans = finalobj.getString("data");
                            if(!ans.equals(null)){
                                p=p+Integer.parseInt(ans);
                                t=t+1;
                            }

                        }
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
    }
}

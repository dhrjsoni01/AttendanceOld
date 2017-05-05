package dks.attendance;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.ProgressBar;
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
import java.util.ArrayList;
import java.util.List;

/**
 * Created by DKS on 2/17/2017.
 */

public class Attendance2 extends AppCompatActivity {
    String table,ID;
    int count = 1;
    ListView lvstudent;
    int length=10,go=0;
    int a1,a2,a3,a4,a5,a6,a7,a8,a9,a0;
    ProgressDialog progressDialog;
    Button btn_next;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attendace2);
        Bundle last = getIntent().getExtras();
        ID = last.getString("id");
        table = last.getString("table");
        a0=a1=a2=a3=a4=a5=a6=a7=a8=a9=0;
        lvstudent = (ListView)findViewById(R.id.list1);
        lvstudent.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        btn_next = (Button)findViewById(R.id.next_button);
        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Wait..");
        firstcall();
        lvstudent.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position==0){
                    if (a0==0){
                        a0=1;
                    }else {
                        a0=0;
                    }
                }else  if (position==1){
                    if (a1==0){
                        a1=1;
                    }else {
                        a1=0;
                    }

                }else  if (position==2){
                    if (a2==0){
                        a2=1;
                    }else {
                        a2=0;
                    }

                }else  if (position==3){
                    if (a3==0){
                        a3=1;
                    }else {
                        a3=0;
                    }

                }else  if (position==4){
                    if (a4==0){
                        a4=1;
                    }else {
                        a4=0;
                    }

                }else  if (position==5){
                    if (a5==0){
                        a5=1;
                    }else {
                        a5=0;
                    }

                }else  if (position==6){
                    if (a6==0){
                        a6=1;
                    }else {
                        a6=0;
                    }

                }else  if (position==7){
                    if (a7==0){
                        a7=1;
                    }else {
                        a7=0;
                    }

                }else  if (position==8){
                    if (a8==0){
                        a8=1;
                    }else {
                        a8=0;
                    }

                }else  if (position==9){
                    if (a9==0){
                        a9=1;
                    }else {
                        a9=0;
                    }

                }
            }

        });
    }



    public void next(View view){
        progressDialog.show();
        count=count+10;
        if (length<10){
            go=1;
        }
        Bactivity bactivity = new Bactivity(this);
        bactivity.execute();

    }
    public void prev(View view){
        if(count>10) {
            progressDialog.show();
            count = count - 10;
            Bactivity bactivity = new Bactivity(this);
            bactivity.execute();
        }else{
            new AlertDialog.Builder(this)
                    .setTitle("Warning")
                    .setMessage("You are at 1st page")
                    .setCancelable(true)
                    .show();
        }
    }
    public void firstcall (){
        progressDialog.show();
        Bactivity bactivity = new Bactivity(this);
        bactivity.execute();
    }
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Incomplete Attendance")
                .setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Attendance2.this.finish();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    public class Bactivity extends AsyncTask<String,String,String>{
        Context ctx;
        Bactivity(Context ctx){
            this.ctx=ctx;
        };
        List<String> info= new ArrayList<String>();
        @Override
        protected String doInBackground(String... params) {
            String aurl="http://jecattendance.host22.com/atten2.php";

            try {
                URL url = new URL(aurl);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String data = URLEncoder.encode("table","UTF-8")+"="+URLEncoder.encode(table,"UTF-8")+"&"+
                        URLEncoder.encode("a0","UTF-8")+"="+URLEncoder.encode(String.valueOf(a0),"UTF-8")+"&"+
                        URLEncoder.encode("a1","UTF-8")+"="+URLEncoder.encode(String.valueOf(a1),"UTF-8")+"&"+
                        URLEncoder.encode("a2","UTF-8")+"="+URLEncoder.encode(String.valueOf(a2),"UTF-8")+"&"+
                        URLEncoder.encode("a3","UTF-8")+"="+URLEncoder.encode(String.valueOf(a3),"UTF-8")+"&"+
                        URLEncoder.encode("a4","UTF-8")+"="+URLEncoder.encode(String.valueOf(a4),"UTF-8")+"&"+
                        URLEncoder.encode("a5","UTF-8")+"="+URLEncoder.encode(String.valueOf(a5),"UTF-8")+"&"+
                        URLEncoder.encode("a6","UTF-8")+"="+URLEncoder.encode(String.valueOf(a6),"UTF-8")+"&"+
                        URLEncoder.encode("a7","UTF-8")+"="+URLEncoder.encode(String.valueOf(a7),"UTF-8")+"&"+
                        URLEncoder.encode("a8","UTF-8")+"="+URLEncoder.encode(String.valueOf(a8),"UTF-8")+"&"+
                        URLEncoder.encode("a9","UTF-8")+"="+URLEncoder.encode(String.valueOf(a9),"UTF-8")+"&"+
                        URLEncoder.encode("id","UTF-8")+"="+URLEncoder.encode(ID,"UTF-8")+"&"+
                        URLEncoder.encode("len","UTF-8")+"="+URLEncoder.encode(String.valueOf(length),"UTF-8")+"&"+
                        URLEncoder.encode("no","UTF-8")+"="+URLEncoder.encode(String.valueOf(count),"UTF-8");
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
                    for(int i=0;i<parentarray.length();i++){
                        JSONObject finalobject = parentarray.getJSONObject(i);
                        if(finalobject.getString("name").equals("end")){
                            length = parentarray.length()-1;
                            break;
                        }
                        info.add(finalobject.getString("roll")+"\n"+finalobject.getString("name"));
                    }
                    return response;
                }

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

        @Override
        protected void onPostExecute(String result) {
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.row,R.id.txt_title,info);
            lvstudent.setAdapter(arrayAdapter);
            progressDialog.dismiss();
            a0=a1=a2=a3=a4=a5=a6=a7=a8=a9=0;
            if(result==null){
                Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_LONG);
            }else {
                if (go == 1) {
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
                }

                if (length < 10) {
                    btn_next.setText("Submit");
                }

                super.onPostExecute(result);
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
    }



}

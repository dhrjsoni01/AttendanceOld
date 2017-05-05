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
import android.widget.EditText;
import android.widget.Toast;

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
 * Created by DKS on 1/24/2017.
 */

public class registerpage extends AppCompatActivity {
    EditText et_name,et_eamil,et_pass,et_repass,et_mobile;
    String name,email,pass,repass,mobile;
    ProgressDialog progressDialog;
    AlertDialog network;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Registring..");
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        network = new AlertDialog.Builder(this).create();
        network.setMessage("No Internet Connection");
        network.setTitle("Error");

        et_eamil = (EditText)findViewById(R.id.new_email);
        et_name =(EditText)findViewById(R.id.new_name);
        et_pass = (EditText)findViewById(R.id.new_pass);
        et_repass = (EditText)findViewById(R.id.new_re_pass);
        et_mobile = (EditText)findViewById(R.id.mobile);


    }

    public void regnow(View view){
        if (isNetworkAvailable()) {
            name = et_name.getText().toString();
            email = et_eamil.getText().toString();
            pass = et_pass.getText().toString();
            repass = et_repass.getText().toString();
            mobile = et_mobile.getText().toString();
            if (name.length() < 3) {
                Toast.makeText(getApplicationContext(), "enter your name", Toast.LENGTH_LONG).show();
            } else if (email.length() < 9) {
                Toast.makeText(getApplicationContext(), "invalid email", Toast.LENGTH_LONG).show();
            } else if (pass.length() < 6) {
                Toast.makeText(getApplicationContext(), "Password is too short", Toast.LENGTH_LONG).show();
            } else if (mobile.length() < 10) {
                Toast.makeText(getApplicationContext(), "invalid mobile no", Toast.LENGTH_LONG).show();
            } else{
                if (pass.equals(repass)) {
                    Toast.makeText(getApplicationContext(), mobile, Toast.LENGTH_LONG).show();
                    progressDialog.show();
                    Btask btask = new Btask(this);
                    btask.execute();

                } else {
                    Toast.makeText(getApplicationContext(), "password dosn't matched", Toast.LENGTH_LONG).show();
                }

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
        Btask(Context ctx){
            this.ctx=ctx;
        }

        @Override
        protected String doInBackground(String... params) {
            String reg_url="http://jecattendance.host22.com/reg.php";

           try {
               URL url = new URL(reg_url);
               HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
               httpURLConnection.setDoOutput(true);
               httpURLConnection.setDoInput(true);
               OutputStream outputStream = httpURLConnection.getOutputStream();
               BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
               String data = URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8") + "&" +
                       URLEncoder.encode("pass", "UTF-8") + "=" + URLEncoder.encode(pass, "UTF-8") + "&" +
                       URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(name, "UTF-8") + "&" +
                       URLEncoder.encode("mob", "UTF-8") + "=" + URLEncoder.encode(mobile, "UTF-8");
               bufferedWriter.write(data);
               bufferedWriter.flush();
               bufferedWriter.close();
               outputStream.close();

               InputStream inputStream = httpURLConnection.getInputStream();
               BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
               String response ="";
               String line = "";
               while ((line=bufferedReader.readLine())!=null){
                   response+= line;
               }
               bufferedReader.close();
               inputStream.close();
               httpURLConnection.disconnect();

               return response;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return null;
        }

        @Override
        protected void onPostExecute(String s) {
            progressDialog.dismiss();
            if(s.equals("done")) {
                Toast.makeText(getApplicationContext(), "Registertion successfull..", Toast.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(), mobile, Toast.LENGTH_LONG).show();
                finish();
            }else{
                Toast.makeText(getApplicationContext(), "Email already registerd..", Toast.LENGTH_LONG).show();
            }
        }
    }
}

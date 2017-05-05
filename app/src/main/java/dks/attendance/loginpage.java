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
import android.widget.Button;
import android.widget.EditText;
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

public class loginpage extends AppCompatActivity {
    EditText et_email,et_pass;
    String email,pass;
    ProgressDialog progressDialog;
    AlertDialog network,ad;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        et_email = (EditText)findViewById(R.id.username);
        et_pass = (EditText)findViewById(R.id.password);

        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading, please wait..");
        network = new AlertDialog.Builder(this).create();
        network.setMessage("No Internet Connection");
        network.setTitle("Error");
    }

    public void loginnow(View view){
        if (isNetworkAvailable()) {
            email = et_email.getText().toString();
            pass = et_pass.getText().toString();
            if (email.length()<5||pass.length()<6) {
                ad = new AlertDialog.Builder(this).create();
                ad.setTitle("Warning");
                ad.setMessage("Invalid Email or Password");
                ad.show();
            }else{
                progressDialog.show();
                btask task = new btask(this);
                task.execute(email, pass);
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

    public class btask extends AsyncTask<String,String,String>
    {
        AlertDialog alertDialog;
        Context ctx;
        btask(Context ctx){
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

            String login_url = "http://jecattendance.host22.com/login.php";

            String uname = params[0];
            String upass = params[1];
            try {
                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String data = URLEncoder.encode("email","UTF-8")+"="+URLEncoder.encode(uname,"UTF-8")+"&"+
                        URLEncoder.encode("pass","UTF-8")+"="+URLEncoder.encode(upass,"UTF-8");
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
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String s) {
            progressDialog.dismiss();

            if (s.equals("invalid")){
                alertDialog.setMessage("Invalid Email or Password");
                alertDialog.show();

            }
            else if (s.equals("unverified")){
                alertDialog.setMessage("Sorry, Registration is not verified wait for some time or contact us");
                alertDialog.show();
            }
            else if (s.equals("error")){
                alertDialog.setMessage("Connection Error");
                alertDialog.show();
            }
            else{
                Intent branch = new Intent(getApplicationContext(), Branch.class);
                branch.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                branch.putExtra("name", s);
                startActivity(branch);
                finish();
            }
        }
    }


}

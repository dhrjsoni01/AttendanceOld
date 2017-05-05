package dks.attendance;

/**
 * Created by DKS on 2/24/2017.
 */


        import android.app.ProgressDialog;
        import android.content.Context;
        import android.content.Intent;
        import android.database.Cursor;
        import android.os.AsyncTask;
        import android.support.v7.app.AlertDialog;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.view.Menu;
        import android.view.MenuItem;
        import android.view.View;
        import android.widget.Button;
        import android.widget.CalendarView;
        import android.widget.ListView;
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
        import java.util.ArrayList;

public class Record2 extends AppCompatActivity {
    DbHelper database;
    CalendarView calendarView;
    ListView listView;
    String[] subjects;
    int br,yr;
    String roll,name,table;
    TextView nameofuser;
    ProgressDialog progressDialog;
    AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.record2);

        Bundle values = getIntent().getExtras();
        br = values.getInt("branch");
        yr = values.getInt("year");
        roll = values.getString("roll");
        name = values.getString("name");
        table = values.getString("table");

        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading..");
        progressDialog.show();

        alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setMessage("Sorry, No Record Found");
        alertDialog.setTitle("Error");

        nameofuser = (TextView)findViewById(R.id.nameofuser);
        nameofuser.setText("Hello "+ name);

        database =  new DbHelper(this);
        listView = (ListView)findViewById(R.id.list_view);
        database = new DbHelper(this);
        calendarView = (CalendarView)findViewById(R.id.calendar);

        set_subject();
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                ArrayList<model> arrayList = new ArrayList<>();
                Cursor res;
                res = database.getdata(String.valueOf(year) + "-" + String.valueOf(String.format("%02d",month + 1)) + "-" + String.valueOf(String.format("%02d",dayOfMonth)));
              /*  if (month<9 && dayOfMonth<9) {
                    res = database.getdata(String.valueOf(year) + "-0" + String.valueOf(month + 1) + "-0" + String.valueOf(dayOfMonth));
                }
                else if (month>9 && dayOfMonth<9) {
                    res = database.getdata(String.valueOf(year) + "-" + String.valueOf(month + 1) + "-0" + String.valueOf(dayOfMonth));
                }
                else if (month<9 && dayOfMonth>9) {
                    res = database.getdata(String.valueOf(year) + "-0" + String.valueOf(month + 1) + "-" + String.valueOf(dayOfMonth));
                }
                else {
                    res = database.getdata(String.valueOf(year) + "-" + String.valueOf(month + 1) + "-" + String.valueOf(dayOfMonth));
                }*/
                if (res.getCount()==0){
                    Toast.makeText(getApplicationContext(),"no result",Toast.LENGTH_LONG).show();
                    listView.setAdapter(null);
                    return;
                }
                while (res.moveToNext()){
                    String status="";
                    if (Integer.parseInt(res.getString(1))==1){
                        status = "You was present";
                    }else{
                        status = "You was absent";
                    }
                    arrayList.add(new model(subjects[Integer.parseInt(res.getString(0))],"By: "+ res.getString(2),status));
                }
                CustomAdaptor customAdaptor = new CustomAdaptor(getApplicationContext(),R.layout.row2,arrayList);
                listView.setAdapter(customAdaptor);

            }
        });

        getdata getdata = new getdata(this);
        getdata.execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_record, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.percent_view){
            Intent check = new Intent(getApplicationContext(),Record.class);
            check.putExtra("branch",br);
            check.putExtra("year",yr);
            check.putExtra("name",name);
            check.putExtra("table",table);
            check.putExtra("roll",roll);
            startActivity(check);
        }
        if (id == R.id.percent_view_action){
            Intent check = new Intent(getApplicationContext(),Record.class);
            check.putExtra("branch",br);
            check.putExtra("year",yr);
            check.putExtra("name",name);
            check.putExtra("table",table);
            check.putExtra("roll",roll);
            startActivity(check);
        }
        return super.onOptionsItemSelected(item);
    }

    public class getdata extends AsyncTask<String,String,String>{
        Context ctx;
        public getdata(Context ctx) {
            this.ctx=ctx;
        }

        @Override
        protected void onPreExecute() {
            database.dropdata();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String check_url="http://jecattendance.host22.com/rec2.php";

            try {
                URL url = new URL(check_url);
                HttpURLConnection httpURLConnection =(HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String data = URLEncoder.encode("table","UTF-8")+"="+URLEncoder.encode(table,"UTF-8")+"&"+
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
                        for(int z=0;z<parentarray.length();z++){
                            JSONObject finalobj = parentarray.getJSONObject(z);
                            String dateof= finalobj.getString("date");
                            String userof = finalobj.getString("user");
                            String subjectof = finalobj.getString("subject");
                            String valueof = finalobj.getString("data");
                            int intsub= Integer.parseInt(subjectof);
                            int intdata = Integer.parseInt(valueof);
                            boolean add = database.insetdata(dateof,intsub,intdata,userof);
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

        @Override
        protected void onPostExecute(String s) {
            progressDialog.dismiss();
            if (s.equals("error")){
                alertDialog.show();
            }
        }
    }

    public void set_subject(){
        if((br==0)&&(yr==0)){
            subjects =getResources().getStringArray(R.array.ce1);
        }
        if((br==0)&&(yr==1)){
            subjects =getResources().getStringArray(R.array.ce2);
        }
        if((br==0)&&(yr==2)){
            subjects =getResources().getStringArray(R.array.ce3);
        }
        if((br==0)&&(yr==3)){
            subjects =getResources().getStringArray(R.array.ce4);
        }
        if((br==1)&&(yr==0)){
            subjects =getResources().getStringArray(R.array.cs1);
        }
        if((br==1)&&(yr==1)){
            subjects =getResources().getStringArray(R.array.cs2);
        }
        if((br==1)&&(yr==2)){
            subjects =getResources().getStringArray(R.array.cs3);
        }
        if((br==1)&&(yr==3)){
            subjects =getResources().getStringArray(R.array.cs4);
        }
        if((br==2)&&(yr==0)){
            subjects =getResources().getStringArray(R.array.ec1);
        }
        if((br==2)&&(yr==1)){
            subjects =getResources().getStringArray(R.array.ec2);
        }
        if((br==2)&&(yr==2)){
            subjects =getResources().getStringArray(R.array.ec3);
        }
        if((br==2)&&(yr==3)){
            subjects =getResources().getStringArray(R.array.ec4);
        }
        if((br==3)&&(yr==0)){
            subjects =getResources().getStringArray(R.array.ee1);
        }
        if((br==3)&&(yr==1)){
            subjects =getResources().getStringArray(R.array.ee2);
        }
        if((br==3)&&(yr==2)){
            subjects =getResources().getStringArray(R.array.ee3);
        }
        if((br==3)&&(yr==3)){
            subjects =getResources().getStringArray(R.array.ee4);
        }
        if((br==4)&&(yr==0)){
            subjects =getResources().getStringArray(R.array.ip1);
        }
        if((br==4)&&(yr==1)){
            subjects =getResources().getStringArray(R.array.ip2);
        }
        if((br==4)&&(yr==2)){
            subjects =getResources().getStringArray(R.array.ip3);
        }
        if((br==4)&&(yr==3)){
            subjects =getResources().getStringArray(R.array.ip4);
        }
        if((br==5)&&(yr==0)){
            subjects =getResources().getStringArray(R.array.it1);
        }
        if((br==5)&&(yr==1)){
            subjects =getResources().getStringArray(R.array.it2);
        }
        if((br==5)&&(yr==2)){
            subjects =getResources().getStringArray(R.array.it3);
        }
        if((br==5)&&(yr==3)){
            subjects =getResources().getStringArray(R.array.it4);
        }
        if((br==6)&&(yr==0)){
            subjects =getResources().getStringArray(R.array.me1);
        }
        if((br==6)&&(yr==1)){
            subjects =getResources().getStringArray(R.array.me2);
        }
        if((br==6)&&(yr==2)){
            subjects =getResources().getStringArray(R.array.me3);
        }
        if((br==6)&&(yr==3)){
            subjects =getResources().getStringArray(R.array.me4);
        }
    }
}

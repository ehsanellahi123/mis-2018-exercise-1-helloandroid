package com.example.mis.helloandroid;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;


public class MainActivity extends AppCompatActivity {

    TextView tv1, tv2;
    Button b1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv1 = (TextView) findViewById(R.id.tv1);
        tv2 = (TextView) findViewById(R.id.tv2);
        b1 = (Button) findViewById(R.id.b1);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getData(tv1.getText().toString());
            }
        });
    }

    private void getData(String url) {

        ConnectivityManager ConnectionManager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=ConnectionManager.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnected()==true )
        {
            new RetrieveFeedTask().execute(url);

        }
        else
        {
            Toast.makeText(MainActivity.this, "Network Not Available ! Connect internet", Toast.LENGTH_LONG).show();

        }


    }

    class RetrieveFeedTask extends AsyncTask<String, Void, String>
    {
        ProgressDialog p;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            p=new ProgressDialog(MainActivity.this);
            p.setMessage("please Wait");
            p.setCancelable(false);
            p.show();

        }

        @Override
        protected String doInBackground(String... strings)
        {
            String response = "";
            try {
                HttpParams httpParameters = new BasicHttpParams();
                // Set the timeout in milliseconds until a connection is established.
                // The default value is zero, that means the timeout is not used.
                int timeoutConnection = 3000;
                HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
                // Set the default socket timeout (SO_TIMEOUT)
                // in milliseconds which is the timeout for waiting for data.
                int timeoutSocket = 3000;
                HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

                DefaultHttpClient client = new DefaultHttpClient(httpParameters);

                HttpGet httpGet = new HttpGet(strings[0]);

                HttpResponse httpResponse = client.execute(httpGet);

                HttpEntity entity = httpResponse.getEntity();

                response = EntityUtils.toString(entity);

            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String s)
        {
            if(s!=null||s !="")
            {
                super.onPostExecute(s);
                if (p.isShowing())
                    p.dismiss();
                tv2.setText(s.toString());
            }
            else
            {
                Toast.makeText(MainActivity.this, "No Data Found", Toast.LENGTH_LONG).show();
            }
        }
    }
}

package com.example.foodminderschedule;

import android.os.AsyncTask;
import android.renderscript.ScriptGroup;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class fetchData extends AsyncTask<String, Void, String> {
    String data = "";
    String name = "";



    @Override
    protected void onPostExecute(String url) {
        super.onPostExecute(url);
        MainActivity.prodName = this.name;
    }


    @Override
    protected String doInBackground(String... urls) {
        try {
            URL url = new URL(urls[0]);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            while(line != null){
                line = bufferedReader.readLine();
                data = data+line;
            }

            JSONObject JO = new JSONObject(data);
            JSONArray prod = JO.getJSONArray("products");
            //name = JO.get("title")+"";
            name = prod.getJSONObject(0).getString("product_name");

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("TAG",data);

        return null;
    }
}

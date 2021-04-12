package com.example.whatstheweather;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity
{
    EditText cityEditText;
    TextView resultTextView;

    public void getWeather(View view)
    {
        try
        {
            DownloadTask task = new DownloadTask();
            String cityName = cityEditText.getText().toString();
            task.execute("https://samples.openweathermap.org/data/2.5/weather?q=London,uk&appid=439d4b804bc8187953eb36d2a8c26a02");
            resultTextView.setVisibility(View.VISIBLE);
            //Tastatur
            InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            mgr.hideSoftInputFromWindow(cityEditText.getWindowToken(), 0);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    public class DownloadTask extends AsyncTask<String,Void,String>
    {
        @Override
        protected String doInBackground(String... urls)
        {
            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;

            try
            {
                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();

                while (data != -1)
                {
                    char current = (char) data;
                    result += current;
                    data = reader.read();
                }

                return result;
            }
            catch (Exception e)
            {
                e.printStackTrace();;
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s)
        {
            super.onPostExecute(s);

            try
            {
                JSONObject jsonObject = new JSONObject(s);
                String weatherInfo = jsonObject.getString("main");
                Log.i("WeatherINFO", weatherInfo);

                JSONObject mainJSONObject = new JSONObject(weatherInfo);
                String temperatureInK = mainJSONObject.getString("temp");
                int temperatureInC = (int) (Double.parseDouble(temperatureInK) - 273.15);
                String temperature = temperatureInC + "" + " °C";

                if (!temperature.equals(""))
                {
                    resultTextView.setText(temperature);
                }
                else
                {
                    Toast.makeText(MainActivity.this, "Temperatur konnte nicht gefunden werden", Toast.LENGTH_SHORT).show();
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

        }

    }
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cityEditText = findViewById(R.id.cityEditText);
        resultTextView = findViewById(R.id.resultTextView);
    }
}
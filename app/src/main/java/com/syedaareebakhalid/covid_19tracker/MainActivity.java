package com.syedaareebakhalid.covid_19tracker;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.syedaareebakhalid.covid_19tracker.Models.Template;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements Callback<Template>, View.OnClickListener {
    private static final String TAG = MainActivity.class.getSimpleName();

    public static final String BASE_URL = "https://corona.lmao.ninja/";

    private static Retrofit retrofit = null;

    public Template template;

    private TextView recoveredTextView;
    private TextView casesTextView;
    private TextView deathTextView;

    private Button btnNextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(retrofit == null){
            retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        }

        ApiService apiService = retrofit.create(ApiService.class);
        Call<Template> call = apiService.getData("all");
        call.enqueue(this);

        btnNextView = (Button) findViewById(R.id.btnNextView);
        btnNextView.setOnClickListener(this);

    }

    @Override
    public void onResponse(Call<Template> call, Response<Template> response) {
        if(response.isSuccessful()){

            template = response.body();

            deathTextView = (TextView) findViewById(R.id.deathTextView);
            casesTextView = (TextView) findViewById(R.id.casesTextView);
            recoveredTextView = (TextView) findViewById(R.id.recoveredTextView);

            String totalCases = template.getCases().toString();
            String totalDeaths = template.getDeaths().toString();
            String totalRecovered = template.getRecovered().toString();

            deathTextView.setText(totalDeaths);
            casesTextView.setText(totalCases);
            recoveredTextView.setText(totalRecovered);
        }
    }

    @Override
    public void onFailure(Call<Template> call, Throwable t) {
        Log.e(TAG, t.toString());
    }

    @Override
    public void onClick(View v) {
        Intent nextView = new Intent(getApplicationContext(),ListByCountry.class);
        startActivity(nextView);
    }
}

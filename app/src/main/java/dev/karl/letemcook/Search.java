package dev.karl.letemcook;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Search extends AppCompatActivity implements RecViewInterface{

    String API_KEY;
    String API_URL = "https://api.api-ninjas.com/v1/recipe?query=";

    ArrayList<RecipeModel> recipeList = new ArrayList<>();
    int pagination = 0;

    EditText etSearchBar;
    TextView tvSearchBtn, noResIndi, back, forw;
    String searchStr;
    RecyclerView rv;
    ProgressBar pb;

    RecAdapter recAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        getWindow().setStatusBarColor(ContextCompat.getColor(Search.this, R.color.orange));
        getWindow().setNavigationBarColor(ContextCompat.getColor(Search.this, R.color.white));

        etSearchBar = findViewById(R.id.etSearchBar);
        tvSearchBtn = findViewById(R.id.tvSearchButton);
        noResIndi = findViewById(R.id.tvNoResultIndicator);

        back = findViewById(R.id.tvBack);
        forw = findViewById(R.id.tvForw);

        rv = findViewById(R.id.rvRecipe);
        pb = findViewById(R.id.progressBar);

        pb.setVisibility(View.INVISIBLE);

        API_KEY = getIntent().getStringExtra("apikey");

        noResIndi.setText("Search results will appear here");

        back.setVisibility(View.INVISIBLE);
        forw.setVisibility(View.INVISIBLE);

        tvSearchBtn.setOnClickListener(view -> {
            noResIndi.setText("");
            noResIndi.setVisibility(View.INVISIBLE);
            back.setVisibility(View.INVISIBLE);
            forw.setVisibility(View.INVISIBLE);
            searchStr = etSearchBar.getText().toString();
            if (isValidString(searchStr)){
                pagination = 0;
                getRecipes(searchStr, null);
            }else {
                //notify user
                Toast.makeText(Search.this, "invalid string", Toast.LENGTH_SHORT).show();

            }
        });

        back.setOnClickListener(view -> {
            //
            pagination -= 10;
            getRecipes(searchStr, pagination);
            noResIndi.setVisibility(View.INVISIBLE);
            forw.setVisibility(View.VISIBLE);
        });
        forw.setOnClickListener(view -> {
            //
            pagination += 10;
            getRecipes(searchStr, pagination);
        });
        //getRecipes("asd", null);

        //ads
        AdView mAdView = findViewById(R.id.adViewHome);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    private void getRecipes(String searchTerm, @Nullable Integer offset){
        recipeList.clear();
        if (recAdapter != null){ recAdapter.notifyDataSetChanged(); }
        pb.setVisibility(View.VISIBLE);
        String full_url = API_URL + searchTerm;

        if (offset != null) full_url = full_url + "&offset=" + offset;

        RequestQueue queue = Volley.newRequestQueue(Search.this);

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, full_url, null, response -> {
            //
            Log.d("letemcook_log", response.toString());

            if (response.toString().equals("[]")){
                //empty
                noResIndi.setVisibility(View.VISIBLE);
                noResIndi.setText("No results for \"" + searchTerm + "\"");

                recipeList.clear();
                //update recview
                loadRecView();

            }else {
                Gson gson = new Gson();

                // Define the type of the collection you want to convert to
                Type personListType = new TypeToken<ArrayList<RecipeModel>>(){}.getType();

                // Convert JSON array string to List of Person objects
                recipeList = gson.fromJson(response.toString(), personListType);

                //
                loadRecView();

            }
            //int tempLen = response.length();

        }, error -> {
            //
            Log.d("letemcook_log", error.toString());
        }){
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("X-Api-Key", API_KEY);
                return headers;
            }
        };
        queue.add(request);
    }
    private static boolean isValidString(String username) {
        String regex = "^[a-zA-Z0-9 ]+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(username);
        return matcher.matches();
    }

    @Override
    public void onClickItem(int position) {
        Intent intent = new Intent(Search.this, RecipeViewer.class);
        intent.putExtra("recipe", recipeList.get(position));
        startActivity(intent);
    }

    private void loadRecView(){
        pb.setVisibility(View.INVISIBLE);

        if (recipeList.size() == 0 && pagination > 0){
            noResIndi.setVisibility(View.VISIBLE);
            noResIndi.setText("No more results");
            forw.setVisibility(View.INVISIBLE);
        }
        if (pagination == 0){
            back.setVisibility(View.INVISIBLE);
        } else if (pagination > 0) {
            back.setVisibility(View.VISIBLE);
        }
        if (recipeList.size() >= 10){
            forw.setVisibility(View.VISIBLE);
        }else{
            forw.setVisibility(View.INVISIBLE);
        }

        recipeList.add(new RecipeModel("","","",""));
        recAdapter = new RecAdapter(recipeList, this, Search.this);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(Search.this);
        rv.setLayoutManager(layoutManager);
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.setAdapter(recAdapter);

    }
}
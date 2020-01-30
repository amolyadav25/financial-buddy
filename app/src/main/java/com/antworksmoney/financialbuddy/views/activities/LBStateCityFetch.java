package com.antworksmoney.financialbuddy.views.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.antworksmoney.financialbuddy.R;
import com.antworksmoney.financialbuddy.helpers.Entity.RegionalDataInfo;
import com.antworksmoney.financialbuddy.helpers.adapters.RegionalDataListAdapter;
import com.antworksmoney.financialbuddy.helpers.dataFetch.AppConstant;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class LBStateCityFetch extends AppCompatActivity {

    private Toolbar searchtollbar;

    private MenuItem item_search;

    private Menu search_menu;

    private static final String TAG = "MainActivity";

    private android.widget.ProgressBar progressBar;

    private RecyclerView serviceList;

    private RegionalDataListAdapter dataListAdapter;

    private Intent intent;

    private SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intent = getIntent();
        setTheme(intent.getIntExtra("theme", R.style.AppTheme));
        this.setContentView(R.layout.activity_regional_data_fetch);

        searchtollbar = findViewById(R.id.searchtoolbar);

        progressBar = findViewById(R.id.loader);

        serviceList = findViewById(R.id.dataList);

        serviceList.setLayoutManager(new LinearLayoutManager(
                getApplicationContext(), LinearLayoutManager.VERTICAL, false));

        serviceList.setHasFixedSize(true);

        mSharedPreferences = getSharedPreferences("PersonalDetails", Context.MODE_PRIVATE);

        Toolbar mToolbar = findViewById(R.id.toolbar);

        String stateName = "";
        if (!intent.getStringExtra("stateName").trim().equalsIgnoreCase("")) {
            stateName = intent.getStringExtra("stateName");
        }

        if (!intent.getStringExtra("stateName").trim().equalsIgnoreCase("")) {

            mToolbar.setTitle("Available Cities");
            getCityIdFromTheList(stateName);

        } else {
            mToolbar.setTitle("Available States");
            getStatesDataIntoList();
        }

        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setDisplayShowTitleEnabled(true);
        }

        setSearchtollbar();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                    circleReveal(R.id.searchtoolbar, 1, true, true);
                else
                    searchtollbar.setVisibility(View.VISIBLE);

                item_search.expandActionView();
                return true;

            case android.R.id.home:
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void setSearchtollbar() {
        if (searchtollbar != null) {
            searchtollbar.inflateMenu(R.menu.menu_search);
            search_menu = searchtollbar.getMenu();

            searchtollbar.setNavigationOnClickListener(v -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                    circleReveal(R.id.searchtoolbar, 1, true, false);
                else
                    searchtollbar.setVisibility(View.GONE);
            });

            item_search = search_menu.findItem(R.id.action_filter_search);

            MenuItemCompat.setOnActionExpandListener(item_search, new MenuItemCompat.OnActionExpandListener() {
                @Override
                public boolean onMenuItemActionCollapse(MenuItem item) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        circleReveal(R.id.searchtoolbar, 1, true, false);
                    } else
                        searchtollbar.setVisibility(View.GONE);
                    return true;
                }

                @Override
                public boolean onMenuItemActionExpand(MenuItem item) {
                    return true;
                }
            });

            initSearchView();
        }
    }


    public void initSearchView() {
        final SearchView searchView = (SearchView) search_menu.findItem(R.id.action_filter_search).getActionView();
        searchView.setSubmitButtonEnabled(false);
        ImageView closeButton = searchView.findViewById(R.id.search_close_btn);
        closeButton.setImageResource(R.drawable.ic_close);
        EditText txtSearch = searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        txtSearch.setHint("Search..");
        txtSearch.setHintTextColor(Color.DKGRAY);
        txtSearch.setTextColor(getResources().getColor(R.color.tab_title_dark));
        AutoCompleteTextView searchTextView = searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        try {
            Field mCursorDrawableRes = TextView.class.getDeclaredField("mCursorDrawableRes");
            mCursorDrawableRes.setAccessible(true);
            mCursorDrawableRes.set(searchTextView, R.drawable.search_cursor);
        } catch (Exception e) {
            e.printStackTrace();
        }

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                callSearch(query);
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                callSearch(newText);
                return true;
            }

            void callSearch(String query) {
                if (dataListAdapter != null) {
                    dataListAdapter.getFilter().filter(query);

                } else {
                    Toast.makeText(getApplicationContext(), "Please Wait ! fetching cities !!", Toast.LENGTH_SHORT).show();
                }


            }

        });

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void circleReveal(int viewID, int posFromRight, boolean containsOverflow, final boolean isShow) {
        final View myView = findViewById(viewID);

        int width = myView.getWidth();

        if (posFromRight > 0)
            width -= (posFromRight * getResources().getDimensionPixelSize(R.dimen.abc_action_button_min_width_material)) - (getResources().getDimensionPixelSize(R.dimen.abc_action_button_min_width_material) / 2);
        if (containsOverflow)
            width -= getResources().getDimensionPixelSize(R.dimen.abc_action_button_min_width_overflow_material);

        int cx = width;
        int cy = myView.getHeight() / 2;

        Animator anim;
        if (isShow)
            anim = ViewAnimationUtils.createCircularReveal(myView, cx, cy, 0, (float) width);
        else
            anim = ViewAnimationUtils.createCircularReveal(myView, cx, cy, (float) width, 0);

        anim.setDuration((long) 220);

        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (!isShow) {
                    super.onAnimationEnd(animation);
                    myView.setVisibility(View.INVISIBLE);
                }
            }
        });

        if (isShow) myView.setVisibility(View.VISIBLE);
        anim.start();


    }


    private void getStatesDataIntoList() {
        ArrayList<RegionalDataInfo> dataList = new ArrayList<>();

        JsonObjectRequest dataRequest = new JsonObjectRequest(
                Request.Method.GET,
                AppConstant.commonAPIUrl + "commonapi/getState",
                null,
                response -> {
                    Log.e(TAG, response.toString());
                    progressBar.setVisibility(View.GONE);

                    try {
                        for (int i = 0; i < response.getJSONArray("state_list").length(); i++) {
                            dataList.add(new RegionalDataInfo(
                                    ((JSONObject) response.getJSONArray("state_list").get(i)).getString("state"),
                                    ((JSONObject) response.getJSONArray("state_list").get(i)).getString("code")));
                        }

                        dataListAdapter = new RegionalDataListAdapter(getApplicationContext(), dataList, serviceList);
                        serviceList.setAdapter(dataListAdapter);

                        if (dataListAdapter != null) {
                            dataListAdapter.setOnClick(position -> {
                                View view = Objects.requireNonNull(serviceList.findViewHolderForAdapterPosition(position)).itemView;

                                Intent intent = new Intent();
                                intent.putExtra("name", "stateName=" + ((TextView) view.findViewById(R.id.name)).getText().toString().trim());
                                intent.putExtra("stateCode", dataList.get(position).getCode());
                                setResult(RESULT_OK, intent);
                                finish();

                            });
                        }


                    } catch (Exception e) {
                        progressBar.setVisibility(View.GONE);
                        e.printStackTrace();
                    }


                },
                error ->
                {
                    progressBar.setVisibility(View.GONE);
                    Log.e(TAG, error.toString());

                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> params = new HashMap<>();
                params.put("Authorization", mSharedPreferences.getString("loginToken", ""));
                params.put("Content-Type", "application/json");
                return params;
            }
        };

        dataRequest.setShouldCache(false);

        dataRequest.setRetryPolicy(new DefaultRetryPolicy(
                AppConstant.MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(dataRequest);

    }

    private void getCityIdFromTheList(String cityName) {
        HashMap<String, String> dataFetch = new HashMap<>();

        JsonObjectRequest dataRequest = new JsonObjectRequest(
                Request.Method.GET,
                AppConstant.commonAPIUrl + "commonapi/getState",
                null,
                response -> {
                    Log.e(TAG, response.toString());

                    try {
                        for (int i = 0; i < response.getJSONArray("state_list").length(); i++) {
                            dataFetch.put(
                                    ((JSONObject) response.getJSONArray("state_list").get(i)).getString("state").trim(),
                                    ((JSONObject) response.getJSONArray("state_list").get(i)).getString("code").trim());
                        }

                        Log.e(TAG, cityName);
                        Log.e(TAG, dataFetch.get(cityName));

                        getCityNameFromAPI(dataFetch.get(cityName));
                    } catch (Exception e) {
                        progressBar.setVisibility(View.GONE);
                        e.printStackTrace();
                    }


                },
                error ->
                {
                    progressBar.setVisibility(View.GONE);
                    Log.e(TAG, error.toString());

                }) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> params = new HashMap<>();
                params.put("Authorization", mSharedPreferences.getString("loginToken", ""));
                params.put("Content-Type", "application/json");
                return params;
            }
        };

        dataRequest.setShouldCache(false);

        dataRequest.setRetryPolicy(new DefaultRetryPolicy(
                AppConstant.MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(dataRequest);

    }

    private void getCityNameFromAPI(String stateCode) {

        ArrayList<RegionalDataInfo> dataList = new ArrayList<>();

        JSONObject outerObject = new JSONObject();

        try {
            outerObject.put("state_code", stateCode);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.e(TAG, outerObject.toString());

        JsonObjectRequest dataRequest = new JsonObjectRequest(
                Request.Method.POST,
                AppConstant.commonAPIUrl + "commonapi/cityList",
                outerObject,
                response -> {
                    Log.e(TAG, response.toString());
                    progressBar.setVisibility(View.GONE);

                    try {
                        for (int i = 0; i < response.getJSONArray("city_list").length(); i++) {
                            dataList.add(new RegionalDataInfo(
                                    ((JSONObject) response.getJSONArray("city_list").get(i)).getString("id"),
                                    ((JSONObject) response.getJSONArray("city_list").get(i)).getString("city_name"),
                                    ((JSONObject) response.getJSONArray("city_list").get(i)).getString("state_code")));
                        }

                        dataListAdapter = new RegionalDataListAdapter(getApplicationContext(), dataList, serviceList);
                        serviceList.setAdapter(dataListAdapter);

                        if (dataListAdapter != null) {
                            dataListAdapter.setOnClick(position -> {
                                View view = Objects.requireNonNull(serviceList.findViewHolderForAdapterPosition(position)).itemView;

                                Intent intent = new Intent();
                                intent.putExtra("name", "cityName=" + ((TextView) view.findViewById(R.id.name)).getText().toString().trim());
                                setResult(RESULT_OK, intent);
                                finish();

                            });
                        }

                    } catch (Exception e) {
                        progressBar.setVisibility(View.GONE);
                        e.printStackTrace();
                    }


                },
                error ->
                {
                    progressBar.setVisibility(View.GONE);
                    Log.e(TAG, error.toString());

                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> params = new HashMap<>();
                params.put("Authorization", mSharedPreferences.getString("loginToken", ""));
                params.put("Content-Type", "application/json");
                return params;
            }
        };

        dataRequest.setShouldCache(false);

        dataRequest.setRetryPolicy(new DefaultRetryPolicy(
                AppConstant.MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(dataRequest);


    }
}

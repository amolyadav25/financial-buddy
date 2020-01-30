package com.antworksmoney.financialbuddy.views.fragments.Training;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.antworksmoney.financialbuddy.R;
import com.antworksmoney.financialbuddy.helpers.Entity.TrainingEntity;
import com.antworksmoney.financialbuddy.helpers.adapters.ServiceListAdapter;
import com.antworksmoney.financialbuddy.helpers.dataFetch.AppConstant;
import com.antworksmoney.financialbuddy.views.activities.HomeActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class TrainingFragment extends Fragment {

    public TrainingFragment() {
        // Required empty public constructor
    }

    public static TrainingFragment newInstance() {
        return new TrainingFragment();
    }

    private TextView toolBarTitle;

    private PieChart trainingDataChart;

    private ArrayList<Integer> amountFilled;

    private ArrayList<String> textList;

    private ArrayList<PieEntry> pieEntries;

    private ArrayList<Integer> colours;

    private CoordinatorLayout snackBarView;

    private RecyclerView trainingDataList, blogsDataList, pdfDataList;

    private Button takeToMCQ;

    private RequestQueue mRequestQueue;

    private static final String TAG = "TrainingFragment";

    private ProgressBar trainingDataLoader;

    private Toolbar top_toolBar;

    private SharedPreferences preferences;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_blogs_and_videos_list, container, false);

        toolBarTitle = rootView.findViewById(R.id.toolBarTitle);

        trainingDataChart = rootView.findViewById(R.id.trainingDataChart);

        snackBarView = rootView.findViewById(R.id.snackBarView);

        takeToMCQ = rootView.findViewById(R.id.takeToMCQ);

        top_toolBar = rootView.findViewById(R.id.top_toolBar);

        preferences = getContext().getSharedPreferences("PersonalDetails",MODE_PRIVATE);

        trainingDataList = rootView.findViewById(R.id.trainingDataList);
        trainingDataList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        trainingDataList.setHasFixedSize(true);

        blogsDataList = rootView.findViewById(R.id.blogsDataList);
        blogsDataList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        blogsDataList.setHasFixedSize(true);

        pdfDataList = rootView.findViewById(R.id.pdfDataList);
        pdfDataList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        pdfDataList.setHasFixedSize(true);

        trainingDataChart.setRotationEnabled(true);
        trainingDataChart.setCenterText("Training completed");

        mRequestQueue = Volley.newRequestQueue(getContext());

        trainingDataLoader = rootView.findViewById(R.id.trainingDataLoader);

        textList = new ArrayList<>();

        amountFilled = new ArrayList<>();

        pieEntries = new ArrayList<>();

        colours = new ArrayList<>();

        toolBarTitle.setText("Training");

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        trainingDataChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                Log.e("data", e.toString().split(":")[2].trim());

                int position = 0;
                for (int i = 0; i < amountFilled.size(); i++) {
                    if (Double.parseDouble(String.valueOf(amountFilled.get(i))) == Double.parseDouble(e.toString().split(":")[2].trim())) {
                        position = i;
                        break;
                    }
                }

                Snackbar.make(snackBarView, Math.round(Float.parseFloat(e.toString().split(":")[2].trim()))
                        + " Percent " + textList.get(position) + " !!", Snackbar.LENGTH_SHORT).show();


            }

            @Override
            public void onNothingSelected() {

            }
        });

        takeToMCQ.setOnClickListener(v -> {

        });

        top_toolBar.setNavigationOnClickListener(v -> {
            if (getActivity() != null) {
                ((HomeActivity) getActivity())
                        .getmDrawerLayout()
                        .openDrawer(GravityCompat.START);
            }
        });

        loadVideos();

        loadBlogs();

        loadPDfs();

        loadTrainingData();
    }

    private void loadTrainingData(){
        try {

            Log.e(TAG, new JSONObject().put("userData", new JSONObject().put("mobile", preferences.getString("user_phone", ""))).toString());

            JsonObjectRequest dataRequest = new JsonObjectRequest(
                    Request.Method.POST,
                    AppConstant.BaseUrl + "get_user_training",
                    new JSONObject().put("userData", new JSONObject().put("mobile", preferences.getString("user_phone", ""))),
                    response -> {
                        trainingDataChart.setVisibility(View.VISIBLE);
                        trainingDataLoader.setVisibility(View.GONE);
                        Log.e(TAG, response.toString());
                        try {
                            loadTraingData(response.getJSONObject("result").getString("traning_percentage"));
                        }catch (Exception e){
                            loadTraingData("0");
                            e.printStackTrace();
                        }
                    },
                    error -> {
                        Log.e("TRAINING_DATA_ERROR",error.toString());
                        trainingDataChart.setVisibility(View.VISIBLE);
                        trainingDataLoader.setVisibility(View.GONE);
                    });

            dataRequest.setRetryPolicy(new DefaultRetryPolicy(
                    AppConstant.MY_SOCKET_TIMEOUT_MS,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            dataRequest.setShouldCache(false);

            mRequestQueue.add(dataRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void loadTraingData(String traning_percentage){

        amountFilled.add(Integer.parseInt(traning_percentage));
        amountFilled.add(100 - amountFilled.get(0));

        colours.add(getContext().getResources().getColor(R.color.colorPrimary));
        colours.add(Color.LTGRAY);

        textList.add("Completed");
        textList.add("Remaining");


        Description description = new Description();
        description.setText("Training Data of Financial Buddy");
        description.setTextSize(10f);

        trainingDataChart.setHoleRadius(50f);
        trainingDataChart.setDescription(description);

        for (int i = 0; i < amountFilled.size(); i++) {
            pieEntries.add(new PieEntry(amountFilled.get(i)));
        }

        Legend legend = trainingDataChart.getLegend();
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setPosition(Legend.LegendPosition.LEFT_OF_CHART);


        PieDataSet dataSet = new PieDataSet(pieEntries, "Training Meter");
        dataSet.setSliceSpace(1f);
        dataSet.setValueTextSize(14f);
        dataSet.setColors(colours);

        PieData pieData = new PieData(dataSet);
        trainingDataChart.setData(pieData);
        trainingDataChart.animateXY(4000, 4000);
        trainingDataChart.invalidate();
    }


    private void loadVideos() {
       try {
           JsonObjectRequest dataRequest = new JsonObjectRequest(
                   Request.Method.POST,
                   AppConstant.BaseUrl + "offer-videos",
                   new JSONObject().put("mobile", preferences.getString("user_phone","")),
                   response -> {
                       try {
                           ArrayList<TrainingEntity> dataList = new ArrayList<>();
                           JSONArray dataArray = response.getJSONArray("videoUrl");
                           for (int i = 0; i < dataArray.length(); i++) {
                               JSONObject dataObject = dataArray.getJSONObject(i);

                               TrainingEntity data = new TrainingEntity();
                               data.setId(dataObject.getString("id"));
                               data.setDescription(dataObject.getString("title"));
                               data.setUrl(dataObject.getString("url"));
                               data.setLongDescription(dataObject.getString("title"));
                               data.setThumbnail(dataObject.getString("url"));
                               data.setIsSeen(dataObject.getString("is_view"));

                               dataList.add(data);
                           }

                           ServiceListAdapter dataListAdapter = new ServiceListAdapter(getContext(), dataList, "Videos");
                           trainingDataList.setAdapter(dataListAdapter);

                       } catch (Exception e) {
                           e.printStackTrace();
                       }

                   },
                   error -> {
                       Log.e(TAG, error.toString());
                   });

           dataRequest.setRetryPolicy(new DefaultRetryPolicy(
                   AppConstant.MY_SOCKET_TIMEOUT_MS,
                   DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                   DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

           dataRequest.setShouldCache(false);

           mRequestQueue.add(dataRequest);

       }catch (Exception e){
           e.printStackTrace();
       }
    }


    private void loadBlogs() {
        try {
            JsonObjectRequest dataRequest = new JsonObjectRequest(
                    Request.Method.POST,
                    AppConstant.BaseUrl + "blogs",
                    new JSONObject().put("mobile", preferences.getString("user_phone","")),
                    response -> {
                        ArrayList<TrainingEntity> dataList = new ArrayList<>();
                        try {

                            JSONArray dataArray = response.getJSONArray("blogs_data");
                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject dataObject = dataArray.getJSONObject(i);
                                TrainingEntity data = new TrainingEntity();

                                data.setId(dataObject.getString("ID"));
                                data.setDescription(dataObject.getString("post_title"));
                                data.setLongDescription(dataObject.getString("post_content"));
                                data.setUrl(dataObject.getString("guid"));
                                data.setThumbnail(dataObject.getString("blog_fetured_image"));
                                data.setIsSeen(dataObject.getString("is_view"));
                                dataList.add(data);
                            }

                            ServiceListAdapter dataListAdapter = new ServiceListAdapter(getContext(), dataList, "Blogs");
                            blogsDataList.setAdapter(dataListAdapter);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    },
                    error -> {
                        Log.e(TAG, error.toString());
                    }
            );

            dataRequest.setRetryPolicy(new DefaultRetryPolicy(
                    AppConstant.MY_SOCKET_TIMEOUT_MS,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            dataRequest.setShouldCache(false);

            mRequestQueue.add(dataRequest);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    private void loadPDfs() {
       try {
           JsonObjectRequest dataRequest = new JsonObjectRequest(
                   Request.Method.POST,
                   AppConstant.BaseUrl + "pdf_instruction",
                   new JSONObject().put("mobile",preferences.getString("user_phone","")),
                   response -> {
                       ArrayList<TrainingEntity> dataList = new ArrayList<>();
                       try {

                           JSONArray dataArray = response.getJSONArray("blogs_data");
                           for (int i = 0; i < dataArray.length(); i++) {
                               JSONObject dataObject = dataArray.getJSONObject(i);
                               TrainingEntity data = new TrainingEntity();

                               data.setId(dataObject.getString("id"));
                               data.setDescription(dataObject.getString("title"));
                               data.setLongDescription(dataObject.getString("title"));
                               data.setUrl(dataObject.getString("pdf_url"));
                               data.setThumbnail("https://cdn3.iconfinder.com/data/icons/file-format-outline/512/pdf_.pdf_file_file_format_extension_format_-512.png");
                               data.setIsSeen(dataObject.getString("is_view"));
                               dataList.add(data);
                           }

                           ServiceListAdapter dataListAdapter = new ServiceListAdapter(getContext(), dataList, "Pdfs");
                           pdfDataList.setAdapter(dataListAdapter);

                       } catch (Exception e) {
                           e.printStackTrace();
                       }

                   },
                   error -> {
                       Log.e(TAG, error.toString());
                   }
           );

           dataRequest.setRetryPolicy(new DefaultRetryPolicy(
                   AppConstant.MY_SOCKET_TIMEOUT_MS,
                   DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                   DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

           dataRequest.setShouldCache(false);

           mRequestQueue.add(dataRequest);
       }catch (Exception e){
           e.printStackTrace();
       }
    }

}

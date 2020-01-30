package com.antworksmoney.financialbuddy.views.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.provider.ContactsContract;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.antworksmoney.financialbuddy.R;
import com.antworksmoney.financialbuddy.helpers.Entity.ProfileInfo;
import com.antworksmoney.financialbuddy.helpers.adapters.ContactsCompleteListDataViewAdapter;
import com.antworksmoney.financialbuddy.helpers.dataFetch.AppConstant;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Objects;

public class ContactsPickerActivity extends AppCompatActivity {

    private Toolbar searchtollbar;

    private MenuItem item_search;

    private Menu search_menu;

    private RecyclerView mRecyclerView;

    @SuppressLint("StaticFieldLeak")
    static ContactsCompleteListDataViewAdapter adapter;

    private RelativeLayout loadingLayout;

    private TextView contactLoadingText;

    private Button ProceedButton;

    private ArrayList<ProfileInfo> selectedContactsList, completeList;

    private CoordinatorLayout snackBarView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_picker);

        searchtollbar = findViewById(R.id.searchtoolbar);

        mRecyclerView = findViewById(R.id.contactList);

        loadingLayout = findViewById(R.id.loadingLayout);

        contactLoadingText = findViewById(R.id.contactFoundText);

        ProceedButton = findViewById(R.id.ProceedButton);

        selectedContactsList = new ArrayList<>();

        completeList = new ArrayList<>();

        snackBarView = findViewById(R.id.snackBarView);

        Toolbar mToolbar = findViewById(R.id.toolbar);
        mToolbar.setTitle("Select contacts");

        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setDisplayShowTitleEnabled(true);
        }

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mRecyclerView.setHasFixedSize(true);

        getMyContacts getContacts = new getMyContacts();

        Object[] contactComponents = new Object[9];
        contactComponents[0] = getApplicationContext();
        contactComponents[1] = mRecyclerView;
        contactComponents[2] = adapter;
        contactComponents[3] = contactLoadingText;
        contactComponents[4] = loadingLayout;
        contactComponents[5] = selectedContactsList;
        contactComponents[6] = ProceedButton;
        contactComponents[7] = snackBarView;
        contactComponents[8] = completeList;

        getContacts.execute(contactComponents);

        setSearchtollbar();

        ProceedButton.setOnClickListener(view -> {

            if (selectedContactsList.size() > 0) {
                Intent intent = new Intent();
                intent.putExtra("contactList", selectedContactsList);
                intent.putExtra("completeList", completeList);
                setResult(RESULT_OK, intent);
                finish();
            } else {
                Snackbar.make(snackBarView, "Please select contacts first !!", Snackbar.LENGTH_SHORT).show();
            }
        });

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
                if (adapter != null) {
                    adapter.getFilter().filter(query);

                } else {
                    Snackbar.make(snackBarView, "Please Wait ! fetching contacts !!", Snackbar.LENGTH_SHORT).show();
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

    private static class getMyContacts extends AsyncTask<Object, String, String> {

        private static final String TAG = "getMyContacts";

        @SuppressLint("StaticFieldLeak")
        Context mContext;

        @SuppressLint("StaticFieldLeak")
        RecyclerView mRecyclerView;

        @SuppressLint("StaticFieldLeak")
        RelativeLayout mLoadingLayout;

        @SuppressLint("StaticFieldLeak")
        TextView mLoadingText;

        @SuppressLint("StaticFieldLeak")
        Button proceedButton;

        @SuppressLint("StaticFieldLeak")
        CoordinatorLayout snackbarView;

        ArrayList<ProfileInfo> selectedContactsList,
                numbersArrayList;

        ContactsCompleteListDataViewAdapter mAdapter;

        Handler mHandler;

        private static final int DELAY = 100;

        @Override
        protected void onPreExecute() {
            mHandler = new Handler(Looper.getMainLooper());
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Object... objects) {

            mContext = (Context) objects[0];
            mRecyclerView = (RecyclerView) objects[1];
            mAdapter = (ContactsCompleteListDataViewAdapter) objects[2];
            mLoadingText = (TextView) objects[3];
            mLoadingLayout = (RelativeLayout) objects[4];
            selectedContactsList = (ArrayList<ProfileInfo>) objects[5];
            proceedButton = (Button) objects[6];
            snackbarView = (CoordinatorLayout) objects[7];
            numbersArrayList = (ArrayList<ProfileInfo>) objects[8];

            ContentResolver contentResolver = mContext.getContentResolver();
            Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
            if (cursor != null && cursor.getCount() > 0) {
                for (int i = 0; i < cursor.getCount(); i++) {

                    int finalI = i;
                    mHandler.post(() -> mLoadingText.setText("Please wait !!! \n" + "Fetched " + (finalI + 1) + " contacts out of "+cursor.getCount()+" contacts"));

                    cursor.moveToNext();

                    int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)));
                    if (hasPhoneNumber > 0) {

                        String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));

                        String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                        Cursor phoneCursor = contentResolver.query(
                                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                null,
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id},
                                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");

                        if (phoneCursor != null) {
                            if (phoneCursor.moveToNext()) {
                                String phoneNumber = phoneCursor.getString(
                                        phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                                numbersArrayList.add(new ProfileInfo(name, phoneNumber));
                                phoneCursor.close();
                            }
                        }
                    }

                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Log.e(TAG, String.valueOf(numbersArrayList.size()));

            adapter = new ContactsCompleteListDataViewAdapter(mContext, numbersArrayList, mRecyclerView);
            mRecyclerView.setAdapter(adapter);

            if (adapter != null) {
                adapter.setOnClick(position -> {

                    if (selectedContactsList.size() <= 9) {

                        boolean present = false;
                        int matchedAtPosition = 0;

                        View view = Objects.requireNonNull(mRecyclerView.findViewHolderForAdapterPosition(position)).itemView;
                        TextView contactText = view.findViewById(R.id.contact_text);
                        TextView number = view.findViewById(R.id.contacts_number);

                        ProfileInfo info = new ProfileInfo(
                                ((TextView) view.findViewById(R.id.contacts_name)).getText().toString().trim(),
                                ((TextView) view.findViewById(R.id.contacts_number)).getText().toString().trim());


                        for (int i = 0; i < selectedContactsList.size(); i++) {
                            if (selectedContactsList.get(i).getPhoneNumber().trim().
                                    equalsIgnoreCase(number.getText().toString().trim())) {
                                matchedAtPosition = i;
                                present = true;
                                break;
                            }
                        }

                        if (!present) {
                            selectedContactsList.add(info);
                            proceedButton.setText("Proceed (" + selectedContactsList.size() + ")");
                            contactText.setVisibility(View.INVISIBLE);
                        } else {
                            selectedContactsList.remove(matchedAtPosition);
                            contactText.setVisibility(View.VISIBLE);
                            if (selectedContactsList.size() > 0) {
                                proceedButton.setText("Proceed (" + selectedContactsList.size() + ")");
                            } else {
                                proceedButton.setText("Proceed");
                            }
                        }

                        for (int i = 0; i < selectedContactsList.size(); i++) {
                            Log.e("Name", selectedContactsList.get(i).getName());
                            Log.e("Number", selectedContactsList.get(i).getPhoneNumber());
                        }
                    } else
                        Snackbar.make(snackbarView, "Max limit reached", Snackbar.LENGTH_SHORT).show();

                });
            }


            mHandler.postDelayed(() -> mLoadingLayout.setVisibility(View.GONE), DELAY);


        }
    }
}

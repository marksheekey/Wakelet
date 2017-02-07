package uk.co.happyapper.wakelet;

import android.app.ActionBar;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements FeedFragment.OnListFragmentInteractionListener,CollectionFragment.OnCollectionListener {

    private final OkHttpClient client = new OkHttpClient();
    FeedFragment feedFragment;
    CollectionFragment collectionFragment;
    FragmentManager fragmentManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        feedFragment = new FeedFragment();
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.container,feedFragment);
        ft.commit();



    }


    @Override
    public void onListFragmentInteraction(int action,int id,Feed item) {

        if(action == 0){
            collectionFragment = new CollectionFragment();
            fragmentManager = getSupportFragmentManager();
            FragmentTransaction ft = fragmentManager.beginTransaction();

            ft.replace(R.id.container,collectionFragment);
            ft.commit();
        }

        if(action == 1) {

            if (item.links.get(0).type == 2) {

                try {
                    getText(id, item.links.get(0).source);
                } catch (Exception e) {
                    feedFragment.setText(id, "Can't get text");
                }


            }
        }

    }


    public void getText(final int id,String url) throws Exception {
        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override public void onFailure(Call call, IOException e) {
                feedFragment.setText(id,"Couldn't get text");
            }

            @Override public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
                feedFragment.setText(id,response.body().string());
            }
        });
    }


    @Override
    public void onCollectionInteraction(int id, Feed item) {

    }
}

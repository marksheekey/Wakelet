package uk.co.happyapper.wakelet;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


public class FeedFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private ArrayList<Feed> mFeed;
    RecyclerView recyclerView;

    public FeedFragment() {
    }


    @SuppressWarnings("unused")
    public static FeedFragment newInstance(int columnCount) {
        FeedFragment fragment = new FeedFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feed_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }

            mFeed = new ArrayList<Feed>();

            mFeed = getFeed();

            recyclerView.setAdapter(new MyFeedRecyclerViewAdapter(mFeed, mListener));
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(int action,int id,Feed item);
    }

    public ArrayList<Feed> getFeed(){
        ArrayList<Feed> data = new ArrayList<>();
        Feed anItem = new Feed();
        anItem.links = new ArrayList<>();
        anItem.links = createLink(anItem.links,0,"Top Picks","Haway the lads","http://ichef.bbci.co.uk/onesport/cps/480/cpsprodpb/14265/production/_91333528_mcnair-2.jpg",0);
        anItem.links = createLink(anItem.links,0,"Top Picks","Keep The Faith","ix3Wcl193BU",1);
        anItem.user = createUser(0,"Mark Sheekey","profile0");
        data.add(anItem);

        Feed Item2 = new Feed();
        Item2.links = new ArrayList<>();
        Item2.links = createLink(Item2.links,1,"Top Picks","Basque MTB","xGQ3MrYBj7w",1);
        Item2.user = createUser(1,"BasqueMTB","profile1");
        data.add(Item2);

        Feed Item3 = new Feed();
        Item3.links = new ArrayList<>();
        Item3.links = createLink(Item3.links,2,"Top Picks","OK HTTP","https://raw.github.com/square/okhttp/master/README.md",2);
        Item3.user = createUser(2,"Square","profile2");
        data.add(Item3);

        return data;


    }

    public User createUser(int id,String name,String thumb){
        User thisUser = new User();
        thisUser.id = id;
        thisUser.name = name;
        thisUser.thumb = thumb;

        return thisUser;
    }



    public ArrayList<Link> createLink(ArrayList<Link> current,int id,String from,String title,String source,int type){
        Link thisLink = new Link();
        ArrayList<Link> links = new ArrayList<>();
        links.addAll(current);
        thisLink.id = id;
        thisLink.from = from;
        thisLink.title = title;
        thisLink.source = source;
        thisLink.type = type;
        thisLink.text = "";
        links.add(thisLink);

        return links;
    }

    public void setText(final int id,String text){
        Log.i("Fragment","ID "+id+" "+text);

        mFeed.get(id).links.get(0).text = text;
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                recyclerView.getAdapter().notifyItemChanged(id);
            }
        });

    }

}

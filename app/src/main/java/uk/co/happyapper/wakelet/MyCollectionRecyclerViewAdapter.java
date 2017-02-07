package uk.co.happyapper.wakelet;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pierfrancescosoffritti.youtubeplayer.AbstractYouTubeListener;
import com.pierfrancescosoffritti.youtubeplayer.YouTubePlayerView;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.OkHttpClient;


public class MyCollectionRecyclerViewAdapter extends RecyclerView.Adapter<MyCollectionRecyclerViewAdapter.ViewHolder>  {

    private final List<Feed> mValues;
    private final CollectionFragment.OnCollectionListener mListener;
    OkHttpClient client = new OkHttpClient();

    public MyCollectionRecyclerViewAdapter(List<Feed> items, CollectionFragment.OnCollectionListener listener) {
        mValues = items;
        mListener = listener;


    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_collection, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mItem = mValues.get(position);
        holder.mName.setText(mValues.get(position).user.name);
        holder.mTitle.setText(mValues.get(position).links.get(0).title);
        holder.mFrom.setText("From "+mValues.get(position).links.get(0).from);
        if(holder.mItem.links.size()==1){
            holder.mItems.setText("1 item");
        }else{
            holder.mItems.setText(holder.mItem.links.size()+" items");
        }

        holder.mImage.setVisibility(View.GONE);
        holder.mVideo.setVisibility(View.GONE);
        holder.mText.setVisibility(View.GONE);


        int productImageId = holder.mView.getContext().getResources().getIdentifier(
                holder.mItem.user.thumb, "drawable", holder.mView.getContext().getPackageName());

        Picasso.with(holder.mView.getContext())
                .load(productImageId)
                .fit()
                .into(holder.mProfile);

        if(holder.mItem.links.get(0).type == 0) {

            holder.mImage.setVisibility(View.VISIBLE);
            Picasso.with(holder.mView.getContext())
                    .load(holder.mItem.links.get(0).source)
                    .placeholder(R.drawable.wakelet_blue)
                    .fit()
                    .into(holder.mImage);
        }

        if(holder.mItem.links.get(0).type == 1) {

            holder.mVideo.setVisibility(View.VISIBLE);
            holder.mVideo.initialize(new AbstractYouTubeListener() {
                @Override
                public void onReady() {
                    holder.mVideo.loadVideo(holder.mItem.links.get(0).source, 0);
                }
            }, true);

        }

        if(holder.mItem.links.get(0).type == 2) {

            holder.mText.setVisibility(View.VISIBLE);
            holder.mText.setText(holder.mItem.links.get(0).text);
            if(holder.mItem.links.get(0).text.isEmpty()) {
                mListener.onCollectionInteraction(position, holder.mItem);
            }else{
                holder.mItem.links.get(0).text = "Getting Text";
            }

        }

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                  mListener.onCollectionInteraction(position,holder.mItem);
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mName;
        public final TextView mTitle;
        public final ImageView mImage;
        public final TextView mFrom;
        public final CircleImageView mProfile;
        public final YouTubePlayerView mVideo;
        public final TextView mText;
        public final TextView mItems;

        public Feed mItem;


        public ViewHolder(View view) {
            super(view);
            mView = view;
            mName = (TextView) view.findViewById(R.id.name);
            mTitle = (TextView) view.findViewById(R.id.title);
            mImage = (ImageView) view.findViewById(R.id.item_image);
            mFrom = (TextView) view.findViewById(R.id.from);
            mProfile = (CircleImageView) view.findViewById(R.id.thumb_nail);
            mVideo = (YouTubePlayerView) view.findViewById(R.id.youtube_player_view);
            mText = (TextView) view.findViewById(R.id.item_text);
            mItems = (TextView) view.findViewById(R.id.items);




        }


    }



}

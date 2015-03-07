package com.tiramisu.feedreadermk4;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

/**
 * Created by ASUS on 17-01-2015.
 */

/**
 * Created by ASUS on 16-01-2015.
 */
public class StreamResultAdapter extends RecyclerView.Adapter<StreamResultAdapter.StreamViewHolder> {
    public LayoutInflater inflater;
    List<StreamResult> streamResults = Collections.emptyList();
    Context context;
    ClickListener clickListener;

    public StreamResultAdapter(Context context, List<StreamResult> streamResults) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.streamResults = streamResults;

    }

    @Override
    public StreamViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.stream_result_row, parent, false);
        StreamViewHolder holder = new StreamViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(StreamViewHolder holder, int position) {
        StreamResult current = streamResults.get(position);
        holder.title.setText(current.getTitle());
        //holder.feedId.setText(current.getFeedId() + " subscribers");
        holder.thumbnail.setImageBitmap(current.getThumbnail());
        //Log.d("Hello", searchResults.get(position).getTitle());
    }


    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    public int getItemCount() {
        return streamResults.size();
    }

    class StreamViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title;
        //TextView feedId;
        ImageView thumbnail;

        public StreamViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title_text);
            //feedId = (TextView) itemView.findViewById(R.id.sub_text);
            thumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (clickListener != null)
                clickListener.entryClicked(v, getPosition());
        }
    }

    /*public interface ClickListener {
        public void itemClicked(View view, int position);
    }*/


}

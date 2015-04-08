package com.tiramisu.feedreadermk4;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

/**
 * Created by ASUS on 30-01-2015.
 */
public class PodcastStreamResultAdapter extends RecyclerView.Adapter<PodcastStreamResultAdapter.PodcastStreamViewHolder> {
    public LayoutInflater inflater;
    List<StreamResult> streamResults = Collections.emptyList();
    Context context;
    ClickListener clickListener;


    public PodcastStreamResultAdapter(Context context, List<StreamResult> streamResults) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.streamResults = streamResults;
    }
    @Override
    public PodcastStreamViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.podcast_stream_result_row, parent, false);
        PodcastStreamViewHolder holder = new PodcastStreamViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(PodcastStreamViewHolder holder, int position) {
        StreamResult current = streamResults.get(position);
        holder.title.setText(current.getTitle());
        holder.thumbnail.setImageBitmap(current.getThumbnail());
        holder.button.setImageResource(R.drawable.podcast_play_68dp);
    }

    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    public int getItemCount() {
        return streamResults.size();
    }

    class PodcastStreamViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title;
        ImageView thumbnail;
        ImageButton button;
        public PodcastStreamViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.podTitle);
            thumbnail = (ImageView) itemView.findViewById(R.id.podThumbnail);
            button = (ImageButton) itemView.findViewById(R.id.podPP);
            button.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(clickListener != null){
                clickListener.podEntryClicked(v, getPosition());
            }
        }
    }
}

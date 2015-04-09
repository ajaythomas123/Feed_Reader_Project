package com.tiramisu.feedreadermk4;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collections;
import java.util.List;

/**
 * Created by ASUS on 16-01-2015.
 */
public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.SearchViewHolder> {
    public LayoutInflater inflater;
    List<SearchResult> searchResults = Collections.emptyList();
    Context context;
    ClickListener clickListener;

    public SearchResultAdapter(Context context, List<SearchResult> searchResults) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.searchResults = searchResults;

    }

    @Override
    public SearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.search_result_row, parent, false);
        SearchViewHolder holder = new SearchViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(SearchViewHolder holder, int position) {
        SearchResult current = searchResults.get(position);
        holder.title.setText(current.getTitle());
        holder.subscribers.setText(current.getSubscribers() + " subscribers");
        holder.sIcon.setImageBitmap(current.getsIcon());
        //Log.d("Hello", searchResults.get(position).getTitle());
    }


    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    public int getItemCount() {
        return searchResults.size();
    }

    class SearchViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title;
        TextView subscribers;
        ImageView sIcon;
        ImageButton subscribeButton;

        public SearchViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title_text);
            subscribers = (TextView) itemView.findViewById(R.id.sub_text);
            sIcon = (ImageView) itemView.findViewById(R.id.thumbnail);
            subscribeButton = (ImageButton) itemView.findViewById(R.id.subscribe_button);
            subscribeButton.setOnClickListener(this);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if (clickListener != null) {
                if (v.getId() == R.id.subscribe_button) {
                    clickListener.subscribe(v, getPosition());
                } else
                    clickListener.itemClicked(v, getPosition());
            }
        }


    }

    /*public interface ClickListener {
        public void itemClicked(View view, int position);
    }*/


}

package com.tiramisu.feedreadermk4;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.text.method.ScrollingMovementMethod;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by ASUS on 23-01-2015.
 */
public class ArticleFragment extends Fragment implements View.OnClickListener{
CardView cView;
    String itemUrl;
    ImageButton shareButton, browserButton;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_article, container, false);
        TextView titleText = (TextView) view.findViewById(R.id.title_text);
        TextView contentText = (TextView) view.findViewById(R.id.content_text);
        shareButton = (ImageButton) view.findViewById(R.id.article_share_button);
        itemUrl = this.getArguments().getString("itemUrl");
        browserButton = (ImageButton) view.findViewById(R.id.article_browser_button);
        String content = getArguments().getString("content");
        shareButton.setOnClickListener(this);
        browserButton.setOnClickListener(this);
        contentText.setMovementMethod(new ScrollingMovementMethod());
        URLImageParser p = new URLImageParser(contentText, getActivity());
        Spanned htmlSpan = Html.fromHtml(content+"\n", p, null);
        titleText.setText(getArguments().getString("title"));
        contentText.setText(htmlSpan);
        cView = (CardView) view.findViewById(R.id.card_article);
        cView.setZ((float) 20);
        //contentText.setText(Html.fromHtml(content));
        return view;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.article_browser_button){
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(itemUrl));
            startActivity(i);
        }

        if(v.getId() == R.id.article_share_button){
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_TEXT, itemUrl);
            startActivity(i);
        }
    }
}

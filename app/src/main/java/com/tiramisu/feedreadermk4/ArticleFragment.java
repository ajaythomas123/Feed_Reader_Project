package com.tiramisu.feedreadermk4;

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
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * Created by ASUS on 23-01-2015.
 */
public class ArticleFragment extends Fragment {
CardView cView;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_article, container, false);
        TextView titleText = (TextView) view.findViewById(R.id.title_text);
        TextView contentText = (TextView) view.findViewById(R.id.content_text);
        String content = getArguments().getString("content");
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
}

package com.rg.nomadvpn.ui.home;

import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.rg.nomadvpn.R;

public class ButtonDisconnect {
    private View view;
    private CardView card;
    private ConstraintLayout layout;
    private TextView title;

    private Handler handler = new Handler();

    public ButtonDisconnect(View view) {
        this.view = view;
        init();
    }

    private void init() {
        card = view.findViewById(R.id.card_disconnect);
        layout = view.findViewById(R.id.layout_disconnect);
        title = view.findViewById(R.id.title_disconnect);
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        card.setOnClickListener(onClickListener);
    }

    public void showButton() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                card.setVisibility(View.VISIBLE);
            }
        });
    }

    public void hideButton() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                card.setVisibility(View.GONE);
            }
        });
    }

}

package com.rg.nomadvpn.ui.home;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import androidx.cardview.widget.CardView;

import com.rg.nomadvpn.MainActivity;
import com.rg.nomadvpn.R;

public class ButtonConnect {
    public View rootView;
    public CardView cardView;

    public ButtonConnect(View rootView) {
        this.rootView = rootView;
        this.cardView = rootView.findViewById(R.id.button_connect);
        clickHandler();
    }

    public void clickHandler() {
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(MainActivity.LOGTAG, "Click on the card");
            }
        });
    }
}

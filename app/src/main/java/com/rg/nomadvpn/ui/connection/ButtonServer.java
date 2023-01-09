package com.rg.nomadvpn.ui.connection;

import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.rg.nomadvpn.R;
import com.rg.nomadvpn.db.ServerCurrent;
import com.rg.nomadvpn.db.ServerHolder;
import com.rg.nomadvpn.model.ServerHolderConfiguration;
import com.rg.nomadvpn.utils.MyApplicationContext;

public class ButtonServer {
    private View view;
    private LinearLayout layoutCard;
    private TextView textCountry;
    private TextView textCity;
    private ImageView imageFlag;
    private Handler handler = new Handler();

    public ButtonServer(View view) {
        this.view = view;

        layoutCard = view.findViewById(R.id.button_server);
        textCountry = view.findViewById(R.id.item_country);
        textCity = view.findViewById(R.id.item_city);
        imageFlag = view.findViewById(R.id.item_image);
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        layoutCard.setOnClickListener(onClickListener);
    }

    public void initServerButton() {
        int serverIndex = ServerCurrent.getServerIndex();

        ServerHolder serverHolder = ServerHolder.getInstance();
        ServerHolderConfiguration serverHolderConfiguration = serverHolder.getServerById(serverIndex);

        handler.post(new Runnable() {
            @Override
            public void run() {
                textCountry.setText(serverHolderConfiguration.getCountry());
                textCity.setText(serverHolderConfiguration.getCity());

                int drawableInt = MyApplicationContext.getAppContext().getResources().getIdentifier(serverHolderConfiguration.getFlagName(), "drawable", MyApplicationContext.getAppContext().getPackageName());
                Drawable drawable = MyApplicationContext.getAppContext().getDrawable(drawableInt);
                imageFlag.setImageDrawable(drawable);
            }
        });
    }
}

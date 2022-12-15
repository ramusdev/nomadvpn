package com.rg.nomadvpn.ui.server;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rg.nomadvpn.MainActivity;
import com.rg.nomadvpn.R;
import com.rg.nomadvpn.controller.ConnectionController;
import com.rg.nomadvpn.databinding.FragmentItemBinding;
import com.rg.nomadvpn.db.ServerCurrent;
import com.rg.nomadvpn.model.ServerHolderConfiguration;
import com.rg.nomadvpn.service.VpnConnectionService;
import com.rg.nomadvpn.ui.connection.ConnectionFragment;
import com.rg.nomadvpn.ui.server.placeholder.PlaceholderContent.PlaceholderItem;
import com.rg.nomadvpn.utils.MyApplicationContext;

import java.util.ArrayList;

/**
 * {@link RecyclerView.Adapter} that can display a {@link PlaceholderItem}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyItemRecyclerViewAdapter extends RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder> {

    private final ArrayList<ServerHolderConfiguration> mValues;

    public MyItemRecyclerViewAdapter(ArrayList<ServerHolderConfiguration> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(FragmentItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.countryText.setText(mValues.get(position).getCountry());
        holder.cityText.setText(mValues.get(position).getCity());

        String imageName = mValues.get(position).getFlagName();
        int drawableInt = MyApplicationContext.getAppContext().getResources().getIdentifier(imageName, "drawable", MyApplicationContext.getAppContext().getPackageName());
        Drawable drawable = MyApplicationContext.getAppContext().getResources().getDrawable(drawableInt);
        holder.flagImage.setImageDrawable(drawable);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView countryText;
        public final TextView cityText;
        public ImageView flagImage;
        public ServerHolderConfiguration mItem;

        public ViewHolder(FragmentItemBinding binding) {
            super(binding.getRoot());
            countryText = binding.itemCountry;
            cityText = binding.itemCity;
            flagImage = binding.itemImage;

            binding.getRoot().setOnClickListener(this);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + countryText.getText() + "'";
        }

        @Override
        public void onClick(View v) {
            ConnectionController.getInstance().disconnectClickSleep();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    ConnectionController.getInstance().initClick();
                }
            }).start();

            ServerCurrent.setServerId(mItem.getId());

            FragmentTransaction fragmentTransaction = MainActivity.getInstance().getSupportFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.slide_in, R.anim.slide_out);
            fragmentTransaction.replace(R.id.nav_host_fragment_content_main, ConnectionFragment.class, null).commit();
        }
    }
}
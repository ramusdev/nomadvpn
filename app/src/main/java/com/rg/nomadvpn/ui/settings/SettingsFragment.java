package com.rg.nomadvpn.ui.settings;

import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import com.rg.nomadvpn.R;
import com.rg.nomadvpn.utils.MyApplicationContext;

public class SettingsFragment extends Fragment {

    private View view;
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Toolbar
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(MyApplicationContext.getAppContext().getResources().getString(R.string.menu_settings));

        // View
        view = inflater.inflate(R.layout.fragment_settings, container, false);

        // Set text
        String text = MyApplicationContext.getAppContext().getResources().getString(R.string.about_text);
        TextView textView = view.findViewById(R.id.main_text);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setText(Html.fromHtml(text));

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
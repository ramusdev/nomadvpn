package com.rg.nomadvpn;

import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;
import com.google.android.material.navigation.NavigationView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import com.rg.nomadvpn.ui.settings.SettingsFragment;
import com.rg.nomadvpn.ui.connection.ConnectionFragment;
import com.rg.nomadvpn.utils.MyApplicationContext;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private static MainActivity instance;
    public final static String LOGTAG = "Logtagname";
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
        setContentView(R.layout.activity_main);

        // Change fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.nav_host_fragment_content_main, ConnectionFragment.class, null)
                // .setReorderingAllowed(true)
                // .addToBackStack(null)
                .commit();

        // Set toolbar title
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(MyApplicationContext.getAppContext().getResources().getString(R.string.menu_settings));
        setSupportActionBar(toolbar);

        // Navigation
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.navigation_view);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        );
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        navigationView.bringToFront();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                drawerLayout.closeDrawers();
                if (item.getItemId() == R.id.nav_connection) {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            item.setChecked(true);
                            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                            // fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                            fragmentTransaction.setCustomAnimations(R.anim.slide_in, R.anim.slide_out);
                            fragmentTransaction.replace(R.id.nav_host_fragment_content_main, ConnectionFragment.class, null).commit();
                        }
                    }, 275);
                }
                if (item.getItemId() == R.id.nav_settings) {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            item.setChecked(true);
                            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                            // fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                            fragmentTransaction.setCustomAnimations(R.anim.slide_in, R.anim.slide_out);
                            fragmentTransaction.replace(R.id.nav_host_fragment_content_main, SettingsFragment.class, null).commit();
                        }
                    }, 275);
                }
                return false;
            }
        });

        // Change bar color
        Window windows = this.getWindow();
        windows.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        windows.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        windows.setStatusBarColor(MyApplicationContext.getAppContext().getResources().getColor(R.color.status_background));
        windows.setNavigationBarColor(MyApplicationContext.getAppContext().getResources().getColor(R.color.status_background));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public static MainActivity getInstance() {
        return MainActivity.instance;
    }
}
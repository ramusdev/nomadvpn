package com.rg.nomadvpn;

import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.material.snackbar.Snackbar;
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

import com.rg.nomadvpn.databinding.ActivityMainBinding;
import com.rg.nomadvpn.ui.gallery.GalleryFragment;
import com.rg.nomadvpn.ui.home.HomeFragment;
import com.rg.nomadvpn.utils.MyApplicationContext;

import de.blinkt.openvpn.core.VpnStatus;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private static MainActivity instance;
    public final static String LOGTAG = "Logtagname";
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);
        /*
        binding.appBarMain.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        */

        // DrawerLayout drawer = binding.drawerLayout;
        // NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        /*
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        */

        // Change fragment
        // getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_content_main, HomeFragment.class, "main_fragment_tag").commit();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.nav_host_fragment_content_main, HomeFragment.class, null, "tag_name_two")
                .setReorderingAllowed(true)
                .addToBackStack(null)
                .commit();

        // Set toolbar title
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Connection");
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
                            fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                            fragmentTransaction.replace(R.id.nav_host_fragment_content_main, HomeFragment.class, null).commit();
                        }
                    }, 275);
                }
                if (item.getItemId() == R.id.nav_settings) {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            item.setChecked(true);
                            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                            fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                            fragmentTransaction.replace(R.id.nav_host_fragment_content_main, GalleryFragment.class, null).commit();
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
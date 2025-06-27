package com.example.eat_now;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AlertDialog;

import com.example.eat_now.activities.LoginActivity;
import com.example.eat_now.utils.CartManager;
import com.example.eat_now.utils.RestaurantFirestoreUtil;
import com.example.eat_now.utils.SimpleImageManager;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        Button logoutButton = navigationView.findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(v -> logout());

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_view_profile, R.id.nav_favourite, R.id.nav_my_cart, R.id.nav_address)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    private void logout() {
        mAuth.signOut();
        CartManager.getInstance().clearCart();
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_reset) {
            // Reset restaurants
            new AlertDialog.Builder(this)
                    .setTitle("Reset Restaurants")
                    .setMessage("This will add new Sylhet restaurants. Continue?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        RestaurantFirestoreUtil.addSampleData(this);
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
            return true;
        }

        //  Simple image setting
        if (id == R.id.action_set_images) {
            new AlertDialog.Builder(this)
                    .setTitle("Set Images")
                    .setMessage("This will set fixed images for all restaurants and food items. Continue?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        // Set all images at once - SUPER SIMPLE!
                        SimpleImageManager.setAllImages(this);
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}

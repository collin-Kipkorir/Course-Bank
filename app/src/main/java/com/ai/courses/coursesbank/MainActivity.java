package com.ai.courses.coursesbank;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.primary)));
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        // Change the color of the status bar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.primary));
        }

        // Set default title to "Home"
        setActionBarTitle("Courses");



        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);

        // Set up ActionBarDrawerToggle
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // Handle item clicks here
                Fragment selectedFragment = null;
                int itemId = item.getItemId();
                if (itemId == R.id.nav_item1) {
                    // Handle item 1 click
                    selectedFragment = new HomeFragment();

                } else if (itemId == R.id.nav_item2) {
                    // Handle item 2 click
                    Intent intent = new Intent(MainActivity.this,AboutActivity.class);
                    startActivity(intent);
                }
                else if (itemId == R.id.nav_item3) {
                    // Rate App
                    String appPackageName = getPackageName();

                    try {
                        // Open the app's page on the Google Play Store
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                    } catch (ActivityNotFoundException e) {
                        // If the Play Store app is not installed, open the Play Store website
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                    }
                }
                else if (itemId == R.id.nav_item4) {
                    // Feedback
                    sendFeedbackEmail();
                }
                else if (itemId == R.id.nav_item5) {
                    // Privacy Policy and Terms
                    showPrivacyDialog();
                }
                else if (itemId == R.id.nav_item6) {
                    // Share App
                    // Get the package name dynamically
                    String appPackageName = getPackageName();

                    // Create the content you want to share
                    String shareMessage = "Check out this awesome app: https://play.google.com/store/apps/details?id=" + appPackageName;

                    // Create an Intent to share text
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);

                    // Start the activity to show the share dialog
                    startActivity(Intent.createChooser(shareIntent, "Share via"));
                }else {
                    // Handle other items
                }

                // Close the drawer after handling item click
                drawerLayout.closeDrawers();
                if (selectedFragment != null) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, selectedFragment)
                            .commit();
                }
                return true;
            }
        });

        // Check if the user has accepted the privacy policy
        if (!isPrivacyPolicyAccepted()) {
            // Show the privacy dialog
            showPrivacyDialog();
        }
    }

    private void sendFeedbackEmail() {
        String[] recipients = {"colloflix@gmail.com"};
        String subject = "Online Courses Feedback";
        String body = "Dear developers,\n\nI have some feedback about the app: ";

        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto:")); // Only email apps should handle this
        emailIntent.putExtra(Intent.EXTRA_EMAIL, recipients);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, body);

        try {
            startActivity(emailIntent);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "No email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isPrivacyPolicyAccepted() {
        // Retrieve the acceptance status from SharedPreferences
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        return preferences.getBoolean("privacy_accepted", false);
    }

    private void setPrivacyPolicyAccepted(boolean accepted) {
        // Save the acceptance status in SharedPreferences
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("privacy_accepted", accepted);
        editor.apply();
    }

    private void showPrivacyDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_privacy, null);
        builder.setView(dialogView)
                .setCancelable(false);

        AlertDialog dialog = builder.create();
        dialog.show();

        // Set up the WebView
        WebView webViewPrivacy = dialogView.findViewById(R.id.webViewPrivacy);

        // Retrieve privacy policy HTML content from Firebase
        DatabaseReference privacyRef = FirebaseDatabase.getInstance().getReference("policies/privacy");
        privacyRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String privacyContent = dataSnapshot.child("content").getValue(String.class);
                    if (privacyContent != null) {
                        webViewPrivacy.loadDataWithBaseURL(null, privacyContent, "text/html", "UTF-8", null);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors
            }
        });

        // Set click listeners for the Accept and Decline buttons
        Button btnAccept = dialogView.findViewById(R.id.btnAccept);
        Button btnDecline = dialogView.findViewById(R.id.btnDecline);

        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle Accept button click
                setPrivacyPolicyAccepted(true); // Set privacy policy as accepted
                dialog.dismiss();
                // You can add more actions as needed
            }
        });

        btnDecline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle Decline button click
                // For example, close the app or show a message
                finish();
                // You can add more actions as needed
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Open or close the drawer when the ActionBarDrawerToggle is clicked
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    drawerLayout.closeDrawers();
                    Fragment selectedFragment = null;
                    String title = ""; // Initialize title

                    if (item.getItemId() == R.id.action_home) {
                        selectedFragment = new HomeFragment();
                        title = "Courses"; // Set title for HomeFragment
                    } else if (item.getItemId() == R.id.action_account) {
                        selectedFragment = new AccountFragment();
                        title = "Challenges"; // Set title for AccountFragment
                    } else if (item.getItemId() == R.id.action_notifications) {
                        selectedFragment = new NotificationsFragment();
                        title = "Account"; // Set title for NotificationsFragment
                    }

                    if (selectedFragment != null) {
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, selectedFragment)
                                .commit();
                        setActionBarTitle(title); // Update ActionBar title
                    }

                    return true;
                }
            };

    private void setActionBarTitle(String title) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(title); // Set ActionBar title
        }
    }

}

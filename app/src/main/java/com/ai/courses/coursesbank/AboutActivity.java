package com.ai.courses.coursesbank;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class AboutActivity extends AppCompatActivity {

    // Assuming you have these TextViews and CircleImageViews in your layout
    private TextView managerNameTextView, managerTitleTextView, devNameTextView, devTitleTextView;
    private CircleImageView managerImageView, devImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
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


        // Initialize your views
        managerNameTextView = findViewById(R.id.managername);
        managerTitleTextView = findViewById(R.id.managertitle);
        devNameTextView = findViewById(R.id.devname);
        devTitleTextView = findViewById(R.id.devtitle);
        managerImageView = findViewById(R.id.managerImageView);
        devImageView = findViewById(R.id.devImageView);
        TextView chatTileTextView = findViewById(R.id.chatTile);
        TextView chatTaglineTextView = findViewById(R.id.chatTagline);
        TextView chatDescTextView = findViewById(R.id.chatDesc);
        CardView chatNow = findViewById(R.id.chatNow);
        chatNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendFeedbackEmail();
            }
        });

        // Retrieve team member data from Firebase
        DatabaseReference teamRef = FirebaseDatabase.getInstance().getReference("team");
        teamRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Assuming the "manager" and "developer" nodes exist in your database
                DataSnapshot managerSnapshot = dataSnapshot.child("manager");
                DataSnapshot devSnapshot = dataSnapshot.child("developer");

                // Retrieve data from snapshots
                String managerName = managerSnapshot.child("name").getValue(String.class);
                String managerTitle = managerSnapshot.child("title").getValue(String.class);
                String managerImageURL = managerSnapshot.child("imageUrl").getValue(String.class);

                String devName = devSnapshot.child("name").getValue(String.class);
                String devTitle = devSnapshot.child("title").getValue(String.class);
                String devImageURL = devSnapshot.child("imageUrl").getValue(String.class);

                String chatTitle = dataSnapshot.child("title").getValue(String.class);
                String chatTagline = dataSnapshot.child("tagline").getValue(String.class);
                String chatDesc = dataSnapshot.child("desc").getValue(String.class);

                // Update the TextViews with the retrieved data
                chatTileTextView.setText(chatTitle);
                chatTaglineTextView.setText(chatTagline);
                chatDescTextView.setText(chatDesc);
                chatNow.setVisibility(View.VISIBLE);
                // Update UI with retrieved data
                if (managerName != null && managerTitle != null && managerImageURL != null) {
                    managerNameTextView.setText(managerName);
                    managerTitleTextView.setText(managerTitle);
                    // Load image using a library like Picasso or Glide
                    Picasso.get().load(managerImageURL).into(managerImageView);
                }

                if (devName != null && devTitle != null && devImageURL != null) {
                    devNameTextView.setText(devName);
                    devTitleTextView.setText(devTitle);
                    // Load image using a library like Picasso or Glide
                    Picasso.get().load(devImageURL).into(devImageView);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors
            }
        });

        // Other code in your onCreate method
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

}

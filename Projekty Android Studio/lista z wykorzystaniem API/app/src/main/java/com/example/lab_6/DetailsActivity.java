package com.example.lab_6;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.util.Base64;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;

public class DetailsActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        String title = getIntent().getStringExtra("title");
        String base64Image = getIntent().getStringExtra("imageBase64");
        String description = getIntent().getStringExtra("description");

        TextView titleView = findViewById(R.id.textViewDetail);
        ImageView imageView = findViewById(R.id.imageViewDetail);
        TextView descriptionView = findViewById(R.id.artworkDescription);

        titleView.setText(title);
        descriptionView.setText(description);

        View backButton = findViewById(R.id.backButton);

        backButton.setOnClickListener(v -> {
            // Wysyłanie wiadomości Toast z treścią obrazu po powrocie
            Toast.makeText(DetailsActivity.this, "Wrócono do listy: " + title, Toast.LENGTH_SHORT).show();
            finish(); // Powrót do poprzedniej aktywności
        });

        byte[] decodedString = Base64.decode(base64Image.split(",")[1], Base64.DEFAULT);
        Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        imageView.setImageBitmap(decodedBitmap);
    }
}
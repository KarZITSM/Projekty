package com.example.lab_6;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    private ArrayList<Artwork> artworks = new ArrayList<>();
    private ArtworkAdapter adapter;
    private static final String TAG = "MainActivity"; // Tag dla logów

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = findViewById(R.id.listViewArtworks);
        adapter = new ArtworkAdapter(this, artworks);
        listView.setAdapter(adapter);


        fetchArtData();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Artwork artwork = artworks.get(position);
                Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
                intent.putExtra("title", artwork.getTitle());
                intent.putExtra("imageBase64", artwork.getImageBase64());
                intent.putExtra("description", artwork.getDescription());
                startActivity(intent);
            }
        });
    }

    private void fetchArtData() {
        String url = "https://api.artic.edu/api/v1/artworks?page=2&limit=10";
        Log.d(TAG, "Making API request to: " + url);
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject  response) {
                        Log.d(TAG, "API Response received: " + response.toString());
                        try {
                            // Parse the "data" part of the response to get the artwork details
                            JSONArray artworksArray = response.getJSONArray("data");  // Correctly extract the "data" array
                            for (int i = 0; i < artworksArray.length(); i++) {
                                JSONObject artworkJson = artworksArray.getJSONObject(i);
                                String title = artworkJson.getString("title");
                                String description = artworkJson.optString("description", "Brak opisu");
                                JSONObject thumbnailJson = artworkJson.optJSONObject("thumbnail");
                                String base64Image = null;
                                if (thumbnailJson != null) {
                                    base64Image = thumbnailJson.optString("lqip");
                                }

                                // If image exists, add artwork to the list
                                if (base64Image != null && !base64Image.isEmpty()) {
                                    artworks.add(new Artwork(title, base64Image, description));
                                    Log.d(TAG, "Artwork added: " + title);
                                } else {
                                    Log.w(TAG, "No image found for artwork: " + title);
                                }
                            }
                            adapter.notifyDataSetChanged();
                            Log.d(TAG, "Adapter notified, UI updated");
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e(TAG, "Error while processing response: " + e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Log.e(TAG, "Error fetching data: " + error.getMessage());
                    }
                });

        queue.add(request);
    }
}
package com.example.lab_6;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import java.util.List;

public class ArtworkAdapter extends BaseAdapter {

    private Context context;
    private List<Artwork> artworks;

    public ArtworkAdapter(Context context, List<Artwork> artworks) {
        this.context = context;
        this.artworks = artworks;
    }

    @Override
    public int getCount() {
        return artworks.size();
    }

    @Override
    public Object getItem(int position) {
        return artworks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_artwork, null);
        }

        ImageView imageView = convertView.findViewById(R.id.imageView);
        TextView titleView = convertView.findViewById(R.id.textViewTitle);

        Artwork artwork = artworks.get(position);
        titleView.setText(artwork.getTitle());

        // Jeśli obrazek jest w base64, przekonwertuj na Bitmap
        String base64Image = artwork.getImageBase64();
        byte[] decodedString = Base64.decode(base64Image.split(",")[1], Base64.DEFAULT);
        Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        imageView.setImageBitmap(decodedBitmap);

        return convertView;
    }
}
package com.example.bitmapregiondecoder;

import androidx.appcompat.app.AppCompatActivity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    private HighImageView photoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        photoView = findViewById(R.id.img);
        InputStream inputStream = null;
        try {
            inputStream = getAssets().open("test.jpg");
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            photoView.setImage(inputStream, bitmap.getWidth(), bitmap.getHeight());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

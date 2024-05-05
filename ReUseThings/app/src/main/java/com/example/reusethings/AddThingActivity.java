package com.example.reusethings;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import android.content.SharedPreferences;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class AddThingActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri selectedImageUri;
    private DatabaseHelper databaseHelper;
    private Bitmap selectedBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_thing);

        databaseHelper = new DatabaseHelper(this);

        EditText itemName = findViewById(R.id.item_name);
        Button chooseImageButton = findViewById(R.id.button_choose_image);
        EditText phoneNumber = findViewById(R.id.phone_number);
        EditText address = findViewById(R.id.address);
        Button saveButton = findViewById(R.id.button_save);
        ImageView imagePreview = findViewById(R.id.image_preview);
        ImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> finish());


        chooseImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String itemNameStr = itemName.getText().toString();
                String itemPhoneStr = phoneNumber.getText().toString();
                String itemAddressStr = address.getText().toString();
                InputStream inputStream = null;

                if (itemNameStr.isEmpty() || itemPhoneStr.isEmpty() || itemAddressStr.isEmpty()) {
                    Toast.makeText(AddThingActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    inputStream = getContentResolver().openInputStream(selectedImageUri);
                    selectedBitmap = BitmapFactory.decodeStream(inputStream);
                    inputStream.close(); // Ensure to close the stream
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                if (selectedBitmap == null) {
                    Toast.makeText(AddThingActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }


                // Retrieve the current user's ID or username
                SharedPreferences sharedPreferences = getSharedPreferences("shared_prefs", Context.MODE_PRIVATE);
                String username = sharedPreferences.getString("username", "").toString();

                if (username == null) {
                    Toast.makeText(AddThingActivity.this, "User not logged in", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Save to the database
                boolean isAdded = databaseHelper.addItem(itemNameStr, selectedBitmap, itemPhoneStr, itemAddressStr, username);

                if (isAdded) {
                    Toast.makeText(AddThingActivity.this, "Item added successfully", Toast.LENGTH_SHORT).show();
                    finish(); // Close the activity after saving
                } else {
                    Toast.makeText(AddThingActivity.this, "Error saving item", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();

            try {
                InputStream inputStream = getContentResolver().openInputStream(selectedImageUri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                ImageView imagePreview = findViewById(R.id.image_preview);
                imagePreview.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(this, "Image not found", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}


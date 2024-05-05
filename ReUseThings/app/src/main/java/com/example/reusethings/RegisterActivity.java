package com.example.reusethings;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

public class RegisterActivity extends AppCompatActivity {

    EditText Username, Email ,Password1, Password2;
    Button btn;
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Username = findViewById(R.id.Username);
        Email = findViewById(R.id.Email);
        Password1 = findViewById(R.id.Password1);
        Password2 = findViewById(R.id.Password2);
        btn = findViewById(R.id.Registerbutton);
        tv = findViewById(R.id.ExistingUser);

        Database db = new Database(getApplicationContext(), "ReUseThings", null, 1);

        btn.setOnClickListener(v -> {
            String username = Username.getText().toString();
            String email = Email.getText().toString();
            String password1 = Password1.getText().toString();
            String password2 = Password2.getText().toString();

            if(username.length() == 0 || email.length() == 0 || password1.length() == 0 || password2.length() == 0){
                Toast.makeText(getApplicationContext(), "Please fill all the details", Toast.LENGTH_SHORT).show();
            }
            else if(password1.compareTo(password2) != 0) {
                Toast.makeText(getApplicationContext(), "Passwords doesn't the same", Toast.LENGTH_SHORT).show();
            }
            else if (!isValidPassword(password1)) {
                Toast.makeText(getApplicationContext(), "Passwords must contains at least 8 letters (1 letter, 1 digit, 1 special symbol)", Toast.LENGTH_SHORT).show();
            }
            else if (db.usernameExists(username)) {
                Toast.makeText(getApplicationContext(), "Username already exists", Toast.LENGTH_SHORT).show();
            }
            else {
                db.register(username, email, password1);
                Toast.makeText(getApplicationContext(), "User added successfully", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }

        });

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });
    }

    public static boolean isValidPassword(String password) {
        int f1 = 0, f2 = 0, f3 = 0;
        if (password.length() < 8) {
            return false;
        }
        else {
            for (int p = 0; p < password.length(); p++) {
                char c = password.charAt(p);
                if (Character.isLetter(c)){
                    f1 = 1;
                }
                else if (Character.isDigit(c)){
                    f2 = 1;
                }
                // special symbol
                else if(c >= 33 && c <= 46 || c == 64){
                    f3 = 1;
                }
            }
        }
        return f1 == 1 && f2 == 1 && f3 == 1;
    }
}
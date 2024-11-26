package com.example.inventry;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    // Predefined password
    private static final String PREDEFINED_PASSWORD = "1234"; // Change this to your desired password

    private EditText passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Initialize the EditText for password input
        passwordEditText = findViewById(R.id.password);

        // Directly handle password match with minimal code
        passwordEditText.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
                // Not used
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                // If password matches, transition immediately
                if (charSequence.length() == PREDEFINED_PASSWORD.length() && charSequence.toString().equals(PREDEFINED_PASSWORD)) {
                    // Password matched, start MainActivity2 and finish the current activity
                    Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                    startActivity(intent);
                    finish();  // Close the current activity
                }
            }

            @Override
            public void afterTextChanged(android.text.Editable editable) {
                // Not used
            }
        });
    }
}

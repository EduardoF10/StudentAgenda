package com.example.studentagenda;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;



public class EditActivity extends AppCompatActivity {

    EditText etItem;
    Button btnSave;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        etItem = findViewById(R.id.etItem);
        btnSave = findViewById(R.id.btnSave);

        getSupportActionBar().setTitle("Edit item");

        // Using the existing item to set up the text for the edit box
        etItem.setText(getIntent().getStringExtra(MainActivity.KEY_ITEM_TEXT));

        // When the user presses the save button
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an intent which will contain the results
                Intent intent = new Intent();
                // Capture the text in the edit box
                String editTextString = etItem.getText().toString();
                // Here we add the String item to the model if and only if there is text on it
                if (editTextString.length() >= 1) {
                    // Pass the data (results of editing)
                    intent.putExtra(MainActivity.KEY_ITEM_TEXT, editTextString);
                    intent.putExtra(MainActivity.KEY_ITEM_POSITION, getIntent().getExtras().getInt(MainActivity.KEY_ITEM_POSITION));
                    // Set the result of the intent
                    setResult(RESULT_OK, intent);
                    // Finish activity, close the screen and go back
                    finish();
                } else {
                    // Prompts the user to enter an item if the add button was pressed with the text box empty
                    Toast.makeText(getApplicationContext(), "Text cannot be empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
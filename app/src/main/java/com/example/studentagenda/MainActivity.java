package com.example.studentagenda;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String KEY_ITEM_TEXT = "item_text";
    public static final String KEY_ITEM_POSITION = "item_position";
    public static final int EDIT_TEXT_CODE = 20;


    // This list of strings is defined as our model of items in the todo list
    List<String> items;

    Button btnAdd;
    EditText etItem;
    RecyclerView rvItem;
    ItemsAdapter itemsAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAdd = findViewById(R.id.addButton);
        etItem = findViewById(R.id.editTextBox);
        rvItem = findViewById(R.id.itemsBox);

        loadItems();

        ItemsAdapter.OnLongClickListener longClickedItem = new ItemsAdapter.OnLongClickListener() {
            @Override
            public void onItemLongClicked(int position) {
                // Delete the item from the model at the passed position
                items.remove(position);
                // Notify the adapter
                itemsAdapter.notifyItemRemoved(position);
                // This displays a small pop up or dialogue that shows up briefly and then it disappears. Gives the user some feedback
                Toast.makeText(getApplicationContext(), "Item was removed", Toast.LENGTH_SHORT).show();
                // Save after removing an item from the list
                saveItems();

            }
        };

        ItemsAdapter.OnClickListener onClickListener = new ItemsAdapter.OnClickListener() {
            @Override
            public void onItemCLicked(int position) {
                // This is to test if the click method works (check Logcat while the app is running)
                Log.d("MainActivity", "Single click at position " + position);
                // Create the new activity (1st param: current instance, 2nd param: destination)
                Intent i = new Intent(MainActivity.this, EditActivity.class);
                // Pass the data being edited
                i.putExtra(KEY_ITEM_TEXT, items.get(position));
                i.putExtra(KEY_ITEM_POSITION, position);
                // Display the activity
                startActivityForResult(i, EDIT_TEXT_CODE);

            }
        };
        itemsAdapter = new ItemsAdapter(items, longClickedItem, onClickListener);
        // Setting the adapter on the recycler view
        rvItem.setAdapter(itemsAdapter);
        // This basic LayoutManager put things vertically
        rvItem.setLayoutManager(new LinearLayoutManager(this));

        btnAdd.setOnClickListener(new View.OnClickListener() {
            // This part of the code is reached if the button is pressed (clicked)
            @Override
            public void onClick(View v) {
                // etItem.getText() returns an Editable so we need toString() to change it into a String
                String todoItem = etItem.getText().toString();

                // Here we add the String item to the model if and only if there is text on it
                if (todoItem.length() >= 1) {
                    items.add(todoItem);
                    // Notify adapter that an item is inserted
                    itemsAdapter.notifyItemInserted(items.size() - 1);
                    // The text in the edit box resets each time a text is added to the model
                    etItem.setText("");
                    // This displays a small pop up or dialogue that shows up briefly and then it disappears. Gives the user some feedback
                    Toast.makeText(getApplicationContext(), "Item was added", Toast.LENGTH_SHORT).show();
                    // Save after adding an item into the list
                    saveItems();
                } else {
                    // Prompts the user to enter an item if the add button was pressed with the text box empty
                    Toast.makeText(getApplicationContext(), "Enter an item first", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    // Handle  the result of the edit activity
    @SuppressLint("MissingSuperCall")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK && requestCode == EDIT_TEXT_CODE) {
            // Retrieve the updated text value
            String editText = data.getStringExtra(KEY_ITEM_TEXT);
            // Extract the original position of the edited item from the position key
            int position = data.getExtras().getInt(KEY_ITEM_POSITION);
            // Update the model at the right position with the new edited text
            items.set(position, editText);
            // Notify the adapter
            itemsAdapter.notifyItemChanged(position);
            // Persist the changes
            saveItems();
            Toast.makeText(getApplicationContext(), "Item was edited successfully", Toast.LENGTH_SHORT).show();
        } else {
            Log.w("MainActivity", "Unknown call to onActivityResult");
        }
    }


    // PERSISTENCE IMPLEMENTATION -------------------------------------------------------

    // This method returns the file in which we will store our list of todo items
    private File getDataFile() {
        // returns the file in the directory with the name: data.txt
        return new File(getFilesDir(), "data.txt");
    }

    // This function will load items by reading every line of the data file
    private void loadItems() {
        // Reads all the lines out of the data file and loads them into an ArrayList which is assign to our model
        try {
            items = new ArrayList<>(FileUtils.readLines(getDataFile(), String.valueOf(Charset.defaultCharset())));
        } catch (IOException e) {
            Log.e("MainActivity", "Error reading items", e);
            // An empty ArrayList is returned if an error occurs reading the items on the list
            items = new ArrayList<>();
        }
    }

    // This function will save items by writing them into the data file
    private void saveItems() {
        try {
            FileUtils.writeLines(getDataFile(), items);
        } catch (IOException e) {
            Log.e("MainActivity", "Error writing items", e);
        }
    }

    // --------------------------------------------------------------------------------

}
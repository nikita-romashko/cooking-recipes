package com.example.cookingrecipes;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

public class RecipesActivity extends AppCompatActivity {

    private ListView listView;
    private Spinner spinnerCategory;
    private Button btnFilter;

    private List<String> recipeNames;
    private List<String> recipeDetails;
    private List<String> recipeCategories;
    private List<String> filteredNames;
    private List<String> filteredDetails;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_recipes);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        listView = findViewById(R.id.listView);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        btnFilter = findViewById(R.id.btnFilter);

        recipeNames = new ArrayList<>();
        recipeDetails = new ArrayList<>();
        recipeCategories = new ArrayList<>();

        recipeNames.add(getString(R.string.omelette_name));
        recipeDetails.add(getString(R.string.omelette_details));
        recipeCategories.add(getString(R.string.breakfast));

        recipeNames.add(getString(R.string.borscht_name));
        recipeDetails.add(getString(R.string.borscht_details));
        recipeCategories.add(getString(R.string.lunch));

        recipeNames.add(getString(R.string.pasta_name));
        recipeDetails.add(getString(R.string.pasta_details));
        recipeCategories.add(getString(R.string.dinner));

        filteredNames = new ArrayList<>(recipeNames);
        filteredDetails = new ArrayList<>(recipeDetails);

        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.categories_array, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(spinnerAdapter);


        btnFilter.setOnClickListener(v -> {
            filterRecipes();
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = filteredNames.get(position);
                String details = filteredDetails.get(position);

                Intent intent = new Intent(RecipesActivity.this, RecipeDetailActivity.class);
                intent.putExtra("recipe_name", name);
                intent.putExtra("recipe_details", details);
                startActivity(intent);
            }
        });
    }

    private void filterRecipes() {
        String selectedCategory = spinnerCategory.getSelectedItem().toString();
        filteredNames.clear();
        filteredDetails.clear();

        for (int i = 0; i < recipeNames.size(); i++) {
            if (selectedCategory.equals(getString(R.string.all_categories)) ||
                    recipeCategories.get(i).equals(selectedCategory)) {
                filteredNames.add(recipeNames.get(i));
                filteredDetails.add(recipeDetails.get(i));
            }
        }

        showRecipes(filteredNames);

        if (filteredNames.isEmpty()) {
            Toast.makeText(this, R.string.no_recipes, Toast.LENGTH_SHORT).show();
        }
    }

    private void showRecipes(List<String> filteredNames) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, filteredNames);
        listView.setAdapter(adapter);
    }
}
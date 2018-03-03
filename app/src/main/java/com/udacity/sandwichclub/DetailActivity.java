package com.udacity.sandwichclub;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.udacity.sandwichclub.databinding.ActivityDetailBinding;
import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.utils.JsonUtils;

import java.util.List;

public class DetailActivity extends AppCompatActivity {

    public static final String LOG_TAG = DetailActivity.class.getName();

    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;

    private ActivityDetailBinding mBinding;
    // Sandwich whose detailed information is shown
    private Sandwich sandwich;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail);

        // Check for errors
        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }
        int position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
        if (position == DEFAULT_POSITION) {
            // EXTRA_POSITION not found in intent
            closeOnError();
            return;
        }
        String[] sandwiches = getResources().getStringArray(R.array.sandwich_details);
        String json = sandwiches[position];
        sandwich = JsonUtils.parseSandwichJson(json);
        if (sandwich == null) {
            // Sandwich data unavailable
            closeOnError();
            return;
        }

        //Toolbar settings
        setSupportActionBar(mBinding.toolbar);
        //Set title
        mBinding.actionBarSubtitle.setTitle(sandwich.getMainName());
        //Add back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //Add proper image
        //Set max height so that the image doesn't fill the whole screen
        //Get the device screen dimensions
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        int screenHeight = getResources().getDisplayMetrics().heightPixels;
        //Set the dimensions of the image based on the screen dimensions
        Picasso.with(this)
                .load(sandwich.getImage())
                .resize(screenWidth, (int) (0.6 * screenHeight))
                .centerCrop()
                .into(mBinding.imageIv);

        //Add the other information
        populateUI();
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    private void populateUI() {
        // Set origin text
        String placeOfOrigin = sandwich.getPlaceOfOrigin();
        if (TextUtils.isEmpty(placeOfOrigin)) {
            placeOfOrigin = getString(R.string.no_origin);
        }
        String originLabel = getString(R.string.detail_place_of_origin_label);
        String originText = originLabel + " " + placeOfOrigin;
        mBinding.actionBarSubtitle.setSubtitle(originText);
        mBinding.actionBarSubtitle
                .setExpandedSubtitleTextAppearance(R.style.TextAppearance_AppCompat_Title);

        // Set other names
        String otherNames = sandwich.getAlsoKnownAs();
        if (!TextUtils.isEmpty(otherNames)) {
            mBinding.alsoKnownTv.setText(otherNames);
        } else {
            // If there aren't any other names, hide this section
            mBinding.alsoKnownLayout.setVisibility(View.GONE);
        }

        // Set description
        if (!TextUtils.isEmpty(sandwich.getDescription())) {
            mBinding.descriptionTv.setText(sandwich.getDescription());
        } else {
            // If there isn't description, show a message
            mBinding.descriptionTv.setText(R.string.no_description_message);
        }

        // Set ingredients list
        List<String> ingredients = sandwich.getIngredients();
        if (ingredients.size() > 0) {
            // Create ingredients text from array
            StringBuilder ingredientsText = new StringBuilder();
            ingredientsText.append("- ");
            ingredientsText.append(ingredients.get(0));
            for (int i = 1; i < ingredients.size(); i++) {
                ingredientsText.append("\n");
                ingredientsText.append("- ");
                ingredientsText.append(ingredients.get(i));
            }
            mBinding.ingredientsTv.setText(ingredientsText);
        } else {
            // If there aren't ingredients, show a message
            mBinding.ingredientsTv.setText(R.string.no_ingredients_message);
        }
    }

    // Handle back button on toolbar
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}

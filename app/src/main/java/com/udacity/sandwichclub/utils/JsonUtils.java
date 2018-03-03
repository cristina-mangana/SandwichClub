package com.udacity.sandwichclub.utils;

import android.text.TextUtils;
import android.util.Log;

import com.udacity.sandwichclub.model.Sandwich;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.udacity.sandwichclub.DetailActivity.LOG_TAG;

public class JsonUtils {

    public static Sandwich parseSandwichJson(String json) {

        /* Key value for the "name" object. It holds "mainName" and "alsoKnownAs" strings.*/
        final String NAME_KEY = "name";

        /* Key value for the "mainName" string.*/
        final String MAIN_NAME_KEY = "mainName";

        /* Key value for the "alsoKnownAs" string.*/
        final String ALSO_KNOWN_AS_KEY = "alsoKnownAs";

        /* Key value for the "placeOfOrigin" string.*/
        final String ORIGIN_KEY = "placeOfOrigin";

        /* Key value for the "description" string.*/
        final String DESCRIPTION_KEY = "description";

        /* Key value for the "image" string. This string is the url to download the image.*/
        final String IMAGE_KEY = "image";

        /* Key value for the "ingredients" array. It contains every ingredient as a string.*/
        final String INGREDIENTS_KEY = "ingredients";

        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(json)) {
            return null;
        }

        // Create an empty Sandwich Object so that we can start adding information about it
        Sandwich sandwich = new Sandwich();
        try {
            JSONObject baseJsonSandwich = new JSONObject(json);

            if (baseJsonSandwich.has(NAME_KEY)) {
                JSONObject name = baseJsonSandwich.getJSONObject(NAME_KEY);

                // Extract name and add it to the Sandwich Object
                if (name.has(MAIN_NAME_KEY)) {
                    String mainName = name.getString(MAIN_NAME_KEY);
                    sandwich.setMainName(mainName);
                }

                // Extract other names
                if (name.has(ALSO_KNOWN_AS_KEY)) {
                    JSONArray otherNames = name.getJSONArray(ALSO_KNOWN_AS_KEY);
                    // Create "also known as" string from array
                    StringBuilder alsoKnownAsString = new StringBuilder();
                    if (otherNames.length() > 0) {
                        alsoKnownAsString = new StringBuilder(otherNames.getString(0));
                        if (otherNames.length() > 1) {
                            for (int j = 1; j < otherNames.length(); j++) {
                                alsoKnownAsString.append(", ");
                                alsoKnownAsString.append(otherNames.getString(j));
                            }
                        }
                    }
                    // Add other names to the Sandwich Object
                    sandwich.setAlsoKnownAs(alsoKnownAsString.toString());
                }
            }

            // Extract place of origin and add it to the Sandwich Object
            if (baseJsonSandwich.has(ORIGIN_KEY)) {
                String placeOfOrigin = baseJsonSandwich.getString(ORIGIN_KEY);
                sandwich.setPlaceOfOrigin(placeOfOrigin);
            }

            // Extract description and add it to the Sandwich Object
            if (baseJsonSandwich.has(DESCRIPTION_KEY)) {
                String description = baseJsonSandwich.getString(DESCRIPTION_KEY);
                sandwich.setDescription(description);
            }

            // Extract image url and add it to the Sandwich Object
            if (baseJsonSandwich.has(IMAGE_KEY)) {
                String imageUrlString = baseJsonSandwich.getString(IMAGE_KEY);
                sandwich.setImage(imageUrlString);
            }

            // Extract ingredients and add to the Sandwich Object as a List<String>
            if (baseJsonSandwich.has(INGREDIENTS_KEY)) {
                JSONArray JSONIngredients = baseJsonSandwich.getJSONArray(INGREDIENTS_KEY);
                List<String> ingredients = new ArrayList<>();
                for (int j = 0; j < JSONIngredients.length(); j++) {
                    ingredients.add(JSONIngredients.getString(j));
                }
                sandwich.setIngredients(ingredients);
            }

        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the JSON results", e);
        }

        // Return the Sandwich Object
        return sandwich;
    }
}

package com.example.fitfeast;

import android.util.Log;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import com.example.fitfeast.BuildConfig;

public class PexelsImageLoader {

    private static final String TAG = "PEXELS_IMAGE";

    private static final String API_KEY = BuildConfig.PEXELS_API_KEY;;

    public static void loadFoodImage(String mealName, ImageView imageView) {

        new Thread(() -> {

            try {

                String query = simplifyMealName(mealName);

                Log.d(TAG, "Original meal: " + mealName);
                Log.d(TAG, "Search query: " + query);

                String apiUrl =
                        "https://api.pexels.com/v1/search?query=" +
                                query +
                                "&per_page=1";

                URL url = new URL(apiUrl);

                HttpURLConnection conn =
                        (HttpURLConnection) url.openConnection();

                conn.setRequestProperty("Authorization", API_KEY);

                int responseCode = conn.getResponseCode();

                Log.d(TAG, "Pexels Response Code: " + responseCode);

                InputStream stream;

                if (responseCode >= 200 && responseCode < 300) {
                    stream = conn.getInputStream();
                } else {
                    stream = conn.getErrorStream();
                }

                BufferedReader reader =
                        new BufferedReader(new InputStreamReader(stream));

                StringBuilder response = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                reader.close();

                Log.d(TAG, "Pexels Raw Response: " + response.toString());

                JSONObject json = new JSONObject(response.toString());

                JSONArray photos = json.getJSONArray("photos");

                if (photos.length() == 0) {

                    Log.e(TAG, "No image found for: " + query);

                    return;
                }

                JSONObject photo = photos.getJSONObject(0);

                String imageUrl =
                        photo.getJSONObject("src").getString("medium");

                Log.d(TAG, "Image URL: " + imageUrl);

                imageView.post(() ->
                        Picasso.get()
                                .load(imageUrl)
                                .placeholder(R.drawable.food_default)
                                .error(R.drawable.food_default)
                                .into(imageView)
                );

            }
            catch (Exception e) {

                Log.e(TAG, "Pexels image loading failed", e);

            }

        }).start();

    }

    // Simplify meal names
    private static String simplifyMealName(String meal) {

        meal = meal.toLowerCase();

        if (meal.contains("chicken"))
            return "chicken food";

        if (meal.contains("fish"))
            return "grilled fish";

        if (meal.contains("egg"))
            return "eggs breakfast";

        if (meal.contains("dal"))
            return "dal curry";

        if (meal.contains("rice"))
            return "brown rice";

        if (meal.contains("roti"))
            return "roti indian bread";

        if (meal.contains("oats"))
            return "oatmeal";

        if (meal.contains("fruit"))
            return "fruit salad";

        return "healthy meal";
    }
}
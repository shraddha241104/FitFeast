package com.example.fitfeast;

import android.util.Log;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class FoodImageLoader {

    private static final String TAG = "UNSPLASH_IMAGE";

    public static void loadFoodImage(String mealName, ImageView imageView) {

        try {

            Log.d(TAG, "Original meal name: " + mealName);

            // Clean the meal name
            String query = simplifyMealName(mealName);

            Log.d(TAG, "Simplified query: " + query);

            String imageUrl =
                    "https://source.unsplash.com/600x600/?" + query + ",food";

            Log.d(TAG, "Unsplash URL: " + imageUrl);

            Picasso.get()
                    .load(imageUrl)
                    .placeholder(R.drawable.food_default)
                    .error(R.drawable.food_default)
                    .into(imageView, new Callback() {

                        @Override
                        public void onSuccess() {
                            Log.d(TAG, "Image loaded successfully for: " + query);
                        }

                        @Override
                        public void onError(Exception e) {
                            Log.e(TAG, "Image load failed for: " + query, e);
                        }
                    });

        } catch (Exception e) {

            Log.e(TAG, "Unexpected error while loading image", e);

        }

    }

    // Extract main food keyword
    private static String simplifyMealName(String meal) {

        meal = meal.toLowerCase();

        if (meal.contains("chicken"))
            return "chicken curry";

        if (meal.contains("fish"))
            return "grilled fish";

        if (meal.contains("egg"))
            return "scrambled eggs";

        if (meal.contains("dal"))
            return "dal curry";

        if (meal.contains("rice"))
            return "brown rice";

        if (meal.contains("roti"))
            return "roti";

        if (meal.contains("oats"))
            return "oatmeal";

        if (meal.contains("fruit"))
            return "fruit salad";

        return "healthy food";
    }
}
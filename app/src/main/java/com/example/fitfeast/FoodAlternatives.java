package com.example.fitfeast;

import java.util.HashMap;

public class FoodAlternatives {

    public static String getAlternative(String food) {

        HashMap<String, String> map = new HashMap<>();

        map.put("Rice", "Roti (2 medium)");
        map.put("Paneer", "Tofu");
        map.put("Chicken", "Grilled Fish");
        map.put("Eggs", "Paneer bhurji");
        map.put("Milk", "Soy milk");
        map.put("Bread", "Oats");

        if (map.containsKey(food)) {
            return map.get(food);
        } else {
            return "No alternative available";
        }
    }
}

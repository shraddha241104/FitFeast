package com.example.fitfeast;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import com.example.fitfeast.BuildConfig;

public class GeminiImageHelper {

    private static final String TAG = "GEMINI_IMAGE";
    private static final String API_KEY = BuildConfig.GEMINI_API_KEY;;

    public static void generateFoodImage(String mealName, ImageView imageView) {

        new Thread(() -> {

            try {

                String prompt = "professional food photography of plated meal: " + mealName;

                Log.d(TAG, "Generating image for meal: " + mealName);

                URL url = new URL(
                        "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.1-flash-image-preview:generateContent?key=" + API_KEY
                );

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);

                /* TEXT PART */

                JSONObject part = new JSONObject();
                part.put("text", prompt);

                JSONArray parts = new JSONArray();
                parts.put(part);

                JSONObject content = new JSONObject();
                content.put("parts", parts);

                JSONArray contents = new JSONArray();
                contents.put(content);

                /* IMAGE RESPONSE CONFIG */

                JSONObject generationConfig = new JSONObject();
                JSONArray modalities = new JSONArray();
                modalities.put("IMAGE");

                generationConfig.put("responseModalities", modalities);

                /* FINAL REQUEST BODY */

                JSONObject body = new JSONObject();
                body.put("contents", contents);
                body.put("generationConfig", generationConfig);

                Log.d(TAG, "Request Body: " + body.toString());

                OutputStream os = conn.getOutputStream();
                os.write(body.toString().getBytes());
                os.close();

                int responseCode = conn.getResponseCode();
                Log.d(TAG, "Response Code: " + responseCode);

                InputStream stream;

                if (responseCode >= 200 && responseCode < 300) {
                    stream = conn.getInputStream();
                } else {
                    stream = conn.getErrorStream();
                }

                BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

                StringBuilder response = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                reader.close();

                Log.d(TAG, "Gemini Raw Response: " + response.toString());

                JSONObject fullResponse = new JSONObject(response.toString());

                JSONArray candidates = fullResponse.getJSONArray("candidates");

                JSONObject partData = candidates
                        .getJSONObject(0)
                        .getJSONObject("content")
                        .getJSONArray("parts")
                        .getJSONObject(0);

                JSONObject inlineData = partData.getJSONObject("inlineData");

                String base64 = inlineData.getString("data");

                Log.d(TAG, "Image Base64 received");

                byte[] decoded = Base64.decode(base64, Base64.DEFAULT);

                Bitmap bitmap = BitmapFactory.decodeByteArray(decoded, 0, decoded.length);

                imageView.post(() -> imageView.setImageBitmap(bitmap));

                Log.d(TAG, "Image successfully displayed");

            }
            catch (Exception e) {

                Log.e(TAG, "Image generation failed", e);

            }

        }).start();
    }
}
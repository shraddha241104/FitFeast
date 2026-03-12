package com.example.fitfeast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import com.example.fitfeast.BuildConfig;

public class GeminiHelper {

    // 🔑 Replace with your Gemini API Key
    private static final String API_KEY = BuildConfig.GEMINI_API_KEY;

    public interface GeminiCallback {
        void onSuccess(String result);
        void onError(String error);
    }

    public static void generateDietPlan(int calories,
                                        String dietType,
                                        String allergies,
                                        GeminiCallback callback) {

        new Thread(() -> {

            try {

                // 🔹 Strong prompt for Gemini
                String prompt =
                        "Generate a healthy Indian diet plan.\n" +
                                "Total calories must equal " + calories + ".\n" +
                                "Diet type: " + dietType + ".\n" +
                                "Avoid allergies: " + allergies + ".\n\n" +

                                "Return ONLY JSON in this format:\n" +

                                "{\n" +
                                "\"breakfast1\":{\"meal\":\"food name\",\"calories\":200},\n" +
                                "\"breakfast2\":{\"meal\":\"food name\",\"calories\":200},\n" +
                                "\"lunch1\":{\"meal\":\"food name\",\"calories\":300},\n" +
                                "\"lunch2\":{\"meal\":\"food name\",\"calories\":300},\n" +
                                "\"dinner1\":{\"meal\":\"food name\",\"calories\":250},\n" +
                                "\"dinner2\":{\"meal\":\"food name\",\"calories\":250}\n" +
                                "}";

                // 🔹 Correct Gemini API endpoint
                URL url = new URL(
                        "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=" + API_KEY
                );

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);

                // 🔹 Correct request structure
                JSONObject part = new JSONObject();
                part.put("text", prompt);

                JSONArray parts = new JSONArray();
                parts.put(part);

                JSONObject content = new JSONObject();
                content.put("parts", parts);

                JSONArray contents = new JSONArray();
                contents.put(content);

                JSONObject requestBody = new JSONObject();
                requestBody.put("contents", contents);

                OutputStream os = conn.getOutputStream();
                os.write(requestBody.toString().getBytes());
                os.close();

                // 🔹 Read response
                InputStream stream;

                if (conn.getResponseCode() >= 400) {
                    stream = conn.getErrorStream();
                } else {
                    stream = conn.getInputStream();
                }

                BufferedReader reader =
                        new BufferedReader(new InputStreamReader(stream));

                StringBuilder response = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                String rawResponse = response.toString();

                // 🔹 Debug log (important)
                System.out.println("Gemini Raw Response: " + rawResponse);

                JSONObject fullResponse = new JSONObject(rawResponse);

                // 🔹 Validate response
                if (!fullResponse.has("candidates")) {
                    callback.onError("No candidates returned from AI");
                    return;
                }

                JSONArray candidates = fullResponse.getJSONArray("candidates");

                if (candidates.length() == 0) {
                    callback.onError("Empty AI response");
                    return;
                }

                JSONObject textPart =
                        candidates.getJSONObject(0)
                                .getJSONObject("content")
                                .getJSONArray("parts")
                                .getJSONObject(0);

                String result = textPart.getString("text");

                callback.onSuccess(result);

            }
            catch (Exception e) {

                e.printStackTrace();

                callback.onError(e.getMessage());
            }

        }).start();
    }
}
package com.example.android.bookship;

import android.text.TextUtils;
import android.util.Log;

import com.example.android.bookship.Class.Books;
import com.example.android.bookship.Class.Details;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class Queryclass {
    /**
     * Create a private constructor because no one should ever create a {@link Queryclass} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private Queryclass() {
    }

    /**
     * Query the GoogleBooks dataset and return an {@link Books} object to represent a single book.
     */
    public static List<Books> fetchBookData(String requestUrl) {
        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e("Queryclass", "Error closing input stream", e);
        }

        // Extract relevant fields from the JSON response and create an {@link Event} object
        List<Books> books = extractFeatureFromJson(jsonResponse);

        // Return the {@link Event}
        return books;
    }

    /**
     * Query the GoogleBooks dataset and return an {@link Details} object to represent the details of a single book.
     */
    public static Details fetchBookDetails(String requestUrl) {
        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e("Queryclass", "Error closing input stream", e);
        }

        // Extract relevant fields from the JSON response and create an {@link Event} object
        Details bookDetails = extractDetailFeatureFromJson(jsonResponse);

        // Return the {@link Event}
        return bookDetails;
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e("QueryUtils", "Error with creating URL ", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e("Queryclass", "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e("Queryclass", "Problem retrieving the books JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static List<Books> extractFeatureFromJson(String bookJSON) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(bookJSON)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding earthquakes to
        List<Books> books = new ArrayList<>();

        // Try to parse the JSON response string. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse = new JSONObject(bookJSON);

            // Extract the JSONArray associated with the key called "items",
            // which represents a list of features (or book).
            JSONArray bookArray = baseJsonResponse.getJSONArray("items");

            // For each book in the bookArray, create an {@link book} object
            for (int i = 0; i < bookArray.length(); i++) {

                // Get a single book at position i within the list of books
                JSONObject currentBook = bookArray.getJSONObject(i);

                String selfLink = currentBook.getString("selfLink");

                // Get the book info for that particular book
                JSONObject bookinfo = currentBook.getJSONObject("volumeInfo");

                // Extract the value for the key called "title"
                String bookname = bookinfo.getString("title");

                List<String> authornames;

                if (!bookinfo.has("authors"))
                    authornames = null;
                else {
                    // Extract the array for the key called "authors"
                    JSONArray arrayAuthornames = bookinfo.getJSONArray("authors");

                    authornames = new ArrayList<>();

                    // convert the array into a List of Strings.
                    for (int j = 0; j < arrayAuthornames.length(); j++) {

                        String authorname = arrayAuthornames.getString(j);
                        authornames.add(authorname);
                    }
                }
                String thumbnail, description;
                if (!bookinfo.has("imageLinks"))
                    thumbnail = null;
                else {// Extract the JSONObject for the key called "imageLinks"
                    JSONObject imagelinks = bookinfo.getJSONObject("imageLinks");

                    // Extract the value for the key called "thumbnail"
                    thumbnail = imagelinks.getString("thumbnail");
                }

                if (!bookinfo.has("description"))
                    description = null;
                else {
                    // Extract the value for the key called "description"
                    description = bookinfo.getString("description");
                }

                double bookRating;
                if (!bookinfo.has("averageRating"))
                    bookRating = 3.5;
                else
                    bookRating = bookinfo.getDouble("averageRating");

                // create new Book object using constructor
                Books book = new Books(selfLink, bookname, authornames, thumbnail, description, bookRating);

                // Add the new {@link book} to the list of books.
                books.add(book);
            }
        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("Queryclass", "Problem parsing the book JSON results", e);
        }
        return books;
    }

    private static Details extractDetailFeatureFromJson(String bookJSON) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(bookJSON)) {
            return null;
        }

        Details details = null;

        // Try to parse the JSON response string. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // Create a JSONObject from the JSON response string
            JSONObject currentBook = new JSONObject(bookJSON);

            // Get the book info for that particular book
            JSONObject bookinfo = currentBook.getJSONObject("volumeInfo");

            // Get the access info of the particular book
            JSONObject accessinfo = currentBook.getJSONObject("accessInfo");

            // Sample link of the book
            String sampleLink;

            if(!accessinfo.has("webReaderLink"))
                sampleLink = null;
            else
                sampleLink = accessinfo.getString("webReaderLink");

            // Extract the value for the key called "title"
            String bookname = bookinfo.getString("title");

            List<String> authornames;

            if (!bookinfo.has("authors"))
                authornames = null;
            else {
                // Extract the array for the key called "authors"
                JSONArray arrayAuthornames = bookinfo.getJSONArray("authors");

                authornames = new ArrayList<>();

                // convert the array into a List of Strings.
                for (int j = 0; j < arrayAuthornames.length(); j++) {

                    String authorname = arrayAuthornames.getString(j);
                    authornames.add(authorname);
                }
            }
            String img, description;
            if (!bookinfo.has("imageLinks"))
                img = null;
            else {// Extract the JSONObject for the key called "imageLinks"
                JSONObject imagelinks = bookinfo.getJSONObject("imageLinks");

                if(imagelinks.has("small")) {
                    // Extract the value for the key called "thumbnail"
                    img = imagelinks.getString("small");
                } else
                    img = imagelinks.getString("thumbnail");
            }

            if (!bookinfo.has("description"))
                description = null;
            else {
                // Extract the value for the key called "description"
                description = bookinfo.getString("description");
            }

            double bookRating;
            if (!bookinfo.has("averageRating"))
                bookRating = 3.5;
            else
                bookRating = bookinfo.getDouble("averageRating");

            int pageCount;
            String publishedYear, infoLink;
            if (!bookinfo.has("publishedDate"))
                publishedYear = null;
            else
                publishedYear = bookinfo.getString("publishedDate");

            if (!bookinfo.has("pageCount"))
                pageCount = 0;
            else
                pageCount = bookinfo.getInt("pageCount");

            if(!bookinfo.has("infoLink"))
                infoLink = bookinfo.getString("previewLink");
            else
                infoLink = bookinfo.getString("infoLink");

            // create new Detalis of book object using constructor
            details = new Details(bookname, authornames, img, description, bookRating, pageCount, publishedYear, infoLink, sampleLink);

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("Queryclass", "Problem parsing the book JSON results", e);
        }
        return details;
    }
}

package com.olgazelenko.doordash;

import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Scanner;

import okhttp3.mockwebserver.MockResponse;
import okio.BufferedSource;
import okio.Okio;

public class RestServiceTestHelper {
    public static String convertStreamToString(InputStream is) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        reader.close();
        return sb.toString();
    }

    public static String getStringFromFile(Context context, String filePath) throws Exception {
        final InputStream stream = context.getResources().getAssets().open(filePath);

        String ret = convertStreamToString(stream);
        //Make sure you close all streams.
        stream.close();
        return ret;
    }

    public static String readFile(ClassLoader classLoader, String fileName) {
        String result = "";

        try {
            URL resource = classLoader.getResource(fileName);
            File f = new File(resource.toURI());
            Scanner in = new Scanner(new FileReader(f));
            StringBuilder b = new StringBuilder();
            while (in.hasNextLine()) {
                b.append(in.nextLine());
            }
            result = b.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        return result;
    }

    public static String enqueueResponse(Context context, String filePath, Map<String, String> headers) throws Exception {
        final InputStream stream = context.getResources().getAssets().open(filePath);
        BufferedSource source = Okio.buffer(Okio.source(stream));
        MockResponse mockResponse = new MockResponse();
        for (Map.Entry<String, String> header : headers.entrySet()) {
            mockResponse.addHeader(header.getKey(), header.getValue());
        }
        return source.readString(StandardCharsets.UTF_8);
    }
}

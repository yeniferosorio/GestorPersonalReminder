package com.example.myapplication.config;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Util {
    static File file;

    static void saveProperties(Properties p) throws IOException {
        FileOutputStream fr = new FileOutputStream(file);
        p.store(fr, "Properties");
        fr.close();
        System.out.println("After saving properties: " + p);
    }

    public static String getProperty(String key, Context context) throws IOException {
        Properties properties = new Properties();

        AssetManager assetManager = context.getAssets();
        InputStream inputStream = assetManager.open("config.properties");
        properties.load(inputStream);
        return properties.getProperty(key);

    }

    public static void setProperty(String key, String data, Context context) throws IOException {

        Properties properties = new Properties();
        AssetManager assetManager = context.getAssets();
        System.out.println("guardar '" + data + "' en: " + key);

        InputStream inputStream = assetManager.open("config.properties");
        properties.load(inputStream);
        properties.setProperty(key, data);
        properties.store(new FileOutputStream("C:\\Games\\config.properties"),null);



    }
}
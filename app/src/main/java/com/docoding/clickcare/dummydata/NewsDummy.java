package com.docoding.clickcare.dummydata;

import com.docoding.clickcare.R;
import com.docoding.clickcare.model.NewsModel;

import java.util.ArrayList;

public class NewsDummy {

    private static String[] newsTitle = {
            "6 Buah-buahan untuk penderita COVID-19",
            "Suplemen yang cocok di minum saat puasa",
            "Tips pola hidup sehat bagi lansia"
    };

    private static String[] newsDate = {
            "10 April",
            "11 Maret",
            "20 Mei"
    };

    private static String[] newsTime = {
            "10:20 AM",
            "14:23 PM",
            "09:10 AM"
    };

    private static int[] newsImages = {
            R.drawable.buah_1,
            R.drawable.health_supplement,
            R.drawable.health_food
    };


    public static ArrayList<NewsModel> ListData() {
        ArrayList<NewsModel> list = new ArrayList<>();
        for (int position = 0; position < newsTitle.length; position++) {
            NewsModel news = new NewsModel();
            news.setTitle(newsTitle[position]);
            news.setTime(newsTime[position]);
            news.setDate(newsDate[position]);
            news.setPhoto(newsImages[position]);
            list.add(news);
        }

        return list;
    }
}

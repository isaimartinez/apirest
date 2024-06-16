package com.imv.ec.apirest.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class WebScrapingService {

    public String scrapeWebsite(String url) {
        try {
            Document doc = Jsoup.connect(url).get();
            StringBuilder result = new StringBuilder();
            Elements images = doc.select("img[src]");
            result.append("Images:\n");
            for (Element img : images) {
                result.append(img.attr("src")).append("\n");
            }
            result.append("\nText Content:\n");
            result.append(doc.text());
            return result.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return "Error occurred while scraping the website.";
        }
    }
}

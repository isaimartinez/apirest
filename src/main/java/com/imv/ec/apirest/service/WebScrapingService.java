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
            result.append("<h1>Images:</h1>");
            for (Element img : images) {
                String src = img.attr("src");
                result.append("<img src=\"").append(src).append("\" alt=\"Image\"><br>");
            }
            result.append("<hr>");
            result.append("<h1>Text Content:</h1>");
            result.append("<p>").append(doc.text()).append("</p>");
            return result.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return "<p>Error occurred while scraping the website.</p>";
        }
    }
}

package com.imv.ec.apirest.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class WebScrapingService {

    @Autowired
    private AIService aiService;

    public Mono<String> scrapeWebsite(String url) {
        try {
            Document doc = Jsoup.connect(url).get();
            StringBuilder result = new StringBuilder();
            List<Mono<String>> aiTasks = new ArrayList<>();

            Elements images = doc.select("img[src^=//upload.wikimedia.org]");
            result.append("<h1>Images and Descriptions:</h1>");
            for (Element img : images) {
                String src = img.attr("src");
                    result.append("<img src=\"").append(src).append("\" alt=\"Image\"><br>");
                    aiTasks.add(aiService.describeImage(src));
            }

            String textContent = doc.text();
            String shortTextContent = textContent.length() > 400 ? textContent.substring(0, 400) : textContent;
            aiTasks.add(aiService.summarizeText(shortTextContent));

            return Mono.zip(aiTasks, responses -> {
                for (Object response : responses) {
                    result.append("<p>").append(response.toString()).append("</p>");
                }
                return result.toString();
            });
        } catch (IOException e) {
            e.printStackTrace();
            return Mono.just("<p>Error occurred while scraping the website.</p>");
        }
    }
}

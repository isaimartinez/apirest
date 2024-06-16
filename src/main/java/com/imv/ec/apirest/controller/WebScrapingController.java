package com.imv.ec.apirest.controller;

import com.imv.ec.apirest.service.WebScrapingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebScrapingController {

    @Autowired
    private WebScrapingService webScrapingService;

    @GetMapping(value = "/scrape", produces = "text/html")
    public String scrapeWebsite(@RequestParam(name = "url") String url) {
        return webScrapingService.scrapeWebsite(url);
    }
}

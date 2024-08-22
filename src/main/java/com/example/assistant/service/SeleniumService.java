package com.example.assistant.service;

import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.stereotype.Service;

@Service
public class SeleniumService {

    @Lookup
    public WebDriver getDriver(){
        return null;
    }


}

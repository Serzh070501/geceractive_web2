package com.example.generactive_web2.mock;




import com.example.generactive_web2.model.Configuration;
import com.example.generactive_web2.model.Resolution;

import java.util.Random;

public final class ConfigurationMock {

    public static Configuration getConfiguration() {
        return new Configuration(Resolution.values()[new Random().nextInt(Resolution.values().length - 1)]);
    }

    private ConfigurationMock() {

    }
}
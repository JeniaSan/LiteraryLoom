package com.polishuchenko.bookstore.config;

import org.testcontainers.containers.MySQLContainer;

public class CustomMySqlContainer extends MySQLContainer<CustomMySqlContainer> {
    private static final String DB_IMAGE = "mysql:8";
    private static final String DB_URL = "TEST_DB_URL";
    private static final String DB_USERNAME = "TEST_DB_USERNAME";
    private static final String DB_PASSWORD = "TEST_DB_PASSWORD";
    private static CustomMySqlContainer mySqlContainer;

    private CustomMySqlContainer() {
        super(DB_IMAGE);
    }

    public static synchronized CustomMySqlContainer getInstance() {
        if (mySqlContainer == null) {
            mySqlContainer = new CustomMySqlContainer();
        }
        return mySqlContainer;
    }

    @Override
    public void start() {
        super.start();
        System.setProperty(DB_URL, mySqlContainer.getJdbcUrl());
        System.setProperty(DB_USERNAME, mySqlContainer.getJdbcUrl());
        System.setProperty(DB_PASSWORD, mySqlContainer.getJdbcUrl());
    }

    @Override
    public void stop() {
    }
}

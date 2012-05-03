package ro.utcluj.ecsb.utils;

import org.apache.log4j.PropertyConfigurator;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Properties;

public class Utils {
    private Utils() {
    }

    public static Properties loadConfiguration(String propertiesFile) throws IOException {
        Properties props = new Properties();
        BufferedReader in = null;
        try {
            in = new BufferedReader(new FileReader(propertiesFile));
            props.load(in);
        } finally {
            if (in != null) {
                in.close();
            }
        }
        return props;
    }

    public static void initLogger() {
        System.setProperty("logfile.name", "results_" + new Timestamp(new Date().getTime()));
        PropertyConfigurator.configure("log4j.properties");
    }

    public static String propertiesToString(Properties properties) {
        StringBuilder propertiesStringBuilder = new StringBuilder("\n");
        for (String property : properties.stringPropertyNames()) {
            propertiesStringBuilder.append(property).append('=').append(properties.getProperty(property)).append('\n');
        }
        return propertiesStringBuilder.toString();
    }
}

package ro.utcluj.ecsb.utils;

import org.apache.log4j.PropertyConfigurator;
import ro.utcluj.ecsb.ECSB;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.Properties;

public class EcsbUtils {
    private EcsbUtils() {
    }

    public static Properties loadConfiguration(URL propertiesFile) throws IOException {
        Properties props = new Properties();
        BufferedReader in = null;
        try {
            in = new BufferedReader(new FileReader(propertiesFile.getPath()));
            props.load(in);
        } finally {
            if (in != null) {
                in.close();
            }
        }
        return props;
    }

    public static void initLogger(String configDir, String resultsFileName) {
        Locale usLocale = new Locale("en","US");
        Locale.setDefault(usLocale);

        System.setProperty("logfile.name", System.getProperty("user.dir") + "/results/" + resultsFileName);
        PropertyConfigurator.configure(configDir + "/log4j.properties");
    }

    public static void initLogger(String resultsFileName) {
        Locale usLocale = new Locale("en","US");
        Locale.setDefault(usLocale);

        System.setProperty("logfile.name", System.getProperty("user.dir") + "/results/" + resultsFileName);
        PropertyConfigurator.configure(ECSB.class.getResource("log4j.properties"));
    }

    public static String propertiesToString(Properties properties) {
        StringBuilder propertiesStringBuilder = new StringBuilder("\n");
        for (String property : properties.stringPropertyNames()) {
            if (!"result_file".equals(property)) {
                propertiesStringBuilder.append(property).append('=').append(properties.getProperty(property)).append('\n');
            }
        }
        return propertiesStringBuilder.toString();
    }
}

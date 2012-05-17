package ro.utcluj.ecsb.utils;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.log4j.PropertyConfigurator;
import ro.utcluj.ecsb.ECSB;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Locale;
import java.util.Properties;

public class EcsbUtils {
    private EcsbUtils() {
    }

    public static Properties loadConfiguration(boolean distributed, String propertiesPath) throws IOException {
        Properties props = new Properties();
        BufferedReader in = null;
        try {
            if (!distributed){
                in = new BufferedReader(new FileReader(propertiesPath));
                props.load(in);
            } else{
                Path path = new Path(propertiesPath);
                FileSystem fs = FileSystem.get(path.toUri(), new Configuration());
                props.load(fs.open(path));
            }
        } finally {
            if (in != null) {
                in.close();
            }
        }
        return props;
    }

    public static void initLogger(boolean distributed, String resultsFileName) throws IOException {
        Locale usLocale = new Locale("en","US");
        Locale.setDefault(usLocale);

        System.setProperty("logfile.name","./" + resultsFileName);

        Properties logProps = new Properties();
        if (!distributed){
            BufferedReader in = new BufferedReader(new FileReader(ECSB.CONF_PATH + "log4j.properties"));
            logProps.load(in);
        } else{
            Path path = new Path(ECSB.CONF_PATH + "log4j.properties");
            FileSystem fs = FileSystem.get(path.toUri(), new Configuration());
            logProps.load(fs.open(path));
        }
        PropertyConfigurator.configure(logProps);
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

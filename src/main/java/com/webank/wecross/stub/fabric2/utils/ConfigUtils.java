package com.webank.wecross.stub.fabric2.utils;

import static com.webank.wecross.exception.WeCrossException.ErrorCode.FIELD_MISSING;

import com.moandjiezana.toml.Toml;
import com.webank.wecross.exception.WeCrossException;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

public class ConfigUtils {

    public static final Logger logger = LoggerFactory.getLogger(ConfigUtils.class);

    public static Toml getToml(String fileName) throws WeCrossException {
        try {
            PathMatchingResourcePatternResolver resolver =
                    new PathMatchingResourcePatternResolver();
            return new Toml().read(resolver.getResource(fileName).getInputStream());
        } catch (IllegalStateException e) {
            throw new WeCrossException(
                    WeCrossException.ErrorCode.UNEXPECTED_CONFIG,
                    "Toml file " + fileName + "format error: " + e.getMessage());
        } catch (FileNotFoundException e) {
            throw new WeCrossException(
                    WeCrossException.ErrorCode.DIR_NOT_EXISTS,
                    "Toml file " + fileName + "not found: " + e.getMessage());
        } catch (Exception e) {
            throw new WeCrossException(
                    WeCrossException.ErrorCode.INTERNAL_ERROR,
                    "Something wrong with parse " + fileName + ": " + e.getMessage());
        }
    }

    public static String classpath2Absolute(String fileName) throws WeCrossException {
        try {
            PathMatchingResourcePatternResolver resolver =
                    new PathMatchingResourcePatternResolver();
            return resolver.getResource(fileName).getFile().getAbsolutePath();
        } catch (Exception e) {
            throw new WeCrossException(
                    WeCrossException.ErrorCode.INTERNAL_ERROR,
                    "Something wrong with parse " + fileName + ": " + e.getMessage());
        }
    }

    public static Map<String, Object> getTomlMap(String fileName) throws WeCrossException {
        return getToml(fileName).toMap();
    }

    // Check if the file exists or not
    public static boolean fileIsExists(String path) {
        try {
            PathMatchingResourcePatternResolver resolver_temp =
                    new PathMatchingResourcePatternResolver();
            resolver_temp.getResource(path).getFile();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static boolean parseBoolean(Toml toml, String key, boolean defaultReturn) {
        Boolean res = toml.getBoolean(key);

        if (res == null) {
            logger.info("{} has not set, default to {}", key, defaultReturn);
            return defaultReturn;
        }
        return res;
    }

    public static int parseInt(Toml toml, String key, int defaultReturn) {
        Long res = toml.getLong(key);

        if (res == null) {
            logger.info(key + " has not set, default to {}", defaultReturn);
            return defaultReturn;
        }
        return res.intValue();
    }

    public static int parseInt(Toml toml, String key) throws WeCrossException {
        Long res = toml.getLong(key);

        if (res == null) {
            String errorMessage = "'" + key + "' item not found";
            throw new WeCrossException(FIELD_MISSING, errorMessage);
        }
        return res.intValue();
    }

    public static long parseLong(Toml toml, String key, long defaultReturn) {
        Long res = toml.getLong(key);

        if (res == null) {
            logger.info(key + " has not set, default to {}", defaultReturn);
            return defaultReturn;
        }
        return res.longValue();
    }

    public static String parseString(Toml toml, String key, String defaultReturn) {
        try {
            return parseString(toml, key);
        } catch (WeCrossException e) {
            return defaultReturn;
        }
    }

    public static String parseString(Toml toml, String key) throws WeCrossException {
        String res = toml.getString(key);

        if (res == null) {
            String errorMessage = "\"" + key + "\" item not found";
            throw new WeCrossException(FIELD_MISSING, errorMessage);
        }
        return res;
    }

    public static String parseString(Map<String, String> map, String key) throws WeCrossException {
        String res = map.get(key);

        if (res == null) {
            String errorMessage = "\"" + key + "\" item not found";
            throw new WeCrossException(FIELD_MISSING, errorMessage);
        }
        return res;
    }

    public static String parseStringBase(Map<String, Object> map, String key)
            throws WeCrossException {
        @SuppressWarnings("unchecked")
        String res = (String) map.get(key);

        if (res == null) {
            String errorMessage = "\"" + key + "\" item not found";
            throw new WeCrossException(FIELD_MISSING, errorMessage);
        }
        return res;
    }

    public static List<String> parseStringList(Map<String, Object> map, String key)
            throws WeCrossException {
        @SuppressWarnings("unchecked")
        List<String> res = (List<String>) map.get(key);

        if (res == null) {
            String errorMessage = "\"" + key + "\" item illegal";
            throw new WeCrossException(FIELD_MISSING, errorMessage);
        }
        return res;
    }

    public static Map<String, String> parseMapBase(Map<String, Object> map, String key)
            throws WeCrossException {
        @SuppressWarnings("unchecked")
        Map<String, String> res = (Map<String, String>) map.get(key);

        if (res == null) {
            throw new WeCrossException(FIELD_MISSING, "'" + key + "' item not found");
        }
        return res;
    }
}

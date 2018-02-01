package nl.acidcats.teatimer.util;

import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * Created on 11/09/2017.
 */

@SuppressWarnings("unchecked")
public class BundleUtil {

    public static <T> T getArg(@Nullable Bundle savedInstanceState, @Nullable Bundle intentArgs, String key, T defaultValue) {
        T value = getArg(savedInstanceState, intentArgs, key);
        return (value == null) ? defaultValue : value;
    }

    public static <T> T getArg(@Nullable Bundle savedInstanceState, @Nullable Bundle intentArgs, String key) {
        if (savedInstanceState != null && savedInstanceState.containsKey(key)) {
            Object object = savedInstanceState.get(key);
            if (object != null) {
                return (T) savedInstanceState.get(key);
            }
        }

        if (intentArgs != null && intentArgs.containsKey(key)) {
            Object object = intentArgs.get(key);
            if (object != null) {
                return (T) intentArgs.get(key);
            }
        }

        return null;
    }

    public static <T> T getMandatoryArg(@Nullable Bundle savedInstanceState, @Nullable Bundle intentArgs, String key, T defaultValue) {
        T value = getMandatoryArg(savedInstanceState, intentArgs, key);
        return (value == null) ? defaultValue : value;
    }

    public static <T> T getMandatoryArg(@Nullable Bundle savedInstanceState, @Nullable Bundle intentArgs, String key) {
        T result = getArg(savedInstanceState, intentArgs, key);
        if (result != null) return result;

        throw new IllegalArgumentException("Missing mandatory argument with key: " + key);
    }

    public static <T> T getBundleValue(Bundle bundle, String key, T defaultValue) {
        T value = getBundleValue(bundle, key);
        return (value == null) ? defaultValue : value;
    }

    public static <T> T getBundleValue(Bundle bundle, String key) {
        if (bundle != null && bundle.containsKey(key)) {
            Object value = bundle.get(key);
            if (value != null) {
                return (T) value;
            }
        }

        return null;
    }

    public static <T> T getMandatoryBundleValue(Bundle bundle, String key, T defaultValue) {
        T value = getMandatoryBundleValue(bundle, key);
        return (value == null) ? defaultValue : value;
    }

    public static <T> T getMandatoryBundleValue(Bundle bundle, String key) {
        if (bundle != null && bundle.containsKey(key)) {
            Object value = bundle.get(key);
            if (value != null) {
                return (T) value;
            }
        }

        throw new IllegalArgumentException("Missing mandatory value in Bundle with key: " + key);
    }
}

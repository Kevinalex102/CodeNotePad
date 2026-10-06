package localization;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 *
 * @author kevin
 */
public class Localization {
    private static Locale locale = Locale.ENGLISH;
    private static ResourceBundle bundle = ResourceBundle.getBundle("resources.lang.messages", locale);

    private Localization() {}

    public static String get(String key) {
        return bundle.getString(key);
    }

    public static void setLocale(Locale newLocale) {
        locale = newLocale;
        bundle = ResourceBundle.getBundle(
                "resources.lang.messages", 
                locale, 
                ResourceBundle.Control.getNoFallbackControl(
                        ResourceBundle.Control.FORMAT_DEFAULT
                )
        );
    }
}
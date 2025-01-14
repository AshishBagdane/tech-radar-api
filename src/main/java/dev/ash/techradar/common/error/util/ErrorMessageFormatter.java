package dev.ash.techradar.common.error.util;

import lombok.experimental.UtilityClass;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * Utility class for formatting error messages.
 */
@UtilityClass
public class ErrorMessageFormatter {

    private static final String TEMPLATE_PATTERN = "\\{\\}";

    private static final String LIST_DELIMITER = ", ";

    /**
     * Formats a message with placeholder replacements.
     */
    public String format(String messageTemplate, Object... args) {
        if (messageTemplate == null) {
            return "";
        }

        String result = messageTemplate;
        for (Object arg : args) {
            result = result.replaceFirst(TEMPLATE_PATTERN,
                                         arg != null ? arg.toString() : "null");
        }
        return result;
    }

    /**
     * Formats validation errors into a readable string.
     */
    public String formatValidationErrors(Map<String, String> validationErrors) {
        return validationErrors.entrySet().stream()
            .map(entry -> String.format("%s: %s",
                                        StringUtils.capitalize(entry.getKey()),
                                        entry.getValue()))
            .collect(Collectors.joining(LIST_DELIMITER));
    }

    /**
     * Creates a comma-separated list from an array of items.
     */
    public String formatList(Object... items) {
        return String.join(LIST_DELIMITER,
                           java.util.Arrays.stream(items)
                               .map(Object::toString)
                               .collect(Collectors.toList()));
    }
}

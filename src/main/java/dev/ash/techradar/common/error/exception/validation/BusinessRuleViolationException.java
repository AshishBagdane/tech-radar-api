package dev.ash.techradar.common.error.exception.validation;

import dev.ash.techradar.common.error.core.ErrorCode;

import java.util.List;

/**
 * Exception thrown when a business rule validation fails.
 */
public class BusinessRuleViolationException extends ValidationException {

    public BusinessRuleViolationException(String rule, String reason) {
        super(
            ErrorCode.BUSINESS_RULE_VIOLATION,
            String.format("Business rule '%s' violated: %s", rule, reason),
            createErrorContext()
                .attribute("rule", rule)
                .attribute("reason", reason)
                .build()
        );
    }

    public BusinessRuleViolationException(String rule, String operation, List<String> violations) {
        super(
            ErrorCode.BUSINESS_RULE_VIOLATION,
            String.format("Business rule violations detected for operation '%s'", operation),
            createErrorContext()
                .attribute("rule", rule)
                .attribute("operation", operation)
                .attribute("violations", violations)
                .attribute("violationCount", violations.size())
                .build()
        );
    }

    public BusinessRuleViolationException(String entityType, String entityId,
                                          String rule, String condition) {
        super(
            ErrorCode.BUSINESS_RULE_VIOLATION,
            String.format("%s (ID: %s) violates business rule '%s': %s",
                          entityType, entityId, rule, condition),
            createErrorContext()
                .attribute("entityType", entityType)
                .attribute("entityId", entityId)
                .attribute("rule", rule)
                .attribute("condition", condition)
                .build()
        );
    }
}

package dev.ash.techradar.common.error.exception.system;

import dev.ash.techradar.common.error.core.ErrorCode;
import org.springframework.dao.DataAccessException;

/**
 * Exception thrown when database operations fail.
 */
public class DatabaseException extends SystemException {

    public DatabaseException(String operation, DataAccessException cause) {
        super(
            ErrorCode.DATABASE_ERROR,
            cause,
            createErrorContext()
                .attribute("operation", operation)
                .attribute("errorType", cause.getClass().getSimpleName())
                .attribute("sqlState", extractSqlState(cause))
                .attribute("errorCode", extractErrorCode(cause))
                .build()
        );
    }

    public DatabaseException(String operation, String entity, String action, String reason) {
        super(
            ErrorCode.DATABASE_ERROR,
            String.format("Database operation failed while %s %s: %s", action, entity, reason),
            createErrorContext()
                .attribute("operation", operation)
                .attribute("entity", entity)
                .attribute("action", action)
                .attribute("reason", reason)
                .build()
        );
    }

    private static String extractSqlState(DataAccessException ex) {
        try {
            return ex.getMostSpecificCause().getMessage();
        } catch (Exception e) {
            return "Unknown SQL State";
        }
    }

    private static String extractErrorCode(DataAccessException ex) {
        try {
            return ex.getMostSpecificCause().getClass().getSimpleName();
        } catch (Exception e) {
            return "Unknown Error Code";
        }
    }
}

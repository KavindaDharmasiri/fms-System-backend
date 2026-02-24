/**
 * ALL RIGHT RESERVED By kavinda_d
 *
 * @AUTHOR : kavinda_d
 * @PROJECT : fms backend
 */
package net.com.fms_core.enums;

public enum ErrorCode {
    INVALID_INPUT("INVALID_INPUT", "Invalid input provided"),
    EMAIL_NOT_VALID("EMAIL_NOT_VALID", "Email should be valid"),
    PASSWORD_NOT_SECURE("PASSWORD_NOT_SECURE", "Password does not meet security requirements"),
    RESOURCE_NOT_FOUND("RESOURCE_NOT_FOUND", "The requested resource was not found"),
    DUPLICATE_ENTRY("DUPLICATE_ENTRY", "Duplicate entry detected"),
    UNAUTHORIZED("UNAUTHORIZED", "You are not authorized to access this resource"),
    FORBIDDEN("FORBIDDEN", "Access to this resource is forbidden"),
    INTERNAL_SERVER_ERROR("INTERNAL_SERVER_ERROR", "An unexpected error occurred"),
    DATABASE_ERROR("DATABASE_ERROR", "A database error occurred"),
    DEADLOCK_DETECTED("DEADLOCK_DETECTED", "A deadlock occurred. Please retry the operation"),
    EXTERNAL_SERVICE_UNAVAILABLE("EXTERNAL_SERVICE_UNAVAILABLE", "The external service is temporarily unavailable"),
    ENTITY_NOT_FOUND("ENTITY_NOT_FOUND", "The requested entity was not found"),
    UNKNOWN_PATH("UNKNOWN_PATH", "The requested path is unknown"),
    CONSTRAINT_VIOLATION("CONSTRAINT_VIOLATION", "A constraint violation occurred"),
    MALFORMED_REQUEST("MALFORMED_REQUEST", "The request body is malformed or cannot be read"),
    DATABASE_UNAVAILABLE("DATABASE_UNAVAILABLE", "The database is temporarily unavailable"),
    NOT_FOUND("NOT_FOUNT", "Data Not Found");
    private final String code;
    private final String defaultMessage;
    ErrorCode(String code, String defaultMessage) {
        this.code = code;
        this.defaultMessage = defaultMessage;
    }
    public String getCode() {
        return code;
    }
    public String getDefaultMessage() {
        return defaultMessage;
    }
}

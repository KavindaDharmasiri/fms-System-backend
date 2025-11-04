/**
 * ALL RIGHT RESERVED By kavinda_d
 *
 * @AUTHOR : kavinda_d
 * @PROJECT : fms backend
 */
package net.com.fms_core.enums;

public enum OperatorSymbol {
    EQUAL("==", "EQUAL"),
    NOT_EQUAL("!=", "NOT_EQUAL"),
    GREATER_THAN(">", "GREATERTHAN"),
    LESS_THAN("<", "LESSTHAN"),
    GREATER_THAN_OR_EQUAL(">=", "GREATERTHAN_OR_EQUAL"),
    LESS_THAN_OR_EQUAL("<=", "LESSTHAN_OR_EQUAL"),
    IN("IN", "IN"),
    NOT_IN("NOT IN", "NOT IN");
    private final String symbol;
    private final String code;
    OperatorSymbol(String symbol, String code) {
        this.symbol = symbol;
        this.code = code;
    }
    public String getSymbol() {
        return symbol;
    }
    public String getCode() {
        return code;
    }
    public static String getSymbolByCode(String code) {
        for (OperatorSymbol op : values()) {
            if (op.code.equalsIgnoreCase(code)) {
                return op.symbol;
            }
        }
        return code; // fallback to raw code if not found
    }
}

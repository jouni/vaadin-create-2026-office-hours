package com.example.ui;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.function.Function;

/**
 * Turns a list of rows into a CSV file (RFC 4180).
 *
 * - UTF-8 with a byte-order mark, so Excel opens accented names correctly
 * - CRLF line endings
 * - Fields with commas, quotes or line breaks are quoted ("Barcelona, ES")
 * - Text that starts with = + - @ is prefixed with ' so spreadsheet apps
 *   don't run it as a formula (CSV injection)
 */
public final class CsvExport {

    /** One column: its header and how to read the value from a row. */
    public record Column<T>(String header, Function<? super T, ?> value) {}

    public static <T> Column<T> column(String header, Function<? super T, ?> value) {
        return new Column<>(header, value);
    }

    public static <T> byte[] toBytes(List<? extends T> rows, List<Column<T>> columns) {
        StringBuilder csv = new StringBuilder("\uFEFF");
        appendRow(csv, columns.stream().<Object>map(Column::header).toList());
        for (T row : rows) {
            appendRow(csv, columns.stream().<Object>map(c -> c.value().apply(row)).toList());
        }
        return csv.toString().getBytes(StandardCharsets.UTF_8);
    }

    private static void appendRow(StringBuilder csv, List<Object> cells) {
        for (int i = 0; i < cells.size(); i++) {
            if (i > 0) csv.append(',');
            csv.append(escape(cells.get(i)));
        }
        csv.append("\r\n");
    }

    static String escape(Object value) {
        if (value == null) return "";
        String text = value.toString();
        if (value instanceof CharSequence && !text.isEmpty()
                && "=+-@\t\r".indexOf(text.charAt(0)) >= 0) {
            text = "'" + text;
        }
        if (text.contains(",") || text.contains("\"") || text.contains("\n") || text.contains("\r")) {
            return '"' + text.replace("\"", "\"\"") + '"';
        }
        return text;
    }

    private CsvExport() {}
}

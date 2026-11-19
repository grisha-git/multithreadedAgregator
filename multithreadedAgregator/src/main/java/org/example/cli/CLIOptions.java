package org.example.cli;

import java.util.List;

public class CLIOptions {
    private List<String> apis;    // Какие API опрашивать (например: job, currency)
    private String format;        // Формат файла: json или csv
    private String mode;          // Режим: create (новый) или append (дозапись)

    public CLIOptions(List<String> apis, String format, String mode) {
        this.apis = apis;
        this.format = format;
        this.mode = mode;
    }

    public List<String> getApis() { return apis; }
    public String getFormat() { return format; }
    public String getMode() { return mode; }
}
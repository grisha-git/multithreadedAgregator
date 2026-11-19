package org.example.cli;

import java.util.List;

public class CLIOptions {
    private List<String> apis;
    private String format;
    private String mode;

    public CLIOptions(List<String> apis, String format, String mode) {
        this.apis = apis;
        this.format = format;
        this.mode = mode;
    }

    public List<String> getApis() { return apis; }
    public String getFormat() { return format; }
    public String getMode() { return mode; }
}
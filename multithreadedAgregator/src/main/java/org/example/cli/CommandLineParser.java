package org.example.cli;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandLineParser {
    public static CLIOptions parse(String[] args) {
        List<String> apis = new ArrayList<>();
        String format = "json";
        String mode = "create";

        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "--apis":
                    if (i + 1 < args.length) {
                        String[] apiArray = args[i + 1].split(",");
                        apis.addAll(Arrays.asList(apiArray));
                        i++;
                    }
                    break;
                case "--format":
                    if (i + 1 < args.length) {
                        format = args[i + 1];
                        i++;
                    }
                    break;
                case "--mode":
                    if (i + 1 < args.length) {
                        mode = args[i + 1];
                        i++;
                    }
                    break;
                default:
                    break;
            }
        }

        if (apis.isEmpty()) {
            throw new IllegalArgumentException("Empty parameters");
        }

        return new CLIOptions(apis, format, mode);
    }
}

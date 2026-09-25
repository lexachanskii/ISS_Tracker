package com.alex;

import com.alex.controller.Application;
import com.alex.data.common.ApiConfig;
import com.alex.parser.JsonParser;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            if (args.length < 3) {
                System.out.println("Usage: <config.json> <format> <outputFile>");
                return;
            }

            Application app = new Application();

            ApiConfig config = JsonParser.deserialize(args[0], ApiConfig.class);
            if (args.length >= 4 && args[3].equals("auto")) {
                app.run(config, args[1], args[2]);
            } else {
                app.runInteractive(config,args[1], args[2]);
            }

        } catch (RuntimeException | IOException e) {

            System.out.println(e.getMessage());
        }

    }
}

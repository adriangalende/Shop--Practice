package arteco.valen.springFirstTry;

import org.apache.commons.cli.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Console {
    public static void main(String[] args) throws IOException {
        BufferedReader buf = new BufferedReader(new InputStreamReader(System.in));
        String line;

        System.out.println("Welcome to Lanistae Shop Manager: Who are you?");
        Cli cli=new Cli();
        do {
             line = buf.readLine();
             cli.parse(new String[]{line});

        } while (line != null && !"exit".equals(line));
    }



    }


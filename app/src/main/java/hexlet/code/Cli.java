package hexlet.code;

import picocli.CommandLine.Command;

import java.util.concurrent.Callable;

@Command(name = "gendiff", mixinStandardHelpOptions = true, version = "0.0.1",
        description = "Compares two configuration files and shows a difference.")
public class Cli implements Callable<Integer> {
    @Override
    public Integer call() {
        return 0;
    }
}
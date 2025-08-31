package hexlet.code;

import picocli.CommandLine.Parameters;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.util.concurrent.Callable;

@Command(name = "gendiff", mixinStandardHelpOptions = true, version = "0.0.1",
        description = "Compares two configuration files and shows a difference.")
public class Cli implements Callable<String> {
    @Parameters(index = "0", description = "path to first file")
    private String filepath1;

    @Parameters(index = "1", description = "path to second file")
    private String filepath2;

    @Option(names = {"-f", "--format"}, description = "output format [default: stylish]", hideParamSyntax = true)
    private String format = "stylish";

    @Override
    public String call() throws Exception {
        String diff = Differ.generate(filepath1, filepath2);

        System.out.println(diff);

        return diff;
    }
}
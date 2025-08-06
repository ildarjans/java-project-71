package hexlet.code;

import com.fasterxml.jackson.core.type.TypeReference;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;
import java.util.concurrent.Callable;

@Command(name = "gendiff", mixinStandardHelpOptions = true, version = "0.0.1",
        description = "Compares two configuration files and shows a difference.")
public class Cli implements Callable<Integer> {
    @Parameters(index = "0", description = "path to first file")
    private String filepath1;

    @Parameters(index = "1", description = "path to second file")
    private String filepath2;

    @Option(names = {"-f", "--format"}, description = "output format [default: stylish]", hideParamSyntax = true)
    private String format = "stylish";

    @Override
    public Integer call() throws Exception {
        Path path1 = Paths.get(filepath1).toAbsolutePath().normalize();
        Path path2 = Paths.get(filepath2).toAbsolutePath().normalize();

        if (!Files.exists(path1) || !Files.exists(path2)) {
            throw new Exception("File does not exist");
        }


        String content1 = Files.readString(path1);
        String content2 = Files.readString(path2);
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> map1 = objectMapper.readValue(content1, new TypeReference<Map<String,Object>>(){});
        Map<String, Object> map2 = objectMapper.readValue(content2, new TypeReference<Map<String,Object>>(){});

        return 0;
    }
}
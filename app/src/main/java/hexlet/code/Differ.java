package hexlet.code;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Differ {
    private enum DiffType {
        SAME,
        ADDED,
        REMOVED,
        UPDATED,
    }

    public static Map<String, Object> getDiffType(Object source, Object target) {
        Map<String, Object> result = new HashMap<>();

        if (source == null && target != null) {
            result.put("newValue" ,target);
            result.put("type" ,DiffType.ADDED);

            return result;
        }

        if (source != null && target == null) {
            result.put("oldValue" ,source);
            result.put("type" ,DiffType.REMOVED);

            return result;
        }

        if (source.equals(target)) {

            result.put("oldValue", source);
            result.put("newValue", target);
            result.put("type", DiffType.UPDATED);

            return result;
        }

        result.put("oldValue", source);
        result.put("newValue", target);
        result.put("type", DiffType.SAME);

        return result;
    }


    public static String getDiffValue(String key, Map<String, Object> diff, int depth) {
        var type = diff.get("type");
        String indent = " ".repeat(depth);

        switch (type) {
            case DiffType.ADDED -> {
                return String.format("%s+ %s: %s", indent, key, diff.get("newValue"));
            }
            case DiffType.REMOVED -> {
                return String.format("%s- %s: %s", indent, key, diff.get("oldValue"));
            }

            case DiffType.UPDATED -> {
                String oldValue = String.format("%s- %s: %s", indent, key, diff.get("oldValue"));
                String newValue = String.format("%s+ %s: %s", indent, key, diff.get("newValue"));

                return String.format("%s\n%s", oldValue, newValue);
            }
            case DiffType.SAME -> {
                return String.format("%s  %s: %s", indent, key, diff.get("oldValue"));
            }

            default -> throw new IllegalStateException("Unexpected value: " + type);
        }
    }

    public static String generate(String filepath1, String filepath2) throws Exception {
        Path path1 = Paths.get(filepath1).toAbsolutePath().normalize();
        Path path2 = Paths.get(filepath2).toAbsolutePath().normalize();


        if (!Files.exists(path1) || !Files.exists(path2)) {
            throw new Exception("File does not exist");
        }

        String content1 = Files.readString(path1);
        String content2 = Files.readString(path2);
        ObjectMapper objectMapper = new ObjectMapper();

        Map<String, Object> map1 = objectMapper.readValue(content1, new TypeReference<>(){});
        Map<String, Object> map2 = objectMapper.readValue(content2, new TypeReference<>(){});

        List<String> keys = Stream.concat(
                map1.keySet().stream(),
                map2.keySet().stream()
        ).collect(Collectors.toSet()).stream().sorted().toList();

        String result = keys.stream().map(k -> {
            var diff = Differ.getDiffType(map1.getOrDefault(k, null), map2.getOrDefault(k, null));
            return Differ.getDiffValue(k, diff, 4);
        }).collect(Collectors.joining("\n"));

        return String.format("{\n%s\n}", result);
    }
}

package io.github.technowoficial.springsmartspecification.path;

import java.util.List;

public class PathUtils {

    public static List<String> convertStringToList(String path) {
        if (path.startsWith("#{target.") || path.startsWith("${target.")) {
            path = path.substring("#{target.".length(), path.length() - 1);
        }
        return List.of(path.split("\\."));
    }

    public static String join(List<String> path, String separator) {
        return path.stream().reduce((a, b) -> a + separator + b).get();
    }

}

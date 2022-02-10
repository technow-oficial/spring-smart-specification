package io.github.technowoficial.springsmartspecification.path;

import java.util.List;
import java.util.Optional;

public class PathPartExtractor {

    private String string;
    private int start;
    private int end;

    public PathPartExtractor(String string) {
        this.string = string;
        this.start = 0;
        this.end = string.length();
    }

    public void ignore(PathPart part) {
        if (part.getIndex() == start) {
            start += part.getPart().length();
            string = string.substring(part.getPart().length());
        } else if (part.getIndex() + part.getPart().length() == end) {
            end -= part.getPart().length();
            string = string.substring(0, string.length() - part.getPart().length());
        } else {
            throw new IllegalArgumentException("Invalid path part index: " + part.getIndex());
        }
    }

    public void ignore(List<PathPart> parts) {
        for (PathPart part : parts) {
            ignore(part);
        }
    }

    public Optional<PathPart> tryExtract(String pattern) {
        PathPart part = null;
        if (string.toLowerCase().startsWith(pattern.toLowerCase())) {
            String partString = string.substring(0, pattern.length());
            part = PathPart.builder().pattern(pattern).part(partString).index(start).build();
        } else if (string.toLowerCase().endsWith(pattern.toLowerCase())) {
            String partString = string.substring(string.length() - pattern.length());
            part = PathPart.builder().pattern(pattern).part(partString).index(end - pattern.length()).build();
        }
        if (part != null) {
            ignore(part);
        }
        return Optional.ofNullable(part);
    }

    public boolean isEmpty() {
        return string == null || string.isEmpty();
    }

    public String getString() {
        return string;
    }

}

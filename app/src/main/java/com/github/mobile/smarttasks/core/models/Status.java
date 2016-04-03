package com.github.mobile.smarttasks.core.models;

public enum Status {
    UNRESOLVED("Unresolved"),
    RESOLVED("Resolved"),
    CANNOT_RESOLVE("Can't resolve");

    private final String name;

    private Status(String s) {
        name = s;
    }

    public boolean equalsName(String otherName) {
        return (otherName != null) && name.equals(otherName);
    }

    public String toString() {
        return this.name;
    }
}

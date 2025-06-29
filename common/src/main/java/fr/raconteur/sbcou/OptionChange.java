package fr.raconteur.sbcou;

public enum OptionChange {
    NEW("new"),
    MODIFIED("modified"),
    DELETED("deleted"),
    EQUAL("equal");

    private final String value;

    OptionChange(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

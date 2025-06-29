package fr.raconteur.sbcou;

public enum OptionHandlingState {
    UNKNOWN("unknown"),
    DEFAULT("default"),
    IGNORE("ignore"),
    NESTED_CASE("nested_case"),
    OVERRIDE("override");

    private final String value;

    OptionHandlingState(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

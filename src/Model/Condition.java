package Model;

public enum Condition {
    WORKING("working"),
    NOT_WORKING("notWorking");

    public final String value;

    Condition(String value) {
        this.value = value;
    }

//    public String getValue() {
//        return this.value;
//    }
}

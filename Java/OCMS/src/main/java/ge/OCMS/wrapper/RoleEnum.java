package ge.OCMS.wrapper;

import lombok.Getter;

@Getter
public enum RoleEnum {
    STUDENT("STUDENT"),
    INSTRUCTOR("INSTRUCTOR");

    private final String value;

    RoleEnum(String value) {
        this.value = value;
    }
}

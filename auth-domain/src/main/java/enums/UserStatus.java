package enums;

import lombok.Getter;

@Getter
public enum UserStatus {
    ACTIVE("계정 활성화");

    private final String description;

    UserStatus(String description) {
        this.description = description;
    }
}

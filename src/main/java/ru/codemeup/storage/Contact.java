package ru.codemeup.storage;

import lombok.*;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

@Getter
@Setter
@AllArgsConstructor
public class Contact {
    private String fullName;
    private String phoneNumber;
    private String email;

    public String toStringInFormat() {
        return MessageFormat.format("{0};{1};{2}", fullName, phoneNumber, email);
    }

    @Override
    public String toString() {
        return fullName + " | " +
                phoneNumber + " | " +
                email;
    }
}

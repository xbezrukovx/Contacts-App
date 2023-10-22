package ru.codemeup.storage;

import java.io.IOException;

public interface ContactsStorage {
    @StorageMethod(value = "add",
            args = {"full name", "phone number", "email"},
            help = "Adds a new user to your contacts."
    )
    boolean add(String fullName, String phoneNumber, String email);
    @StorageMethod(value = "print",
            help = "Displays actual contacts. NOTICE: Contacts might be unsaved."
    )
    void printContacts();
    @StorageMethod(value = "save",
            help = "Saves contacts into output file."
    )
    boolean save() throws IOException;
    @StorageMethod(value = "delete",
            args = {"email"},
            help = "Deletes contacts by email address."
    )
    boolean deleteByEmail(String email);
}

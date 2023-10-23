package ru.codemeup.storage;

import java.io.IOException;

public interface ContactsStorage {
    @StorageMethod(value = "add",
            args = {"full name", "phone number", "email"},
            help = "Adds a new user to your contacts."
    )
    boolean add(String fullName, String phoneNumber, String email);
    @StorageMethod(value = "print",
            help = "Displays the current list of contacts. ATTENTION. Contacts may be unsaved."
    )
    void printContacts();
    @StorageMethod(value = "save",
            help = "Saves contacts to an output file."
    )
    boolean save() throws IOException;
    @StorageMethod(value = "delete",
            args = {"email"},
            help = "Deletes contacts by email address."
    )
    boolean deleteByEmail(String email);
}

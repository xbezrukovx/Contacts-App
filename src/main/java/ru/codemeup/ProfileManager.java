package ru.codemeup;

import org.springframework.stereotype.Component;
import ru.codemeup.storage.ContactsStorage;

@Component
public class ProfileManager {
    private final ContactsStorage contactsStorage;

    public ProfileManager(ContactsStorage contactsStorage) {
        this.contactsStorage = contactsStorage;
    }

    public ContactsStorage getContactsStorage(){
        return contactsStorage;
    }
}

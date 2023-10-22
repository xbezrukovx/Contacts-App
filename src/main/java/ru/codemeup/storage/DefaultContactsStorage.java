package ru.codemeup.storage;

import org.springframework.beans.factory.annotation.Value;

import java.io.*;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class DefaultContactsStorage implements ContactsStorage{
    private final List<Contact> contacts;
    private final String saveFilePath;

    public DefaultContactsStorage(
            @Value("${app.contacts.saveFile}") String saveFilePath
    ) {
        this.saveFilePath = saveFilePath;
        contacts = new ArrayList<>();
    }

    @Override
    public void printContacts() {
        if (contacts.size() == 0) {
            System.out.println("Contacts list is empty.");
        }
        contacts.stream()
                .map(Contact::toString)
                .forEach(System.out::println);
    }

    @Override
    public boolean add(String fullName, String phoneNumber, String email) throws IllegalArgumentException, NullPointerException{
        if(!isValidEmail(email)) throw new IllegalArgumentException("Incorrect email format.");
        if(!isValidEmail(phoneNumber)) throw new IllegalArgumentException("Incorrect phone number format.");
        if(fullName == null || fullName.isEmpty()) throw new NullPointerException("Full name is null or empty");
        contacts.add(new Contact(fullName, phoneNumber, email));
        System.out.println(MessageFormat.format("Contact with name {0} was created",fullName));
        return true;
    }

    @Override
    public boolean deleteByEmail(String email){
        List<Contact> contactsToDelete = findByEmail(email);
        AtomicInteger countDeleted = new AtomicInteger();
        contactsToDelete.forEach(c -> {
            if (contacts.remove(c)) countDeleted.getAndIncrement();
        });
        System.out.println(MessageFormat.format("{0} contacts were deleted.",countDeleted));
        return true;
    }

    @Override
    public boolean save() throws IOException{
        File outputFile = new File(saveFilePath);
        outputFile.createNewFile();
        PrintWriter writer = new PrintWriter(outputFile);
        contacts.forEach(c -> writer.write(c.toStringInFormat()+System.lineSeparator()));
        writer.flush();
        writer.close();
        System.out.println("File was saved.");
        return true;
    }

    private List<Contact> findByEmail(String email) {
        return contacts.stream()
                .filter(c -> c.getEmail().equals(email))
                .collect(Collectors.toList());
    }

    private boolean isValidEmail(String email) {
        String regexPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        return email.matches(regexPattern);
    }

    private boolean isValidPhoneNumber(String phoneNumber){
        String regexPattern = "^[+]\\d+$";
        return phoneNumber.matches(regexPattern);
    }
}

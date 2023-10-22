package ru.codemeup.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import ru.codemeup.storage.ContactsStorage;
import ru.codemeup.storage.DefaultContactsStorage;

import java.io.IOException;

@ComponentScan("ru.codemeup")
@Configuration
@Profile("default")
@PropertySources(
        value = {
                @PropertySource("classpath:application-default.properties")
        }
)
public class DefaultAppConfig {
    @Bean
    public ContactsStorage contactsStorage(@Value("${app.contacts.saveFile}") String saveFile) throws IOException {
        return new DefaultContactsStorage(saveFile);
    }
}

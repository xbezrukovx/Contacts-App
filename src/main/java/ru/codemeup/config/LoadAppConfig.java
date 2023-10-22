package ru.codemeup.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import ru.codemeup.storage.ContactsStorage;
import ru.codemeup.storage.LoadContactsStorage;

import java.io.IOException;

@ComponentScan("ru.codemeup")
@Configuration
@Profile("load")
@PropertySources(
        value = {
                @PropertySource("classpath:application-load.properties")
        }
)
public class LoadAppConfig {
    @Bean
    public ContactsStorage contactsStorage(
            @Value("${app.contacts.loadFile}") String loadFile,
            @Value("${app.contacts.saveFile}") String saveFile
    ) throws IOException{
        return new LoadContactsStorage(loadFile, saveFile);
    }
}

package ru.codemeup;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.codemeup.config.GeneralAppConfig;
import ru.codemeup.storage.ContactsStorage;
import ru.codemeup.storage.DefaultContactsStorage;
import ru.codemeup.storage.LoadContactsStorage;
import ru.codemeup.storage.StorageMethod;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

public class Main {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(GeneralAppConfig.class);
        ContactsStorage storage = context.getBean(ProfileManager.class).getContactsStorage();

        HashMap<String, Method> methods = (HashMap<String, Method>)
                Arrays.stream(stream(storage
                                .getClass()
                                .getInterfaces()
                        ).filter(i -> i.equals(ContactsStorage.class)
                                ).findFirst()
                                .get()
                                .getMethods())
                        .filter(m -> m.getAnnotation(StorageMethod.class) != null)
                        .collect(Collectors
                                .toMap(key -> key.getAnnotation(StorageMethod.class).value(), value -> value)
                        );


        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        greetings();
        if (storage instanceof LoadContactsStorage) System.out.println("Contacts were loaded from the file");
        while (true) {
            Method method = getMethod(reader, methods);
            if (method == null) continue;
            Object[] params = getParams(reader, method);
            executeMethod(method, storage, params);
        }
    }

    public static void greetings() {
        System.out.println("=================================");
        System.out.println("=     Application.Contacts      =");
        System.out.println("=       by Bezrukov Denis       =");
        System.out.println("=================================");
        System.out.println("Please enter the command \"help\" to see a list of available commands");
    }

    public static void executeMethod(Method method, Object instance, Object[] params) {
        try {
            method.invoke(instance, params);
        } catch (InvocationTargetException e) {
            Throwable root = e;
            while (e.getCause() != null && e.getCause() != root) {
                root = e.getCause();
            }
            StackTraceElement cause = stream(root.getStackTrace()).findFirst().get();
            if (cause.getClassName().equals(LoadContactsStorage.class.getName())
                    || cause.getClassName().equals(DefaultContactsStorage.class.getName())
            ) {
                System.out.println(root.getMessage() + " Try again.");
            } else {
                throw new RuntimeException(e);
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static Method getMethod(BufferedReader reader, HashMap<String, Method> methods) {
        try {
            System.out.print("Waiting for a new command: ");
            String line = reader.readLine().trim().toLowerCase();
            if (line.equals("help")) {
                help(methods.values().stream().toList());
                return null;
            } else if (!methods.containsKey(line)) {
                System.out.println("There is no such command. Please try again.");
                return null;
            } else {
                return methods.get(line);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Object[] getParams(BufferedReader reader, Method method) {
        String[] params = method.getAnnotation(StorageMethod.class).args();
        List<String> userParams = new ArrayList<>();
        for (String p : params) {
            System.out.print("Please, enter " + p + ": ");
            try {
                String line = reader.readLine().trim();
                userParams.add(line);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return userParams.toArray();
    }

    public static void help(List<Method> methods) {
        for (Method method : methods) {
            StorageMethod annotation = method.getAnnotation(StorageMethod.class);
            String help = annotation.help();
            String name = annotation.value();
            StringBuilder paramsBuilder = new StringBuilder();
            for (int i = 0; i < annotation.args().length; i++) {
                paramsBuilder.append(i + 1).append(". ").append(annotation.args()[i]);
                if (i < annotation.args().length - 1) paramsBuilder.append(", ");
            }
            String params = paramsBuilder.toString();
            String methodHelp = MessageFormat.format("{0} - {1} Input parameters: {2}.",
                    name,
                    help,
                    params.length() > 0 ? params : "without parameters."
            );
            System.out.println(methodHelp);
        }
    }
}
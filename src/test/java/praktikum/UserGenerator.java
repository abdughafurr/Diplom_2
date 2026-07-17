package praktikum;

import java.util.UUID;

public class UserGenerator {

    public static User getRandom() {
        String randomPart = UUID.randomUUID().toString().substring(0, 8);
        String email = "test-" + randomPart + "@yandex.ru";
        String password = "password-" + randomPart;
        String name = "user-" + randomPart;
        return new User(email, password, name);
    }
}
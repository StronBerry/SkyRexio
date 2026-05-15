package user;

import utils.PropertyReader;

public final class UserFactory {

    private UserFactory() {
    }

    public static User validUser() {
        return new User(
                PropertyReader.getProperty("skyrexio.user"),
                PropertyReader.getProperty("skyrexio.password")
        );
    }
}

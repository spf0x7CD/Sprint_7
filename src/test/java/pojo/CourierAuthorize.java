package pojo;

public class CourierAuthorize {
    private String login;
    private String password;

    public CourierAuthorize(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public CourierAuthorize() {
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return String.format(
                "pojo.CourierCreate{\nlogin='%s',\n password='%s'\n}",
                login, password
        );
    }
}

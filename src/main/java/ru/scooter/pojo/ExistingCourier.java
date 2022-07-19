package ru.scooter.pojo;

public class ExistingCourier {

    private String login;
    private String password;

    public ExistingCourier(String login, String password) {
        this.login = login;
        this.password = password;
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
        return "ExistingCourier{" +
                "login='" + login + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}

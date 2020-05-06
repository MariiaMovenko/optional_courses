package ua.nure.movenko.summaryTask4.entities;

import ua.nure.movenko.summaryTask4.enums.Role;
import ua.nure.movenko.summaryTask4.enums.UserStatus;

/**
 * User entity.
 *
 * @author M.Movenko
 */
public class User extends Entity {

    private String login;
    private String password;
    private Role role;
    private String firstName;
    private String lastName;
    private String email;
    private UserStatus status;
    private String avatarPath;
    
    public User() {}

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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
    
    public void setEmail(String email) {

        this.email= email;
    }
    
    public String getEmail() {
    	
    	return email;
    }

    public String getAvatarPath() {
        return avatarPath;
    }

    public void setAvatarPath(String avatarPath) {
        this.avatarPath = avatarPath;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + getId() + '\'' +
                "login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", role=" + role +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", status=" + status +
                ", avatarPath='" + avatarPath + '\'' +
                '}';
    }
}

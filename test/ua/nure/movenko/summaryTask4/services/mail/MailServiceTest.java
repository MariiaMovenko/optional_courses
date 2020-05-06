package ua.nure.movenko.summaryTask4.services.mail;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import ua.nure.movenko.summaryTask4.entities.User;
import ua.nure.movenko.summaryTask4.enums.Role;
import ua.nure.movenko.summaryTask4.enums.UserStatus;

import javax.mail.*;
import java.util.Properties;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class MailServiceTest {

    private final Properties mailProperties = getMailProperties();
    private final String messageTemplatePath = "C:\\Users\\nefre\\IdeaProjects\\SummaryTaskMariia\\src\\resources\\";

    @InjectMocks
    private final MailService mailService = spy(new MailService(mailProperties, messageTemplatePath));

    @Test
    public void shouldSendMailWithoutPassword() throws MessagingException {
        User userToSend = createUser(Role.STUDENT);
        mailService.sendMailWithoutPassw(userToSend);
    }

    @Test
    public void shouldSendMailWithPassword() throws MessagingException {
        User userToSend = createUser(Role.STUDENT);
        String password = "password";

        mailService.sendMailWithPassw(userToSend, password);
    }


    private Properties getMailProperties() {
        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.socketFactory.port", "465");
        properties.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.port", "465");
        properties.put("mail.user.name", "optionalcoursesteam@gmail.com");
        properties.put("mail.user.password", "summaryTaskMariia");
        return properties;
    }

    private User createUser(Role role) {
        User user = new User();
        user.setId(1);
        user.setLogin("login");
        user.setPassword("password");
        user.setRole(role);
        user.setLastName("Petrov");
        user.setFirstName("Ivan");
        user.setEmail("nko@gmail.com");
        user.setStatus(UserStatus.ACTIVE);
        return user;
    }
}

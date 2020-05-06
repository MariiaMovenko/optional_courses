package ua.nure.movenko.summaryTask4.services.mail;

import org.apache.log4j.Logger;
import ua.nure.movenko.summaryTask4.entities.User;
import ua.nure.movenko.summaryTask4.exception.SendingMailException;

import java.io.*;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * Mail Service entity
 *
 * @author M.Movenko
 */
public class MailService {

    private static final Logger LOG = Logger.getLogger(MailService.class);

    private final Properties mailProperties;
    private final String messageTemplatePath;

    private static final String USER_NAME_PROPERTY = "mail.user.name";
    private static final String USER_PASSWORD_PROPERTY = "mail.user.password";
    public static final String SUBJECT = "OPTIONAL COURSES MESSAGE";
    private static final String CONFIRM_REGISTRATION_TEMPLATE = "mail/registrationMessage.txt";
    private static final String RESTORE_PASSWORD_TEMPLATE = "mail/restorePasswordMessage.txt";
    private static final String CONFIRM_REGISTRATION_WITH_PASSWORD_TEMPLATE = "mail/registrationByAdminMessage.txt";


    public MailService(Properties mailProperties, String messageTemplatePath) {
        this.mailProperties = mailProperties;
        this.messageTemplatePath = messageTemplatePath;
    }

    /**
     * Sends an email with confirming registration link and generated password  to {@code newUser}
     *
     * @param password to be sent
     * @param newUser  user to whom an email should be sent
     * @throws MessagingException if message wasn't sent
     */
    public void sendMailWithPassw(User newUser, String password)
            throws MessagingException {
        Message msg = new MimeMessage(getSession());
        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(newUser.getEmail()));
        msg.setSubject(SUBJECT);
        String message = String.format(readFromFile(messageTemplatePath + CONFIRM_REGISTRATION_WITH_PASSWORD_TEMPLATE),
                newUser.getLogin(), password, newUser.getLogin(), newUser.getId());
        msg.setText(message);
        Transport.send(msg);
    }

    /**
     * Sends an email with  generated password  to {@code user}
     *
     * @param user     user to whom an email should be sent
     * @param password to be sent
     * @throws MessagingException if message wasn't sent
     */
    public void sendRestoredPassword(User user, String password)
            throws MessagingException {
        Message msg = new MimeMessage(getSession());
        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(user.getEmail()));
        msg.setSubject(SUBJECT);
        String message = String.format(readFromFile(messageTemplatePath + RESTORE_PASSWORD_TEMPLATE),
                user.getLogin(), password);
        msg.setText(message);
        Transport.send(msg);
    }

    /**
     * Sends an email with confirming registration link to {@code newUser}
     *
     * @param newUser user to whom an email should be sent
     * @throws MessagingException if message wasn't sent
     */
    public void sendMailWithoutPassw(User newUser)
            throws MessagingException {
        Message msg = new MimeMessage(getSession());
        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(newUser.getEmail()));
        msg.setSubject(SUBJECT);
        String message = String.format(readFromFile(messageTemplatePath +
                CONFIRM_REGISTRATION_TEMPLATE), newUser.getLogin(), newUser.getId());
        msg.setText(message);
        Transport.send(msg);
    }

    private Session getSession() {
        return Session.getDefaultInstance(mailProperties,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(mailProperties.getProperty(USER_NAME_PROPERTY),
                                mailProperties.getProperty(USER_PASSWORD_PROPERTY));
                    }
                });
    }

    private static String readFromFile(String sourceFileName) {
        StringBuilder temp = new StringBuilder();
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(
                new FileInputStream(sourceFileName), "Cp1251"))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                temp.append(line).append(System.lineSeparator());
            }
        } catch (IOException e) {
            LOG.error("Can't handle method ReadFromFile", e);
            throw new SendingMailException(e);
        }
        return temp.toString();
    }
}


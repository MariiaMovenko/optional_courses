package ua.nure.movenko.summaryTask4.web.listeners;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.xml.sax.SAXException;
import ua.nure.movenko.summaryTask4.db.transaction.TransactionManager;
import ua.nure.movenko.summaryTask4.db.dao.course.CourseDAOImpl;
import ua.nure.movenko.summaryTask4.db.dao.dictionary.TitleDictionaryDAOImpl;
import ua.nure.movenko.summaryTask4.db.dao.journal.JournalDAOImpl;
import ua.nure.movenko.summaryTask4.db.dao.theme.ThemeDAOImpl;
import ua.nure.movenko.summaryTask4.db.dao.user.UserDAOImpl;
import ua.nure.movenko.summaryTask4.db.transaction.TransactionManagerImpl;
import ua.nure.movenko.summaryTask4.exception.ApplicationException;
import ua.nure.movenko.summaryTask4.security.Authorization;
import ua.nure.movenko.summaryTask4.security.AuthorizationImpl;
import ua.nure.movenko.summaryTask4.services.mail.MailService;
import ua.nure.movenko.summaryTask4.services.course.CourseService;
import ua.nure.movenko.summaryTask4.services.course.CourseServiceImpl;
import ua.nure.movenko.summaryTask4.services.dictionary.*;
import ua.nure.movenko.summaryTask4.services.journal.JournalService;
import ua.nure.movenko.summaryTask4.services.journal.JournalServiceImpl;
import ua.nure.movenko.summaryTask4.services.pdf.PDFCreationService;
import ua.nure.movenko.summaryTask4.services.theme.ThemeService;
import ua.nure.movenko.summaryTask4.services.theme.ThemeServiceImpl;
import ua.nure.movenko.summaryTask4.services.user.UserService;
import ua.nure.movenko.summaryTask4.services.user.UserServiceImpl;
import ua.nure.movenko.summaryTask4.threads.UpdateStatusRunnable;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Context listener.
 *
 * @author M.Movenko
 */
public class ContextListener implements ServletContextListener {

    private static final Logger LOG = Logger.getLogger(ContextListener.class);
    private ScheduledExecutorService executorService;

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        ServletContext servletContext = servletContextEvent.getServletContext();
        initLog4J(servletContext);

        String dbSource = servletContext.getInitParameter("SOURCE_DB");
        DataSource ds = getDataSource(dbSource);

        TransactionManager transactionManager = new TransactionManagerImpl(ds);
        service(servletContext, transactionManager);
        Map<String, HttpSession> sessionMap = new HashMap<>();
        servletContext.setAttribute("sessionMap", sessionMap);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        executorService.shutdown();
        LOG.trace("context destroyed");
    }

    /**
     * Initializes log4j framework.
     *
     * @param servletContext Servlet context object
     */
    private void initLog4J(ServletContext servletContext) {
        log("Log4J initialization started");
        try {
            PropertyConfigurator.configure(
                    servletContext.getRealPath("WEB-INF/log4j.properties"));
            LOG.trace("Log4j has been initialized");
        } catch (Exception ex) {
            log("Cannot configure Log4j");
            ex.printStackTrace();
        }
        LOG.trace("Log4J initialization finished");
    }

    private void service(ServletContext servletContext, TransactionManager transactionManager) {

        String mailPropertiesName = servletContext.getInitParameter("MAIL_CONFIG");
        MailService mailService = getMailService(mailPropertiesName);
        UserService userService = new UserServiceImpl(transactionManager, new UserDAOImpl(), mailService);
        servletContext.setAttribute("USER_SERVICE", userService);
        CourseService courseService = new CourseServiceImpl(transactionManager, new CourseDAOImpl(),
                new TitleDictionaryDAOImpl());
        servletContext.setAttribute("COURSE_SERVICE", courseService);
        ThemeService themeService = new ThemeServiceImpl(transactionManager, new ThemeDAOImpl());
        servletContext.setAttribute("THEME_SERVICE", themeService);
        TitleDictionaryService dictionaryService = new TitleDictionaryServiceImpl(transactionManager,
                new TitleDictionaryDAOImpl());
        servletContext.setAttribute("DICTIONARY_SERVICE", dictionaryService);
        JournalService journalService = new JournalServiceImpl(transactionManager, new JournalDAOImpl());
        servletContext.setAttribute("JOURNAL_SERVICE", journalService);
        PDFCreationService pdfCreationService = new PDFCreationService(journalService);
        servletContext.setAttribute("PDF_SERVICE", pdfCreationService);

        String securityConfigName = servletContext.getInitParameter("SECURITY");
        setAuthorizationMap(servletContext, securityConfigName);
        Runnable run = new UpdateStatusRunnable(userService);
        executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(run, 0, 1, TimeUnit.DAYS);

    }

    private MailService getMailService(String mailPropertiesPath) {
        try {
            File mailPropertiesFile = new File(this.getClass().getResource(mailPropertiesPath).toURI().getPath());
            Properties mailProperties = new Properties();
            mailProperties.load(new FileReader(mailPropertiesFile));
            return new MailService(mailProperties, this.getClass().getResource("/").toURI().getPath());
        } catch (Exception ex) {
            LOG.error("Can't load authorization map file: '" + mailPropertiesPath + "'", ex);
            throw new ApplicationException("Can't load authorization map file: '" + mailPropertiesPath + "'");
        }
    }


    private DataSource getDataSource(String dsName) {
        DataSource dataSource = null;
        try {
            Context initContext = new InitialContext();
            Context envContext = (Context) initContext.lookup("java:/comp/env");
            dataSource = (DataSource) envContext.lookup(dsName);
            LOG.trace("DataSource is successfully received.");
        } catch (NamingException e) {
            LOG.error("Can't get DataSource ", e);
        }
        return dataSource;
    }

    private void setAuthorizationMap(ServletContext servletContext, String fileName) {
        try {
            File file = new File(this.getClass().getResource(fileName).toURI());
            Authorization authorizationMap = AuthorizationImpl.newInstance(file);
            servletContext.setAttribute("authorization", authorizationMap);
            LOG.trace("AuthorizationMap has been successfully loaded.");
        } catch (IOException | URISyntaxException | ParserConfigurationException | SAXException ex) {
            LOG.error("Can't load authorization map file: '" + fileName + "'", ex);
            throw new ApplicationException("Can't load authorization map file: '" + fileName + "'", ex);
        }
    }

    private void log(String msg) {
        System.out.println("[ContextListener] " + msg);
    }
}

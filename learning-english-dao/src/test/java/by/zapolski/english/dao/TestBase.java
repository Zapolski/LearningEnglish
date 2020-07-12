package by.zapolski.english.dao;

import by.zapolski.english.domain.*;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

class TestBase {

    SessionFactory sessionFactory;

    void testSessionFactorySetUp(){
        // setup the session factory
        Configuration configuration = new Configuration();
        configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
        configuration.setProperty("hibernate.connection.driver_class", "org.h2.Driver");
        configuration.setProperty("hibernate.connection.url", "jdbc:h2:mem:test");
        configuration.setProperty("hibernate.hbm2ddl.auto", "create");
        configuration.setProperty("hibernate.current_session_context_class", "thread");
        configuration.setProperty("show_sql", "true");

        configuration.addAnnotatedClass(Word.class);
        configuration.addAnnotatedClass(Rule.class);
        configuration.addAnnotatedClass(Resource.class);
        configuration.addAnnotatedClass(Language.class);
        configuration.addAnnotatedClass(Context.class);
        configuration.addAnnotatedClass(Translation.class);
        configuration.addAnnotatedClass(Phrase.class);

        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties()).build();
        sessionFactory = configuration.buildSessionFactory(serviceRegistry);
    }
}

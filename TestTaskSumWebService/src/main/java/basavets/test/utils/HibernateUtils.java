package basavets.test.utils;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.springframework.context.annotation.Bean;

@org.springframework.context.annotation.Configuration
public class HibernateUtils {

    @Bean(name = "entityManagerFactory")
    public SessionFactory getSessionFactory() {
        return new Configuration().configure().buildSessionFactory();
    }
}


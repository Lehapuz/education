import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class Singleton {

    public static SessionFactory factory;
    private Singleton() {
    }

    public static synchronized SessionFactory getSessionFactory() {

        if (factory == null) {
            factory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
        }
        return factory;
    }

    //public static SessionFactory getSessionFactory(){
    //    StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure("hibernate.cfg.xml").build();
    //    Metadata metadata = new MetadataSources(registry).getMetadataBuilder().build();
    //    SessionFactory sessionFactory = metadata.getSessionFactoryBuilder().build();
    //    return sessionFactory;
    //}
}

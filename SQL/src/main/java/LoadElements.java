import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class LoadElements {

    private static List<Subscription> subscriptions;
    private static List<Purchaselist> purchaselists;

    public static void loadElements() {
        try {
            Session session = Singleton.getSessionFactory().openSession();
            Transaction transaction = session.beginTransaction();

            String hql = "From " + Subscription.class.getSimpleName();
            subscriptions = session.createQuery(hql).getResultList();
            System.out.println(subscriptions.size());

            String hql1 = "From " + Purchaselist.class.getSimpleName();
            purchaselists = session.createQuery(hql1).getResultList();
            System.out.println(purchaselists.size());


            for (Subscription subscription : subscriptions) {
                for (Purchaselist purchaselist : purchaselists) {
                    Student student = subscription.getStudent();
                    Course course = subscription.getCourse();
                    if (student.getName().equals(purchaselist.getStudentName())
                            && course.getName().equals(purchaselist.getCourseName())) {

                        System.out.println(student.getId() + " - " + course.getId());

                        Linkedpurchaselist linkedpurchaselist = new Linkedpurchaselist();
                        Linkedpurchaselistkey linkedpurchaselistkey = new Linkedpurchaselistkey(subscription.getStudent().getId(),
                                subscription.getCourse().getId());
                        linkedpurchaselist.setLinkedpurchaselistkey(linkedpurchaselistkey);
                        session.save(linkedpurchaselist);
                    }
                }
            }

            transaction.commit();
            session.close();
            Singleton.getSessionFactory().close();

        } catch (Exception ex) {
            ex.printStackTrace();

        }
    }
}

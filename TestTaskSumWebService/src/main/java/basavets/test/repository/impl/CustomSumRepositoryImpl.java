package basavets.test.repository.impl;

import basavets.test.entity.Data;
import basavets.test.repository.CustomSumRepository;
import basavets.test.utils.HibernateUtils;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@AllArgsConstructor
public class CustomSumRepositoryImpl implements CustomSumRepository {

    private static final String NAME = "name";
    private final HibernateUtils hibernateUtils;

    @Override
    public void save(Data data) {
        try (Session session = hibernateUtils.getSessionFactory().getCurrentSession()) {
            session.beginTransaction();
            session.persist(data);
            session.getTransaction().commit();
        }
    }

    @Override
    public void delete(Data data) {
        try (Session session = hibernateUtils.getSessionFactory().getCurrentSession()) {
            session.beginTransaction();
            session.remove(data);
            session.getTransaction().commit();
        }
    }

    @Override
    public Optional<Data> findByName(String name) {
        Optional<Data> data;
        try (Session session = hibernateUtils.getSessionFactory().getCurrentSession()) {
            session.beginTransaction();
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Data> criteriaQuery = criteriaBuilder.createQuery(Data.class);
            Root<Data> dataRoot = criteriaQuery.from(Data.class);
            criteriaQuery.select(dataRoot).where(criteriaBuilder.equal(dataRoot.get(NAME), name));
            data = session.createQuery(criteriaQuery).getResultList().stream().findAny();
            session.getTransaction().commit();
        }
        return data;
    }
}

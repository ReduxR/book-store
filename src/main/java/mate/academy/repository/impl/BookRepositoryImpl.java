package mate.academy.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import java.util.List;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Book;
import mate.academy.repository.BookRepository;
import org.springframework.stereotype.Repository;

@Repository
public class BookRepositoryImpl implements BookRepository {
    private final EntityManagerFactory factory;
    
    public BookRepositoryImpl(EntityManagerFactory factory) {
        this.factory = factory;
    }
    
    @Override
    public Book save(Book book) {
        EntityManager manager = null;
        EntityTransaction transaction = null;
        try {
            manager = factory.createEntityManager();
            transaction = manager.getTransaction();
            transaction.begin();
            
            manager.persist(book);
            transaction.commit();
            return book;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException("Can't save book with title: " + book.getTitle(), e);
        } finally {
            if (manager != null) {
                manager.close();
            }
        }
    }
    
    @Override
    public List<Book> findAll() {
        EntityManager manager = factory.createEntityManager();
        return manager.createQuery("from Book", Book.class).getResultList();
    }
}

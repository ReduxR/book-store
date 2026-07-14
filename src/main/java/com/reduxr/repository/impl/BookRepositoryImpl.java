package com.reduxr.repository.impl;

import com.reduxr.exception.DataProcessingException;
import com.reduxr.model.Book;
import com.reduxr.repository.BookRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BookRepositoryImpl implements BookRepository {
    private final EntityManagerFactory factory;
    
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

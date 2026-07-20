package com.reduxr.specification.book;

import com.reduxr.model.Book;
import com.reduxr.specification.SpecificationProvider;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class TitleSpecificationProvider implements SpecificationProvider<Book> {
    @Override
    public Specification<Book> getSpecification(String[] params) {
        return (root, query, cb) 
                -> root.get("title").in(List.of(params));
    }
    
    @Override
    public String getKey() {
        return "title";
    }
}

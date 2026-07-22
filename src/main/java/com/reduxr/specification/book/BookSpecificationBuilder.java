package com.reduxr.specification.book;

import com.reduxr.dto.BookSearchParametersDto;
import com.reduxr.model.Book;
import com.reduxr.specification.SpecificationBuilder;
import com.reduxr.specification.SpecificationProviderManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookSpecificationBuilder implements SpecificationBuilder<Book> {
    private static final String TITLE_KEY = "title";
    private static final String AUTHOR_KEY = "author";
    private static final String ISBN_KEY = "isbn";
    
    private final SpecificationProviderManager<Book> bookSpecificationProviderManager;
    
    @Override
    public Specification<Book> build(BookSearchParametersDto searchParameters) {
        Specification<Book> specification = Specification.allOf();
        if (searchParameters.title() != null && searchParameters.title().length > 0) {
            specification = specification.and(bookSpecificationProviderManager
                    .getSpecificationProvider(TITLE_KEY)
                    .getSpecification(searchParameters.title()));
        }
        if (searchParameters.author() != null && searchParameters.author().length > 0) {
            specification = specification.and(bookSpecificationProviderManager
                    .getSpecificationProvider(AUTHOR_KEY)
                    .getSpecification(searchParameters.author()));
        }
        if (searchParameters.isbn() != null && searchParameters.isbn().length > 0) {
            specification = specification.and(bookSpecificationProviderManager
                    .getSpecificationProvider(ISBN_KEY)
                    .getSpecification(searchParameters.isbn()));
        }
        return specification;
    }
}

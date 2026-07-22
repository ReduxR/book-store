package com.reduxr.specification.book;

import com.reduxr.exception.SpecificationProviderException;
import com.reduxr.model.Book;
import com.reduxr.specification.SpecificationProvider;
import com.reduxr.specification.SpecificationProviderManager;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookSpecificationProviderManager implements SpecificationProviderManager<Book> {
    private final List<SpecificationProvider<Book>> bookSpecificationProviders;
    
    @Override
    public SpecificationProvider<Book> getSpecificationProvider(String key) {
        return bookSpecificationProviders.stream()
                .filter(sp -> sp.getKey().equals(key))
                .findFirst()
                .orElseThrow(() -> new SpecificationProviderException(
                        "Can't get specification provider by given key"));
    }
}

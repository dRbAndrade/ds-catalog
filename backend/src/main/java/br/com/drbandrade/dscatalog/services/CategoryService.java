package br.com.drbandrade.dscatalog.services;

import br.com.drbandrade.dscatalog.entities.Category;
import br.com.drbandrade.dscatalog.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository repository;

    public List<Category> findAll(){
        return repository.findAll();
    }

}

package br.com.drbandrade.dscatalog.services;

import br.com.drbandrade.dscatalog.dto.CategoryDTO;
import br.com.drbandrade.dscatalog.entities.Category;
import br.com.drbandrade.dscatalog.repositories.CategoryRepository;
import br.com.drbandrade.dscatalog.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository repository;

    @Transactional(readOnly = true)
    public List<CategoryDTO> findAll(){
        return repository.findAll().stream().map(CategoryDTO::new).toList();
    }

    @Transactional(readOnly = true)
    public CategoryDTO findById(long id) {
        Optional<Category> obj = repository.findById(id);
        return new CategoryDTO(obj.orElseThrow(()->new ResourceNotFoundException("Categoria não encontrada")));

    }

    @Transactional
    public CategoryDTO insert(CategoryDTO dto) {
        Category category = new Category();
        category.setName(dto.getName());
        category = repository.save(category);
        return new CategoryDTO(category);
    }

    @Transactional
    public CategoryDTO edit(long id, CategoryDTO dto) {
        try {
            Category category = repository.getById(id);
            category.setName(dto.getName());
            repository.save(category);
            return new CategoryDTO(category);
        }catch (EntityNotFoundException ex){
            throw new ResourceNotFoundException("Recurso com o id "+id+" não foi encontrado");
        }
    }
}

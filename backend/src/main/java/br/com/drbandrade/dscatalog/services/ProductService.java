package br.com.drbandrade.dscatalog.services;

import br.com.drbandrade.dscatalog.dto.ProductDTO;
import br.com.drbandrade.dscatalog.entities.Product;
import br.com.drbandrade.dscatalog.repositories.ProductRepository;
import br.com.drbandrade.dscatalog.services.exceptions.DatabaseIntegrityException;
import br.com.drbandrade.dscatalog.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository repository;

    @Transactional(readOnly = true)
    public Page<ProductDTO> findAllPaged(PageRequest pageRequest){
        return repository.findAll(pageRequest).map(ProductDTO::new);
    }

    @Transactional(readOnly = true)
    public ProductDTO findById(long id) {
        Optional<Product> obj = repository.findById(id);
        Product entity= obj.orElseThrow(()->new ResourceNotFoundException("Categoria não encontrada"));
        return new ProductDTO(entity,entity.getCategories());

    }

    @Transactional
    public ProductDTO insert(ProductDTO dto) {
        Product product = new Product();
        product.setName(dto.getName());
        product = repository.save(product);
        return new ProductDTO(product);
    }

    @Transactional
    public ProductDTO edit(long id, ProductDTO dto) {
        try {
            Product product = repository.getById(id);
            product.setName(dto.getName());
            repository.save(product);
            return new ProductDTO(product);
        }catch (EntityNotFoundException ex){
            throw new ResourceNotFoundException("Recurso com o id "+id+" não foi encontrado");
        }
    }

    public void delete(long id) {
        try {
            repository.deleteById(id);
        }catch (EmptyResultDataAccessException ex){
            throw new ResourceNotFoundException("Produto não encontrado");
        }catch(DataIntegrityViolationException ex){
            throw new DatabaseIntegrityException("Violação de integridade: Há tabelas que dependem de Produto");
        }

    }

}

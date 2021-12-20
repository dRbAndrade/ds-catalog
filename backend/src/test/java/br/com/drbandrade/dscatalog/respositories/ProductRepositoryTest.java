package br.com.drbandrade.dscatalog.respositories;

import static org.junit.jupiter.api.Assertions.*;

import br.com.drbandrade.dscatalog.entities.Product;
import br.com.drbandrade.dscatalog.repositories.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;

@DataJpaTest
public class ProductRepositoryTest {

    Product p;

    @BeforeEach
    public void initiate(){
        p = Product.generateProduct();
    }

    @Autowired
    private ProductRepository repository;

    @Test
    public void deleteShouldDeleteObjectWhenIdExists(){

        repository.deleteById(1l);
        assertFalse(repository.findById(1l).isPresent());

    }

    @Test
    public void deleteShouldThrowsExceptionWhenIdNotExists(){

        assertThrows(EmptyResultDataAccessException.class,()->repository.deleteById(1000L));

    }

    @Test
    public void saveShouldPersistObjectWithAutoIncrementWhenIdIsNull(){

        p.setId(null);
        p = repository.save(p);
        assertNotNull(p.getId());
        assertEquals(26, p.getId());

    }

    @Test
    public void findByIdShouldReturnObjectWhenIdExists(){

        assertTrue(repository.findById(1L).isPresent());

    }

    @Test
    public void findByIdShouldBeEmptyWhenIdNotExists(){

        assertTrue(repository.findById(10000L).isEmpty());

    }



}

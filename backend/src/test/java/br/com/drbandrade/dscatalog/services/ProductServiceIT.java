package br.com.drbandrade.dscatalog.services;

import br.com.drbandrade.dscatalog.dto.ProductDTO;
import br.com.drbandrade.dscatalog.repositories.ProductRepository;
import br.com.drbandrade.dscatalog.services.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class ProductServiceIT {

    @Autowired
    private ProductService service;

    @Autowired
    private ProductRepository repository;

    private Long existingId;
    private Long nonExistingId;
    private Long totalProductsCount;

    @BeforeEach
    void setUp() throws Exception{
        existingId = 1L;
        nonExistingId = 1000L;
        totalProductsCount = 25L;
    }

    @Test
    public void deleteShouldDeleteResourceWhenIdExists(){
        service.delete(existingId);
        assertEquals(totalProductsCount-1,repository.count());
    }
    @Test
    public void deleteShouldThrowExceptionWhenIdNotExists(){
        assertThrows(ResourceNotFoundException.class,()->service.delete(nonExistingId));

    }
    @Test
    public void findaAllPagedShouldReturnPageWhenPage0Size10(){
        PageRequest pageable = PageRequest.of(0,10);
        Page<ProductDTO> result = service.findAllPaged(pageable);
        assertFalse(result.isEmpty());
        assertEquals(0,result.getNumber());
        assertEquals(10,result.getSize());
        assertEquals(totalProductsCount,result.getTotalElements());

    }
    @Test
    public void findaAllPagedShouldReturnEmptyPageWhenPageDoesNotExist(){
        PageRequest pageable = PageRequest.of(50,10);
        Page<ProductDTO> result = service.findAllPaged(pageable);
        assertTrue(result.isEmpty());

    }
    @Test
    public void findaAllPagedShouldReturnSortedPageWhenSortByName(){
        PageRequest pageable = PageRequest.of(0,10, Sort.by("name"));
        Page<ProductDTO> result = service.findAllPaged(pageable);
        assertFalse(result.isEmpty());
        assertEquals("Macbook Pro",result.getContent().get(0).getName());
        assertEquals("PC Gamer",result.getContent().get(1).getName());
        assertEquals("PC Gamer Alfa",result.getContent().get(2).getName());

    }
}

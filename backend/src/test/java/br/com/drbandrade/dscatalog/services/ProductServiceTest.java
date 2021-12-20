package br.com.drbandrade.dscatalog.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import br.com.drbandrade.dscatalog.dto.ProductDTO;
import br.com.drbandrade.dscatalog.entities.Product;
import br.com.drbandrade.dscatalog.repositories.CategoryRepository;
import br.com.drbandrade.dscatalog.repositories.ProductRepository;
import br.com.drbandrade.dscatalog.services.exceptions.DatabaseIntegrityException;
import br.com.drbandrade.dscatalog.services.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
public class ProductServiceTest {


    @InjectMocks
    private ProductService service;

    @Mock
    private ProductRepository productRepository;
    @Mock
    private CategoryRepository categoryRepository;

    private long existingId;
    private long nonExistingId;
    private long dependantId;
    private ProductDTO productEditDTO;
    private PageImpl<Product> page;
    private Product product;

    @BeforeEach
    public void initiate(){

        existingId = 1L;
        nonExistingId = 2L;
        dependantId = 3L;
        product = Product.generateProduct();
        page = new PageImpl<>(List.of(product));
        productEditDTO = new ProductDTO();

        doNothing().when(productRepository).deleteById(existingId);
        doThrow(EmptyResultDataAccessException.class).when(productRepository).deleteById(nonExistingId);
        doThrow(DatabaseIntegrityException.class).when(productRepository).deleteById(dependantId);
        when(productRepository.findAll((Pageable) ArgumentMatchers.any())).thenReturn(page);
        when(productRepository.save(ArgumentMatchers.any())).thenReturn(product);
        when(productRepository.findById(existingId)).thenReturn(Optional.of(product));
        when(productRepository.findById(nonExistingId)).thenReturn(Optional.empty());
        when(productRepository.getById(existingId)).thenReturn(product);
        when(productRepository.getById(nonExistingId)).thenThrow(ResourceNotFoundException.class);
    }

    @Test
    public void deleteShouldDoNothingWhenIdExists(){

        assertDoesNotThrow(()->service.delete(existingId));
        verify(productRepository,times(1)).deleteById(existingId);

    }

    @Test
    public void deleteShouldThrowExceptionWhenIdNotExists(){

        assertThrows(ResourceNotFoundException.class,()->service.delete(nonExistingId));
        verify(productRepository,times(1)).deleteById(nonExistingId);

    }

    @Test
    public void deleteShouldThrowExceptionWhenDependantId(){

        assertThrows(DatabaseIntegrityException.class,()->service.delete(dependantId));
        verify(productRepository,times(1)).deleteById(dependantId);

    }

    @Test
    public void findAllPagedShouldReturnPage(){

        Pageable pageable = PageRequest.of(0,10);
        Page<ProductDTO> result = service.findAllPaged(pageable);

        assertNotNull(result);
        verify(productRepository,times(1)).findAll(pageable);


    }

    @Test
    public void findByIdShouldReturnDTOWhenIdExists(){

        final ProductDTO[] dto = new ProductDTO[1];
        assertDoesNotThrow(()-> dto[0] = service.findById(existingId));
        assertNotNull(dto[0]);

    }

    @Test
    public void findByIdShouldThrowExceptionWhenIdNotExists(){

        assertThrows(ResourceNotFoundException.class,()->service.findById(nonExistingId));

    }

    @Test
    public void updateShouldReturnProductDTOWhenIdExists(){

        final ProductDTO[] dto = new ProductDTO[1];
        assertDoesNotThrow(()-> dto[0] = service.edit(existingId, productEditDTO));
        assertNotNull(dto[0]);

    }
    @Test
    public void updateShouldThrowExceptionWhenIdNotExists(){

        assertThrows(ResourceNotFoundException.class,()->service.findById(nonExistingId));

    }




}

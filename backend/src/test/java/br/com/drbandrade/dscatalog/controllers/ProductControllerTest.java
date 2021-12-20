package br.com.drbandrade.dscatalog.controllers;

import br.com.drbandrade.dscatalog.dto.ProductDTO;
import br.com.drbandrade.dscatalog.entities.Product;
import br.com.drbandrade.dscatalog.services.ProductService;
import br.com.drbandrade.dscatalog.services.exceptions.DatabaseIntegrityException;
import br.com.drbandrade.dscatalog.services.exceptions.ResourceNotFoundException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService service;

    @Autowired
    private ObjectMapper objectMapper;

    private PageImpl<ProductDTO> page;
    private ProductDTO productDTO;
    private Long existingId = 1L;
    private Long nonExistingId = 2L;
    private Long dependantId = 3L;

    ProductControllerTest() {
    }

    @BeforeEach
    void initiate(){
        productDTO = new ProductDTO(Product.generateProduct());
        page = new PageImpl<>(List.of());

        when(service.findAllPaged(any())).thenReturn(page);

        when(service.findById(existingId)).thenReturn(productDTO);
        when(service.findById(nonExistingId)).thenThrow(ResourceNotFoundException.class);

        when(service.edit(eq(existingId),any())).thenReturn(productDTO);
        when(service.edit(eq(nonExistingId),any())).thenThrow(ResourceNotFoundException.class);

        when(service.insert(any())).thenReturn(productDTO);

        doNothing().when(service).delete(existingId);
        doThrow(ResourceNotFoundException.class).when(service).delete(nonExistingId);
        doThrow(DatabaseIntegrityException.class).when(service).delete(dependantId);
    }

    @Test
    public void findAllShouldReturnPage() throws Exception {
        mockMvc.perform(get("/products").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void findByIdShouldReturnProductWhenIdExists() throws Exception {

        mockMvc.perform(get("/products/{id}",existingId).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.description").exists());

    }

    @Test
    public void findByIdShouldReturnNotFoundWhenIdNotExists() throws Exception {
        mockMvc.perform(get("/products/{id}",nonExistingId).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateShouldReturnProductDTOWhenIdExists() throws Exception{

        String jsonBody = objectMapper.writeValueAsString(productDTO);

        mockMvc.perform(put("/products/{id}",existingId)
                    .content(jsonBody).contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.description").exists());

    }

    @Test
    public void updateShouldReturnNotFoundWhenIdNotExists() throws Exception{

        String jsonBody = objectMapper.writeValueAsString(productDTO);

        mockMvc.perform(put("/products/{id}",nonExistingId)
                        .content(jsonBody).contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

    }

    @Test
    public void insertShouldReturnCreatedAndProductDTO() throws Exception {

        String jsonBody = objectMapper.writeValueAsString(productDTO);

        mockMvc.perform(post("/products/")
                        .content(jsonBody).contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists());

    }

    @Test
    public void deleteShouldReturnNoContentWhenIdExists()throws Exception{
        mockMvc.perform(delete("/products/{id}",existingId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
    @Test
    public void deleteShouldReturnNotFoundWhenIdNotExists()throws Exception{
        mockMvc.perform(delete("/products/{id}",nonExistingId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

}
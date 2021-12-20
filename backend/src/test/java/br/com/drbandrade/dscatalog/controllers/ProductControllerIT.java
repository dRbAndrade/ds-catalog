package br.com.drbandrade.dscatalog.controllers;

import br.com.drbandrade.dscatalog.dto.ProductDTO;
import br.com.drbandrade.dscatalog.entities.Product;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ProductControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private Long existingId;
    private Long nonExistingId;
    private Long dependantId;
    private Long totalProductsCount;


    @BeforeEach
    void setUp() throws Exception{
        existingId = 1L;
        nonExistingId = 1000L;
        totalProductsCount = 25L;
    }

    @Test
    public void findAllShouldReturnSortedPagedWhenSortByName() throws Exception{

        mockMvc.perform(get("/products/?page=0&size=12&sort=name,asc")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").exists())
                .andExpect(jsonPath("$.totalElements").value(totalProductsCount))
                .andExpect(jsonPath("$.content[0].name").value("Macbook Pro"))
                .andExpect(jsonPath("$.content[1].name").value("PC Gamer"))
                .andExpect(jsonPath("$.content[2].name").value("PC Gamer Alfa"));

    }
    @Test
    public void updateShouldReturnProductDTOWhenIdExists() throws Exception{

        ProductDTO productDTO = new ProductDTO(Product.generateProduct());
        String jsonBody = objectMapper.writeValueAsString(productDTO);

        String expectedName = productDTO.getName();
        String expectedDescription = productDTO.getDescription();

        mockMvc.perform(put("/products/{id}",existingId)
                        .content(jsonBody).contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(existingId))
                .andExpect(jsonPath("$.name").value(expectedName))
                .andExpect(jsonPath("$.description").value(expectedDescription));

    }
    @Test
    public void updateShouldReturnNotFoundWhenIdNotExists() throws Exception{

        ProductDTO productDTO = new ProductDTO(Product.generateProduct());
        String jsonBody = objectMapper.writeValueAsString(productDTO);

        mockMvc.perform(put("/products/{id}",nonExistingId)
                        .content(jsonBody).contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

    }

}

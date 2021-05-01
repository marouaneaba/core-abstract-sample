package com.example.demo.controller;

import com.example.demo.ShopApiEndpoints;
import com.example.demo.dto.out.stock.Stock;
import com.example.demo.dto.out.stock.StockOutline;
import com.example.demo.facade.StockFacade;
import com.example.demo.shoe.repository.ShoeRepository;
import com.example.demo.stock.creator.StockCreator;
import com.example.demo.stock.domain.StockDomain;
import com.example.demo.stock.fixture.ShoeFixture;
import com.example.demo.stock.helper.StockHelper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(StockController.class)
@Import({StockDomain.class, StockCreator.class, StockHelper.class, ShoeRepository.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class StockControllerTest {

    protected static final String VERSION = "version";

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper jsonMapper;

    @MockBean
    private StockFacade stockFacade;

    @Autowired
    private StockDomain stockDomain;

    @MockBean
    private ShoeRepository shoeRepository;


    @BeforeAll
    public void setUp() {
        Mockito.when(this.stockFacade.get(anyInt())).thenReturn(stockDomain);
    }

    @Test
    @DisplayName("GET /stock success")
    void shouldReturnOkStatusAndEmptyStateWhenVersionIsThreeAndStockIsEmpty() throws Exception {
        // Given
        Mockito.when(this.shoeRepository.findAll()).thenReturn(ShoeFixture.createStockSome());

        // When
        MvcResult result = this.mockMvc.perform(
                get(ShopApiEndpoints.STOCK)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .header(VERSION, 3)
        )
                .andExpect(status().isOk())
                .andReturn();

        // Then
        Stock stock = jsonMapper.readValue(result.getResponse().getContentAsString(), Stock.class);
        assertThat(stock).isNotNull();
        assertThat(stock).extracting(Stock::getState).isEqualTo(Stock.State.SOME);
        assertThat(stock.getShoes().stream().mapToInt(StockOutline::getQuantity).sum()).isBetween(1,30);
        assertThat(stock.getShoes().stream().mapToInt(StockOutline::getQuantity).sum()).isEqualTo(8);
    }
    
}

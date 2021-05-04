package com.example.demo.controller;

import com.example.demo.ShopApiEndpoints;
import com.example.demo.dto.stock.out.Stock;
import com.example.demo.dto.stock.out.StockItem;
import com.example.demo.facade.StockFacade;
import com.example.demo.stock.creator.StockCreator;
import com.example.demo.stock.domain.StockDomain;
import com.example.demo.stock.fixture.StockFixture;
import com.example.demo.stock.helper.StockHelper;
import com.example.demo.stock.repository.StockMeasureRepository;
import com.example.demo.utils.ShopConstant;
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
@Import({StockDomain.class, StockCreator.class, StockHelper.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class StockControllerTest {

  protected static final String VERSION = "version";

  @Autowired protected MockMvc mockMvc;

  @Autowired protected ObjectMapper jsonMapper;

  @MockBean private StockFacade stockFacade;

  @Autowired private StockDomain stockDomain;

  @MockBean private StockMeasureRepository stockMeasureRepository;

  @BeforeAll
  public void setUp() {
    Mockito.when(this.stockFacade.get(anyInt())).thenReturn(stockDomain);
  }

  @Test
  @DisplayName("GET /stock success")
  void shouldReturnOkStatusAndStateEmptyWhenGetStockVersionIsThreeAndStockIsEmpty()
      throws Exception {
    // Given
    Mockito.when(this.stockMeasureRepository.findAll()).thenReturn(StockFixture.createStockSome());

    // When
    MvcResult result =
        this.mockMvc
            .perform(
                get(ShopApiEndpoints.STOCK)
                    .accept(MediaType.APPLICATION_JSON_VALUE)
                    .header(VERSION, 3))
            .andExpect(status().isOk())
            .andReturn();

    // Then
    Stock stock = jsonMapper.readValue(result.getResponse().getContentAsString(), Stock.class);
    assertThat(stock).isNotNull();
    assertThat(stock).extracting(Stock::getState).isEqualTo(Stock.State.SOME);
    assertThat(stock.getShoes()).extracting("name").containsOnlyNulls();
    assertThat(stock.getShoes()).extracting("id").containsOnlyNulls();
    assertThat(stock.getShoes().stream().mapToInt(StockItem::getQuantity).sum())
        .isBetween(ShopConstant.STOCK_MIN_CAPACITE, ShopConstant.STOCK_MAX_CAPACITE);
    assertThat(stock.getShoes().stream().mapToInt(StockItem::getQuantity).sum()).isEqualTo(8);
  }
}

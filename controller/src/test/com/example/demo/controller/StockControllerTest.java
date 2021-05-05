package com.example.demo.controller;

import com.example.demo.ShopApiEndpoints;
import com.example.demo.dto.stock.out.Stock;
import com.example.demo.dto.stock.out.StockItem;
import com.example.demo.facade.StockFacade;
import com.example.demo.stock.creator.StockCreator;
import com.example.demo.stock.domain.StockDomain;
import com.example.demo.stock.entity.StockMeasure;
import com.example.demo.stock.fixture.StockFixture;
import com.example.demo.stock.helper.StockHelper;
import com.example.demo.stock.repository.StockMeasureRepository;
import com.example.demo.utils.ShopConstant;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(StockController.class)
@Import({StockDomain.class, StockCreator.class, StockHelper.class, StockMeasureRepository.class})
class StockControllerTest {

  protected static final String VERSION = "version";

  @Autowired protected MockMvc mockMvc;

  @MockBean private StockFacade stockFacade;

  @Autowired private StockDomain stockDomain;

  @MockBean private StockMeasureRepository stockMeasureRepository;

  @Autowired protected ObjectMapper jsonMapper;

  @Captor ArgumentCaptor<StockMeasure> argCaptor;

  @BeforeEach
  public void setUp() {
    Mockito.when(this.stockFacade.get(anyInt())).thenReturn(stockDomain);
  }

  @Test
  @DisplayName("GET /stock success")
  void shouldReturnStatusOkAndStateSomeWhenGetStockVersionIsThreeAndFetchSomeStock()
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

  @Test
  @DisplayName("GET /stock success")
  void shouldReturnStatusOKAndStateFullWhenGetStockVersionIsThreeAndFetchFullStock()
      throws Exception {
    // Given
    Mockito.when(this.stockMeasureRepository.findAll()).thenReturn(StockFixture.createStockFull());

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
    assertThat(stock).extracting(Stock::getState).isEqualTo(Stock.State.FULL);
    assertThat(stock.getShoes()).extracting("name").containsOnlyNulls();
    assertThat(stock.getShoes()).extracting("id").containsOnlyNulls();
    assertThat(stock.getShoes().stream().mapToInt(StockItem::getQuantity).sum())
        .isBetween(ShopConstant.STOCK_MIN_CAPACITE, ShopConstant.STOCK_MAX_CAPACITE);
    assertThat(stock.getShoes().stream().mapToInt(StockItem::getQuantity).sum()).isEqualTo(30);
  }

  @Test
  @DisplayName("GET /stock success")
  void shouldReturnStatusOKAndStateEmptyWhenGetStockVersionIsThreeAndFetchEmptyStock()
      throws Exception {
    // Given
    Mockito.when(this.stockMeasureRepository.findAll()).thenReturn(StockFixture.createStockEmpty());

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
    assertThat(stock).extracting(Stock::getState).isEqualTo(Stock.State.EMPTY);
    assertThat(stock.getShoes()).extracting("name").containsOnlyNulls();
    assertThat(stock.getShoes()).extracting("id").containsOnlyNulls();
    assertThat(stock.getShoes().stream().mapToInt(StockItem::getQuantity).sum())
        .isBetween(ShopConstant.STOCK_MIN_CAPACITE, ShopConstant.STOCK_MAX_CAPACITE);
    assertThat(stock.getShoes().stream().mapToInt(StockItem::getQuantity).sum()).isZero();
  }

  @Test
  @DisplayName("PATCH /stock success")
  void
      shouldReturnStatusNoContentAndUpdateStockExistingWhenStockVersionIsThreeAndStockRequestIsExistingAndShoesLessThanThirty()
          throws Exception {
    // Given
    StockMeasure stockMeasureQuantity2 = StockFixture.createStockSome().get(0);
    Mockito.when(
            this.stockMeasureRepository.findByShoe_ColorAndShoe_NameAndShoe_Size(
                any(), any(), any()))
        .thenReturn(Optional.ofNullable(stockMeasureQuantity2));

    // Same stockMeasureQuantity2 data and Quantity 10
    byte[] bytes =
        new ClassPathResource("json/stock/patch-with-stock-item-valid-json-patch-payload.json")
            .getInputStream()
            .readAllBytes();

    // When
    this.mockMvc
        .perform(
            patch(ShopApiEndpoints.STOCK_SHOES)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(VERSION, 3)
                .content(bytes))
        .andExpect(status().isNoContent())
        .andReturn();

    // Then
    Mockito.verify(stockMeasureRepository).save(argCaptor.capture());
    assertThat(argCaptor).isNotNull();
    // stockMeasureQuantity2 have quantity 2 and patch json file have quantity 10
    assertThat(argCaptor.getValue()).extracting(StockMeasure::getQuantity).isEqualTo(12);
  }

  @Test
  @DisplayName("PATCH /stock success")
  void
      shouldReturnNoContentStatusAndCreateNewStockWhenStockVersionIsThreeAndStockRequestNotEmptyAndAndStockNotExistingAndQuantityLessThanThirty()
          throws Exception {
    // Given
    Mockito.when(
            this.stockMeasureRepository.findByShoe_ColorAndShoe_NameAndShoe_Size(
                any(), any(), any()))
        .thenReturn(Optional.ofNullable(null));

    // Same stockMeasureQuantity2 data and Quantity 10
    byte[] bytes =
        new ClassPathResource("json/stock/patch-with-stock-item-valid-json-patch-payload.json")
            .getInputStream()
            .readAllBytes();

    // When
    this.mockMvc
        .perform(
            patch(ShopApiEndpoints.STOCK_SHOES)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(VERSION, 3)
                .content(bytes))
        .andExpect(status().isNoContent())
        .andReturn();

    // Then
    Mockito.verify(this.stockMeasureRepository).save(this.argCaptor.capture());
    assertThat(this.argCaptor).isNotNull();
    // fetch null stock measure repository and patch json file  request stock quantity 10
    assertThat(this.argCaptor.getValue()).extracting(StockMeasure::getQuantity).isEqualTo(10);
  }

  @Test
  @DisplayName("PATCH /stock Bad request")
  void
      shouldReturnBadRequest400StatusAndStockNotUpdatedWhenStockVersionIsThreeAndBodyRequestIsEmpty()
          throws Exception {
    // Given
    Mockito.when(
            this.stockMeasureRepository.findByShoe_ColorAndShoe_NameAndShoe_Size(
                any(), any(), any()))
        .thenReturn(Optional.ofNullable(null));
    byte[] bytes = null;

    // When
    this.mockMvc
        .perform(
            patch(ShopApiEndpoints.STOCK_SHOES)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(VERSION, 3)
                .content(bytes))
        .andExpect(status().isBadRequest())
        .andReturn();

    // Then
    Mockito.verifyNoInteractions(this.stockMeasureRepository);
  }

  @Test()
  @DisplayName("PATCH /stock Bad request")
  void
      shouldThrowStockCapacityExceptionAndReturnBadRequest400StatusAndStockNotUpdatedWhenStockVersionIsThreeAndRequestStockQunatityGreaterThan30()
          throws Exception {
    // Given
    Mockito.when(this.stockMeasureRepository.findAll())
        .thenReturn(List.of(StockMeasure.builder().quantity(35).build()));

    Mockito.when(
            this.stockMeasureRepository.findByShoe_ColorAndShoe_NameAndShoe_Size(
                any(), any(), any()))
        .thenReturn(Optional.ofNullable(null));

    byte[] bytes =
        new ClassPathResource(
                "json/stock/patch-with-stock-item-not-valid-quanity-json-patch-payload.json")
            .getInputStream()
            .readAllBytes();

    // When
    MvcResult result =
        this.mockMvc
            .perform(
                patch(ShopApiEndpoints.STOCK_SHOES)
                    .accept(MediaType.APPLICATION_JSON_VALUE)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .header(VERSION, 3)
                    .content(bytes))
            .andExpect(status().isBadRequest())
            .andReturn();

    // Then
    assertThat(result.getResponse().getContentAsString())
        .isEqualTo("Stock capacity limited of 30 shoes.");
  }
}

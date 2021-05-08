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
import com.example.demo.stock.mapper.StockMapper;
import com.example.demo.stock.repository.StockMeasureRepository;
import com.example.demo.utils.ShopConstant;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(StockController.class)
@Import({StockDomain.class, StockCreator.class, StockHelper.class})
class StockControllerTest {

  private static final String VERSION = "version";
  public static final String JSON_STOCK_PATCH_STOCK_ITEM_VALID_JSON = "json/stock/patch-with-stock-item-valid-json-patch-shoes-payload.json";
  public static final String JSON_STOCK_PATCH_ITEM_QUANITY_NOT_VALID = "json/stock/patch-with-stock-item-not-valid-quanity-json-patch-payload.json";
  public static final String JSON_STOCK_PATCH_ITEM_VALID = "json/stock/patch-with-stock-item-valid-json-patch-stock-payload.json";
  public static final String JSON_STOCK_PATCH_COLLECTION_DUPLICATE_SHOE_NO_VALID = "json/stock/patch-collection-shoe-with-no-valid-json-payload-duplicate-shoe.json";
  public static final String JSON_STOCK_PATCH_COLLECTION_SHOE_NO_VALID_CAPACITY_EXCEPTION = "json/stock/patch-collection-shoe-with-no-valid-json-payload-capacity-exception.json";

  @Autowired protected MockMvc mockMvc;

  @MockBean private StockFacade stockFacade;

  @Autowired private StockDomain stockDomain;

  @MockBean private StockMeasureRepository stockMeasureRepository;

  @Autowired protected ObjectMapper jsonMapper;

  @Captor ArgumentCaptor<StockMeasure> stockMeasureCaptor;

  @Captor ArgumentCaptor<List<StockMeasure>> stockMeasureListCaptor;

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
  @DisplayName("PATCH /stock/shoes No content")
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
        new ClassPathResource(JSON_STOCK_PATCH_STOCK_ITEM_VALID_JSON)
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
    Mockito.verify(stockMeasureRepository).save(stockMeasureCaptor.capture());
    assertThat(stockMeasureCaptor).isNotNull();
    // stockMeasureQuantity2 have quantity 2 and patch json file have quantity 10
    assertThat(stockMeasureCaptor.getValue()).extracting(StockMeasure::getQuantity).isEqualTo(12);
  }

  @Test
  @DisplayName("PATCH /stock/shoes No content")
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
        new ClassPathResource(JSON_STOCK_PATCH_STOCK_ITEM_VALID_JSON)
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
    Mockito.verify(this.stockMeasureRepository).save(this.stockMeasureCaptor.capture());
    assertThat(this.stockMeasureCaptor).isNotNull();
    // fetch null stock measure repository and patch json file  request stock quantity 10
    assertThat(this.stockMeasureCaptor.getValue())
        .extracting(StockMeasure::getQuantity)
        .isEqualTo(10);
  }

  @Test
  @DisplayName("PATCH /stock/shoes Bad request")
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
  @DisplayName("PATCH /stock/shoes Bad request")
  void
      shouldThrowStockCapacityExceptionAndReturnBadRequest400StatusAndStockNotUpdatedWhenRequestStockQunatityAddStockDatabaseGreaterThan30AndVersionIsThree()
          throws Exception {

    byte[] bytes =
        new ClassPathResource(
                JSON_STOCK_PATCH_ITEM_QUANITY_NOT_VALID)
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
            .andExpect(status().isBadRequest())
            .andReturn();
  }

  @Test
  @DisplayName("PATCH /stock No content")
  void
      shouldReturnStatusNoContentAndDeleteAllAndInsertNowShoesWhenPatchStockCleanAndVersionIsThree()
          throws Exception {
    // Given
    byte[] bytes =
        new ClassPathResource(JSON_STOCK_PATCH_ITEM_VALID)
            .getInputStream()
            .readAllBytes();
    // When
    MvcResult result =
        this.mockMvc
            .perform(
                patch(ShopApiEndpoints.STOCK)
                    .accept(MediaType.APPLICATION_JSON_VALUE)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .header(VERSION, 3)
                    .content(bytes))
            .andExpect(status().isNoContent())
            .andReturn();

    // Then
    Mockito.verify(this.stockMeasureRepository).saveAll(stockMeasureListCaptor.capture());
    Stock stockResponse = jsonMapper.readValue(new String(bytes), Stock.class);
    List<StockMeasure> stockMeasures =
        stockResponse.getShoes().stream()
            .map(stockItem -> StockMapper.toStockMeasure(stockItem))
            .collect(Collectors.toList());
    assertThat(stockMeasureListCaptor.getValue()).isEqualTo(stockMeasures);
  }

  @Test
  @DisplayName("PATCH /stock Bad request") // todo
  void
      shouldThrowStockShoesDuplicationExceptionAndBadRequestStatusWhenPatchDuplicateShoesAndVersionIsThree()
          throws Exception {

    // Given
    byte[] bytes =
        new ClassPathResource(
                JSON_STOCK_PATCH_COLLECTION_DUPLICATE_SHOE_NO_VALID)
            .getInputStream()
            .readAllBytes();
    // When
    this.mockMvc
        .perform(
            patch(ShopApiEndpoints.STOCK)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(VERSION, 3)
                .content(bytes))
        .andExpect(status().isBadRequest())
        .andExpect(content().string("The collection contains a duplication of shoe."))
        .andReturn();
  }

  @Test
  @DisplayName("PATCH /stock Bad request") // todo
  void
      shouldThrowStockCapacityExceptionAndBadRequestStatusWhenRequestStockQunatityAddStockDatabaseGreaterThan30AndVersionIsThree()
          throws Exception {
    // Given
    byte[] bytes =
        new ClassPathResource(
                JSON_STOCK_PATCH_COLLECTION_SHOE_NO_VALID_CAPACITY_EXCEPTION)
            .getInputStream()
            .readAllBytes();
    // When
    this.mockMvc
        .perform(
            patch(ShopApiEndpoints.STOCK)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(VERSION, 3)
                .content(bytes))
        .andExpect(status().isBadRequest())
        .andExpect(content().string("Stock capacity limited of 30 shoes."))
        .andReturn();
  }
}

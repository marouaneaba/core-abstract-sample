package com.example.demo.stock.domain;

import com.example.demo.dto.stock.out.Stock;
import com.example.demo.dto.stock.out.StockItem;
import com.example.demo.exception.StockCapacityException;
import com.example.demo.facade.StockFacade;
import com.example.demo.stock.creator.StockCreator;
import com.example.demo.stock.entity.StockMeasure;
import com.example.demo.stock.fixture.StockFixture;
import com.example.demo.stock.helper.StockHelper;
import com.example.demo.stock.repository.StockMeasureRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.validation.Validator;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(
    classes = {
      StockDomain.class,
      StockFacade.class,
      StockCreator.class,
      StockHelper.class,
      ObjectMapper.class,
      Validator.class
    })
class StockDomainTest {

  @Autowired private StockDomain stockDomain;

  @Captor ArgumentCaptor<StockMeasure> argCaptor;

  @MockBean private StockMeasureRepository stockMeasureRepository;

  @Test
  void shouldReturnStockStateEmptyWhenSumShoesQunatityIsZero() {
    // Given
    List<StockMeasure> stockMeasures = StockFixture.createStockEmpty();
    Mockito.when(this.stockMeasureRepository.findAll()).thenReturn(stockMeasures);

    // When
    Stock stock = this.stockDomain.getStock();

    // Then
    assertThat(stock).isNotNull();
    assertThat(stock.getState()).isEqualTo(Stock.State.EMPTY);
    assertThat(stock.getShoes().stream().mapToInt(StockItem::getQuantity).sum()).isZero();
  }

  @Test
  void shouldReturnStockStateFullWhenSumShoesQunatityIsThirty() {
    // Given
    Mockito.when(this.stockMeasureRepository.findAll()).thenReturn(StockFixture.createStockFull());

    // When
    Stock stock = this.stockDomain.getStock();

    // Then
    assertThat(stock).isNotNull();
    assertThat(stock.getState()).isEqualTo(Stock.State.FULL);
    assertThat(stock.getShoes()).asList().isNotEmpty();
    assertThat(stock.getShoes().stream().mapToInt(StockItem::getQuantity).sum()).isEqualTo(30);
  }

  @Test
  void shouldReturnStockSomeShoeWhenSumShoesQunatityBetweenThirtyAndZero() {
    // Given
    Mockito.when(this.stockMeasureRepository.findAll()).thenReturn(StockFixture.createStockSome());

    // When
    Stock stock = this.stockDomain.getStock();

    // Then
    assertThat(stock).isNotNull();
    assertThat(stock.getState()).isEqualTo(Stock.State.SOME);
    assertThat(stock.getShoes()).asList().isNotEmpty();
    assertThat(stock.getShoes().stream().mapToInt(StockItem::getQuantity).sum()).isBetween(1, 30);
  }

  @Test
  void shouldUpdateStockQuantityWhenStockByColorAndNameAndSizeExisting() {
    // Given
    Optional<StockMeasure> stockMeasureOptional =
        Optional.ofNullable(StockFixture.createStockSome().get(0));
    Mockito.when(
            this.stockMeasureRepository.findByShoe_ColorAndShoe_NameAndShoe_Size(
                any(), any(), any()))
        .thenReturn(stockMeasureOptional);

    // When
    this.stockDomain.patch(StockFixture.createStockItems().getShoes().get(0));

    // Then
    Mockito.verify(stockMeasureRepository).save(argCaptor.capture());
    assertThat(argCaptor).isNotNull();
    assertThat(argCaptor.getValue()).extracting(StockMeasure::getQuantity).isEqualTo(4);
  }

  @Test
  void shouldCreateNowShoeAndQuantityWhenStockByColorAndNameAndSizeNotExisting() {
    // Given
    Optional<StockMeasure> stockMeasureOptional = Optional.empty();
    Mockito.when(
            this.stockMeasureRepository.findByShoe_ColorAndShoe_NameAndShoe_Size(
                any(), any(), any()))
        .thenReturn(stockMeasureOptional);

    // When
    this.stockDomain.patch(StockFixture.createStockItems().getShoes().get(0));

    // Then
    Mockito.verify(stockMeasureRepository).save(argCaptor.capture());
    assertThat(argCaptor).isNotNull();
    assertThat(argCaptor.getValue()).extracting(StockMeasure::getQuantity).isEqualTo(2);
  }

  @Test
  void shouldThrowStockCapacityExceptionWhenTryPatchStockWithQuantyGreaterThan30() {
    // Given
    Mockito.when(this.stockMeasureRepository.findAll())
        .thenReturn(List.of(StockMeasure.builder().quantity(35).build()));

    StockMeasure stockMeasure =
        StockMeasure.builder()
            .shoe(StockFixture.createStockSome().get(0).getShoe())
            .quantity(35)
            .build();

    Optional<StockMeasure> stockMeasureOptional = Optional.ofNullable(stockMeasure);
    Mockito.when(
            this.stockMeasureRepository.findByShoe_ColorAndShoe_NameAndShoe_Size(
                any(), any(), any()))
        .thenReturn(stockMeasureOptional);

    // When
    assertThrows(
        StockCapacityException.class,
        () -> this.stockDomain.patch(StockFixture.createStockItems().getShoes().get(0)));
  }
}

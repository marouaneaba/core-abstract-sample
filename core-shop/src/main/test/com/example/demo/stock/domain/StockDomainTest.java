package com.example.demo.stock.domain;


import com.example.demo.dto.out.stock.Stock;
import com.example.demo.dto.out.stock.StockOutline;
import com.example.demo.facade.StockFacade;
import com.example.demo.shoe.entity.Shoe;
import com.example.demo.shoe.repository.ShoeRepository;
import com.example.demo.stock.creator.StockCreator;
import com.example.demo.stock.fixture.ShoeFixture;
import com.example.demo.stock.helper.StockHelper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes =
        {StockDomain.class, StockFacade.class, StockCreator.class, StockHelper.class})
class StockDomainTest {

    @Autowired
    private StockDomain stockDomain;

    @MockBean
    private ShoeRepository shoeRepository;


    @Test
    void shouldReturnStockStateEmptyWhenSumShoesQunatityIsZero(){
      // Given
      List<Shoe> shoes = ShoeFixture.createStockEmpty();
      Mockito.when(this.shoeRepository.findAll()).thenReturn(shoes);

      // When
      Stock stock = this.stockDomain.getStock();

      // Then
      assertThat(stock).isNotNull();
      assertThat(stock.getState()).isEqualTo(Stock.State.EMPTY);
      assertThat(stock.getShoes().stream().mapToInt(StockOutline::getQuantity).sum()).isZero();
    }

    @Test
    void shouldReturnStockStateFullWhenSumShoesQunatityIsThirty(){
      // Given
      Mockito.when(this.shoeRepository.findAll()).thenReturn(ShoeFixture.createStockFull());

      // When
      Stock stock = this.stockDomain.getStock();

      // Then
      assertThat(stock).isNotNull();
      assertThat(stock.getState()).isEqualTo(Stock.State.FULL);
      assertThat(stock.getShoes()).asList().isNotEmpty();
      assertThat(stock.getShoes().stream().mapToInt(StockOutline::getQuantity).sum()).isEqualTo(30);
    }

    @Test
    void shouldReturnStockSomeShoeWhenSumShoesQunatityBetweenThirtyAndZero(){
      // Given
      Mockito.when(this.shoeRepository.findAll()).thenReturn(ShoeFixture.createStockSome());

      // When
      Stock stock = this.stockDomain.getStock();

      // Then
      assertThat(stock).isNotNull();
      assertThat(stock.getState()).isEqualTo(Stock.State.SOME);
      assertThat(stock.getShoes()).asList().isNotEmpty();
      assertThat(stock.getShoes().stream().mapToInt(StockOutline::getQuantity).sum()).isBetween(1,30);
    }
}

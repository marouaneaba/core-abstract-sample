package com.example.demo.stock.creator;

import com.example.demo.dto.stock.out.StockItem;
import com.example.demo.shoe.entity.Shoe;
import com.example.demo.stock.entity.StockMeasure;
import com.example.demo.stock.fixture.StockFixture;
import com.example.demo.stock.helper.StockHelper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = {StockHelper.class, StockCreator.class})
class StockCreatorTest {

  @InjectMocks private StockCreator stockCreator;

  @MockBean private StockHelper stockHelper;

  @Test
  void shouldReturnStockOutlineListByShoeListGroupByColorAndSize() {
    // Given
    List<StockMeasure> shoes = StockFixture.createStockSome();
    Mockito.when(this.stockHelper.computeSumShoesQuantityGroupByColorAndSize(any()))
        .thenReturn(this.computeSumShoesQuantityGroupByColorAndSize(shoes));

    // When
    List<StockItem> stockOutlines = this.stockCreator.createStock(shoes);

    // Then
    assertThat(stockOutlines).isNotNull().doesNotContainNull().asList().hasSize(2);
    assertThat(stockOutlines.stream().mapToInt(StockItem::getQuantity).sum()).isEqualTo(8);
  }

  private Map<Shoe, IntSummaryStatistics> computeSumShoesQuantityGroupByColorAndSize(
      List<StockMeasure> stockMeasures) {
    return stockMeasures.stream()
        .collect(
            Collectors.groupingBy(
                stockMeasure ->
                    Shoe.builder()
                        .color(stockMeasure.getShoe().getColor())
                        .size(stockMeasure.getShoe().getSize())
                        .build(),
                Collectors.summarizingInt(stockMeasure -> stockMeasure.getQuantity())));
  }
}

package com.example.demo.stock.creator;

import com.example.demo.dto.out.stock.StockOutline;
import com.example.demo.shoe.entity.Shoe;
import com.example.demo.stock.fixture.ShoeFixture;
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
@SpringBootTest(classes =
       {StockHelper.class, StockCreator.class})
class StockCreatorTest {

    @InjectMocks
    private StockCreator stockCreator;

    @MockBean
    private StockHelper stockHelper;

    @Test
    void shouldReturnStockOutlineListByShoeListGroupByColorAndSize() {
        // Given
        List<Shoe> shoes = ShoeFixture.createStockSome();
        Mockito.when(this.stockHelper.computeSumShoesQuantityGroupByColorAndSize(any()))
                .thenReturn(this.computeSumShoesQuantityGroupByColorAndSize(shoes));

        // When
        List<StockOutline> stockOutlines = this.stockCreator.createStockOutline(shoes);

        // Then
        assertThat(stockOutlines)
                .isNotNull()
                .doesNotContainNull()
                .asList().hasSize(2);
        assertThat(stockOutlines.stream().mapToInt(StockOutline::getQuantity).sum()).isEqualTo(8);
    }

    private Map<Shoe, IntSummaryStatistics> computeSumShoesQuantityGroupByColorAndSize(List<Shoe> shoes) {
        return shoes.stream()
                .collect(
                        Collectors.groupingBy(
                                shoe -> Shoe.builder().color(shoe.getColor()).size(shoe.getSize()).build(),
                                Collectors.summarizingInt(shoe -> shoe.getQuantity())));
    }
}

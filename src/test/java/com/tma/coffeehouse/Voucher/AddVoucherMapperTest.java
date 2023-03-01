package com.tma.coffeehouse.Voucher;

import com.tma.coffeehouse.Product.Product;
import com.tma.coffeehouse.Product.ProductRepository;
import com.tma.coffeehouse.Voucher.DTO.AddVoucherDTO;
import com.tma.coffeehouse.Voucher.Mapper.AddVoucherMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddVoucherMapperTest {
    @Mock
    private ProductRepository productRepository;
    private AddVoucherMapper underTest;
    @BeforeEach
    void setUp() {
        underTest = new AddVoucherMapper(productRepository);
    }
    @Test
    void canConvertAddVoucherDTOToVoucher() {
        AddVoucherDTO.AddVoucherDTOBuilder builder = AddVoucherDTO.builder();
        // Given
        builder.start(new Date("2023-03-01T06:45:57.253Z"));
        builder.end(new Date("2023-03-10T06:45:57.253Z"));
        builder.limitNumber(500);
        builder.minOrderTotal(50000);
        builder.percentage(30.0F);
        builder.maxDiscount(25000);
        builder.productsId(new Long[] {1L, 2L});
        // When
        when(productRepository.findById(any(Long.class))).thenReturn(Optional.of(new Product()));
        Voucher voucher = underTest.dtoTOModel(builder.build());

        assertThat(voucher.getStart()).isNotNull();
        assertThat(voucher.getEnd()).isNotNull();
        assertThat(voucher.getPercentage()).isGreaterThan(0);
        assertThat(voucher.getLimitNumber()).isGreaterThan(0);
        assertThat(voucher.getProducts()).isNotEmpty();
        assertThat(voucher.getMaxDiscount()).isNotNull();
        assertThat(voucher.getMinOrderTotal()).isNotNull();

    }
}
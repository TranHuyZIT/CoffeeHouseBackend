package com.tma.coffeehouse.Voucher;

import com.tma.coffeehouse.Utils.CustomUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;


import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;


import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;


@DataJpaTest
class VoucherRepositoryTest {
    @Autowired
    private VoucherRepository undertest;
    @Test
    void findAllAvailable_ShouldReturnNotOverLimitAndNotExpired_Case1() {
        // Given
            // Valid
        Voucher voucher1 = new Voucher(1L,0.2F,1, CustomUtils.convertStringToDate("01-03-2023"), CustomUtils.convertStringToDate("10-03-2023"), 0, 0, new HashSet<>(), null, null);
            // Invalid
        Voucher voucher2 = new Voucher(2L, 0.2F, 10, CustomUtils.convertStringToDate("01-02-2023"), CustomUtils.convertStringToDate("2-02-2023"), 10000, 0, new HashSet<>(), null, null);
        Voucher record1 = undertest.save(voucher1);
        Voucher record2 = undertest.save(voucher2);

        // When
        Set<Voucher> valids = undertest.findAllAvailable(CustomUtils.getDateWithoutTime());
        // Then
        assertThat(valids).contains(record1);
        assertThat(valids).doesNotContain(record2);
        System.out.println("Test 1 passed!");

    }
    @Test
    void findAllAvailable_ShouldReturnNotOverLimitAndNotExpired_Case2() {
        // Given
        // Valid
        Voucher voucher1 = new Voucher(1L,0.2F,1, CustomUtils.convertStringToDate("01-03-2023"), CustomUtils.convertStringToDate("10-03-2023"), 0, 0, new HashSet<>(), null, null);
        // Invalid
        Voucher voucher2 = new Voucher(2L, 0.2F, 0, CustomUtils.convertStringToDate("01-03-2023"), CustomUtils.convertStringToDate("2-03-2023"), 10000, 0, new HashSet<>(), null, null);
        Voucher record1 = undertest.save(voucher1);
        Voucher record2 = undertest.save(voucher2);

        // When
        Set<Voucher> valids = undertest.findAllAvailable(CustomUtils.getDateWithoutTime());
        // Then
        assertThat(valids).contains(record1);
        assertThat(valids).doesNotContain(record2);
        System.out.println("Test 2 passed!");
    }
    @Test
    void findAllAvailable_ShouldReturnNotOverLimitAndNotExpired_Case3() {
        // Given
        // Valid
        Voucher voucher1 = new Voucher(1L,0.2F,0, CustomUtils.convertStringToDate("01-02-2023"), CustomUtils.convertStringToDate("10-02-2023"), 0, 0, new HashSet<>(), null, null);
        // Invalid
        Voucher voucher2 = new Voucher(2L, 0.2F, 0, CustomUtils.convertStringToDate("01-03-2023"), CustomUtils.convertStringToDate("2-03-2023"), 10000, 0, new HashSet<>(), null, null);
        Voucher record1 = undertest.save(voucher1);
        Voucher record2 = undertest.save(voucher2);

        // When
        Set<Voucher> valids = undertest.findAllAvailable(CustomUtils.getDateWithoutTime());
        System.out.println(valids);
        System.out.println(new Timestamp(new Date().getTime()));
        // Then
        assertThat(valids).doesNotContain(record1);
        assertThat(valids).doesNotContain(record2);
        System.out.println("Test 3 passed!");
    }
    @Test
    void findAllAvailable_ShouldReturnNotOverLimitAndNotExpired_Case4() {
        // Given
        // Valid
        Voucher voucher1 = new Voucher(1L,0.2F,1, CustomUtils.convertStringToDate("01-02-2023"), CustomUtils.convertStringToDate("10-02-2023"), 0, 0, new HashSet<>(), null, null);
        // Invalid
        Voucher voucher2 = new Voucher(2L, 0.2F, -1, CustomUtils.convertStringToDate("01-03-2023"), CustomUtils.convertStringToDate("2-03-2023"), 10000, 0, new HashSet<>(), null, null);
        Voucher record1 = undertest.save(voucher1);
        Voucher record2 = undertest.save(voucher2);
        System.out.println(voucher1);
        System.out.println(voucher2);

        // When
        Set<Voucher> valids = undertest.findAllAvailable(CustomUtils.getDateWithoutTime());
        System.out.println(valids);
        System.out.println(new Timestamp(new Date().getTime()));
        // Then
        assertThat(valids).doesNotContain(record1);
        assertThat(valids).doesNotContain(record2);
        System.out.println("Test 4 passed!");
    }
    @Test
    void findAllAvailable_ShouldReturnNotOverLimitAndNotExpired_Case5() {
        // Given
        // Valid
        Voucher voucher1 = new Voucher(1L,0.2F,1, CustomUtils.convertStringToDate("01-03-2023"), CustomUtils.convertStringToDate("01-03-2023"), 0, 0, new HashSet<>(), null, null);
        // Invalid
        Voucher voucher2 = new Voucher(2L, 0.2F, 0, CustomUtils.convertStringToDate("01-03-2023"), CustomUtils.convertStringToDate("01-03-2023"), 10000, 0, new HashSet<>(), null, null);
        Voucher record1 = undertest.save(voucher1);
        Voucher record2 = undertest.save(voucher2);
        System.out.println(voucher1);
        System.out.println(voucher2);

        // When
        Set<Voucher> valids = undertest.findAllAvailable(CustomUtils.getDateWithoutTime());
        System.out.println(valids);
        System.out.println(new Timestamp(new Date().getTime()));
        // Then
        assertThat(valids).contains(record1);
        assertThat(valids).doesNotContain(record2);
        System.out.println("Test 5 passed!");
    }
    @Test
    void findAllAvailable_ShouldReturnNotOverLimitAndNotExpired_Case6() {
        // Given
        // Valid
        Voucher voucher1 = new Voucher(1L,0.2F,10, CustomUtils.convertStringToDate("10-02-2023"), CustomUtils.convertStringToDate("1-02-2023"), 0, 0, new HashSet<>(), null, null);
        // Invalid
        Voucher voucher2 = new Voucher(2L, 0.2F, 1, CustomUtils.convertStringToDate("01-03-2023"), CustomUtils.convertStringToDate("2-03-2023"), 10000, 0, new HashSet<>(), null, null);
        Voucher record1 = undertest.save(voucher1);
        Voucher record2 = undertest.save(voucher2);
        System.out.println(voucher1);
        System.out.println(voucher2);

        // When
        Set<Voucher> valids = undertest.findAllAvailable(CustomUtils.getDateWithoutTime());
        System.out.println(valids);
        System.out.println(new Timestamp(new Date().getTime()));
        // Then
        assertThat(valids).doesNotContain(record1);
        assertThat(valids).contains(record2);
        System.out.println("Test 6 passed!");
    }

}
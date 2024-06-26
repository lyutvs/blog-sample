package com.example.stock.domain

import org.assertj.core.api.BDDAssertions.then
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.test.context.TestConstructor
import org.springframework.transaction.annotation.Transactional
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors

@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
//@Transactional
class StockServiceTest(
    private val stockService: StockService,
    private val stockRepository: StockRepository,
) {

    @Test
    fun `decrease `() {
        //given
        val stockId = stockRepository.save(
            Stock(
                productId = 1L,
                quantity = 100L
            )
        ).id!!

        //when
        stockService.decrease(
            stockId = stockId,
            quantity = 1L
        )

        //then
        val stock = stockRepository.findByIdOrNull(stockId)!!

        then(stock.quantity).isEqualTo(99)
    }

    @Test
    fun `동시에 100개 요청`() {
        //given
        val stockId = stockRepository.save(
            Stock(
                productId = 1L,
                quantity = 100L
            )
        ).id!!
        val threadCount = 100
        val latch = CountDownLatch(threadCount)


        //when
        val executorService = Executors.newFixedThreadPool(32)
        (1..threadCount).forEach { _ ->
            executorService.submit {
                try {
                    stockService.decrease(stockId, 1L)
                } finally {
                    latch.countDown()
                }
            }
        }

        latch.await()
        //then

        val stock = stockRepository.findByIdOrNull(stockId)!!
        println("quantity ${stock.quantity}")

        then(stock.quantity).isEqualTo(0)

    }

    @Test
    fun asdasdasd() {
        println("asdasdasda")
    }
}
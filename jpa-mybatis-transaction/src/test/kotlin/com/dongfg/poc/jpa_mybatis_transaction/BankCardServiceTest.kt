package com.dongfg.poc.jpa_mybatis_transaction

import com.dongfg.poc.jpa_mybatis_transaction.entity.BankCard
import com.github.javafaker.Faker
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.jdbc.core.JdbcTemplate
import java.util.*

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class BankCardServiceTest {

    @Autowired
    private lateinit var bankCardService: BankCardService

    @Autowired
    private lateinit var bankCardRepository: BankCardRepository

    @Autowired
    private lateinit var jdbcTemplate: JdbcTemplate

    private val fakeRecordSize = 11
    private val recordIdList = arrayListOf<Long>()

    private val faker = Faker(Locale.CHINA)

    @BeforeAll
    fun prepare() {
        repeat(fakeRecordSize) {
            val id = bankCardService.create(
                BankCard(
                    holderName = faker.name().fullName(),
                    holderIdNumber = faker.idNumber().valid(),
                    bankName = faker.finance().iban(),
                    cardNo = faker.finance().creditCard(),
                    cardMobile = faker.phoneNumber().cellPhone()
                )
            )
            recordIdList.add(id)
        }
        val count = bankCardRepository.count()
        assertTrue(count >= fakeRecordSize)
    }

    @AfterAll
    fun cleanup() {
        recordIdList.forEach {
            jdbcTemplate.execute("delete from bank_card where id = $it")
        }
    }


    @Test
    fun jpaWriteMybatisRead() {
        val id = recordIdList.random()
        val cardBefore = bankCardRepository.findByIdOrNull(id)
        assertNotNull(cardBefore)
        println(cardBefore!!)

        cardBefore.apply {
            holderName = faker.name().fullName()
            holderIdNumber = faker.idNumber().valid()
            bankName = faker.finance().iban()
            cardNo = faker.finance().creditCard()
            cardMobile = faker.phoneNumber().cellPhone()
        }

        val cardAfter = bankCardService.jpaWriteMybatisRead(cardBefore)

        assertNotNull(cardAfter)
        println(cardAfter)
        assertEquals(cardBefore.holderName, cardAfter.holderName)
        assertEquals(cardBefore.holderIdNumber, cardAfter.holderIdNumber)
        assertEquals(cardBefore.bankName, cardAfter.bankName)
        assertEquals(cardBefore.cardNo, cardAfter.cardNo)
        assertEquals(cardBefore.cardMobile, cardAfter.cardMobile)
    }

    @Test
    fun mybatisWriteJpaRead() {
        val id = recordIdList.random()
        val cardBefore = bankCardRepository.findByIdOrNull(id)
        assertNotNull(cardBefore)
        println(cardBefore!!)

        cardBefore.apply {
            holderName = faker.name().fullName()
            holderIdNumber = faker.idNumber().valid()
            bankName = faker.finance().iban()
            cardNo = faker.finance().creditCard()
            cardMobile = faker.phoneNumber().cellPhone()
        }

        val cardAfter = bankCardService.mybatisWriteJpaRead(cardBefore)

        assertNotNull(cardAfter)
        println(cardAfter)
        assertEquals(cardBefore.holderName, cardAfter.holderName)
        assertEquals(cardBefore.holderIdNumber, cardAfter.holderIdNumber)
        assertEquals(cardBefore.bankName, cardAfter.bankName)
        assertEquals(cardBefore.cardNo, cardAfter.cardNo)
        assertEquals(cardBefore.cardMobile, cardAfter.cardMobile)
    }

    @Test
    fun rollbackMethod1() {
        val id = recordIdList.random()
        val card0 = bankCardRepository.findByIdOrNull(id)
        assertNotNull(card0)
        println("Card0: ${card0!!}")

        val card1 = BankCard(
            id = card0.id,
            createdAt = card0.createdAt,
            holderName = faker.name().fullName(),
            holderIdNumber = faker.idNumber().valid(),
            bankName = faker.finance().iban(),
            cardNo = faker.finance().creditCard(),
            cardMobile = faker.phoneNumber().cellPhone()
        )
        println("Card1: $card1")

        val card2 = BankCard(
            id = card0.id,
            createdAt = card0.createdAt,
            holderName = faker.name().fullName(),
            holderIdNumber = faker.idNumber().valid(),
            bankName = faker.finance().iban(),
            cardNo = faker.finance().creditCard(),
            cardMobile = faker.phoneNumber().cellPhone()
        )
        println("Card2: $card2")

        val cardAfter = bankCardService.rollbackMethod1(id, card1, card2)
        assertNotNull(cardAfter)
        println(cardAfter)
        assertEquals(card0.holderName, cardAfter.holderName)
        assertEquals(card0.holderIdNumber, cardAfter.holderIdNumber)
        assertEquals(card0.bankName, cardAfter.bankName)
        assertEquals(card0.cardNo, cardAfter.cardNo)
        assertEquals(card0.cardMobile, cardAfter.cardMobile)
    }

    @Test
    fun rollbackMethod2() {
        val id = recordIdList.random()
        val card0 = bankCardRepository.findByIdOrNull(id)
        assertNotNull(card0)
        println("Card0: ${card0!!}")

        val card1 = BankCard(
            id = card0.id,
            createdAt = card0.createdAt,
            holderName = faker.name().fullName(),
            holderIdNumber = faker.idNumber().valid(),
            bankName = faker.finance().iban(),
            cardNo = faker.finance().creditCard(),
            cardMobile = faker.phoneNumber().cellPhone()
        )
        println("Card1: $card1")

        val card2 = BankCard(
            id = card0.id,
            createdAt = card0.createdAt,
            holderName = faker.name().fullName(),
            holderIdNumber = faker.idNumber().valid(),
            bankName = faker.finance().iban(),
            cardNo = faker.finance().creditCard(),
            cardMobile = faker.phoneNumber().cellPhone()
        )
        println("Card2: $card2")

        val cardAfter = bankCardService.rollbackMethod2(id, card1, card2)
        println("Card0: $card0")
        assertNotNull(cardAfter)
        println(cardAfter)
        assertEquals(card0.holderName, cardAfter.holderName)
        assertEquals(card0.holderIdNumber, cardAfter.holderIdNumber)
        assertEquals(card0.bankName, cardAfter.bankName)
        assertEquals(card0.cardNo, cardAfter.cardNo)
        assertEquals(card0.cardMobile, cardAfter.cardMobile)
    }
}
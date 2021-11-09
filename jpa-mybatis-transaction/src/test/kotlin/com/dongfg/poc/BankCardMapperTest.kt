package com.dongfg.poc

import com.dongfg.poc.entity.BankCard
import com.github.javafaker.Faker
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.transaction.annotation.Transactional
import java.util.*

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
internal class BankCardMapperTest {
    @Autowired
    private lateinit var bankCardService: BankCardService

    @Autowired
    private lateinit var bankCardRepository: BankCardRepository

    @Autowired
    private lateinit var bankCardMapper: BankCardMapper

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
        Assertions.assertTrue(count >= fakeRecordSize)
    }

    @AfterAll
    fun cleanup() {
        recordIdList.forEach {
            jdbcTemplate.execute("delete from bank_card where id = $it")
        }
    }

    @Test
    fun findById() {
        val id = recordIdList.random()
        val card = bankCardMapper.findById(id)
        assertNotNull(card)
        println(card)
    }

    @Test
    fun updateById() {
        val id = recordIdList.random()
        val cardBefore = bankCardMapper.findById(id)
        assertNotNull(cardBefore)
        println(cardBefore!!)
        bankCardMapper.updateById(cardBefore.apply {
            holderName = faker.name().fullName()
            holderIdNumber = faker.idNumber().valid()
            bankName = faker.finance().iban()
            cardNo = faker.finance().creditCard()
            cardMobile = faker.phoneNumber().cellPhone()
        })
        val cardAfter = bankCardMapper.findById(id)
        assertNotNull(cardAfter)
        println(cardAfter!!)
        assertEquals(cardBefore.holderName, cardAfter.holderName)
        assertEquals(cardBefore.holderIdNumber, cardAfter.holderIdNumber)
        assertEquals(cardBefore.bankName, cardAfter.bankName)
        assertEquals(cardBefore.cardNo, cardAfter.cardNo)
        assertEquals(cardBefore.cardMobile, cardAfter.cardMobile)
    }
}
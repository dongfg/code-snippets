package com.dongfg.poc

import com.dongfg.poc.entity.BankCard
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
import org.springframework.transaction.annotation.Transactional
import java.util.*

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
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
    fun create() {
        val card = BankCard(
            holderName = faker.name().fullName(),
            holderIdNumber = faker.idNumber().valid(),
            bankName = faker.finance().iban(),
            cardNo = faker.finance().creditCard(),
            cardMobile = faker.phoneNumber().cellPhone()
        )
        val id = bankCardService.create(card)
        val record = bankCardRepository.findByIdOrNull(id)
        assertNotNull(record)
        record!!
        assertNotNull(record.createdAt).also { println("created: ${record.createdAt}") }
        assertNotNull(record.isDelete)

        assertFalse(record.isDelete!!)
        assertNull(record.updatedAt)
        assertNull(record.deletedAt)
    }

    @Test
    fun update() {
        val recordBefore = bankCardRepository.findByIdOrNull(recordIdList.random())
        assertNotNull(recordBefore)
        recordBefore!!

        recordBefore.apply {
            holderName = faker.name().fullName()
            holderIdNumber = faker.idNumber().valid()
            bankName = faker.finance().iban()
        }

        bankCardService.update(recordBefore)

        val recordAfter = bankCardRepository.findByIdOrNull(recordBefore.id!!)
        assertNotNull(recordAfter)
        recordAfter!!
        assertNotNull(recordAfter.updatedAt).also {
            println("updated: ${recordAfter.updatedAt}")
        }

        assertEquals(recordBefore.holderName, recordAfter.holderName)
        assertEquals(recordBefore.holderIdNumber, recordAfter.holderIdNumber)
        assertEquals(recordBefore.bankName, recordAfter.bankName)
    }

    @Test
    fun delete() {
        val countBefore = bankCardRepository.count()
        val deleteId = recordIdList.random()
        bankCardService.delete(deleteId)
        val countAfter = bankCardRepository.count()
        assertEquals(1, countBefore - countAfter)

        val deletedRecord = bankCardService.findDeleted()

        assertEquals(1, deletedRecord.size)
        val c = deletedRecord[0]
        println(c)
        assertNotNull(c.isDelete)
        assertTrue(c.isDelete!!)
    }
}
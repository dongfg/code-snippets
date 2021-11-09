package com.dongfg.poc

import com.dongfg.poc.entity.BankCard
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import javax.persistence.EntityManager

interface BankCardRepository : CrudRepository<BankCard, Long>

@Service
class BankCardService {
    @Autowired
    private lateinit var bankCardRepository: BankCardRepository

    @Autowired
    private lateinit var entityManager: EntityManager

    fun create(card: BankCard): Long {
        bankCardRepository.save(card.apply {
            userId = -1
            createdAt = LocalDateTime.now()
        })
        return card.id!!
    }

    fun update(card: BankCard) {
        bankCardRepository.save(card.apply {
            updatedAt = LocalDateTime.now()
        })
    }

    fun delete(id: Long) {
        bankCardRepository.deleteById(id)
    }

    @Suppress("UNCHECKED_CAST")
    fun findDeleted(): List<BankCard> {
        return entityManager.createNativeQuery(
            "select * from bank_card where is_delete = true",
            BankCard::class.java
        ).resultList as List<BankCard>
    }
}
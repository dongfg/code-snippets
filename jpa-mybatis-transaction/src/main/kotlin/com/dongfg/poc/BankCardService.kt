package com.dongfg.poc

import com.dongfg.poc.entity.BankCard
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Select
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

interface BankCardRepository : JpaRepository<BankCard, Long>

@Mapper
interface BankCardMapper {
    @Select("select * from bank_card where id = #{id}")
    fun findById(id: Long): BankCard?
    fun updateById(bankCard: BankCard): Int
}

@Service
class BankCardService {
    @Autowired
    private lateinit var bankCardRepository: BankCardRepository

    @Autowired
    private lateinit var bankCardMapper: BankCardMapper

    @Autowired
    private lateinit var bankCardServiceTransaction: BankCardServiceTransaction

    fun create(card: BankCard): Long {
        bankCardRepository.save(card.apply {
            userId = -1
            createdAt = LocalDateTime.now()
        })
        return card.id!!
    }

    /**
     * 默认情况：JPA 未提交， Mybatis 查询不到
     */
    @Transactional
    fun jpaWriteMybatisRead(card: BankCard): BankCard {
        bankCardRepository.save(card)
        bankCardRepository.flush()
        return bankCardMapper.findById(card.id!!)!!
    }

    /**
     * 默认情况：Mybatis 自动提交， JPA 可以查询到
     */
    @Transactional
    fun mybatisWriteJpaRead(card: BankCard): BankCard {
        bankCardMapper.updateById(card)
        return bankCardRepository.findByIdOrNull(card.id!!)!!
    }

    fun rollbackMethod1(id: Long, card1: BankCard, card2: BankCard): BankCard {
        try {
            bankCardServiceTransaction.rollbackMethod1(card1, card2)
        } catch (e: Exception) {
            println(e.message ?: "事务回滚")
        }
        return bankCardRepository.findByIdOrNull(id)!!
    }

    fun rollbackMethod2(id: Long, card1: BankCard, card2: BankCard): BankCard {
        try {
            bankCardServiceTransaction.rollbackMethod2(card1, card2)
        } catch (e: Exception) {
            println(e.message ?: "事务回滚")
        }
        return bankCardMapper.findById(id)!!
    }
}
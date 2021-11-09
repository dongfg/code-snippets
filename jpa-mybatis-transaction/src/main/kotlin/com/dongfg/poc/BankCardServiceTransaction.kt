package com.dongfg.poc

import com.dongfg.poc.entity.BankCard
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

@Service
class BankCardServiceTransaction {
    @Autowired
    private lateinit var bankCardRepository: BankCardRepository

    @Autowired
    private lateinit var bankCardMapper: BankCardMapper

    @Transactional
    @Throws(RuntimeException::class)
    fun rollbackMethod1(card1: BankCard, card2: BankCard) {
        bankCardMapper.updateById(card1)
        bankCardRepository.saveAndFlush(card2)
        // always true
        card1.id?.let {
            throw RuntimeException("手动回滚")
        }
    }

    @Transactional
    @Throws(RuntimeException::class)
    fun rollbackMethod2(card1: BankCard, card2: BankCard) {
        bankCardRepository.save(card1)
        bankCardMapper.updateById(card2)
        // always true
        card1.id?.let {
            throw RuntimeException("手动回滚")
        }
    }
}
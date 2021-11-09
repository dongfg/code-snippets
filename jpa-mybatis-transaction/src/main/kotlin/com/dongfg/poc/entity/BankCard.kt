package com.dongfg.poc.entity

import java.time.LocalDateTime
import javax.persistence.*

/**
 * 银行卡
 * @property id 自增ID
 * @property userId 用户ID
 * @property holderName 户主姓名
 * @property holderIdNumber 户主身份证号
 * @property bankName 开户行
 * @property cardNo 银行卡号
 * @property cardMobile 银行预留手机号
 * @property isDelete 删除标识
 * @property createdAt 创建时间
 * @property updatedAt 更新时间
 * @property deletedAt 删除时间
 */
@Entity
@Table(name = "`bank_card`")
class BankCard(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    var userId: Long? = null,
    var holderName: String,
    var holderIdNumber: String? = null,
    var bankName: String? = null,
    var cardNo: String,
    var cardMobile: String? = null,
    var createdAt: LocalDateTime? = null,
    var updatedAt: LocalDateTime? = null,
    var isDelete: Boolean? = false,
    var deletedAt: LocalDateTime? = null,
) {
    override fun toString(): String {
        return "BankCard(id=$id, userId=$userId, holderName='$holderName', holderIdNumber=$holderIdNumber, bankName=$bankName, cardNo='$cardNo', cardMobile=$cardMobile, createdAt=$createdAt, updatedAt=$updatedAt, isDelete=$isDelete, deletedAt=$deletedAt)"
    }
}
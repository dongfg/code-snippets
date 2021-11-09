# JPA 软删除

### Entity 添加

```text
@SQLDelete(sql = "update bank_card set is_delete = true, deleted_at = CURRENT_TIMESTAMP where id = ?")
@Where(clause = "is_delete = false")
```

### 查询已删除的数据
```kotlin
entityManager.createNativeQuery(
    "select * from bank_card where is_delete = true",
    BankCard::class.java)
```
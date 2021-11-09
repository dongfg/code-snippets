# Jackson 字段读写控制

### 只读

- 不反序列化传入的值，即传入的值无效

```text
@JsonProperty(access = JsonProperty.Access.READ_ONLY)
```

### 只写

- 接受值传入，但序列化时不输出

```text
@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
```
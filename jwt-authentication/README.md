# jwt authentication

### Tips

- Authentication 认证对象，Principal（用户名）、Credentials（密码、Token）以及是否已认证
- AuthenticationProvider 验证 Authentication 是否合法，从未认证到已认证，比如验证密码
- Filter
    - 构造 Authentication
    - AuthenticationProvider 验证
    - **SecurityContextHolder.getContext().setAuthentication**
- UserDetailsService 获取用户
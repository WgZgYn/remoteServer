## Postgresql服务器
### 重点理解`Entity`中的对应关系
## 网页服务器
## Bug
*  ### AccountRole
    #### 说明
    枚举值大小写敏感   
    #### 修复
    暂时使用小写
*  ### Repository Lazy
    #### 说明
    经过测试,在测试中如何使用`Lazy`可能会有`could not initialize proxy`异常
    #### 修复
    暂时将所有Lazy转为EAGER
*  ### postgresql 自定义类型AccountRole的映射
    #### 说明
    存在映射错误
    #### 修复
    修改为内置类型
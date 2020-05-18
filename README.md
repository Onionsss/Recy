# Recy
## 更便捷的RecyclerView使用
 [ ![Recy](https://img.shields.io/badge/Recy-1.0.0-orange.svg) ](https://bintray.com/zhang759308541/base/Recy/_latestVersion)
```
依赖方式      
implementation 'com.onion:Recy:1.0.0'
```

```
基础用法 1, 单类型 单布局

实体类
class User(var name: String){}

RecyclerView初始化 

recy.linear().setup { addType<User>( R.layout.item_user ) }

--- recy -> RecyclerView
--- linear -> 使用LinearLayoutManager,默认Vertical

新数据
recy.baseAdapter.models = arrayListOf( User(name = "Linear"), User(name = "Grid"))
增加数据
recy.baseAdapter.addModels(arrayListOf( User(name = "State"), User(name = "Cached")))
```

# Recy
## 更便捷的RecyclerView使用,丢弃Adapter
 [ ![Recy](https://img.shields.io/badge/Recy-1.0.0-orange.svg) ](https://bintray.com/zhang759308541/base/Recy/_latestVersion)
```
依赖方式      
implementation 'com.onion:Recy:1.0.0'
```

```
## 基础用法1, 单类型 单布局

实体类
class User(var name: String){}

RecyclerView初始化 
--- Java代码 ---
recy.linear().setup { addType<User>( R.layout.item_user ) }

--- recy -> RecyclerView
--- linear -> 使用LinearLayoutManager,默认Vertical

新数据
recy.baseAdapter.models = arrayListOf( User(name = "Linear"), User(name = "Grid"))
增加数据
recy.baseAdapter.addModels(arrayListOf( User(name = "State"), User(name = "Cached")))
```
```
## 基础用法2,多布局
### 01 - 单类型多布局
实体类继承Mult 实现getMultType方法,返回对应的布局
class User(var name: String,var type: Int = 1): Mult {

    override fun getMultType(): Int {
        //根据type区分不同的布局
        when(type){
            1 -> {
                return R.layout.item_user //布局1
            }

            2 -> {
                return R.layout.item_user2 //布局2
            }
        }
        return R.layout.item_system2
    }

}

--- Java代码 ---
无需SetUp设置方法,以下只是个别方法
recy.linear().setup {
  empty() //添加空布局
  enableMult() //使用mult多布局, 可不写,默认true
}
当加载数据时,会自动根据Mult里getMultType返回对应的布局

### 02 - 多类型多布局
实体类1
class User(var name: String,var type: Int = 1): Mult {

    override fun getMultType(): Int {
        //根据type区分不同的布局
        when(type){
            1 -> {
                return R.layout.item_user //布局1
            }

            2 -> {
                return R.layout.item_user2 //布局2
            }
        }
        return R.layout.item_system2
    }

}

实体类2
class System(var name: String,var type: Int): Mult {

    override fun getMultType(): Int {
        when(type){
            1 -> {
                return R.layout.item_system1
            }

            2 -> {
                return R.layout.item_system2
            }
        }
        return R.layout.item_system2
    }
}

--- Java代码 ---
recy.grid(spanCount = 2) // Grid布局 每行2个元素

添加数据
recy.baseAdapter.models = arrayListOf( User(name = "Linear",type = 1), User(name = "Grid", type = 2),System(name = "Flow",type = 1), System(name = "Iuos", type = 2) )

当你添加数据时,会自动根据getMultType显示不用的布局,无需任何其他操作
```

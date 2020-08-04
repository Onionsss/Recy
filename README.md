# Recy
## 更便捷的RecyclerView使用,丢弃Adapter
 [ ![Recy](https://img.shields.io/badge/Recy-1.0.0-orange.svg) ](https://bintray.com/zhang759308541/base/Recy/_latestVersion)
 [ ![Apache](https://img.shields.io/badge/%20Apache-2.0-blue.svg) ](https://bintray.com/zhang759308541/base/Recy/_latestVersion)
 [ ![Sb](https://img.shields.io/badge/sb-cnmlgb-ff69b4.svg) ](https://bintray.com/zhang759308541/base/Recy/_latestVersion)

## 依赖方式       
```
implementation 'com.onion:Recy:1.0.4'
```
## 1.0.5版本更新日志
### 修改初始化 Library.init()变成Recy.init()

## 1.0.1版本更新日志
### 增加空布局配置,和自定义空布局
#### 01 - 空布局,无数据展示页面,onReload为点击了系统空布局页面做出的操作

```
recy.grid(spanCount = 1)
            .setup {
                addType<User> { R.layout.item_user }
                empty() //添加空布局
            }.onReload {
                //TODO 当点击当前系统默认空页面进行的逻辑
                // 比如加载数据
            }
            
当recy.baseAdapter.models = arrayListOf()时,加载空数据时会自动显示系统的空页面

01.1 - 初始化时修改系统空界面某些配置
empty(bg = Color.BLACK,img = R.mipmap.empty,text = "暂无数据") //添加空布局,修改配置

01.2 动态修改空布局配置
recy.baseAdapter.empty.text = "数据为空!"  //修改文字
recy.baseAdapter.empty.bg = Color.BLUE   //修改背景颜色
recy.baseAdapter.empty. = R.mipmap.empty_02 //修改图片
```
#### 02 - 自定义空布局
```
当框架自带的空布局无法满足时,用户可以自定义空布局
02.1 定义空布局数据类,实现Empty接口,继承BaseObservable接口,为了动态更新,如不需要则不需要继承如下:
data class SonEmpty(var vTips: String): BaseObservable(),Empty{

    @Bindable
    var tips: String = vTips
        set(value) {
            field = value
            notifyPropertyChanged(BR.tips)
        }

}
创建sonEmpty类
val empty = SonEmpty("没有数据")
使用customEmpty方法 如下:
recy.grid(spanCount = 1)
            .setup {
                addType<User> { R.layout.item_user }
                customEmpty<SonEmpty>(empty,R.layout.recy_empty_page1) //自定义空布局,传入数据类和布局
            }.onBind {
                false
            }.onEmpty {
              //在此能对自定义空布局进行操作
            }
xml代码: 
<?xml version="1.0" encoding="utf-8"?>
<layout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools">

    <data>

        <variable
            name="m"
            type="com.onion.zrecy.SonEmpty" />
    </data>

    <LinearLayout
        android:id="@+id/recy_empty_root"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:background="#ff00ff"
        android:gravity="center"
        android:orientation="vertical"
        android:paddingTop="50dp"
        android:paddingBottom="50dp"
        android:visibility="visible">

        <TextView
            android:id="@+id/empty_text"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_marginTop="10dp"
            android:gravity="center"
            android:text="@{m.tips}"
            android:textColor="#ffffff"
            android:textSize="14sp" />
    </LinearLayout>
</layout>

同样,我们可以动态的更新empty
empty.tips = "我更新了 没有数据!"
```
03 - 自定义type作为空布局
```
该方法比较麻烦,参考多布局
recy.grid(spanCount = 1)
            .setup {
                addType<User> { R.layout.item_user }
                addType<SonEmpty> { R.layout.recy_empty_page1 }
            }.onBind {
                when(itemViewType){
                    //对空布局做操作
                    R.layout.recy_empty_page1 -> {
                        //TODO
                    }
                }
                false
            }
            
//当没有数据时,传入一个空布局数据,有且只有一个元素
recy.baseAdapter.models = arrayListOf( SonEmpty(vTips = "没有数据"))
```
# 接入配置!!!
## Application中写入
```
Library.init(this)
```
## 基础用法1, 单类型 单布局
```
实体类
class User(var name: String){}

RecyclerView初始化 
--- Kotlin代码 ---
recy.linear().setup { addType<User>( R.layout.item_user ) }

--- recy -> RecyclerView
--- linear -> 使用LinearLayoutManager,默认Vertical

新数据
recy.baseAdapter.models = arrayListOf( User(name = "Linear"), User(name = "Grid"))
增加数据
recy.baseAdapter.addModels(arrayListOf( User(name = "State"), User(name = "Cached")))
```
## 基础用法2,多布局 
```
01 - 单类型多布局
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

--- Kotlin代码 ---
无需setup设置方法,以下只是个别方法
recy.linear().setup { 
  empty() //添加空布局
  enableMult() //使用mult多布局, 可不写,默认true
}
当加载数据时,会自动根据Mult里getMultType返回对应的布局

----------------------------------------------------    分割线   ----------------------------------------------

02 - 多类型多布局
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

--- Kotlin代码 ---
recy.grid(spanCount = 2) // Grid布局 每行2个元素

添加数据
recy.baseAdapter.models = arrayListOf( User(name = "Linear",type = 1), User(name = "Grid", type = 2)
,System(name = "Flow",type = 1), System(name = "Iuos", type = 2) )

当你添加数据时,会自动根据getMultType显示不用的布局,无需任何其他操作,如上,界面上会显示四种不同的布局
```
## 配合DataBinding 展示数据
```
以下简单Databinding使用,更多用法参考官方文档
1. 使用databinding:
module下的build.gradle中android节点下加入以下代码
dataBinding {
   enabled = true
}
2.实体类
class User(var name: String,var type: Int = 1): Mult { 

    override fun getMultType(): Int {
        when(type){
            1 -> {
                return R.layout.item_user
            }

            2 -> {
                return R.layout.item_user2
            }
        }
        return R.layout.item_system2
    }

    val xName: String? get() {
        return "我的名字是: ${name}"
    }
}

3. xml 添加layout节点
<?xml version="1.0" encoding="utf-8"?>
<layout>
    //绑定的数据
    <data>
    //必须为m
    //绑定的实体类
        <variable
            name="m" 
            type="com.onion.zrecy.User" /> 
    </data>
    // 你的布局
    <LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
        android:layout_width="match_parent"
        android:layout_height="wrap_content">
        //该TextView即可显示name
        <TextView
            android:text="@{m.name}"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"/>
         // 使用自定义属性
         <TextView
            android:text="@{m.xName}"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"/>
    </LinearLayout>
</layout>
```
## 配合onBind函数使用
```
当xml绑定不满足我们的使用需求时,我们也可以像adapter一样直接执行onBind事件
当初始化RecyclerView时 绑定onBind,如下:
如果是单布局
recy.grid(spanCount = 1)
            .setup {
                addType<User> { R.layout.item_user }
                empty()
            }.onBind {
                // todo
                val model = getModel<User>()  //拿到model
                val binding = getViewDataBinding<ItemUserBinding>() //拿到当前布局
                adapterPosition //当前position 如果使用了header和footer 请使用modelPosition
                modelPosition //当前position 去除了header和footer
                // xxxx代码
                false
            }
            
如果是多布局,请直接使用当前布局id判断,如下:
recy.linear(orientation = LinearLayout.HORIZONTAL)
            .setup {
                addType<User>( R.layout.item_user)
                addType<System>( R.layout.item_system1)
            }.onBind {
                // itemViewType就是布局id
                when(itemViewType){
                    R.layout.item_user -> {
                        val model = getModel<User>()  //拿到model
                        val binding = getViewDataBinding<ItemUserBinding>() //拿到当前user布局
                    }

                    R.layout.item_system1 -> {
                        val model = getModel<System>()  //拿到model
                        val binding = getViewDataBinding<ItemSystem1Binding>() //拿到当前system布局
                    }
                }
                false
            }
```
## Header和Footer
```
recy.linear()
            .setup {
                addHeader(TextView(this))
                addFooter(TextView(this))
                removeHeader(TextView(this))
                removeFooter(TextView(this))
            }
```
## 点击事件,长按事件
```
直接注册,单布局和多布局都是一样,不注册是无法触发点击事件的
1.单击事件注册: 
recy.baseAdapter.onClick(R.id.item_root,R.id.item_root2){
            when(it){
                R.id.item_root -> {
                    val model = getModel<User>()  //拿到model
                    val binding = getViewDataBinding<ItemUserBinding>() //拿到当前user布局
                }
                R.id.item_root2 -> {
                    
                }
            }
        }
        
2.长按事件注册:
recy.baseAdapter.onLongClick()
recy.baseAdapter.addFastClickable(R.id.xxx) //500ms内在此点击无效
```

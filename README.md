# Recy

# FlowLayout
 [ ![FlowLayout](https://img.shields.io/badge/FlowLayout-1.0.0-orange.svg) ](https://bintray.com/zhangqiaa/maven/FlowLayout/_latestVersion)
```
依赖方式      implementation 'com.onion:Recy:1.0.0'
```
```
xml
```
```
<com.onion.flow.FlowLayout
android:id="@+id/flow"
android:background="@color/colorPrimary"
app:flow_textColor="#ffffff"
app:flow_textSize="14sp"
app:flow_backgroundRes="@drawable/select"
app:flow_marginLeft="10dp"
app:flow_paddingLeft="10dp"
android:layout_width="match_parent"
android:layout_height="wrap_content">
```
```
Java
```
```
val data = mutableListOf("梦幻西游","魔兽世界","京东","美团","饿了么","魔兽世界之征途","呵呵","一一一一","QQ飞车","DNF","京东"
          ,"我的世界","360应用市场","美国之心","Windows","呵呵呵")
  flow.setData(data)
  flow.start()
  flow.flowListener = object : FlowClickListener{
           override fun onClick(title: String, position: Int) {
               Toast.makeText(this@MainActivity,"$title-$position",Toast.LENGTH_LONG).show()
           }
       }
```
![image](https://github.com/Onionsss/FlowLayout/blob/master/images/bh.png)

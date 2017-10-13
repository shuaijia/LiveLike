# LiveLike
视频直播 点赞效果  使用三阶贝塞尔曲线控制运动曲线

## 效果

![image](https://github.com/shuaijia/LiveLike/blob/master/img/ccc.gif)

## 使用

### 1、添依赖

```
allprojects {
	repositories {
		...
	    maven { url 'https://www.jitpack.io' }
	}
}
```


```
dependencies {
	compile 'com.github.shuaijia:LiveLike:v1.0'
}
```


### 2、布局中使用

```
<com.jia.jslivelike.FavorLayout
	android:id="@+id/favor_layout"
	android:layout_width="match_parent"
	android:layout_height="match_parent" />
```

和普通view一样使用就ok了

### 3、代码中
```
FavorLayout favor_layout= (FavorLayout) findViewById(R.id.favor_layout);

// 按钮点击  发点赞
fab.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        // 点赞
        favor_layout.addFavor();
    }
});
```
这样就可以实现上图的效果了！

### 欢迎关注我的微信公众号————安卓干货营   获取更多精彩内容！
![image](https://github.com/shuaijia/LiveLike/blob/master/img/ddd.jpg)

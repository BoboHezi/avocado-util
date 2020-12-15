# avocado-util
鳄梨的安卓开源库，主要包括各种工具类和常用的自定义组件，持续更新中......

[![面向Android开发](https://img.shields.io/badge/%E9%9D%A2%E5%90%91-Android%E5%BC%80%E5%8F%91-%232CC159.svg)]()
[![JCenter](https://img.shields.io/badge/JCenter-Bintray-43a047)](https://bintray.com/eli-avocado)

***
### Author: Eli Chang
### E-mail: eliflichang@gmail.com
***

| 版本 | 日期 | 人员 | 内容 |
| :--- | ---------- | ---------- | ---------- |
|  V0.0.1 | 2020.12.07 | Eli Chang | 初版 |

# Gradle引用
```
dependencies {
    implementation 'com.github.eli:AvocadoUtils:0.0.1'
}
```

# CircleImageView 使用方法

xml布局
```xml
    <eli.avocado.widget.CircleImageView
        android:layout_width="100dp"
        android:layout_height="100dp"
        android:src="@mipmap/thumbnil"
        app:radius="50dp"
        app:borderColor="#666"
        app:borderWidth="2px"/>
```
> radius: 圆角尺寸  
> borderColor: 边框颜色  
> borderWidth: 边框宽度

activity中使用
```java
CircleImageView circleImage = findViewById(R.id.circle_image);

// 设置边框颜色
circleImage.setBorderColor(borderColor);
// 设置边框宽度
circleImage.setBorderWidth(borderWidth);
// 设置圆角尺寸
circleImage.setRadius(radius);
```


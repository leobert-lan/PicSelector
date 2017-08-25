# PicSelector

Android 图片、视频选择库 PVSelector

inspired by [PictureSelector](https://github.com/LuckSiege/PictureSelector)

使用支持博客： [CSDN](http://blog.csdn.net/a774057695/article/details/77533120)

[ReadMe in English](https://github.com/leobert-lan/PicSelector/blob/master/README.md)

## Feature 概览

###图片

* 单图、多图选择模式
* 即时拍照
* 图片裁剪 使用开源项目[UCrop](https://github.com/Yalantis/uCrop)
* 图片压缩 可使用：
	* 系统bitmap的API进行压缩
	* 开源项目[LuBan](https://github.com/Curzibn/Luban)进行压缩
* 图片预览，依赖[PhotoView]版本较旧

###视频

* 单个、多个视频选择模式
* 预览

###风格配置

* QQ风格 or WeChat 风格
* 文字、配色的配置

## 集成
当前最新版本：

* 扩展的UCrop module：2.4.0
* PVSelectorlib：1.1.0

编译包托管于JFog-JCenter

使用gradle引入依赖：

如果无法拉取到文件，显式的配置下仓库地址

```
allprojects {
    repositories {
        mavenCentral()
        jcenter {
            url "http://jcenter.bintray.com/"
        }
    }
}
```

添加依赖，**注意需要添加Glide**

```
dependencies {
	   //...
    compile 'individual.leobert.libs:pvselectorlib:1.1.0'
    compile 'individual.leobert.libs:ucrop:2.4.0'
    compile 'com.github.bumptech.glide:glide:3.7.0'
}
```

## 使用

权限声明：

```
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.CAMERA" />
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.MOUNT_UNMOUNT_FILESYSTEMS" />
   
```

manifest文件添加FileProvider配置（适配Android 7.0 私有目录）


```
 <provider
            android:name="android.support.v4.content.FileProvider"
            android:authorities="${applicationId}.provider"
            android:exported="false"
            android:grantUriPermissions="true">
            <meta-data
                android:name="android.support.FILE_PROVIDER_PATHS"
                android:resource="@xml/file_paths" />
        </provider>
```


配置式使用参考[sample](https://github.com/leobert-lan/PicSelector/blob/master/PicSelectorDemo/app/src/main/java/thirdparty/leobert/picselectordemo/DemoActivity.java)
关键在于配置`FunctionConfig`。
 
启动：
```
PictureConfig.init(config);
                    PictureConfig.getPictureConfig().openPhoto(mContext, resultCallback);
```

链式调用：

```
//video
 PVSelector.getVideoSelector(this)
     .multiSelect(4)
     .enableCamera()
     .enablePreview()              
     .setCompleteTxtColor(ContextCompat.getColor(this,
        R.color.colorPrimary))
     .launch(resultCallback);

//picture
 PVSelector.getPhotoSelector(this)
                .singleSelect()
                .enableCamera()
                .enableCrop(FunctionConfig.CROP_MODE_16_9)
                .useSystemCompress(true,true)
                .setSelectedMedia(selectMedia) //已经选择的内容
                .launch(resultCallback);
```

关于UI的配置,API 如下：

```
public interface ICustomStyle<T> {

    /**
     * @param spanCount 单行数量上限
     */
    T setImageSpanCount(int spanCount);

    /**
     * @param mainColor 主色 Context.getColor(resId)
     */
    T setThemeColor(@ColorInt int mainColor);

    /**
     * 设置选择图片页面底部背景色
     *
     * @param color
     */
    T setBottomBarBgColor(@ColorInt int color);


    /**
     * "预览"文字颜色
     *
     * @param color
     */
    T setPreviewTxtColor(@ColorInt int color);

    /**
     * “已完成”文字颜色
     *
     * @param color
     */
    T setCompleteTxtColor(@ColorInt int color);

    /**
     * 设置完成选取的文字
     * @param completeText
     */
    T setCompleteText(CharSequence completeText);

    /**
     * 启用计数checkbox
     */
    T enableDisplayCandidateNo();

    T setCheckedBoxDrawable(@DrawableRes int drawableResId);
```

预览已经选择的图片，库中提供了简单实现：

``` 
PictureConfig.getPictureConfig()
     .externalPicturePreview(mContext, position,
         selectMedia);
```

## License

[MIT](https://opensource.org/licenses/MIT)

```
MIT License

Copyright (c) 2017 leobert-lan

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

```

---

希望喜欢的朋友点个star支持下。

## 感谢：
[PictureSelector](https://github.com/LuckSiege/PictureSelector) v1.0

[UCrop](https://github.com/Yalantis/uCrop)

[LuBan](https://github.com/Curzibn/Luban)

[Glide](https://github.com/bumptech/glide)

[rxAndroid](https://github.com/ReactiveX/RxAndroid)

PhotoView, 是迁移前的版本，主页已经无法找到了。
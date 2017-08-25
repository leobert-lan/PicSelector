# PicSelector
A library for Picture and Video select on Android.

inspired by [PictureSelector](https://github.com/LuckSiege/PictureSelector)

see Support Blog at [CSDN](http://blog.csdn.net/a774057695/article/details/77533120)

[中文ReadMe](https://github.com/leobert-lan/PicSelector/blob/master/README_ZH.md)


## Feature OverView

###Picture

* single or multi picture select modes
* capture image for select
* picture crop, use open-source library:[UCrop](https://github.com/Yalantis/uCrop)
* picture compress, with two scheme：
	* system API of Bitmap
	* open source library:[LuBan](https://github.com/Curzibn/Luban)
* picture preview，rely on [PhotoView],but it's a very old version one

###Video

* single or multi video select modes
* video preview, with system API
* record video for select

###UI style configuration

* Style as QQ or WeChat
* config some important texts and color

## Integrated into project
latest version：

* UCrop module：2.4.0
* PVSelectorlib：1.1.0

The compiled aar library files were delegated by JFog-JCenter.

add dependances via gradle:


if you cannot pull down the dependency，config your jCenter repository with this default JFrog Jcenter url, and add mavenCentral

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

define dependency.

```
dependencies {
	   //...
    compile 'individual.leobert.libs:pvselectorlib:1.1.0'
    compile 'individual.leobert.libs:ucrop:2.4.0'
    compile 'com.github.bumptech.glide:glide:3.7.0'
}
```

## How to use

announce/decline permissions：

```
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.CAMERA" />
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.MOUNT_UNMOUNT_FILESYSTEMS" />
   
```


Adapter Android N (7.0),config FileProvider in manifest to fetch File in sandbox


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


use FunctionConfig to launch, see code at [sample](https://github.com/leobert-lan/PicSelector/blob/master/PicSelectorDemo/app/src/main/java/thirdparty/leobert/picselectordemo/DemoActivity.java)

more details see at `FunctionConfig`。
 
core codes for launch：

```
PictureConfig.init(config);

PictureConfig.getPictureConfig()
    .openPhoto(mContext, resultCallback);
```

use facade API as chain style：

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

APIs for UI config：

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

use the fast&simple implement for quick preview：

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

Hope this library can help you,**star as a support!**。

## Thanks：
[PictureSelector](https://github.com/LuckSiege/PictureSelector) v1.0

[UCrop](https://github.com/Yalantis/uCrop)

[LuBan](https://github.com/Curzibn/Luban)

[Glide](https://github.com/bumptech/glide)

[rxAndroid](https://github.com/ReactiveX/RxAndroid)

PhotoView,  a version before migration to github,too old to find the homepage。
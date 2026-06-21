Android APK 项目说明

这个压缩包是 Android Studio 项目源码，不是最终 APK。
我当前环境没有 Android SDK/Gradle 编译器，所以不能直接生成可安装 APK。

生成 APK 方法：
1. 安装 Android Studio。
2. 打开 Android Studio → Open。
3. 选择本文件夹 FourJingwenAndroidAPKProject。
4. 等待 Gradle 同步完成。
5. 点击 Build → Build Bundle(s) / APK(s) → Build APK(s)。
6. 生成位置一般在：
   app/build/outputs/apk/debug/app-debug.apk
7. 把 app-debug.apk 改名为：
   四部经文播放器.apk
8. 发给 Android 用户。用户点击 APK → 安装 → 打开 → 离线播放。

功能：
- 4个MP3已内置在App里
- 不需要网站
- 不需要网络
- 循环播放
- 每个经文有速度按钮

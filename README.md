# Remove Clone & Space Badge - LSPosed Module

这是一个用于 ColorOS 的 LSPosed 模块，用于移除分身应用和空间应用的角标。

## 功能特性

- 移除分身应用图标角标
- 移除空间应用图标角标
- 支持 Android 11 - 16
- 支持 ColorOS 各版本

## 支持的系统版本

- Android 11 (API 30+)
- Android 12 (API 31+)
- Android 13 (API 33+)
- Android 14 (API 34+)
- Android 15 (API 35+)
- Android 16 (API 36+)

## 使用方法

1. 安装 LSPosed 框架
2. 将此模块安装到系统
3. 在 LSPosed 中启用此模块
4. 重启手机

## 原理

该模块通过 Hook 以下关键方法来实现角标移除：

### Launcher 相关
- `ShortcutInfo.getNotificationCount()` - 返回 0 移除角标计数
- `AppInfo.getNotificationCount()` - 返回 0 移除角标计数
- `FastBitmapDrawable.drawBadge()` - 阻止角标绘制

### SystemUI 相关
- `NotificationIconContainer` - 阻止通知角标渲染
- `StatusBarIconView.setBadgeCount()` - 设置角标计数为 0
- Badge 绘制方法 - 阻止角标绘制

## 技术细节

该模块使用 Xposed Framework API 来 Hook 系统方法。对于不同的 Android 版本和 ColorOS 版本，模块会尝试 Hook 多个可能的类路径：

- 官方 AOSP 路径
- ColorOS/OPlus 特定路径
- 向后兼容的替代路径

## 注意事项

- 此模块仅用于教育和研究目的
- 需要 LSPosed 框架支持
- 不同的 ColorOS 版本可能需要调整 Hook 目标
- 如果某些角标仍未移除，可能需要 Hook 更多的类或方法

## 故障排除

如果角标仍然存在：

1. 检查 LSPosed 日志了解 Hook 是否成功
2. 确认模块已启用并勾选目标应用
3. 尝试清除应用缓存或重启手机
4. 检查 ColorOS 版本是否需要特定的 Hook 目标

## 开发

如需进行本地开发和测试：

```bash
# 构建模块
./gradlew build

# 生成可安装的 APK
./gradlew assembleRelease
```

## 许可证

MIT License

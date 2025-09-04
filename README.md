# Tab Time (标签时间) - Minecraft Forge Mod

## 📋 项目概述 / Project Overview

**中文**: 这是一个Minecraft Forge模组，为玩家提供24小时制时间显示和昼夜状态信息。通过Tab键可以查看当前游戏时间，支持服务端安装即可工作。

**English**: This is a Minecraft Forge mod that provides 24-hour time display and day/night status information for players. Press Tab key to view current game time, supports server-side installation only.

## ✨ 功能特性 / Features

### 🌅 时间显示 / Time Display
- **24小时制时间**: 显示格式为 `HH:MM` 的实时游戏时间
- **昼夜状态**: 根据游戏时间显示 "白天" 或 "夜晚"
- **实时更新**: 每秒更新一次时间信息

### 🎮 使用方式 / Usage
- **服务端模式**: 仅需在服务端安装，时间信息会显示在所有玩家的Tab列表头部
- **客户端模式**: 客户端安装后，按住Tab键时会在屏幕顶部中央显示时间信息
- **兼容性**: 支持混合使用，客户端可选安装

### 🔧 技术特性 / Technical Features
- **服务端兼容**: `displayTest="IGNORE_SERVER_VERSION"` - 无模组客户端可正常连接
- **事件驱动**: 使用Forge事件系统实现时间更新
- **性能优化**: 服务器端每20个tick(1秒)更新一次
- **跨平台**: 支持Windows/Linux/Mac服务器

## 📦 安装说明 / Installation

### 系统要求 / System Requirements
- **Minecraft版本**: 1.20.1
- **Forge版本**: 47.4.0+
- **Java版本**: 17+

### 安装步骤 / Installation Steps

1. **下载模组文件** / Download the mod file
   ```
   tabtime-1.0.0.jar
   ```

2. **放置到mods文件夹** / Place in mods folder
   ```
   # Windows
   %APPDATA%\.minecraft\mods\tabtime-1.0.0.jar

   # Linux/Mac
   ~/.minecraft/mods/tabtime-1.0.0.jar

   # 服务器端 / Server side
   ./mods/tabtime-1.0.0.jar
   ```

3. **重启游戏/服务器** / Restart game/server
   - 客户端: 重启Minecraft启动器
   - 服务端: 重启服务器

## 🎯 使用方法 / How to Use

### 基本操作 / Basic Usage

#### 服务端模式 (推荐) / Server Mode (Recommended)
1. 仅在服务器安装模组 / Install mod only on server
2. 玩家加入后，时间信息会自动显示在Tab列表顶部 / Time info automatically shows in Tab list header when players join
3. 客户端无需安装模组 / No client mod installation required

#### 客户端模式 / Client Mode
1. 在客户端安装模组 / Install mod on client
2. 按住Tab键 / Hold Tab key
3. 时间信息显示在屏幕顶部中央 / Time info displays at top center of screen

### 显示格式 / Display Format
```
HH:MM  [昼夜状态]
例如 / Example:
12:30  白天
19:45  夜晚
```

## 🛠️ 技术实现 / Technical Implementation

### 核心组件 / Core Components

#### 服务端组件 / Server Components
```java
// ServerTabTicker.java - 服务端时间更新器
@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ServerTabTicker {
    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        // 每秒更新所有玩家的Tab头部信息
        // Update Tab header for all players every second
    }
}
```

#### 客户端组件 / Client Components
```java
// TimeTabOverlay.java - 客户端覆盖层
public class TimeTabOverlay implements IGuiOverlay {
    @Override
    public void render(ForgeGui gui, GuiGraphics guiGraphics, float partialTick, int screenWidth, int screenHeight) {
        // 渲染24小时制时间和昼夜状态
        // Render 24-hour time and day/night status
    }
}
```

### 时间计算逻辑 / Time Calculation Logic

#### Minecraft时间系统 / Minecraft Time System
- **一天总tick数**: 24000 ticks
- **每小时tick数**: 1000 ticks (24000 ÷ 24)
- **时间偏移**: +6000 ticks (对应6:00开始)

#### 转换公式 / Conversion Formula
```java
long dayTime = level.getDayTime() % 24000L;
int hour24 = (int)((dayTime + 6000) % 24000) / 1000;
int minute = (int)(((dayTime + 6000) % 1000) * 60 / 1000);
boolean isNight = level.isNight();
```

### 事件处理 / Event Handling

#### 服务端事件 / Server Events
- **TickEvent.ServerTickEvent**: 服务器tick事件，每秒更新时间
- **玩家管理**: 遍历所有在线玩家，更新Tab列表头部

#### 客户端事件 / Client Events
- **RegisterGuiOverlaysEvent**: 注册GUI覆盖层
- **IGuiOverlay.render()**: 渲染时间信息

### 兼容性设计 / Compatibility Design

#### mods.toml配置 / mods.toml Configuration
```toml
[[mods]]
modId="tabtime"
displayTest="IGNORE_SERVER_VERSION"  # 关键兼容性设置
```

#### 版本兼容性 / Version Compatibility
- **Minecraft**: 1.20.1 (通过Forge API抽象)
- **Forge**: 47.4.0+ (使用最新API)
- **Java**: 17+ (支持现代语法)

## 🔍 技术细节 / Technical Details

### 性能影响 / Performance Impact
- **服务器端**: 极低 - 每20tick进行一次简单计算
- **客户端**: 可选 - 仅在按Tab时渲染
- **网络**: 最小 - 使用原版Tab列表协议

### 数据流 / Data Flow
1. **服务器计算**: ServerTabTicker 计算当前时间
2. **协议传输**: 通过Player.setTabListHeader()传输
3. **客户端显示**: 原版Tab列表显示时间信息

### 内存使用 / Memory Usage
- **静态存储**: 最小 - 仅存储tick计数器
- **动态分配**: 每次更新创建Component对象
- **垃圾回收**: 字符串和Component对象生命周期短

## 🐛 已知问题 / Known Issues

### 兼容性注意事项 / Compatibility Notes
- **多世界**: 仅显示玩家当前所在世界的時間
- **维度差异**: 不同维度时间可能不一致
- **模组冲突**: 与修改Tab列表的模组可能冲突

### 限制 / Limitations
- **精确度**: 时间精确到分钟级别
- **显示位置**: 服务端模式下显示在Tab列表顶部
- **语言支持**: 目前仅支持中文显示

## 📝 更新日志 / Changelog

### v1.0.0
- ✅ 初始发布 / Initial release
- ✅ 实现24小时制时间显示 / Implement 24-hour time display
- ✅ 支持服务端模式 / Support server-only mode
- ✅ 添加昼夜状态指示 / Add day/night status indicator
- ✅ 清理代码注释 / Clean up code comments

## 🤝 贡献 / Contributing

欢迎提交问题和建议！ / Issues and suggestions are welcome!

### 开发环境搭建 / Development Setup
1. 克隆项目 / Clone the project
2. 导入到IDE / Import to IDE
3. 运行Gradle任务 / Run Gradle tasks:
   ```bash
   ./gradlew genIntelliJSrcs  # 生成源码 / Generate sources
   ./gradlew build           # 构建项目 / Build project
   ```

## 📄 许可证 / License

All Rights Reserved - 请遵守Minecraft EULA / All Rights Reserved - Please comply with Minecraft EULA

## 🙏 致谢 / Acknowledgments

- **Minecraft Forge**: 优秀的模组开发框架 / Excellent modding framework
- **MCP (Minecraft Coder Pack)**: 代码映射和反编译 / Code mapping and decompilation
- **Minecraft社区**: 技术支持和灵感来源 / Technical support and inspiration

---

**注意**: 本模组仅用于娱乐和学习目的，请遵守Minecraft EULA和相关法律法规。

**Note**: This mod is for entertainment and educational purposes only. Please comply with Minecraft EULA and relevant laws and regulations.

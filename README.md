<p align="center">
  <img src="./readme-header.png" alt="Endfield Industry Banner" width="100%">
</p>

<p align="center">
  <img src="https://img.shields.io/badge/version-1.0.0-32CD32?style=flat-square&color=32CD32" alt="Version">
  <img src="https://img.shields.io/badge/platform-Paper%201.21-6C757D?style=flat-square&logo=linux&logoColor=white" alt="Platform">
  <img src="https://img.shields.io/badge/Java-21-orange?style=flat-square&logo=openjdk&logoColor=white" alt="Java">
</p>

<p align="center">
  <a href="https://github.com/pylonmc/rebar" target="_blank">
    <img src="https://img.shields.io/badge/dependency-Rebar%200.36.2-9370DB?style=flat-square" alt="Rebar">
  </a>
</p>

---

<p align="center">
  <strong>基于 Rebar 框架的 Minecraft 工业插件 | <a href="https://endfield.hypergryph.com/" target="_blank">终末地</a></strong><br>
  <em>电力系统 | 农业机械化 | 丰富物品 | 研究解锁</em>
</p>

---

## 📋 快速导航

<p align="center">

[⚡ 电力系统](#-电力系统) ·
[🌾 农业机械](#-农业机械) ·
[📦 物品材料](#-物品材料) ·
[🚀 快速开始](#-快速开始) ·
[🔧 配置](#-配置)

</p>

---

## ⚡ 电力系统

完整的电力网络系统，支持发电站、继电器、用电设备之间的能量传输与分配。

| 功能 | 说明 |
| :--- | :--- |
| 发电站 | 产生电能的设施，可配置输出功率 |
| 继电器 | 信号放大器，延长电力传输距离 |
| 用电设备 | 消耗电能的机器和工具 |
| 电网管理 | 实时监控电力网络状态 |
| 过载保护 | 自动断路保护，防止电网损坏 |
| 可视化线缆 | 显示电力连接状态 |

---

## 🌾 农业机械

自动化农业设备，大幅提升农业生产效率。

### 采种机

自动从作物中提取种子，支持自定义提取配方。

### 种植机

自动化种植作物，支持多种种子和肥料配方。

---

## 📦 物品材料

丰富的工业材料、食物、药水和植物。

### 材料

- 工业原料：碳块、蓝铁块、钢制零件等
- 矿物资源：蓝铁矿、紫晶矿、源矿等
- 特殊材料：异香石、晶体外壳等

### 食物与药水

- 多种恢复食物
- 增益药水

### 植物

- 谷物类：灰芦麦、柑实、荞花等
- 特殊作物：金石稻、芽针、琼叶参等

> 所有物品均支持 Rebar 研究系统，需要通过研究解锁才能使用。

---

## 🚀 快速开始

### 环境要求

- Minecraft 1.21+
- Java 21+
- [Rebar](https://github.com/pylonmc/rebar) 0.36.2+

### 安装

1. 确保服务器已安装 Rebar 插件
2. 将 `EndfieldIndustry-1.0.0.jar` 放入服务器的 `plugins` 目录
3. 重启服务器

### 构建

```bash
D:\gradle-9.4.0\bin\gradle.bat build
```

构建完成后 JAR 文件会自动复制到配置的服务器目录。

---

## 🔧 配置

插件配置文件位于 `plugins/EndfieldIndustry/config.yml`

### 电力系统配置

| 配置项 | 说明 |
| :--- | :--- |
| `powerStation.emission` | 发电站功率输出 |
| `relay.efficiency` | 继电器传输效率 |
| `grid.overloadThreshold` | 电网过载阈值 |

---

## 📁 项目结构

```
src/main/kotlin/top/mc506lw/rebar/endfield_industry/
├── content/
│   ├── food/           # 食物
│   ├── machines/       # 机器
│   ├── materials/      # 材料
│   ├── minerals/       # 矿物
│   ├── plants/         # 植物
│   ├── potions/        # 药水
│   └── powersystem/    # 电力系统
├── event/              # 事件处理
├── recipes/            # 配方定义
└── util/               # 工具类
```

---

## 🌐 语言支持

- 🇺🇸 英语 (en)
- 🇨🇳 简体中文 (zh_CN)

---

## 📄 许可证

本项目仅供学习交流使用。

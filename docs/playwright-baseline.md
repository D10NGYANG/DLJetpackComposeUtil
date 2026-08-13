# Playwright 截图基线

T-001 建立一组固定 `390x844` 视口的代表性页面基线。截图产物保存在 `output/playwright/`，不复制到源码目录；后续回归可在同一页面和视口重新截图，再使用像素差异工具比较。

## 页面矩阵

| 文件 | 页面覆盖 | 来源 |
|---|---|---|
| `home-mobile.png` | 首页与分类入口 | 自有目录 |
| `m013-home-390.png` | Input/MaterialTheme 入口 | 自有表单与主题迁移 |
| `m014-home-390.png` | MenuInput 入口 | 自有表单 |
| `m017-home-390.png` | RadioCell 入口 | 自有表单 |
| `m005-index-default.png` | IndexBar 默认状态 | 自有导航 |
| `bd-signal-standard-390.png` | BDSignalBeamBox 常规状态 | 自有领域 |
| `time-picker-hms-390.png` | TimePicker HMS 滚轮 | 自有表单 |
| `divider-top-390.png` | Divider 对照页 | 官方/自有对照 |

## 校验

```bash
./scripts/verify-playwright-baseline.sh
```

脚本会校验 8 张 PNG 均存在、非空且尺寸为 `390x844`。截图生成仍使用 Playwright CLI；当前 Demo 的 Wasm Canvas 语义桥和后台 HTTP 连接限制按 T-001 残余风险跟踪，不能由该文件存在性校验替代真实交互验收。

## 扩展规则

新增关键页面时，先用 Playwright 在固定视口保存截图，再将文件加入脚本清单和本表。交互状态应优先选择稳定、可重复的初始状态；动态动画、时间和网络数据必须在截图前固定。

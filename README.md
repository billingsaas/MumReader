## MumReader 妈咪读
不识字的妈妈玩微信，想了解群中发送的文字信息，但微信不提供该功能，其他提供文字转换语音的APP存在问题：
- 收费
- 广告
- 功能太多，使用复杂
- 安全问题，拷贝的文字内容泄漏

所以写个最简单功能的APP，微信中复制文字后自动朗读。     

## 使用方法
- 安装"妈咪读"
- 设置自启动     
设置 -> 应用 -> 应用启动管理 中找到 "妈咪读"，选择手动管理，允许自启动、允许后台活动。
- 启动   
输入内容，点击右下角朗读，点击右上角清空内容。


## 技术点
- 后台常驻   
支持P版本，所以使用了前台服务。
- 监听剪贴板
- 朗读剪贴板内容
- 开机自启动

## 下载
上架太麻烦了，直接这里下载吧 [mum-reader.apk](https://github.com/billingsaas/MumReader/raw/master/output/mum-reader.apk)     
![url](https://github.com/billingsaas/MumReader/raw/master/output/url.png)
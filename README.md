# 背景

你遇到了一个问题，有人说你的DUBBO接口返回的数据有问题，导致功能出错了；于是你要了参数，想调用一下接口，验证一下数据是否有问题。

1. 第一种场景：（通过跳板机）登到机器上，invoke调用接口。
2. 第二种场景：很幸运，公司有一个平台封装了DUBBO泛化调用。你登陆平台，筛选服务，找到接口方法，填充参数，重重的敲击回车键，等待幸运女神的降临。

如果你属于第一种场景，不用继续往下看了。。。

如果你困于第二种场景，久而久之，登录平台，筛选服务的操作令人感到厌倦，甚至有的时候对参数结构不熟悉，上游提供的参数仅包含了关键属性及其数值，此时你还需要同时打开代码，找到参数对比着进行调用前的填充。

你想到了一个办法，可以通过curl调用平台，那就可以减少打开平台的频次。

同时可以在本地做好记录，对不同的服务记录下相应的curl语句，那就可以减少参数结构对比的操作。

坚持了一段时间，本地记录的curl语句越来越多，每次寻找的时候需要认真检索；而且对于一些较早的curl，假如访问的平台有登录权限控制，复用的时候，大概率需要替换一下用户标志。

这，依然很不优雅。

那么现在，有一个新的方案。

**IDEA开发利器，DubboCall插件，它来了。**

# 使用

### 插件安装

添加自定义仓库：Preferences -> Plugins -> Manage Plugin Repositories...

仓库地址： https://raw.githubusercontent.com/yitulin/DubboCall/master/updatePlugins.xml

通过插件市场搜索DubboCall进行安装。

### 插件设置

#### 一键导入

|       type       |     含义     |
| :--------------: | :----------: |
| default_template | 默认模板设置 |
|     variable     |   变量设置   |
|     template     |   模板设置   |

准备好一个配置文件，格式如下：

```json
[
  {
    "bodyJson": "请求体格式",
    "headerJson": "请求头格式",
    "name": "模板名称",
    "type": "template",
    "url": "平台地址"
  },
  {
    "key": "变量key",
    "type": "variable",
    "value": "变量值"
  },
  {
    "name": "模板名称",
    "type": "default_template"
  }
]
```

1. 点击一键导入，选择文件进行导入
2. 点击「OK」保存设置

![](https://gitee.com/yitulin/pictures/raw/master/dubbocall/202109022028355.png)

##### 模板设置

首先打开IDEA的配置（Perferences），插件的配置路径如下：

![](https://gitee.com/yitulin/pictures/raw/master/dubbocall/20210831173519.png)

配置界面如图：

1. 导入功能，需要先把要导入的模板内容复制到剪贴板。

2. 分享功能，会把要分享的模板内容复制到剪贴板。

   ![](https://gitee.com/yitulin/pictures/raw/master/dubbocall/20210831173707.png)

##### 变量设置

如图：

![](https://gitee.com/yitulin/pictures/raw/master/dubbocall/20210831173845968.png)

在模板中使用变量姿势：${变量Key}

### 插件投产

在想要进行调用的方法上右键，选择如下action：

![](https://gitee.com/yitulin/pictures/raw/master/dubbocall/20210831174146.png)

你将会看到：

![](https://gitee.com/yitulin/pictures/raw/master/dubbocall/20210831174542.png)

需要注意的是，模板中配置好的header（请求头）信息、body（请求体）信息，需要在变量替换后，符合json格式。

### 历史记录

「模板+方法=签名」，会记录100个最新的调用。



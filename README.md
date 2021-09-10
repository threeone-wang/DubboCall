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

### 插件功能介绍

![](https://gitee.com/yitulin/pictures/raw/master/dubbocall/202109101519466.png)

![](https://gitee.com/yitulin/pictures/raw/master/dubbocall/202109101525263.png)

举个🌰：

平台模板设定如下：

| 参数信息 |                         值                         |
| :------: | :------------------------------------------------: |
|   地址   |              http://www.dubbocall.com              |
|  请求头  | {<br/>    "Content-Type": "application/json"<br/>} |
|  请求体  |                     ${params}                      |

假设有这么一个RPC方法，可以通过上述平台进行调用。即：本地调用平台，平台调用服务提供者。

![](https://gitee.com/yitulin/pictures/raw/master/dubbocall/202109101545919.png)

将光标置于方法名称位置点击右键，选择如下操作：（或将光标置于方法名称位置，使用快捷键：mac下：ctrl+command+P）

![](https://gitee.com/yitulin/pictures/raw/master/dubbocall/202109101629456.png)

此时会出现一个新的对话框：

![](https://gitee.com/yitulin/pictures/raw/master/dubbocall/202109101634959.png)



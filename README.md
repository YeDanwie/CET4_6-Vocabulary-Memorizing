# CET4_6-Vocabulary-Memorizing
基于javaFx绘制的四六级单词记忆助手程序

## 功能介绍
1. 记忆单词
  - 列表式记单词，已记住的单词会有标记
  - 卡片式记单词，可播放发音
2. 做练习
  题型是简单的单词释义选择题，分为中译英和英译中，干扰项从词库中随机选择</br>
  已经回答正确的题目不会再出现</br>
  统计页面显示当前已经练习的题目数量以及用饼状图表示回答正误率
3. 清空进度
  在设置页面，用户可自行选择清空单词记忆的进度或练习进度，程序将在关闭后完成删除任务
  
## 实现原理
### 播放发音
音频来自有道的API</br>
播放音频的写法示例:
```java
Media media = new Media("http://dict.youdao.com/dictvoice?audio="+english);
MediaPlayer mplayer = new MediaPlayer(media);
mplayer.play();
```

### 页面滑动切换
从卡片页面切换到列表页面的代码示例：
```java
double width = stackPane1.getWidth();
KeyFrame start = new KeyFrame(Duration.ZERO,
        new KeyValue(wcView.getCardPane().translateXProperty(), 0),
        new KeyValue(wlView.getListView().translateXProperty(), -width));
KeyFrame end = new KeyFrame(Duration.seconds(0.5),
        new KeyValue(wcView.getCardPane().translateXProperty(), width),
        new KeyValue(wlView.getListView().translateXProperty(), 0));
Timeline slide = new Timeline(start, end);
slide.setOnFinished(e -> stackPane1.getChildren().remove(wcView.getCardPane()));
slide.play();
				
				stackPane1.getChildren().add(wlView.getListView());
```
上述代码使得两个页面水平滑动切换，动画持续时间0.5秒。


### 进度的保存和删除
每次程序关闭前，用户在程序上学习的进度都会被保存进入Data文件夹，存入硬盘</br>
每次程序开启后，如果学习进度的文件数据存在，则读取数据给相应变量赋值，否则使用默认值</br>
用户在设置页面设定清空单词记忆的进度或练习进度，程序将在关闭后完成删除相应的数据文件

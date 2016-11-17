package com.sothree.slidinguppanel;

/**
 * 定义滚动虚拟视图，用于获取滚动的XY坐标
 *
 * 在WebView内部通讯或者内部屏蔽访问实现View技术框架时实现该接口与上拉组件交互事件
 *
 * Created by wenhua.ywh on 2016/9/22.
 */
public interface IScrollableView {

    int getScrollX();

    int getScrollY();

}

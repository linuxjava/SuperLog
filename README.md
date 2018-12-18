# SuperAdapter
超强的Adapter

项目特点
* 支持RecyclerView单类型和多类型ItemView；
* RecyclerView支持快捷的添加HeaderView和FooterView；
* RecyclerView支持快捷的添加LoadMoreView；
* 支持ListView单类型和多类型ItemView；
* 支持GridView单类型和多类型ItemView；

##  APK下载
[Download](https://github.com/linuxjava/SuperAdapter/raw/master/apk/app-debug.apk)

## 使用
### 添加依赖
```xml
implementation 'xiao.free.superadapter:SuperAdapterLib:0.2'
```
### 简单使用
创建CommonAdapter即可
```xml
mCommonAdapter = new CommonAdapter<String>(this, R.layout.item_single_text) {
    @Override
    protected void convert(ViewHolder holder, String o, int position) {
        holder.setText(R.id.text_content, o);
    }
};
```
### 添加HeaderView和FooterView
```xml
//快捷的添加header和footer
HeaderAndFooterWrapper<String> headerAndFooterWrapper = new HeaderAndFooterWrapper<>(mCommonAdapter);
headerAndFooterWrapper.addHeaderView(R.layout.item_header);
headerAndFooterWrapper.addFootView(R.layout.item_footer);

mRecyclerView.setAdapter(headerAndFooterWrapper);
```
使用HeaderAndFooterWrapper封装mCommonAdapter，然后通过HeaderAndFooterWrapper添加HeaderView和FooterView

![image](https://github.com/linuxjava/SuperAdapter/raw/master/screenshot/2.gif)

### 多类型ItemView
```xml
public static class SendTextHolder implements ItemViewDelegate<ChatMessage> {

    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_chat_send_msg;
    }

    @Override
    public boolean isForViewType(ChatMessage item, int position) {
        return item.getItemType() == ChatMessage.ITEM_TYPE_SEND_TEXT;
    }

    @Override
    public void convert(ViewHolder holder, ChatMessage chatMessage, int position) {

    }
}

public static class RecvTextHolder implements ItemViewDelegate<ChatMessage> {

    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_chat_recv_msg;
    }

    @Override
    public boolean isForViewType(ChatMessage item, int position) {
        return item.getItemType() == ChatMessage.ITEM_TYPE_RECV_TEXT;
    }

    @Override
    public void convert(ViewHolder holder, ChatMessage chatMessage, int position) {

    }
}
```
每一种类型对应创建一个ItemViewDelegate
![image](https://github.com/linuxjava/SuperAdapter/raw/master/screenshot/1.png)

## 感谢
项目源码来源[https://github.com/hongyangAndroid/baseAdapter](https://github.com/hongyangAndroid/baseAdapter)
，在此基础是对MultiItemTypeAdapter和HeaderAndFooterWrapper进行了优化。





package cn.mutils.app.ui.adapter;

import android.content.Context;
import android.database.DataSetObserver;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Constructor;
import java.util.List;

import cn.mutils.core.reflect.ReflectUtil;
import cn.mutils.core.sort.IIndexItem;

/**
 * Created by wenhua.ywh on 2016/5/23.
 */
public class RecyclerAdapter<DATA_ITEM> extends RecyclerView.Adapter<RecyclerAdapter.RecyclerViewHolder<DATA_ITEM>> implements IItemAdapter<DATA_ITEM> {

    protected ViewGroup mContainer;

    protected List<DATA_ITEM> mDataProvider;

    protected Constructor<? extends IItemView<DATA_ITEM>> mItemViewConstructor;

    public void setItemViewClass(Class<? extends IItemView<DATA_ITEM>> itemViewClass){
        mItemViewConstructor= ReflectUtil.getConstructor(itemViewClass, Context.class);
    }

    public Context getContext(){
        return mContainer==null?null:mContainer.getContext();
    }

    public ViewGroup getContainer(){
        return this.mContainer;
    }

    protected void onContainerChanged(){

    }

    public List<DATA_ITEM> getDataProvider(){
        return mDataProvider;
    }

    public void setDataProvider(List<DATA_ITEM> dataProvider){
        this.mDataProvider=dataProvider;
        this.notifyDataSetChanged();
    }

    @Override
    public DATA_ITEM getItem(int position) {
        return mDataProvider==null?null:mDataProvider.get(position);
    }

    @Override
    public int getItemCount() {
        return mDataProvider==null?0:mDataProvider.size();
    }

    @Override
    public int getCount(){
        return mDataProvider==null?0:mDataProvider.size();
    }

    @Override
    public boolean isEmpty() {
        return getCount()==0;
    }

    @Override
    public boolean isEnabled(int position) {
        return true;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return true;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver dataSetObserver) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {

    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    public IItemView<DATA_ITEM> getItemView(int itemViewType){
        return mItemViewConstructor==null?null:ReflectUtil.newInstance(mItemViewConstructor,getContext());
    }

    @Override
    public RecyclerViewHolder<DATA_ITEM> onCreateViewHolder(ViewGroup parent, int viewType) {
        if(this.mContainer==null){
            this.mContainer=parent;
            this.onContainerChanged();
        }
        IItemView<DATA_ITEM> itemView=getItemView(viewType);
        itemView.setAdapter(this);
        itemView.onCreate();
        return new RecyclerViewHolder<DATA_ITEM>(itemView.toView());
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder<DATA_ITEM> holder, int position) {
        IItemView<DATA_ITEM> itemView=holder.getItemView();
        if(itemView==null){
            return;
        }
        itemView.setPosition(position);
        itemView.setDataProvider(this.getItem(position));
        itemView.onResume();
    }

    public static class RecyclerViewHolder<DATA_ITEM> extends RecyclerView.ViewHolder{

        public RecyclerViewHolder(View itemView){
            super(itemView);
        }

        public IItemView<DATA_ITEM> getItemView(){
            return (itemView instanceof IIndexItem)?((IItemView<DATA_ITEM>)itemView):null;
        }

    }

}

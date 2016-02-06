package cn.mutils.app.ui.adapter;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;

import java.util.LinkedList;
import java.util.List;

/**
 * Adapter for {@link TableLayout}
 *
 * @see UIAdapter
 */
public abstract class TableAdapter<DATA_ITEM> extends CacheAdapter<DATA_ITEM, List<IItemView<DATA_ITEM>>> {

    protected int mColumnCount;

    protected List<OTableRow> mCacheRows = new LinkedList<OTableRow>();

    @Override
    public void setContainer(ViewGroup container) {
        if (container != null) {
            if (!(container instanceof TableLayout)) {
                throw new IllegalArgumentException("container is not TableLayout");
            }
        }
        super.setContainer(container);
    }

    public int getColumnCount() {
        return mColumnCount;
    }

    public void setColumnCount(int columnCount) {
        if (columnCount < 1) {
            throw new IllegalArgumentException();
        }
        if (mColumnCount == columnCount) {
            return;
        }
        onColumnTransform(columnCount - mColumnCount);
        mColumnCount = columnCount;
        mCacheInvalidate = true;
        notifyDataSetChanged();
    }

    @Override
    protected int getCacheSizeRequired() {
        if (mDataProvider == null || mColumnCount == 0) {
            return 0;
        }
        int requiredViews = mDataProvider.size();
        int requiredRows = requiredViews / mColumnCount;
        int requiredViews4Bottom = requiredViews % mColumnCount;
        if (requiredViews4Bottom > 0) {
            requiredRows++;
        }
        return requiredRows;
    }

    @Override
    protected boolean freeCacheNoUse() {
        return Math.random() > 0.3 && mCacheViews.size() / getCacheSizeRequired() > 2;
    }

    @Override
    protected void freeCacheAt(int index) {
        List<IItemView<DATA_ITEM>> rowViews = mCacheViews.get(index);
        for (int j = rowViews.size() - 1; j >= 0; j--) {
            IItemView<DATA_ITEM> itemView = rowViews.get(j);
            itemView.setAdapter(null);
            itemView.setDataProvider(null);
            rowViews.remove(j);
        }
        mCacheRows.remove(index);
        mCacheViews.remove(index);
    }

    @Override
    protected void allocCacheAt(int index) {
        OTableRow row = new OTableRow(getContext());
        row.setColumn(mColumnCount);
        List<IItemView<DATA_ITEM>> rowViews = new LinkedList<IItemView<DATA_ITEM>>();
        for (int j = 0; j < mColumnCount; j++) {
            IItemView<DATA_ITEM> itemView = getItemView(-1);
            itemView.setAdapter(this);
            itemView.onCreate();
            fixItemViewParams(itemView);
            row.addView(itemView.toView());
            rowViews.add(itemView);
        }
        mCacheRows.add(index, row);
        mCacheViews.add(index, rowViews);
    }

    @Override
    protected IItemView<DATA_ITEM> getItemViewAt(int index) {
        return mCacheViews.get(index / mColumnCount).get(index % mColumnCount);
    }

    @Override
    protected void onDataSetChanged() {
        if (mColumnCount == 0) {
            return;
        }
        super.onDataSetChanged();
    }

    protected void onColumnTransform(int transform) {
        if (mContainer == null || mDataProvider == null || mColumnCount == 0) {
            return;
        }
        int newColumnCount = mColumnCount + transform;
        int containerRows = mContainer.getChildCount();
        if (transform > 0) {// column increase
            for (int i = 0, size = containerRows; i < size; i++) {
                OTableRow row = mCacheRows.get(i);
                row.setColumn(newColumnCount);
                List<IItemView<DATA_ITEM>> rowViews = mCacheViews.get(i);
                for (int j = 0; j < transform; j++) {
                    IItemView<DATA_ITEM> itemView = getItemView(-1);
                    itemView.setAdapter(this);
                    itemView.onCreate();
                    fixItemViewParams(itemView);
                    rowViews.add(itemView);
                    for (int k = row.getChildCount(); k < mColumnCount; k++) {
                        IItemView<DATA_ITEM> itemMore = rowViews.get(k);
                        row.addView(itemMore.toView());
                    }
                    row.addView(itemView.toView());
                }
            }
            for (int i = mCacheViews.size() - 1; i >= containerRows; i--) {
                freeCacheAt(i);
            }
        } else if (transform < 0) {// column decrease
            List<IItemView<DATA_ITEM>> deleteViews = new LinkedList<IItemView<DATA_ITEM>>();
            for (int i = 0, size = mCacheViews.size(); i < size; i++) {
                OTableRow row = mCacheRows.get(i);
                row.setColumn(newColumnCount);
                List<IItemView<DATA_ITEM>> rowViews = mCacheViews.get(i);
                for (int j = mColumnCount - 1; j >= newColumnCount; j--) {
                    IItemView<DATA_ITEM> itemView = rowViews.get(j);
                    itemView.setAdapter(null);
                    itemView.setDataProvider(null);
                    rowViews.remove(j);
                    if (itemView.toView().getParent() == row) {
                        row.removeView(itemView.toView());
                    }
                    deleteViews.add(itemView);
                    if (deleteViews.size() >= newColumnCount) {
                        addCacheRow(deleteViews, newColumnCount);
                    }
                }
            }
            if (deleteViews.size() != 0) {
                addCacheRow(deleteViews, newColumnCount);
            }
        }
    }

    protected void addCacheRow(List<IItemView<DATA_ITEM>> rowViews, int columnCount) {
        OTableRow newRow = new OTableRow(getContext());
        newRow.setColumn(columnCount);
        LinkedList<IItemView<DATA_ITEM>> newRowViews = new LinkedList<IItemView<DATA_ITEM>>();
        for (int i = 0; i < rowViews.size(); i++) {
            IItemView<DATA_ITEM> itemView = rowViews.get(i);
            newRowViews.add(itemView);
            newRow.addView(itemView.toView());
        }
        for (int i = newRowViews.size(); i < columnCount; i++) {
            IItemView<DATA_ITEM> itemView = getItemView(-1);
            itemView.setAdapter(this);
            itemView.onCreate();
            fixItemViewParams(itemView);
            newRowViews.add(itemView);
            newRow.addView(itemView.toView());
        }
        mCacheViews.add(newRowViews);
        mCacheRows.add(newRow);
        rowViews.clear();
    }

    protected void fixContainerSize() {
        int requiredRows = getCacheSizeRequired();
        if (requiredRows == 0) {
            mContainer.removeAllViews();
            return;
        }
        int requiredViews4Bottom = mDataProvider.size() % mColumnCount;
        requiredViews4Bottom = requiredViews4Bottom != 0 ? requiredViews4Bottom : mColumnCount;
        for (int i = 0; i < requiredRows; i++) {// fix items of row in container
            List<IItemView<DATA_ITEM>> rowViews = mCacheViews.get(i);
            OTableRow row = mCacheRows.get(i);
            int containerRowItems = row.getChildCount();
            int requiredRowItems = (i == requiredRows - 1) ? requiredViews4Bottom : mColumnCount;
            if (requiredRowItems < containerRowItems) {
                row.removeViews(requiredRowItems, containerRowItems - requiredViews4Bottom);
            } else if (requiredRowItems > containerRowItems) {
                for (int j = containerRowItems; j < requiredRowItems; j++) {
                    IItemView<DATA_ITEM> itemView = rowViews.get(j);
                    row.addView(itemView.toView());
                }
            }
        }
        int containerRows = mContainer.getChildCount();
        if (requiredRows != containerRows) {// fix row in container
            if (requiredRows < containerRows) {
                mContainer.removeViews(requiredRows, containerRows - requiredRows);
            } else if (requiredRows > containerRows) {
                for (int i = containerRows; i < requiredRows; i++) {
                    mContainer.addView(mCacheRows.get(i));
                }
            }
        }
    }

    protected void fixItemViewParams(IItemView<DATA_ITEM> itemView) {
        ViewGroup.LayoutParams itemParams = itemView.toView().getLayoutParams();
        boolean renewParams = false;
        if (itemParams != null) {
            if (itemParams instanceof TableRow.LayoutParams) {
                TableRow.LayoutParams itemRowParams = (TableRow.LayoutParams) itemParams;
                if (itemRowParams.width != 0 || itemRowParams.weight != 1) {
                    itemRowParams.width = 0;
                    itemRowParams.weight = 1;
                    itemView.toView().setLayoutParams(itemRowParams);
                }
            } else {
                renewParams = true;
            }
        } else {
            renewParams = true;
        }
        if (renewParams) {
            itemView.toView().setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1));
        }
    }

    static class OTableRow extends TableRow {

        protected int mColumn;

        public OTableRow(Context context) {
            super(context);
        }

        public OTableRow(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public int getColumn() {
            return mColumn;
        }

        public void setColumn(int column) {
            mColumn = column;
        }

        @Override
        public int getVirtualChildCount() {
            super.getVirtualChildCount();
            return mColumn;
        }

    }
}

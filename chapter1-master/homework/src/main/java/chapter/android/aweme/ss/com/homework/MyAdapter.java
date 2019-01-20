package chapter.android.aweme.ss.com.homework;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.Context;
import android.view.LayoutInflater;

import java.util.TooManyListenersException;

import chapter.android.aweme.ss.com.homework.R;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private static final String TAG = "MyAdapter";
    private int mItems;
    private static int viewHolderCount;
    private final ListItemClickListener mOnClickListener;
    public MyAdapter(int ListItems, ListItemClickListener listener) {
        mItems = ListItems;
        mOnClickListener = listener;
        viewHolderCount = 0;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewtype) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.im_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position) {
        Log.d(TAG, "onBindViewHolder: #" + position);
        myViewHolder.bind(position);
    }

    @Override
    public int getItemCount() {
        return 0;
    }
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final ImageView avatar;
        private final ImageView notice;
        private final TextView title;
        private final TextView description;
        private final TextView time;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            avatar = (ImageView) itemView.findViewById(R.id.iv_avatar);
            notice = (ImageView) itemView.findViewById(R.id.robot_notice);
            title = (TextView) itemView.findViewById(R.id.tv_title);
            time = (TextView) itemView.findViewById(R.id.tv_time);
            description = (TextView) itemView.findViewById(R.id.tv_description);
            itemView.setOnClickListener(this);
        }
        public void bind(int position) {
            title.setText(String.valueOf(position));
            time.setText(String.valueOf(position));
            description.setText(String.valueOf(position));
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            if (mOnClickListener != null) {
                mOnClickListener.onListItemClick(clickedPosition);
            }
        }
    }
    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }
}
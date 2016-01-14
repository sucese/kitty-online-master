package com.guoxiaoxing.kitty.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.guoxiaoxing.kitty.R;
import com.guoxiaoxing.kitty.bean.Event;
import com.guoxiaoxing.kitty.bean.EventList;
import com.guoxiaoxing.kitty.ui.base.ListBaseAdapter;

import org.kymjs.kjframe.Core;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 活动列表适配器
 *
 * @author guoxiaoxing
 */
public class EventAdapter extends ListBaseAdapter<Event> {

    private int eventType = EventList.EVENT_LIST_TYPE_NEW_EVENT;

    static class ViewHolder {

        @Bind(R.id.iv_event_status)
        ImageView status;
        @Bind(R.id.iv_event_img)
        ImageView img;
        @Bind(R.id.tv_event_title)
        TextView title;
        @Bind(R.id.tv_event_time)
        TextView time;
        @Bind(R.id.tv_event_spot)
        TextView spot;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public void setEventType(int eventType) {
        this.eventType = eventType;
    }

    @Override
    protected View getRealView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh = null;
        if (convertView == null || convertView.getTag() == null) {
            convertView = getLayoutInflater(parent.getContext()).inflate(
                    R.layout.list_cell_event, null);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        Event item = mDatas.get(position);

        setEventStatus(item, vh);

        new Core.Builder().view(vh.img).url(item.getCover()).doTask();
        vh.title.setText(item.getTitle());
        vh.time.setText(item.getStartTime());
        vh.spot.setText(item.getSpot());

        return convertView;
    }

    private void setEventStatus(Event event, ViewHolder vh) {

        switch (this.eventType) {
            case EventList.EVENT_LIST_TYPE_NEW_EVENT:
                if (event.getApplyStatus() == Event.APPLYSTATUS_CHECKING
                        || event.getApplyStatus() == Event.APPLYSTATUS_CHECKED) {
                    vh.status
                            .setImageResource(R.drawable.icon_event_status_checked);
                    vh.status.setVisibility(View.VISIBLE);
                } else {
                    vh.status.setVisibility(View.GONE);
                }
                break;
            case EventList.EVENT_LIST_TYPE_MY_EVENT:
                if (event.getApplyStatus() == Event.APPLYSTATUS_ATTEND) {
                    vh.status.setImageResource(R.drawable.icon_event_status_attend);
                } else if (event.getStatus() == Event.EVNET_STATUS_APPLYING) {
                    vh.status
                            .setImageResource(R.drawable.icon_event_status_checked);
                } else {
                    vh.status.setImageResource(R.drawable.icon_event_status_over);
                }
                vh.status.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }
}

package net.oschina.app.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import net.oschina.app.R;
import net.oschina.app.base.ListBaseAdapter;
import net.oschina.app.bean.Event;
import net.oschina.app.bean.EventList;

import org.kymjs.kjframe.Core;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 活动列表适配器
 *
 * @author FireAnt（http://my.oschina.net/LittleDY）
 * @created 2014年10月9日 下午6:22:54
 */
public class EventAdapter extends ListBaseAdapter<Event> {

    private int eventType = EventList.EVENT_LIST_TYPE_NEW_EVENT;

    static class ViewHolder {

        @InjectView(R.id.iv_event_status)
        ImageView status;
        @InjectView(R.id.iv_event_img)
        ImageView img;
        @InjectView(R.id.tv_event_title)
        TextView title;
        @InjectView(R.id.tv_event_time)
        TextView time;
        @InjectView(R.id.tv_event_spot)
        TextView spot;

        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
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

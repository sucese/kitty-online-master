package net.oschina.app.team.adapter;

import android.content.Context;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import net.oschina.app.R;
import net.oschina.app.team.bean.TeamDiaryDetail;
import net.oschina.app.team.bean.TeamDiaryDetail.DayData;

import java.util.ArrayList;
import java.util.List;

public class TeamDiaryDetailAdapter extends BaseAdapter {

    private final TeamDiaryDetail data;
    private final Context cxt;
    private final List<String> datas;
    private final int[] weekIndex = new int[7];

    public TeamDiaryDetailAdapter(Context cxt, TeamDiaryDetail datas) {
        this.cxt = cxt;
        this.data = datas;
        this.datas = new ArrayList<String>(20);
    }

    @Override
    public int getCount() {
        if (data == null) {
            return 0;
        }
        int count = 0;
        weekIndex[0] = count;
        count += isNull(data.getSun());
        weekIndex[1] = count;
        count += isNull(data.getSat());
        weekIndex[2] = count;
        count += isNull(data.getFri());
        weekIndex[3] = count;
        count += isNull(data.getThu());
        weekIndex[4] = count;
        count += isNull(data.getWed());
        weekIndex[5] = count;
        count += isNull(data.getTue());
        weekIndex[6] = count;
        count += isNull(data.getMon());
        return count;
    }

    private String getWeek(int position, View hide) {
        String week = "";
        hide.setVisibility(View.GONE);
        if (position == weekIndex[0]) {
            hide.setVisibility(View.VISIBLE);
            week = "星期日";
        }
        if (position == weekIndex[1]) {
            hide.setVisibility(View.VISIBLE);
            week = "星期六";
        }
        if (position == weekIndex[2]) {
            hide.setVisibility(View.VISIBLE);
            week = "星期五";
        }
        if (position == weekIndex[3]) {
            hide.setVisibility(View.VISIBLE);
            week = "星期四";
        }
        if (position == weekIndex[4]) {
            hide.setVisibility(View.VISIBLE);
            week = "星期三";
        }
        if (position == weekIndex[5]) {
            hide.setVisibility(View.VISIBLE);
            week = "星期二";
        }
        if (position == weekIndex[6]) {
            hide.setVisibility(View.VISIBLE);
            week = "星期一";
        }
        return week;
    }

    private int isNull(DayData data) {
        if (data == null) {
            return 0;
        }
        datas.addAll(data.getList());
        return data.getList().size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    static class ViewHolder {
        TextView week;
        TextView content;
        ImageView imageWeek;
        WebView webView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(cxt, R.layout.list_cell_diary_detail,
                    null);
            holder.week = (TextView) convertView
                    .findViewById(R.id.item_diary_detail_week);
            holder.content = (TextView) convertView
                    .findViewById(R.id.item_diary_detail_content);
            holder.imageWeek = (ImageView) convertView
                    .findViewById(R.id.item_diary_detail_img);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.week.setText(getWeek(position, holder.imageWeek));
        holder.content.setText(stripTags(datas.get(position).toString()));
        return convertView;
    }

    /**
     * 移除字符串中的Html标签
     * 
     * @author kymjs (https://github.com/kymjs)
     * @param pHTMLString
     * @return
     */
    public static Spanned stripTags(final String pHTMLString) {
        // String str = pHTMLString.replaceAll("\\<.*?>", "");
        String str = pHTMLString.replaceAll("</li>", "</li><br>");
        return Html.fromHtml(str);
    }
}

package net.oschina.app.util;

import java.util.List;

import net.oschina.app.AppContext;
import net.oschina.app.api.ApiHttpClient;
import net.oschina.app.bean.NotebookData;
import net.oschina.app.db.NoteDatabase;

import cz.msebera.android.httpclient.Header;

import org.kymjs.kjframe.utils.KJLoger;
import org.kymjs.kjframe.utils.SystemTool;

import android.app.Activity;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.protocol.HTTP;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

/**
 * 一个文件云同步解决方案（便签同步）
 *
 * @author kymjs (https://github.com/kymjs)
 */
public class SynchronizeController {

    public interface SynchronizeListener {
        public void onSuccess(byte[] arg2);

        public void onFailure();
    }

    private NoteDatabase noteDb;
    private List<NotebookData> localDatas;
    private Activity aty;
    private static long sPreviousRefreshTime = 0; // 为了流浪考虑，不能刷新过于频繁

    public void doSynchronize(Activity aty, SynchronizeListener cb) {
        noteDb = new NoteDatabase(aty);
        localDatas = noteDb.query();
        this.aty = aty;
        // 同步逻辑太麻烦，要么用二重循环由客户端判断差异文件，要么浪费流量把本地文件全部上传交由服务器判断。
        // 这里我采用两种逻辑：WiFi环境下客户端直接提交全部文件，交由服务器判断。
        // GPRS环境下客户端判断差异文件，并编辑服务器端文件达到同步
        if (SystemTool.isWiFi(aty)) {
            doSynchronizeWithWIFI(cb);
        } else {
            doSynchronizeWithWIFI(cb);
        }
    }

    /**
     * GPRS环境下：使用二重循环遍历差异文件并更新云端文件达到同步
     */
    private void doSynchronizeWithGPRS() {
    }

    /**
     * WIFI环境下：客户端直接提交全部文件，交由服务器做同步判断<br>
     * 由于量可能会比较大，采用json传输，而不是from
     *
     * @author kymjs (https://github.com/kymjs)
     */
    private void doSynchronizeWithWIFI(final SynchronizeListener cb) {
        long currentTime = System.currentTimeMillis();
        // 为流量和服务器考虑，限制请求频率，一分钟刷新一次
        if (currentTime - sPreviousRefreshTime < 60000) {
            return;
        } else {
            // sPreviousRefreshTime = currentTime;
        }

        int uid = AppContext.getInstance().getLoginUid();
        StringBuilder jsonData = new StringBuilder();
        int size = localDatas.size();
        jsonData.append("{\"uid\":").append(uid).append(",\"stickys\":[");
        boolean isFirst = true;
        for (int i = 0; i < size; i++) {
            NotebookData data = localDatas.get(i);
            if (isFirst) {
                isFirst = false;
            } else {
                jsonData.append(",");
            }
            jsonData.append("{");
            jsonData.append("\"id\":").append(data.getId()).append(",");
            jsonData.append("\"iid\":").append(data.getIid()).append(",");
            String content = data.getContent();
            content.replaceAll("\"", "\\\"");
            jsonData.append("\"content\":\"").append(content).append("\",");
            jsonData.append("\"color\":\"").append(data.getColorText())
                    .append("\",");

            if (StringUtils.isEmpty(data.getServerUpdateTime())) {
                data.setServerUpdateTime(StringUtils
                        .getDataTime("yyyy-MM-dd HH:mm:ss"));
            }

            jsonData.append("\"createtime\":\"")
                    .append(data.getServerUpdateTime()).append("\",");
            jsonData.append("\"updatetime\":\"").append(
                    data.getServerUpdateTime());
            jsonData.append("\"}");
        }
        jsonData.append("]}");

        KJLoger.debug("==" + jsonData.toString());

        AsyncHttpClient client = ApiHttpClient.getHttpClient();
        client.addHeader("Content-Type", "application/json; charset=UTF-8");
        client.post(
                aty,
                ApiHttpClient
                        .getAbsoluteApiUrl("action/api/team_stickynote_batch_update"),
                new StringEntity(jsonData.toString(), HTTP.UTF_8),
                "application/json; charset=UTF-8",
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int arg0, Header[] arg1,
                                          byte[] arg2) {
                        KJLoger.debug("获取便签:" + new String(arg2));
                        cb.onSuccess(arg2);
                    }

                    @Override
                    public void onFailure(int arg0, Header[] arg1,
                                          byte[] arg2, Throwable arg3) {
                        cb.onFailure();
                    }

                });
        ApiHttpClient.setHttpClient(client);
    }
}

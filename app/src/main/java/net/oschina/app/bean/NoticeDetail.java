package net.oschina.app.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/** 
 * 通知实体类
 * @author FireAnt（http://my.oschina.net/LittleDY）
 * @version 创建时间：2014年10月27日 下午2:28:42 
 * 
 */
@SuppressWarnings("serial")
@XStreamAlias("oschina")
public class NoticeDetail extends Base {
	
	@XStreamAlias("result")
	private Result result;

	public Result getResult() {
		return result;
	}

	public void setResult(Result result) {
		this.result = result;
	}
	
	
}

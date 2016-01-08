package net.oschina.app.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 软件详情实体类
 * 
 * @author FireAnt（http://my.oschina.net/LittleDY）
 * @version 创建时间：2014年10月23日 下午3:10:54
 * 
 */
@SuppressWarnings("serial")
@XStreamAlias("oschina")
public class SoftwareDetail extends Entity {

	@XStreamAlias("software")
	private Software software;

	public Software getSoftware() {
		return software;
	}

	public void setSoftware(Software software) {
		this.software = software;
	}
}

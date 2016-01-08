package net.oschina.app.bean;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 开源软件分类列表实体
 * @author FireAnt（http://my.oschina.net/LittleDY）
 * @created 2014年12月2日 上午10:54:10
 *
 */
@SuppressWarnings("serial")
@XStreamAlias("oschina")
public class SoftwareCatalogList extends Entity implements ListEntity {
	
	@XStreamAlias("softwarecount")
	private int softwarecount;
	@XStreamAlias("softwareTypes")
	private List<SoftwareType> softwarecataloglist = new ArrayList<SoftwareType>();
	
	public int getSoftwarecount() {
		return softwarecount;
	}

	public void setSoftwarecount(int softwarecount) {
		this.softwarecount = softwarecount;
	}

	public List<SoftwareType> getSoftwarecataloglist() {
		return softwarecataloglist;
	}

	public void setSoftwarecataloglist(List<SoftwareType> softwarecataloglist) {
		this.softwarecataloglist = softwarecataloglist;
	}

	@Override
	public List<?> getList() {
		return softwarecataloglist;
	}
	
	@XStreamAlias("softwareType")
	public class SoftwareType extends Entity {

		@XStreamAlias("name")
		private String name;
		@XStreamAlias("tag")
		private int tag;
		
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public int getTag() {
			return tag;
		}
		public void setTag(int tag) {
			this.tag = tag;
		}
		
	}

}

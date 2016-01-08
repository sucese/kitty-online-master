package net.oschina.app.team.bean;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import net.oschina.app.R;
import net.oschina.app.bean.Entity;
import net.oschina.app.util.StringUtils;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * 任务实体类
 * 
 * @author FireAnt（http://my.oschina.net/LittleDY）
 * @version 创建时间：2015年1月14日 下午4:26:28
 * 
 */

@SuppressWarnings("serial")
@XStreamAlias("issue")
public class TeamIssue extends Entity {

    public final static String TEAM_ISSUE_STATE_OPENED = "opened";
    public final static String TEAM_ISSUE_STATE_UNDERWAY = "underway";
    public final static String TEAM_ISSUE_STATE_ACCEPTED = "accepted";
    public final static String TEAM_ISSUE_STATE_CLOSED = "closed";

    public final static String TEAM_ISSUE_SOURCE_GITOSC = "Git@OSC";
    public final static String TEAM_ISSUE_SOURCE_GITHUB = "GitHub";
    public final static String TEAM_ISSUE_SOURCE_TEAMOSC = "Team@OSC";

    public final static int TEAM_ISSUE_GITPUSHED = 1;// 已同步到git

    @XStreamAlias("state")
    private String state;

    @XStreamAlias("stateLevel")
    private int stateLevel;

    @XStreamAlias("priority")
    private String priority;

    @XStreamAlias("source")
    private String source;

    @XStreamAlias("title")
    private String title;

    @XStreamAlias("description")
    private String description;

    @XStreamAlias("createTime")
    private String createTime;

    @XStreamAlias("updateTime")
    private String updateTime;

    @XStreamAlias("acceptTime")
    private String acceptTime;

    @XStreamAlias("deadlineTime")
    private String deadlineTime;

    @XStreamAlias("author")
    private Author author;

    @XStreamAlias("toUser")
    private ToUser toUser;

    @XStreamAlias("replyCount")
    private int replyCount;

    @XStreamAlias("labels")
    private List<Label> labels;

    @XStreamAlias("authority")
    private Authority authority;

    @XStreamAlias("gitpush")
    private int gitpush;// 是否已经同步到git

    @XStreamAlias("project")
    private TeamProject project;

    @XStreamAlias("collaborator")
    private List<TeamIssueCollaborator> collaborators;

    @XStreamAlias("childIssues")
    private TeamIssueChild childIssues;// 子任务数

    @XStreamAlias("relations")
    private Relations relations;// 关联任务数

    @XStreamAlias("attachments")
    private Attachments attachments;// 附件数量

    public String getState() {
	return state;
    }

    public void setState(String state) {
	this.state = state;
    }

    public int getStateLevel() {
	return stateLevel;
    }

    public void setStateLevel(int stateLevel) {
	this.stateLevel = stateLevel;
    }

    public String getPriority() {
	return priority;
    }

    public void setPriority(String priority) {
	this.priority = priority;
    }

    public int getGitpush() {
	return gitpush;
    }

    public void setGitpush(int gitpush) {
	this.gitpush = gitpush;
    }

    public TeamProject getProject() {
	return project;
    }

    public void setProject(TeamProject project) {
	this.project = project;
    }

    public TeamIssueChild getChildIssues() {
	return childIssues;
    }

    public void setChildIssues(TeamIssueChild childIssues) {
	this.childIssues = childIssues;
    }

    public Relations getRelations() {
	return relations;
    }

    public void setRelations(Relations relations) {
	this.relations = relations;
    }

    public Attachments getAttachments() {
	return attachments;
    }

    public void setAttachments(Attachments attachments) {
	this.attachments = attachments;
    }

    public String getSource() {
	return source;
    }

    public void setSource(String source) {
	this.source = source;
    }

    public String getTitle() {
	return title;
    }

    public void setTitle(String title) {
	this.title = title;
    }

    public String getDescription() {
	return description;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    public String getCreateTime() {
	return createTime;
    }

    public void setCreateTime(String createTime) {
	this.createTime = createTime;
    }

    public String getUpdateTime() {
	return updateTime;
    }

    public void setUpdateTime(String updateTime) {
	this.updateTime = updateTime;
    }

    public String getAcceptTime() {
	return acceptTime;
    }

    public void setAcceptTime(String acceptTime) {
	this.acceptTime = acceptTime;
    }

    public String getDeadlineTime() {
	return deadlineTime;
    }

    public void setDeadlineTime(String deadlineTime) {
	this.deadlineTime = deadlineTime;
    }

    public Author getAuthor() {
	return author;
    }

    public void setAuthor(Author author) {
	this.author = author;
    }

    public ToUser getToUser() {
	return toUser;
    }

    public void setToUser(ToUser toUser) {
	this.toUser = toUser;
    }

    public List<Label> getLabels() {
	return labels;
    }

    public void setLabels(List<Label> labels) {
	this.labels = labels;
    }

    public int getReplyCount() {
	return replyCount;
    }

    public void setReplyCount(int replyCount) {
	this.replyCount = replyCount;
    }

    public Authority getAuthority() {
	return authority;
    }

    public void setAuthority(Authority authority) {
	this.authority = authority;
    }

    public List<TeamIssueCollaborator> getCollaborators() {
	return collaborators;
    }

    public void setCollaborators(List<TeamIssueCollaborator> collaborators) {
	this.collaborators = collaborators;
    }

    @XStreamAlias("label")
    public class Label implements Serializable {

	@XStreamAlias("name")
	private String name;

	@XStreamAlias("color")
	private String color;

	public String getName() {
	    return name;
	}

	public void setName(String name) {
	    this.name = name;
	}

	public String getColor() {
	    return color;
	}

	public void setColor(String color) {
	    this.color = color;
	}

    }

    @XStreamAlias("toUser")
    public class ToUser implements Serializable {

	@XStreamAlias("id")
	private int id;

	@XStreamAlias("name")
	private String name;

	@XStreamAlias("portrait")
	private String portrait;

	public int getId() {
	    return id;
	}

	public void setId(int id) {
	    this.id = id;
	}

	public String getName() {
	    return name;
	}

	public void setName(String name) {
	    this.name = name;
	}

	public String getPortrait() {
	    return portrait;
	}

	public void setPortrait(String portrait) {
	    this.portrait = portrait;
	}
    }

    // 任务的操作权限
    public class Authority implements Serializable {

	@XStreamAlias("delete")
	private boolean delete;

	@XStreamAlias("updateState")
	private boolean updateState;

	@XStreamAlias("updateAssignee")
	private boolean updateAssignee;

	@XStreamAlias("updateDeadlineTime")
	private boolean updateDeadlineTime;

	@XStreamAlias("updatePriority")
	private boolean updatePriority;

	@XStreamAlias("updateLabels")
	private boolean updateLabels;

	public boolean isDelete() {
	    return delete;
	}

	public void setDelete(boolean delete) {
	    this.delete = delete;
	}

	public boolean isUpdateState() {
	    return updateState;
	}

	public void setUpdateState(boolean updateState) {
	    this.updateState = updateState;
	}

	public boolean isUpdateAssignee() {
	    return updateAssignee;
	}

	public void setUpdateAssignee(boolean updateAssignee) {
	    this.updateAssignee = updateAssignee;
	}

	public boolean isUpdateDeadlineTime() {
	    return updateDeadlineTime;
	}

	public void setUpdateDeadlineTime(boolean updateDeadlineTime) {
	    this.updateDeadlineTime = updateDeadlineTime;
	}

	public boolean isUpdatePriority() {
	    return updatePriority;
	}

	public void setUpdatePriority(boolean updatePriority) {
	    this.updatePriority = updatePriority;
	}

	public boolean isUpdateLabels() {
	    return updateLabels;
	}

	public void setUpdateLabels(boolean updateLabels) {
	    this.updateLabels = updateLabels;
	}

    }

    @XStreamAlias("collaborator")
    public class TeamIssueCollaborator implements Serializable {

	@XStreamAlias("id")
	private int id;
	@XStreamAlias("name")
	private String name;
	@XStreamAlias("portrait")
	private String portrait;

	public int getId() {
	    return id;
	}

	public void setId(int id) {
	    this.id = id;
	}

	public String getName() {
	    return name;
	}

	public void setName(String name) {
	    this.name = name;
	}

	public String getPortrait() {
	    return portrait;
	}

	public void setPortrait(String portrait) {
	    this.portrait = portrait;
	}

    }

    @XStreamAlias("project")
    public class TeamProject implements Serializable {
	@XStreamAlias("source")
	private String source;

	@XStreamAlias("team")
	private String team;

	@XStreamAlias("git")
	private TeamGit git;

	public String getSource() {
	    return source;
	}

	public void setSource(String source) {
	    this.source = source;
	}

	public String getTeam() {
	    return team;
	}

	public void setTeam(String team) {
	    this.team = team;
	}

	public TeamGit getGit() {
	    return git;
	}

	public void setGit(TeamGit git) {
	    this.git = git;
	}
    }

    // 子任务
    @XStreamAlias("childIssues")
    public class TeamIssueChild implements Serializable {

	@XStreamAlias("totalCount")
	private int totalCount;

	@XStreamAlias("closedCount")
	private int closedCount;

	@XStreamImplicit(itemFieldName = "issue")
	private List<TeamIssue> childIssues;

	public int getTotalCount() {
	    return totalCount;
	}

	public void setTotalCount(int totalCount) {
	    this.totalCount = totalCount;
	}

	public int getClosedCount() {
	    return closedCount;
	}

	public void setClosedCount(int closedCount) {
	    this.closedCount = closedCount;
	}

	public List<TeamIssue> getChildIssues() {
	    return childIssues;
	}

	public void setChildIssues(List<TeamIssue> childIssues) {
	    this.childIssues = childIssues;
	}
    }

    // 关联任务
    @XStreamAlias("relations")
    public class Relations implements Serializable {
	@XStreamAlias("totalCount")
	private int totalCount;

	public int getTotalCount() {
	    return totalCount;
	}

	public void setTotalCount(int totalCount) {
	    this.totalCount = totalCount;
	}
    }

    // 任务附件
    @XStreamAlias("attachments")
    public class Attachments implements Serializable {
	@XStreamAlias("totalCount")
	private int totalCount;

	public int getTotalCount() {
	    return totalCount;
	}

	public void setTotalCount(int totalCount) {
	    this.totalCount = totalCount;
	}
    }

    public String getIssueStateText() {
	String res = "待办中";
	if (this.state.equals(TEAM_ISSUE_STATE_OPENED)) {
	    res = "待办中";
	} else if (this.state.equals(TEAM_ISSUE_STATE_UNDERWAY)) {
	    res = "进行中";
	} else if (this.state.equals(TEAM_ISSUE_STATE_CLOSED)) {
	    res = "已完成";
	} else {
	    res = "已验收";
	}

	return res;
    }

    public int getIssueStateFaTextId() {
	int res = R.string.fa_circle_o;
	
	if (this.state.equals(TEAM_ISSUE_STATE_OPENED)) {
	    res = R.string.fa_circle_o;
	} else if (this.state.equals(TEAM_ISSUE_STATE_UNDERWAY)) {
	    res = R.string.fa_dot_circle_o;
	} else if (this.state.equals(TEAM_ISSUE_STATE_CLOSED)) {
	    res = R.string.fa_check_circle_o;
	} else {
	    res = R.string.fa_lock_use;
	}
	return res;
    }

    public String getDeadlineTimeText() {
	SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd");
	Date date = StringUtils.toDate(getDeadlineTime(), dataFormat);
	Calendar calendar = Calendar.getInstance();
	calendar.setTime(date);
	return DateFormat.getDateInstance(DateFormat.SHORT).format(date);
    }

}

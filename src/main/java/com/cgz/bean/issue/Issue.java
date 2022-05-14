package com.cgz.bean.issue;

import com.cgz.bean.project.Version;
import com.cgz.bean.user.User;
import lombok.Data;

import java.util.List;

@Data
public class Issue {
    private String id;  //每个Issue的唯一ID
    private String key; //每个Issue的唯一key
    private String self;    //访问该接口，可获得该Issue的所有数据
    private String lastViewed;  //最后查看时间，即获取数据的时间
    private String resolutionDate;  //解决日期
    private String created; //创建日期
    private String updated; //最后的更新日期
    private float workRatio;    //暂不清楚该字段的含义，可查询issuefield表了解
    private String summary; //该项目的总结
    private String description; //该项目的描述
    private String environment; //暂不清楚其含义，可查询issuefield表了解
    private String duedate; //指的是某个日期，暂不清楚其含义，可查询issuefield表了解

    private String priority;    //优先级，这里只记优先级的名字
    private String status;  //该issue的状态，这里只记状态的名字
    private String resolution;  //解决方式，这里只记名字
    private String issueType;   //该issue的类型，这里只记名字
    private String project; //该issue所属的项目，这里只记项目的key

    private User assignee;  //该issue分配给的人
    private User creator;   //创建该issue的人
    private User reporter;  //报告该issue的人

    private int timeEstimate;   //时间估计
    private int aggregateTimeOriginalEstimate;  //总共时间的原始估计
    private int aggregateTimeEstimate;  //总共时间的估计
    private int timeOriginalEstimate;   //时间的原始估计
    private int timeSpent; //时间花费
    private int aggregateTimeSpent;    //总共的时间花费
    private Progress aggregateProgress; //总的进度
    private Progress progress;  //进度
    @Data
    public class Progress {
        private long progress;
        private long total;
        private float percent;
    }
    private TimeTracking timetracking;  //时间追踪吗？，具体含义可查询issuefield表了解
    @Data
    public class TimeTracking{
        private String remainingEstimate;
        private String timeSpent;
        private long remainingEstimateSeconds;
        private long timeSpentSeconds;
    }

    private List<String> labels;   //标签
    private List<Version> fixVersions; //修复版本
    private List<Version> versions;    //指的是该Issue所经历的版本吗，可查询issuefield表了解
    private List<User> watchers;   //所有的watcher，需访问/issue/{}/watchers接口
    private List<User> voters; //所有的voter，需访问/issue/{}/votes接口
    private List<Component> components;   //ACCUMULO-3277，ACCUMULO-3513，该issue的component
    private List<Attachment> attachment;   //ACCUMULO-3513，该issue的补丁

    private List<String> subtasks;     //ACCUMULO-2171，该issue的subtask，与另一个issue相连，这里只记另一个issue的唯一key，而这又与issuelink有何区别呢?
    private String parent; //与subtask对应，大多数issue没有该字段

    private List<String> issueLinks;   //ACCUMULO-4677，该Issue的所有issueLink，数据库issue表只记录issueLink的唯一id
    private List<RemoteLink> remoteLinks;   //ACCUMULO-3277，ACCUMULO-4677，该Issue的所有remoteLink，数据库issue表只记录remoteLink的唯一id

    private List<Comment> comments; //该issue的评论，数据库issue表只记录唯一id，需访问/issue/{}/comment接口
    private List<WorkLog> worklog;  //该issue的worklog，数据库issue表只记录唯一id，需访问/issue/{}/worklog接口
    private List<History> histories;    //该issue的history，需添加请求参数expand=changelog

}

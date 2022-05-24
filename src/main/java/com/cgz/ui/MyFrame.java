package com.cgz.ui;

import com.cgz.dao.breakpoint.BreakPointDao;
import com.cgz.handler.IssueHandler;
import com.cgz.handler.MetaHandler;
import com.cgz.handler.ProjectHandler;
import com.cgz.request.issue.IssueAPI;
import com.mashape.unirest.http.exceptions.UnirestException;

import javax.swing.*;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyFrame {

    public JTextArea jTextArea;
    public JScrollPane scroll;
    public ExecutorService executorService = Executors.newFixedThreadPool(1);

    public void start() {
        JFrame jf = new JFrame("JiraIssue提取器");          // 创建窗口
        jf.setSize(800, 500);                       // 设置窗口大小
        jf.setLocationRelativeTo(null);             // 把窗口位置设置到屏幕中心
        jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); // 当点击窗口的关闭按钮时退出程序（没有这一句，程序不会退出）

        JPanel panel = new JPanel(null);                // 创建面板容器，使用默认的布局管理器

        JButton metaBtn = new JButton("获取元数据");
        metaBtn.setSize(150,40);
        metaBtn.setLocation(50,20);
        panel.add(metaBtn);

        JButton projectBtn = new JButton("获取所有项目信息");
        projectBtn.setSize(150,40);
        projectBtn.setLocation(300,20);
        panel.add(projectBtn);

        JButton issueCountBtn = new JButton("获取issue数量");
        issueCountBtn.setSize(150,40);
        issueCountBtn.setLocation(50,100);
        panel.add(issueCountBtn);

        JButton issueBtn = new JButton("获取issue");
        issueBtn.setSize(150,40);
        issueBtn.setLocation(300,100);
        panel.add(issueBtn);

        JButton pointBtn = new JButton("查询上次位置");
        pointBtn.setSize(150,40);
        pointBtn.setLocation(50,180);
        panel.add(pointBtn);

        JLabel jqlJLabel = new JLabel();
        jqlJLabel.setSize(200,15);
        jqlJLabel.setLocation(50,260);
        jqlJLabel.setText("jql语句(默认为全部):");
        panel.add(jqlJLabel);

        JTextField jqlJTextField = new JTextField();
        jqlJTextField.setSize(400,40);
        jqlJTextField.setLocation(50,280);
        panel.add(jqlJTextField);

        JLabel startAtJLabel = new JLabel();
        startAtJLabel.setSize(200,15);
        startAtJLabel.setLocation(300,160);
        startAtJLabel.setText("开始位置(默认从上次或0开始):");
        panel.add(startAtJLabel);

        JTextField startAtJTextField = new JTextField();
        startAtJTextField.setSize(100,30);
        startAtJTextField.setLocation(300,180);
        panel.add(startAtJTextField);

        JLabel issueCountJLabel = new JLabel();
        issueCountJLabel.setSize(200,15);
        issueCountJLabel.setLocation(300,220);
        issueCountJLabel.setText("要获取的issue数量(默认为全部):");
        panel.add(issueCountJLabel);

        JTextField issueCountJTextField = new JTextField();
        issueCountJTextField.setSize(100,30);
        issueCountJTextField.setLocation(300,240);
        panel.add(issueCountJTextField);

        JTextArea illustrateJTextArea = new JTextArea();
        illustrateJTextArea.setSize(400,80);
        illustrateJTextArea.setLocation(50,340);
        illustrateJTextArea.setEditable(false);
        illustrateJTextArea.setLineWrap(true);
        illustrateJTextArea.setText("说明：\n");
        illustrateJTextArea.append(" ·获取issue数量、获取issue、查询上次位置均需要输入jql语句\n");
        illustrateJTextArea.append(" ·不输入jql语句则为默认\n");
        illustrateJTextArea.append(" ·获取issue需要填写开始位置和数量，不填则为默认\n");
        panel.add(illustrateJTextArea);

        jTextArea = new JTextArea();
        jTextArea.setSize(260,400);
        jTextArea.setLocation(500,20);
        jTextArea.setEditable(false);
        jTextArea.setLineWrap(true);
        jTextArea.setText("运行时信息:\n");
        scroll = new JScrollPane(jTextArea);
        scroll.setSize(260,400);
        scroll.setLocation(500,20);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        panel.add(scroll);

        metaBtn.addActionListener(e -> executorService.submit(()-> {
            try {
                new MetaHandler().getAllMetaData(this);
            } catch (UnirestException | SQLException unirestException) {
                unirestException.printStackTrace();
            }
        }));
        projectBtn.addActionListener(e -> executorService.submit(()-> {
            try {
                new ProjectHandler().getAllProjectInfo(this);
            } catch (UnirestException | SQLException unirestException) {
                unirestException.printStackTrace();
            }
        }));
        issueCountBtn.addActionListener(e -> {
            String jql = "all";
            if(!(textIsNull(jqlJTextField))){
                jql = jqlJTextField.getText();
            }
            int issueCount = new IssueAPI().getIssueCount(jql);
            addJTextAreaInfo("issue数量为"+issueCount+"个。");
            JOptionPane.showMessageDialog(jf,issueCount,"issue数量",JOptionPane.INFORMATION_MESSAGE);
        });
        issueBtn.addActionListener(e -> {
            int startAt=0;
            if(!(textIsNull(startAtJTextField))){
                startAt = Integer.parseInt(startAtJTextField.getText());
            }
            int issueCount=0;
            if(!(textIsNull(issueCountJTextField))) {
                issueCount = Integer.parseInt(issueCountJTextField.getText());
            }
            int finalStartAt = startAt;
            int finalIssueCount = issueCount;
            executorService.submit(()->{
                IssueHandler issueHandler = new IssueHandler(this);
                issueHandler.insertIssuesByCount(jqlJTextField.getText(),finalStartAt, finalIssueCount);
            });
        });

        pointBtn.addActionListener(e -> {
            String jql="all";
            int lastBreakPoint=0;
            if(!textIsNull(jqlJTextField)) {
                jql = jqlJTextField.getText();
            }
            try {
                lastBreakPoint = new BreakPointDao().getLastBreakPoint(jql);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            addJTextAreaInfo("上次已获取"+lastBreakPoint+"个。");
            JOptionPane.showMessageDialog(jf,lastBreakPoint,"上次位置",JOptionPane.INFORMATION_MESSAGE);
        });

        // 4. 把 面板容器 作为窗口的内容面板 设置到 窗口
        jf.setContentPane(panel);
        // 5. 显示窗口，前面创建的信息都在内存中，通过 jf.setVisible(true) 把内存中的窗口显示在屏幕上。
        jf.setVisible(true);
    }

    public void addJTextAreaInfo(String info){
        jTextArea.append(info+"\n");
        //jTextArea.paintImmediately(jTextArea.getBounds());
        jTextArea.setSelectionStart(jTextArea.getText().length());//自动向下滚动
    }

    private boolean textIsNull(JTextField jTextField){
        return "".equals(jTextField.getText())||jTextField.getText()==null;
    }
}

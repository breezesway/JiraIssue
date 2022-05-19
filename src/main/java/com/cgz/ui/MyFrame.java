package com.cgz.ui;

import com.cgz.handler.MetaHandler;
import com.cgz.handler.ProjectHandler;
import com.mashape.unirest.http.exceptions.UnirestException;

import javax.swing.*;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class MyFrame {

    private JTextArea jTextArea;

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
        projectBtn.setLocation(50,100);
        panel.add(projectBtn);

        JButton allIssueBtn = new JButton("获取全部issue");
        allIssueBtn.setSize(150,40);
        allIssueBtn.setLocation(300,20);
        panel.add(allIssueBtn);

        JButton projectIssueBtn = new JButton("获取某项目的issue");
        projectIssueBtn.setSize(150,40);
        projectIssueBtn.setLocation(300,100);
        panel.add(projectIssueBtn);

        jTextArea = new JTextArea();
        jTextArea.setSize(260,400);
        jTextArea.setLocation(500,20);
        jTextArea.setEditable(false);
        jTextArea.setLineWrap(true);
        jTextArea.setText("运行时信息:\n");
        JScrollPane scroll = new JScrollPane(jTextArea);
        scroll.setSize(260,400);
        scroll.setLocation(500,20);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        panel.add(scroll);

        metaBtn.addActionListener(e -> {
            try {
                new MetaHandler().getAllMetaData(this);
            } catch (UnirestException | SQLException unirestException) {
                unirestException.printStackTrace();
            }
        });
        projectBtn.addActionListener(e -> {
            try {
                new ProjectHandler().getAllProjectInfo(this);
            } catch (UnirestException | SQLException unirestException) {
                unirestException.printStackTrace();
            }
        });
        allIssueBtn.addActionListener(e -> {

        });
        projectIssueBtn.addActionListener(e -> {
            String inputContent = JOptionPane.showInputDialog(jf, "输入项目的key(不同项目之间以英文逗号分隔):", "");
            List<String> projectKey = Arrays.stream(inputContent.split(",|，")).toList();
        });

        // 4. 把 面板容器 作为窗口的内容面板 设置到 窗口
        jf.setContentPane(panel);
        // 5. 显示窗口，前面创建的信息都在内存中，通过 jf.setVisible(true) 把内存中的窗口显示在屏幕上。
        jf.setVisible(true);
    }

    public void addJTextAreaInfo(String info){
        jTextArea.append(info+"\n");
        jTextArea.setSelectionStart(jTextArea.getText().length());//自动向下滚动
    }
}

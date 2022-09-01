package com.cgz.ui;

import com.cgz.dao.Database;
import com.cgz.dao.other.BreakPointDao;
import com.cgz.dao.other.DBDao;
import com.cgz.handler.IssueHandler;
import com.cgz.handler.MetaHandler;
import com.cgz.handler.ProjectHandler;
import com.cgz.request.issue.IssueAPI;
import com.mashape.unirest.http.exceptions.UnirestException;

import javax.swing.*;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyFrame {

    public JFrame jf;
    JPanel panel;
    public JTextArea jTextArea;
    public JScrollPane scroll;
    JLabel databaseJLabel;

    public ExecutorService executorService = Executors.newFixedThreadPool(2);

    boolean hasChangedDB = false;
    String currentDB;
    String previousDB;

    public void start() {

        jf = new JFrame("JiraIssue提取器");          // 创建窗口
        jf.setSize(800, 500);                       // 设置窗口大小
        jf.setLocationRelativeTo(null);             // 把窗口位置设置到屏幕中心
        jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); // 当点击窗口的关闭按钮时退出程序（没有这一句，程序不会退出）

        panel = new JPanel(null);                // 创建面板容器，使用默认的布局管理器

        JButton metaBtn = new JButton("获取元数据");
        metaBtn.setSize(150, 40);
        metaBtn.setLocation(50, 20);
        panel.add(metaBtn);

        JButton projectBtn = new JButton("获取所有项目信息");
        projectBtn.setSize(150, 40);
        projectBtn.setLocation(300, 20);
        panel.add(projectBtn);

        JButton issueCountBtn = new JButton("获取issue数量");
        issueCountBtn.setSize(150, 40);
        issueCountBtn.setLocation(50, 100);
        panel.add(issueCountBtn);

        JButton issueBtn = new JButton("获取issue");
        issueBtn.setSize(150, 40);
        issueBtn.setLocation(300, 100);
        panel.add(issueBtn);

        JButton pointBtn = new JButton("查询上次位置");
        pointBtn.setSize(150, 40);
        pointBtn.setLocation(50, 180);
        panel.add(pointBtn);

        JLabel jqlJLabel = new JLabel();
        jqlJLabel.setSize(200, 15);
        jqlJLabel.setLocation(50, 260);
        jqlJLabel.setText("jql语句(默认为全部):");
        panel.add(jqlJLabel);

        JTextField jqlJTextField = new JTextField();
        jqlJTextField.setSize(400, 40);
        jqlJTextField.setLocation(50, 280);
        panel.add(jqlJTextField);

        JLabel startAtJLabel = new JLabel();
        startAtJLabel.setSize(200, 15);
        startAtJLabel.setLocation(300, 160);
        startAtJLabel.setText("开始位置(默认从上次或0开始):");
        panel.add(startAtJLabel);

        JTextField startAtJTextField = new JTextField();
        startAtJTextField.setSize(100, 30);
        startAtJTextField.setLocation(300, 180);
        panel.add(startAtJTextField);

        JLabel issueCountJLabel = new JLabel();
        issueCountJLabel.setSize(200, 15);
        issueCountJLabel.setLocation(300, 220);
        issueCountJLabel.setText("要获取的issue数量(默认为全部):");
        panel.add(issueCountJLabel);

        JTextField issueCountJTextField = new JTextField();
        issueCountJTextField.setSize(100, 30);
        issueCountJTextField.setLocation(300, 240);
        panel.add(issueCountJTextField);

        JTextArea illustrateJTextArea = new JTextArea();
        illustrateJTextArea.setSize(400, 80);
        illustrateJTextArea.setLocation(50, 340);
        illustrateJTextArea.setEditable(false);
        illustrateJTextArea.setLineWrap(true);
        illustrateJTextArea.setText("说明：\n");
        illustrateJTextArea.append(" ·获取issue数量、获取issue、查询上次位置均需要输入jql语句\n");
        illustrateJTextArea.append(" ·不输入jql语句则为默认\n");
        illustrateJTextArea.append(" ·获取issue需要填写开始位置和数量，不填则为默认\n");
        panel.add(illustrateJTextArea);

        databaseJLabel = new JLabel();
        databaseJLabel.setSize(260, 15);
        databaseJLabel.setLocation(500, 20);
        databaseJLabel.setText("正在连接数据库...");
        panel.add(databaseJLabel);

        executorService.submit(this::checkDatabaseConnection);

        JButton changedbBtn = new JButton("切换数据库");
        changedbBtn.setSize(100, 30);
        changedbBtn.setLocation(500, 50);
        panel.add(changedbBtn);

        JButton createdbBtn = new JButton("创建数据库");
        createdbBtn.setSize(100, 30);
        createdbBtn.setLocation(650, 50);
        panel.add(createdbBtn);

        jTextArea = new JTextArea();
        jTextArea.setSize(260, 320);
        jTextArea.setLocation(500, 100);
        jTextArea.setEditable(false);
        jTextArea.setLineWrap(true);
        jTextArea.setText("运行时信息:\n");
        scroll = new JScrollPane(jTextArea);
        scroll.setSize(260, 320);
        scroll.setLocation(500, 100);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        panel.add(scroll);

        metaBtn.addActionListener(e -> executorService.submit(() -> {
            try {
                new MetaHandler().getAllMetaData(this);
            } catch (UnirestException | SQLException unirestException) {
                unirestException.printStackTrace();
            }
        }));
        projectBtn.addActionListener(e -> executorService.submit(() -> {
            try {
                new ProjectHandler().getAllProjectInfo(this);
            } catch (UnirestException | SQLException unirestException) {
                unirestException.printStackTrace();
            }
        }));
        issueCountBtn.addActionListener(e -> {
            String jql = "all";
            if (!(textIsNull(jqlJTextField))) {
                jql = jqlJTextField.getText();
            }
            int issueCount = new IssueAPI().getIssueCount(jql);
            addJTextAreaInfo("issue数量为" + issueCount + "个。");
            JOptionPane.showMessageDialog(jf, issueCount, "issue数量", JOptionPane.INFORMATION_MESSAGE);
        });
        issueBtn.addActionListener(e -> {
            int startAt = -1;
            if (!(textIsNull(startAtJTextField))) {
                startAt = Integer.parseInt(startAtJTextField.getText());
            }
            int issueCount = 0;
            if (!(textIsNull(issueCountJTextField))) {
                issueCount = Integer.parseInt(issueCountJTextField.getText());
            }
            int finalStartAt = startAt;
            int finalIssueCount = issueCount;
            executorService.submit(() -> {
                IssueHandler issueHandler = new IssueHandler(this);
                issueHandler.insertIssuesByCount(jqlJTextField.getText(), finalStartAt, finalIssueCount);
            });
        });

        pointBtn.addActionListener(e -> {
            String jql = "all";
            int lastBreakPoint = 0;
            if (!textIsNull(jqlJTextField)) {
                jql = jqlJTextField.getText();
            }
            try {
                lastBreakPoint = new BreakPointDao().getLastBreakPoint(jql);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            addJTextAreaInfo("上次已获取" + lastBreakPoint + "个。");
            JOptionPane.showMessageDialog(jf, lastBreakPoint, "上次位置", JOptionPane.INFORMATION_MESSAGE);
        });

        changedbBtn.addActionListener(e -> {
            try {
                showCustomDialog(jf,jf);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }finally {
                if (!hasChangedDB){
                    Database.changeDataSource(previousDB);
                }
            }
        });

        createdbBtn.addActionListener(e -> JOptionPane.showMessageDialog(jf, "该功能还未完成", "提示", JOptionPane.INFORMATION_MESSAGE));

        // 4. 把 面板容器 作为窗口的内容面板 设置到 窗口
        jf.setContentPane(panel);
        // 5. 显示窗口，前面创建的信息都在内存中，通过 jf.setVisible(true) 把内存中的窗口显示在屏幕上。
        jf.setVisible(true);
    }

    public void addJTextAreaInfo(String info) {
        jTextArea.append(info + "\n");
        //jTextArea.paintImmediately(jTextArea.getBounds());
        jTextArea.setSelectionStart(jTextArea.getText().length());//自动向下滚动
    }

    private boolean textIsNull(JTextField jTextField) {
        return "".equals(jTextField.getText()) || jTextField.getText() == null;
    }

    private void checkDatabaseConnection() {
        String s = Database.getDatabaseName();
        if (s != null) {
            databaseJLabel.setText("当前连接的数据库: " + s);
            currentDB = s;
        } else {
            JOptionPane.showMessageDialog(jf, "数据库连接失败，未开启MySQL服务！", "数据库连接", JOptionPane.INFORMATION_MESSAGE);
            databaseJLabel.setText("数据库连接失败");
            while (true) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if ((s = Database.getDatabaseName()) != null) {
                    JOptionPane.showMessageDialog(jf, "数据库连接成功！", "数据库连接", JOptionPane.INFORMATION_MESSAGE);
                    databaseJLabel.setText("当前连接的数据库: " + s);
                    currentDB = s;
                    break;
                }
            }
        }
    }

    /**
     * 显示一个自定义的对话框
     *
     * @param owner 对话框的拥有者
     * @param parentComponent 对话框的父级组件
     */
    private void showCustomDialog(Frame owner, Component parentComponent) throws SQLException {
        hasChangedDB = false;
        previousDB = currentDB;
        // 创建一个模态对话框
        final JDialog dialog = new JDialog(owner, "所有的数据库", true);
        // 设置对话框的宽高
        dialog.setSize(400, 300);
        // 设置对话框大小不可改变
        dialog.setResizable(false);
        // 设置对话框相对显示的位置
        dialog.setLocationRelativeTo(parentComponent);

        // 创建对话框的内容面板, 在面板内可以根据自己的需要添加任何组件并做任意是布局
        JPanel diaPanel = new JPanel(null);

        String[] columnNames = {"数据库", "描述"};
        if(!"jira".equals(currentDB)){
            Database.changeDataSource("jira");
            databaseJLabel.setText("当前连接的数据库: jira");
            hasChangedDB = true;
            previousDB = currentDB;
            currentDB = "jira";
        }
        String[][] databases = new DBDao().getAllDatabase();
        JTable table = new JTable(databases, columnNames);

        TableColumn column = table.getColumnModel().getColumn(0);
        column.setPreferredWidth(80);
        column.setMaxWidth(130);
        column.setMinWidth(80);
        JScrollPane subJScrollPane = new JScrollPane(table);
        /*subJScrollPane.add(table.getTableHeader(), BorderLayout.NORTH);
        subJScrollPane.add(table, BorderLayout.CENTER);*/
        subJScrollPane.setSize(330, 170);
        subJScrollPane.setLocation(30, 20);
        subJScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        subJScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        diaPanel.add(subJScrollPane);

        // 创建一个按钮用于关闭对话框
        JButton changeBtn = new JButton("切换");
        changeBtn.setSize(100, 30);
        changeBtn.setLocation(150, 205);
        changeBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if(row==-1){
                JOptionPane.showMessageDialog(jf, "未选择数据库", "提示", JOptionPane.INFORMATION_MESSAGE);
            }else {
                String database = (String) table.getModel().getValueAt(row, 0);
                Database.changeDataSource(database);
                databaseJLabel.setText("当前连接的数据库: " + database);
                hasChangedDB = true;
                previousDB = currentDB;
                currentDB = database;
                dialog.dispose();
            }
        });
        // 添加组件到面板
        diaPanel.add(changeBtn);
        // 设置对话框的内容面板
        dialog.setContentPane(diaPanel);
        // 显示对话框
        dialog.setVisible(true);
    }
}

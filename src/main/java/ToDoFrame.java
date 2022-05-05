import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class ToDoFrame extends JFrame {
    private int userID;
    public int getUserID() {
        return userID;
    }
    public void setUserID(int userID) {
        this.userID = userID;
    }

    private JDBC jdbc = new JDBC("jdbc:mysql://localhost:3307/users", "root", "Password123");
    ArrayList<JTextField> fields = new ArrayList<>();
    ArrayList<JCheckBox> checks = new ArrayList<>();

    JPanel mainPanel = new JPanel();

    ToDoFrame() {
        setTitle("To do list");
        setSize(new Dimension(600, 800));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        ImageIcon img = new ImageIcon("C:\\Users\\Alexandru Duna\\IdeaProjects\\ToDoList\\src\\main\\resources\\frameIcon.png");
        setIconImage(img.getImage());
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel title = new JPanel();
        title.setPreferredSize(new Dimension(600, 80));

        JLabel titleLabel = new JLabel("To do:");
        titleLabel.setFont(new Font("", Font.PLAIN, 35));
        titleLabel.setVerticalAlignment(SwingConstants.CENTER);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setPreferredSize(new Dimension(330, 80));
        title.add(titleLabel);

        //main panel
        mainPanel.setLayout(new FlowLayout((int) LEFT_ALIGNMENT,5,5));
        mainPanel.setPreferredSize(new Dimension(600, 0));

        //add delete task buttons and panel:
        JButton addTask = new JButton();
        addTask.setPreferredSize(new Dimension(50, 50));
        addTask.setIcon(new ImageIcon("C:\\Users\\Alexandru Duna\\IdeaProjects\\ToDoList\\src\\main\\resources\\addTask.png"));
        addTask.setFocusable(false);

        JButton deleteTask = new JButton();
        deleteTask.setPreferredSize(new Dimension(50, 50));
        deleteTask.setIcon(new ImageIcon("C:\\Users\\Alexandru Duna\\IdeaProjects\\ToDoList\\src\\main\\resources\\delete.png"));
        deleteTask.setFocusable(false);

        JPanel buttonpanel = new JPanel();
        buttonpanel.setLayout(new FlowLayout(FlowLayout.CENTER, 50, 5));
        buttonpanel.add(addTask);
        buttonpanel.setPreferredSize(new Dimension(600, 60));

        //action listeners for add and delete
        addTask.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                JTextField textField = new JTextField();
                textField.setPreferredSize(new Dimension(510, 40));
                textField.setFont(new Font("MV Boli", Font.TYPE1_FONT, 20));
                textField.setBorder(new BevelBorder(BevelBorder.LOWERED));
                textField.setVisible(true);

                JCheckBox done = new JCheckBox();
                done.setIcon(new ImageIcon("C:\\Users\\Alexandru Duna\\IdeaProjects\\ToDoList\\src\\main\\resources\\undone.png"));
                done.setSelectedIcon(new ImageIcon("C:\\Users\\Alexandru Duna\\IdeaProjects\\ToDoList\\src\\main\\resources\\done.png"));
                done.setSelected(false);
                done.setBackground(Color.decode("#8DE5E9"));
                done.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (!done.isSelected()) {
                            textField.setBackground(Color.WHITE);
                            setVisible(true);
                        } else {
                            textField.setBackground(Color.GREEN);
                            setVisible(true);
                        }
                    }
                });
                done.setFocusable(false);
                done.setPreferredSize(new Dimension(40, 40));
                done.setVerticalAlignment(SwingConstants.CENTER);
                done.setHorizontalAlignment(SwingConstants.CENTER);

                fields.add(textField);
                checks.add(done);

                mainPanel.add(done);
                mainPanel.add(textField);

                mainPanel.setPreferredSize(new Dimension(600, mainPanel.getHeight() + 40));
                setVisible(true);
            }
        });

        //COULD BE BETTER
        deleteTask.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (int i = 0; i < fields.size(); i++) {
                    if (fields.get(i).getBackground() == Color.GREEN) {
                        mainPanel.remove(fields.get(i));
                        mainPanel.remove(checks.get(i));

                        fields.remove(fields.get(i));
                        checks.remove(checks.get(i));

                        mainPanel.repaint();
                        mainPanel.setVisible(true);
                        repaint();
                        setVisible(true);
                    }
                }
            }
        });

        //SAVE BUTTON:
        JButton saveButton = new JButton("Save");
        saveButton.setPreferredSize(new Dimension(80, 40));
        saveButton.setFocusable(false);
        buttonpanel.add(saveButton);
        buttonpanel.add(deleteTask);
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //saving:
                jdbc.query("delete from tasks where user_id=" + userID + ";");
                for (JTextField f : fields) {
                    boolean done = f.getBackground() == Color.GREEN;

                    int status;
                    if (done) status = 1;
                    else status = 0;

                    jdbc.query("insert into tasks(user_id,task,status) value(" + userID + ",'" + f.getText() + "'," + status + ");");
                }
                //tray message:
                try {
                    SystemTray tray= SystemTray.getSystemTray();
                    TrayIcon icon=new TrayIcon(new ImageIcon("C:\\Users\\Alexandru Duna\\IdeaProjects\\ToDoList\\src\\main\\resources\\done.png").getImage());
                    tray.add(icon);
                    icon.displayMessage("Succesfully saved", "The tasks has been succesfully saved", TrayIcon.MessageType.INFO);
                    icon.setImageAutoSize(true);
                    Thread.sleep(2000);
                    tray.remove(icon);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            }
        });

        //color
        mainPanel.setBackground(Color.decode("#8DE5E9"));
        title.setBackground(Color.decode("#8DE5E9"));
        buttonpanel.setBackground(Color.decode("#8DE5E9"));

        //adds:
        JScrollPane scrollPane = new JScrollPane(mainPanel, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        add(title, BorderLayout.NORTH);
        add(buttonpanel, BorderLayout.SOUTH);
        add(scrollPane, BorderLayout.CENTER);
        setVisible(true);
    }

}
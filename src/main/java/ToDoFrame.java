import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.TimerTask;

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
    ArrayList<JLabel> labels = new ArrayList<>();
    ArrayList<JButton> buttons = new ArrayList<>();

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
        mainPanel.setLayout(new FlowLayout());
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
                JLabel iconLabel = new JLabel();
                iconLabel.setPreferredSize(new Dimension(50, 40));
                ImageIcon icon = new ImageIcon("C:\\Users\\Alexandru Duna\\IdeaProjects\\ToDoList\\src\\main\\resources\\undone.png");
                iconLabel.setIcon(icon);

                JTextField textField = new JTextField();
                textField.setPreferredSize(new Dimension(420, 40));
                textField.setFont(new Font("MV Boli", Font.TYPE1_FONT, 20));
                textField.setBorder(new BevelBorder(BevelBorder.LOWERED));
                textField.setVisible(true);

                JButton done = new JButton("Done");
                done.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (done.getText().equals("Done")) {
                            iconLabel.setIcon(new ImageIcon("C:\\Users\\Alexandru Duna\\IdeaProjects\\ToDoList\\src\\main\\resources\\done.png"));
                            done.setText("Undone");
                            textField.setBackground(Color.GREEN);
                            setVisible(true);
                        } else {
                            iconLabel.setIcon(new ImageIcon("C:\\Users\\Alexandru Duna\\IdeaProjects\\ToDoList\\src\\main\\resources\\undone.png"));
                            done.setText("Done");
                            textField.setBackground(Color.WHITE);
                            setVisible(true);
                        }
                    }
                });
                done.setFocusable(false);
                done.setPreferredSize(new Dimension(80, 40));

                fields.add(textField);
                labels.add(iconLabel);
                buttons.add(done);

                mainPanel.add(iconLabel);
                mainPanel.add(textField);
                mainPanel.add(done);
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
                        mainPanel.remove(buttons.get(i));
                        mainPanel.remove(labels.get(i));

                        fields.remove(fields.get(i));
                        buttons.remove(buttons.get(i));
                        labels.remove(labels.get(i));

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

            }
        });

        //adds:
        JScrollPane scrollPane = new JScrollPane(mainPanel, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        add(title, BorderLayout.NORTH);
        add(buttonpanel, BorderLayout.SOUTH);
        add(scrollPane, BorderLayout.CENTER);
        setVisible(true);
    }

}
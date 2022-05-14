import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.SQLException;

public class LoginFrame extends JFrame {
    private JDBC jdbc = new JDBC("jdbc:mysql://localhost:3307/users", "root", "Password123");
    private JTextField userField;
    private JPasswordField passwordField;
    private JLabel warningLabel;
    private JCheckBox showpassword;

    LoginFrame() {
        //frame settings:
        setSize(new Dimension(500, 330));
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Login");
        ImageIcon icon = new ImageIcon("src/main/resources/login.jpg");
        setIconImage(icon.getImage());

        //title
        JLabel title = new JLabel("Login");
        title.setFont(new Font("", Font.PLAIN, 40));
        title.setSize(new Dimension(500, 50));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setVerticalAlignment(SwingConstants.CENTER);

        //username:
        JLabel userLabel = new JLabel("Username:");
        userLabel.setFont(new Font("", Font.PLAIN, 20));
        userLabel.setSize(new Dimension(500, 30));
        userLabel.setHorizontalAlignment(SwingConstants.LEFT);
        userLabel.setVerticalAlignment(SwingConstants.NORTH);
        userLabel.setBounds(10, 100, 100, 50);

        //password:
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("", Font.PLAIN, 20));
        passwordLabel.setSize(new Dimension(500, 30));
        passwordLabel.setHorizontalAlignment(SwingConstants.LEFT);
        passwordLabel.setVerticalAlignment(SwingConstants.NORTH);
        passwordLabel.setBounds(10, 150, 100, 50);

        //show password button:
        showpassword = new JCheckBox();
        showpassword.setIcon(new ImageIcon("src/main/resources/eye.png"));
        showpassword.setSelectedIcon(new ImageIcon("src/main/resources/noeye.png"));
        showpassword.setHorizontalAlignment(SwingConstants.CENTER);
        showpassword.setVerticalAlignment(SwingConstants.CENTER);
        showpassword.setBounds(440, 145, 40, 40);
        showpassword.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (showpassword.isSelected()) {
                    passwordField.setEchoChar((char) 0);
                } else {
                    passwordField.setEchoChar('•');
                }
            }
        });

        //this is for showing warning messages about authetification
        warningLabel = new JLabel();
        warningLabel.setFont(new Font("", Font.PLAIN, 15));
        warningLabel.setForeground(Color.RED);
        warningLabel.setOpaque(true);
        warningLabel.setHorizontalAlignment(SwingConstants.CENTER);
        warningLabel.setVerticalAlignment(SwingConstants.CENTER);
        warningLabel.setBounds(100, 190, 300, 20);
        warningLabel.setVisible(false);

        //key listeners for Enter are just for submit
        userField = new JTextField();
        userField.setBounds(140, 101, 300, 30);
        userField.setFont(new Font("MV Boli", Font.PLAIN, 20));
        userField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    try {
                        if (jdbc.validAuth(userField.getText(), passwordField.getText())) {
                            warningLabel.setVisible(false);
                            loadTasks();
                        } else {
                            warningLabel.setText("Invalid username or password!");
                            warningLabel.setVisible(true);
                        }
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });

        passwordField = new JPasswordField();
        passwordField.setBounds(140, 151, 300, 30);
        passwordField.setFont(new Font("", Font.PLAIN, 20));
        passwordField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    try {
                        if (jdbc.validAuth(userField.getText(), passwordField.getText())) {
                            warningLabel.setVisible(false);
                            loadTasks();
                        } else {
                            warningLabel.setText("Invalid username or password!");
                            warningLabel.setVisible(true);
                        }
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });

        //submit buuton that verify if the username and password combination is correct
        JButton submit = new JButton("Submit");
        submit.setBounds(60, 220, 150, 30);
        submit.setFocusable(false);
        submit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (jdbc.validAuth(userField.getText(), passwordField.getText())) {
                        warningLabel.setVisible(false);
                        loadTasks();
                    } else {
                        warningLabel.setText("Invalid username or password!");
                        warningLabel.setVisible(true);
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        //create a new account button:
        JButton createAcc = new JButton("Create account");
        createAcc.setBounds(280, 220, 150, 30);
        createAcc.setFocusable(false);
        createAcc.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (passwordField.getText().length() >= 4 && userField.getText().length() >= 4) {

                    if (jdbc.doesntExist(userField.getText())) {

                        jdbc.insertion(userField.getText(), passwordField.getText());
                        warningLabel.setVisible(false);
                        loadTasks();
                    } else {
                        warningLabel.setText("This username is already taken");
                        warningLabel.setVisible(true);
                    }

                } else {
                    warningLabel.setText("Username or password is not long enough");
                    warningLabel.setVisible(true);
                }
            }
        });

        //frame adds:
        add(title, BorderLayout.NORTH);
        setLayout(null);
        add(userLabel);
        add(passwordLabel);
        add(userField);
        add(passwordField);
        add(showpassword);
        add(submit);
        add(createAcc);
        add(warningLabel);
        setVisible(true);
    }

    /**Function that load the tasks from the right user and will open a To do frame with that tasks
     */
    private void loadTasks() {
        ToDoFrame frame = new ToDoFrame();
        frame.setUserID(Integer.parseInt(jdbc.query("select id from users where username='" + userField.getText() + "';")));
        try {
            jdbc.statement = jdbc.connection.createStatement();
            jdbc.resultSet = jdbc.statement.executeQuery("select status, task from tasks where user_id=" + frame.getUserID() + ";");
            while (jdbc.resultSet.next()) {
                if (jdbc.resultSet.getInt(1) == 1) {
                    JTextField textField = new JTextField();
                    textField.setBackground(Color.GREEN);

                    JCheckBox done = new JCheckBox();
                    done.setIcon(new ImageIcon("src/main/resources/undone.png"));
                    done.setSelectedIcon(new ImageIcon("src/main/resources/done.png"));
                    done.setSelected(true);
                    //done.setBackground(Color.decode("#8DE5E9"));
                    done.setFocusable(false);
                    done.setPreferredSize(new Dimension(40, 40));
                    done.setVerticalAlignment(SwingConstants.CENTER);
                    done.setHorizontalAlignment(SwingConstants.CENTER);

                    done.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            if (!done.isSelected()) {
                                textField.setBackground(Color.WHITE);
                                frame.setVisible(true);
                            } else {
                                textField.setBackground(Color.GREEN);
                                frame.setVisible(true);
                            }
                        }
                    });

                    textField.setPreferredSize(new Dimension(510, 40));
                    textField.setFont(new Font("MV Boli", Font.TYPE1_FONT, 20));
                    textField.setBorder(new BevelBorder(BevelBorder.LOWERED));
                    textField.setVisible(true);


                    frame.mainPanel.add(done);
                    frame.mainPanel.add(textField);

                    textField.setText(jdbc.resultSet.getString(2));

                    frame.fields.add(textField);
                    frame.checks.add(done);
                    frame.setVisible(true);
                    dispose();

                    //when the task is not done
                } else {
                    JCheckBox done = new JCheckBox();
                    done.setIcon(new ImageIcon("src/main/resources/undone.png"));
                    done.setSelectedIcon(new ImageIcon("src/main/resources/done.png"));
                    done.setSelected(false);
                    //done.setBackground(Color.decode("#8DE5E9"));
                    done.setFocusable(false);
                    done.setPreferredSize(new Dimension(40, 40));
                    done.setVerticalAlignment(SwingConstants.CENTER);
                    done.setHorizontalAlignment(SwingConstants.CENTER);

                    JTextField textField = new JTextField();

                    done.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            if (!done.isSelected()) {
                                textField.setBackground(Color.WHITE);
                                frame.setVisible(true);
                            } else {
                                textField.setBackground(Color.GREEN);
                                frame.setVisible(true);
                            }
                        }
                    });

                    textField.setPreferredSize(new Dimension(510, 40));
                    textField.setFont(new Font("MV Boli", Font.TYPE1_FONT, 20));
                    textField.setBorder(new BevelBorder(BevelBorder.LOWERED));
                    textField.setVisible(true);

                    frame.mainPanel.add(done);
                    frame.mainPanel.add(textField);

                    textField.setText(jdbc.resultSet.getString(2));

                    frame.fields.add(textField);
                    frame.checks.add(done);
                    frame.setVisible(true);
                }
            }
            dispose();
        } catch (Exception v) {

        }
    }
}

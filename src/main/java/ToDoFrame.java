import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;

public class ToDoFrame extends JFrame {

    ToDoFrame() {
        // setLayout(new FlowLayout(FlowLayout.CENTER,5,5));
        setTitle("To do list");
        setSize(new Dimension(600, 800));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        ImageIcon img = new ImageIcon("C:\\Users\\Alexandru Duna\\IdeaProjects\\ToDoList\\src\\main\\resources\\2.png");
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
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new FlowLayout());
        mainPanel.setPreferredSize(new Dimension(600, 0));

        //add task button:
        JButton addTask = new JButton("Add task");
        addTask.setFocusable(false);
        JPanel buttonpanel = new JPanel();
        buttonpanel.setLayout(new FlowLayout());
        buttonpanel.add(addTask, LEFT_ALIGNMENT);
        buttonpanel.setPreferredSize(new Dimension(600, 40));
        addTask.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JLabel iconLabel = new JLabel();
                iconLabel.setPreferredSize(new Dimension(50, 40));
                ImageIcon icon = new ImageIcon("C:\\Users\\Alexandru Duna\\IdeaProjects\\ToDoList\\src\\main\\resources\\3.png");
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
                            iconLabel.setIcon(new ImageIcon("C:\\Users\\Alexandru Duna\\IdeaProjects\\ToDoList\\src\\main\\resources\\1.png"));
                            done.setText("Undone");
                            textField.setBackground(Color.GREEN);
                            setVisible(true);
                        } else {
                            iconLabel.setIcon(new ImageIcon("C:\\Users\\Alexandru Duna\\IdeaProjects\\ToDoList\\src\\main\\resources\\3.png"));
                            done.setText("Done");
                            textField.setBackground(Color.WHITE);
                            setVisible(true);
                        }
                    }
                });
                done.setFocusable(false);
                done.setPreferredSize(new Dimension(80, 40));

                mainPanel.add(iconLabel);
                mainPanel.add(textField);
                mainPanel.add(done);
                mainPanel.setPreferredSize(new Dimension(600,mainPanel.getHeight()+40));
                setVisible(true);
            }
        });

        JScrollPane scrollPane = new JScrollPane(mainPanel, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        add(title, BorderLayout.NORTH);
        add(buttonpanel, BorderLayout.SOUTH);
        add(scrollPane, BorderLayout.CENTER);
        setVisible(true);
    }

}
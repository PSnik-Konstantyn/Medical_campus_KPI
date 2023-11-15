package org.example;

import redis.clients.jedis.Jedis;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

import static org.example.JedisActions.convertStudentToJson;
import static org.example.Main.jedisPool;

public class StudentWindow extends MedicalFrame {
    private Student student;

    JTextField medicalRequestField;
    JButton submitRequestButton;

    public StudentWindow(Student student) {
        this.student = student;

        setTitle("Студент: " + student.getName() + " " + student.getSurname());
        setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(20, 20, 20, 20);
        Font labelFont = new Font("Arial", Font.BOLD, 16);
        Font buttonFont = new Font("Arial", Font.PLAIN, 16);

        JLabel nameLabel = new JLabel("Ім'я: " + student.getName() + " " + student.getSurname());
        nameLabel.setFont(labelFont);
        System.out.println(student.isIll() + student.getName());
        JLabel healthStatus = new JLabel("Стан: " + (student.isIll() ? "Хворий" : "Здоровий"));
        healthStatus.setFont(labelFont);

        JLabel imageLabel = new JLabel();

        if (student.isIll()) {
            ImageIcon imageIcon = new ImageIcon(new ImageIcon("src/main/resources/ill.png").getImage().getScaledInstance(70, 70, Image.SCALE_DEFAULT));
            imageLabel = new JLabel(imageIcon);
            imageLabel.setHorizontalAlignment(JLabel.CENTER);
        } else {
            ImageIcon imageIcon = new ImageIcon(new ImageIcon("src/main/resources/healthy.png").getImage().getScaledInstance(70, 70, Image.SCALE_DEFAULT));
            imageLabel = new JLabel(imageIcon);
            imageLabel.setHorizontalAlignment(JLabel.CENTER);
        }

        ImageIcon imageIcon = new ImageIcon(new ImageIcon("src/main/resources/edit.jpeg").getImage().getScaledInstance(35, 25, Image.SCALE_DEFAULT));
        JButton editButton = new JButton(imageIcon);
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new UpdateWindow(student).setVisible(true);
            }
        });

        constraints.gridx = 2;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.NORTHEAST;
        add(editButton, constraints);

        medicalRequestField = new JTextField(20);
        medicalRequestField.setFont(labelFont);

        submitRequestButton = new JButton("Відправити запит");
        submitRequestButton.setFont(buttonFont);

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 2;
        constraints.anchor = GridBagConstraints.CENTER;
        add(nameLabel, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        add(healthStatus, constraints);

        constraints.gridx = 0;
        constraints.gridy = 2;
        add(imageLabel, constraints);

        constraints.gridx = 0;
        constraints.gridy = 3;
        add(medicalRequestField, constraints);

        constraints.gridx = 0;
        constraints.gridy = 4;
        add(submitRequestButton, constraints);

        submitRequestButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try (Jedis jedis = jedisPool.getResource()) {
                    String medicalRequest = medicalRequestField.getText();
                    Request newRequest = new Request(student.getStudentID(), medicalRequest, false);
                    jedis.set("request:" + student.getStudentID(), newRequest.toJson());
                    JOptionPane.showMessageDialog(null, "Запит відправлено!");
                }
            }
        });
        setLocationRelativeTo(null);
    }
}

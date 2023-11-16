package org.example.windows;

import org.example.Student;
import redis.clients.jedis.Jedis;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static org.example.Student.convertStudentToJson;
import static org.example.windows.StartWindow.jedisPool;

public class UpdateWindow extends MedicalFrame {
    private Student student;
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField groupField;
    JButton saveButton;

    public UpdateWindow(Student student) {
        this.student = student;

        setTitle("Редагування інформації");
        setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(20, 20, 20, 20);

        JLabel updateLabel = new JLabel("Редагування даних студента");
        updateLabel.setFont(new Font("Arial", Font.BOLD, 16));
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 2;
        constraints.anchor = GridBagConstraints.CENTER;
        add(updateLabel, constraints);

        JLabel firstNameLabel = new JLabel("Ім'я:");
        firstNameField = new JTextField(20);
        firstNameField.setText(student.getName());
        constraints.gridx = 1;
        constraints.gridy = 1;
        add(firstNameLabel, constraints);
        constraints.gridx = 2;
        add(firstNameField, constraints);

        JLabel lastNameLabel = new JLabel("Прізвище:");
        lastNameField = new JTextField(20);
        lastNameField.setText(student.getSurname());
        constraints.gridx = 1;
        constraints.gridy = 2;
        add(lastNameLabel, constraints);
        constraints.gridx = 2;
        add(lastNameField, constraints);

        JLabel groupLabel = new JLabel("Група:");
        groupField = new JTextField(20);
        groupField.setText(student.getGroup());
        constraints.gridx = 1;
        constraints.gridy = 3;
        add(groupLabel, constraints);
        constraints.gridx = 2;
        add(groupField, constraints);

        saveButton = new JButton("Зберегти зміни");
        saveButton.setFont(new Font("Arial", Font.PLAIN, 16));
        constraints.gridx = 0;
        constraints.gridy = 4;
        constraints.gridwidth = 2;
        constraints.anchor = GridBagConstraints.CENTER;
        add(saveButton, constraints);

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                    try (Jedis jedis = jedisPool.getResource()) {
                        if (firstNameField.getText() != null){
                            student.setName(firstNameField.getText());
                        }
                        if (lastNameField.getText() != null){
                            student.setSurname(lastNameField.getText());
                        }
                        if (groupField.getText() != null){
                            student.setGroup(groupField.getText());
                        }
                        jedis.set("st:" + student.getStudentID(), convertStudentToJson(student));
                        dispose();
                        StudentWindow studentWindow = new StudentWindow(student);
                        studentWindow.setVisible(true);
                    }
                dispose();

                new StudentWindow(student).setVisible(true);
            }
        });

        setLocationRelativeTo(null);
    }
}

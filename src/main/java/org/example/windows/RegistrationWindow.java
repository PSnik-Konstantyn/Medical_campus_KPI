package org.example.windows;

import org.example.Student;
import redis.clients.jedis.Jedis;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

import static org.example.Student.convertStudentToJson;
import static org.example.windows.StartWindow.jedisPool;

public class RegistrationWindow extends MedicalFrame {
    JTextField studentNumberField = new JTextField(20);
    JPasswordField passwordField = new JPasswordField(20);
    JTextField firstNameField = new JTextField(20);
    JTextField lastNameField = new JTextField(20);
    JTextField groupField = new JTextField(20);
    JLabel studentNumberLabel = new JLabel("Номер студентського:");
    JLabel passwordLabel = new JLabel("Пароль:");
    JLabel firstNameLabel = new JLabel("Ім'я:");
    JLabel lastNameLabel = new JLabel("Прізвище:");
    JLabel groupLabel = new JLabel("Группа:");
    JLabel registrationLabel = new JLabel("Реєстрація");
    JButton registerButton = new JButton("Зареєструватися");

    public RegistrationWindow() {
        setTitle("Реєстрація");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(5, 5, 5, 5);

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 2;
        constraints.anchor = GridBagConstraints.CENTER;
        add(registrationLabel, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 1;
        add(studentNumberLabel, constraints);

        constraints.gridx = 1;
        add(studentNumberField, constraints);

        constraints.gridx = 0;
        constraints.gridy = 2;
        add(passwordLabel, constraints);

        constraints.gridx = 1;
        add(passwordField, constraints);

        constraints.gridx = 0;
        constraints.gridy = 3;
        add(firstNameLabel, constraints);

        constraints.gridx = 1;
        add(firstNameField, constraints);

        constraints.gridx = 0;
        constraints.gridy = 4;
        add(lastNameLabel, constraints);

        constraints.gridx = 1;
        add(lastNameField, constraints);

        constraints.gridx = 0;
        constraints.gridy = 5;
        add(groupLabel, constraints);

        constraints.gridx = 1;
        add(groupField, constraints);

        constraints.gridx = 0;
        constraints.gridy = 6;
        constraints.gridwidth = 2;
        constraints.anchor = GridBagConstraints.CENTER;

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    try (Jedis jedis = jedisPool.getResource()) {
                        String studentNumber = studentNumberField.getText();
                        String password = new String(passwordField.getPassword());
                        String firstName = firstNameField.getText();
                        String lastName = lastNameField.getText();
                        String group = groupField.getText();
                        Date now = new Date();
                        Student newStudent = new Student(studentNumber, password, firstName, lastName, group, false, now);
                        jedis.set("st:" + studentNumber, convertStudentToJson(newStudent));
                        dispose();
                        StartWindow startWindow = new StartWindow();
                    }
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        });
        add(registerButton, constraints);
        setLocationRelativeTo(null);
    }
}

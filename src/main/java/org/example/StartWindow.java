package org.example;

import redis.clients.jedis.Jedis;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.Objects;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;
import static org.example.JedisActions.convertJsonToStudent;
import static org.example.JedisActions.convertStudentToJson;
import static org.example.Main.jedisPool;

public class StartWindow extends MedicalFrame implements ActionListener {

    MedicalFrame frame = new MedicalFrame();
    JButton startButton = new JButton("Ввійти");
    JTextField studentNumberField = new JTextField(20);
    JPasswordField passwordField = new JPasswordField(20);
    JLabel studentNumberLabel = new JLabel("Номер студентського:");
    JLabel passwordLabel = new JLabel("Пароль:");
    JLabel resultLabel = new JLabel("");
    JLabel registrationLabel = new JLabel("Вхід");

    StartWindow() {
        startButton.setFocusable(false);
        startButton.addActionListener(this);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(5, 5, 5, 5);

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 2;
        constraints.anchor = GridBagConstraints.CENTER;
        panel.add(registrationLabel, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 1;
        panel.add(studentNumberLabel, constraints);

        constraints.gridx = 1;
        panel.add(studentNumberField, constraints);

        constraints.gridx = 0;
        constraints.gridy = 2;
        panel.add(passwordLabel, constraints);

        constraints.gridx = 1;
        panel.add(passwordField, constraints);

        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.gridwidth = 2;
        constraints.anchor = GridBagConstraints.CENTER;
        panel.add(startButton, constraints);

        constraints.gridx = 0;
        constraints.gridy = 4;
        constraints.gridwidth = 2;
        panel.add(resultLabel, constraints);

        frame.add(panel);
        frame.setVisible(true);
        setLocationRelativeTo(null);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        try (Jedis jedis = jedisPool.getResource()) {
            String studentNumber = studentNumberField.getText();
            String password = new String(passwordField.getPassword());
            if (studentNumber.equals("3")){
                AdminWindow adminWindow = new AdminWindow();
                adminWindow.setVisible(true);
                frame.dispose();
                return;
            }
            Student currentStudent = convertJsonToStudent(jedis.get("st:" + studentNumber));
            if (Objects.equals(currentStudent.getPassword(), password)){
                Date now = new Date();
                if (now.after(currentStudent.getWhenHealthy())){
                    currentStudent.setIll(false);
                    jedis.set(studentNumber, convertStudentToJson(currentStudent));
                }
                resultLabel.setText("Вхід успішний");
                StudentWindow studentWindow = new StudentWindow(currentStudent);
                studentWindow.setVisible(true);
                frame.dispose();
            } else {
                resultLabel.setText("Неправильний пароль");
            }
        } catch (Exception e2) {
            RegistrationWindow registrationWindow = new RegistrationWindow();
            registrationWindow.setVisible(true);
            frame.dispose();
        }

    }
}
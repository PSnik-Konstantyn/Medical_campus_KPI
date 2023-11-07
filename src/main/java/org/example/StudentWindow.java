package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StudentWindow extends MedicalFrame {
    private Student student;

    JTextField medicalRequestField;
    JButton submitRequestButton;

    public StudentWindow(Student student) {
        this.student = student;

        setTitle("Студент: " + student.getName() + " " + student.getSurname());

        JLabel nameLabel = new JLabel("Ім'я: " + student.getName() + " " + student.getSurname());
        JLabel healthStatus = new JLabel("Стан: " + (student.isIll() ? "Здоровий" : "Хворий"));
        JLabel imageLabel = new JLabel("Тут може бути зображення студента"); // Додайте код для відображення зображення

        medicalRequestField = new JTextField(20);
        submitRequestButton = new JButton("Відправити запит");

        setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(5, 5, 5, 5);

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 2;
        constraints.anchor = GridBagConstraints.CENTER;
        add(nameLabel, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 2;
        add(healthStatus, constraints);

        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = 2;
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
                // Додайте код для відправки запиту у базу даних (Redis), використовуючи Jedis
                // Тут ви можете використовувати student.getStudentNumber() для ідентифікації студента
                // та medicalRequestField.getText() для збереження тексту запиту
            }
        });

        pack();
        setLocationRelativeTo(null); // Розміщення вікна по центру екрана
    }
}

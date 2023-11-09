package org.example;

import redis.clients.jedis.Jedis;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Set;
import static org.example.Main.jedisPool;

public class AdminWindow extends MedicalFrame {

    AdminWindow() {
        setTitle("Адміністратор");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setSize(600, 400);

        ImageIcon imageIcon = new ImageIcon("logo.jpg");
        setIconImage(imageIcon.getImage());
        getContentPane().setBackground(new Color(255, 204, 229));
        setLocationRelativeTo(null);

        JTabbedPane tabbedPane = new JTabbedPane();

        JPanel panel1 = new JPanel();
        panel1.setLayout(new BorderLayout());
        JLabel searchLabel = new JLabel("Пошук за групою");
        JTextField searchField = new JTextField(20);
        JButton searchButton = new JButton("Пошук");
        JTextArea studentArea = new JTextArea();
        studentArea.setRows(10); // Установите количество строк, которое вы хотите видеть
        studentArea.setColumns(20); // Установите количество столбцов, которое вы хотите видеть
        studentArea.setLineWrap(true); // Разрешить перенос строк
        studentArea.setWrapStyleWord(true);
        JScrollPane studentScrollPane = new JScrollPane(studentArea);
        panel1.add(searchLabel, BorderLayout.NORTH);
        panel1.add(searchField, BorderLayout.CENTER);
        panel1.add(searchButton, BorderLayout.EAST);
        panel1.add(studentScrollPane, BorderLayout.SOUTH);

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String searchGroup = searchField.getText();
                if (!searchGroup.isEmpty()) {
                    studentArea.setText("Результати пошуку хворих для групи " + searchGroup + ":\n");

                    SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                        @Override
                        protected Void doInBackground() throws Exception {
                            try (Jedis jedis = jedisPool.getResource()) {
                                Set<String> studentKeys = jedis.keys("st:*");
                                for (String studentKey : studentKeys) {
                                    Student studentInfo = Student.convertJsonToStudent(jedis.get(studentKey));
                                    if (searchGroup.equals(studentInfo.getGroup()) && studentInfo.isIll()) {
                                        studentArea.append(studentInfo.getName() + studentInfo.getSurname() + "\n");
                                    }
                                }
                            } catch (Exception e2) {
                                e2.printStackTrace();
                            }
                            return null;
                        }
                    };

                    worker.addPropertyChangeListener(new PropertyChangeListener() {
                        @Override
                        public void propertyChange(PropertyChangeEvent evt) {
                            if (evt.getPropertyName().equals("state") && evt.getNewValue() == SwingWorker.StateValue.DONE) {
                                studentScrollPane.revalidate();
                                studentScrollPane.repaint();
                            }
                        }
                    });

                    worker.execute();
                } else {
                    JOptionPane.showMessageDialog(null, "Будь ласка, введіть групу для пошуку.", "Помилка", JOptionPane.ERROR_MESSAGE);
                }
            }
        });



        JPanel panel2 = new JPanel();
        panel2.setLayout(new BorderLayout());
        JLabel requestLabel = new JLabel("Реквестів на лікарняний: " + 1);
        JTextArea requestArea = new JTextArea();
        JScrollPane requestScrollPane = new JScrollPane(requestArea);
        JTextField responseField = new JTextField(20);
        JButton acceptButton = new JButton("Підтвердити");
        JButton rejectButton = new JButton("Відхилити");
        panel2.add(requestLabel, BorderLayout.NORTH);
        panel2.add(requestScrollPane, BorderLayout.CENTER);
        panel2.add(responseField, BorderLayout.SOUTH);
        panel2.add(acceptButton, BorderLayout.WEST);
        panel2.add(rejectButton, BorderLayout.EAST);

        // Додаємо слухача подій для кнопок "Підтвердити" та "Відхилити"
        acceptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Додаткова логіка для підтвердження лікарняного
                // Можна взяти дані з responseField, якщо потрібно
                JOptionPane.showMessageDialog(null, "Лікарняний підтверджено.");
            }
        });

        rejectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Додаткова логіка для відхилення лікарняного
                // Можна взяти дані з responseField, якщо потрібно
                JOptionPane.showMessageDialog(null, "Лікарняний відхилено.");
            }
        });

        tabbedPane.addTab("Студенти", panel1);
        tabbedPane.addTab("Звернення", panel2);

        add(tabbedPane);
    }
}

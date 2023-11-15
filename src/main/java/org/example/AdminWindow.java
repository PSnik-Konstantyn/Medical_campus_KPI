package org.example;

import redis.clients.jedis.Jedis;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Set;

import static org.example.DateUtil.addDays;
import static org.example.JedisActions.convertStudentToJson;
import static org.example.Main.jedisPool;
import static org.example.Request.fromJson;

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

        try (Jedis jedis = jedisPool.getResource()) {
            Set<String> studentKeys = jedis.keys("st:*");
            for (String studentKey : studentKeys) {
                Student studentInfo = Student.convertJsonToStudent(jedis.get(studentKey));
                if (studentInfo.isIll()) {
                    Date newDate = new Date();
                    if (studentInfo.getWhenHealthy().before(newDate)) {
                        studentInfo.setIll(false);
                        jedis.set(studentKey, convertStudentToJson(studentInfo));
                    }
                }
            }
        }

        JTabbedPane tabbedPane = new JTabbedPane();

        JPanel panel1 = new JPanel();
        panel1.setLayout(new BorderLayout());
        JLabel searchLabel = new JLabel("Пошук за групою");
        JTextField searchFieldSt = new JTextField(20);
        JButton searchButton = new JButton("Пошук");
        JTextArea studentArea = new JTextArea();
        studentArea.setRows(10);
        studentArea.setColumns(20);
        studentArea.setLineWrap(true);
        studentArea.setWrapStyleWord(true);
        JScrollPane studentScrollPane = new JScrollPane(studentArea);
        panel1.add(searchLabel, BorderLayout.NORTH);
        panel1.add(searchFieldSt, BorderLayout.CENTER);
        panel1.add(searchButton, BorderLayout.EAST);
        panel1.add(studentScrollPane, BorderLayout.SOUTH);

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String searchGroup = searchFieldSt.getText();
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
                                        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
                                        String formattedDate = format.format(studentInfo.getWhenHealthy());
                                        studentArea.append(studentInfo.getName() + " " + studentInfo.getSurname() + " хворіє до: " + formattedDate + "\n");
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
        ArrayList<String> requestsDirty = new ArrayList<>();
        ArrayList<String> requests = new ArrayList<>();
        JPanel requestPanel = new JPanel();
        requestPanel.setLayout(new BoxLayout(requestPanel, BoxLayout.Y_AXIS));
        JScrollPane requestScrollPane = new JScrollPane(requestPanel);
        panel2.add(requestScrollPane, BorderLayout.CENTER);
        updateRequests(requestPanel);

        tabbedPane.addTab("Студенти", panel1);
        tabbedPane.addTab("Звернення", panel2);

        add(tabbedPane);
    }

    private void addRequestPanel(JPanel parentPanel, String requestText, String studentID) {
        JPanel requestPanel = new JPanel();
        requestPanel.setLayout(new BoxLayout(requestPanel, BoxLayout.Y_AXIS));
        Student currentStudent = new Student();
        try (Jedis jedis = jedisPool.getResource()) {
            currentStudent = Student.convertJsonToStudent(jedis.get("st:" + studentID));
        }
        String name = currentStudent.getName();
        String surname = currentStudent.getSurname();
        String group = currentStudent.getGroup();
        JLabel nameLabel = new JLabel("Ім'я: " + name);
        JLabel surnameLabel = new JLabel("Прізвище: " + surname);
        JLabel groupLabel = new JLabel("Група: " + group);
        JTextArea requestTextArea = new JTextArea(requestText);
        JTextField responseField = new JTextField(20);
        JButton acceptButton = new JButton("Підтвердити");
        JButton rejectButton = new JButton("Відхилити");

        acceptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try (Jedis jedis = jedisPool.getResource()) {
                    String amountOfDays = responseField.getText();
                    Student currentStudent = Student.convertJsonToStudent(jedis.get("st:" + studentID));
                    currentStudent.setIll(true);
                    Date currentDate = new Date();
                    currentStudent.setWhenHealthy(addDays(currentDate, Integer.parseInt(amountOfDays)));
                    System.out.println("Dead" + studentID);
                    jedis.set("st:" + studentID, convertStudentToJson(currentStudent));
                    JOptionPane.showMessageDialog(null, "Лікарняний підтверджено!");
                    updateRequests(parentPanel);
                } catch (Exception e2) {
                    JOptionPane.showMessageDialog(null, "Сталася помилка!");
                    e2.printStackTrace();
                }
            }
        });

        rejectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Лікарняний відхилено.");
                updateRequests(parentPanel);
            }
        });

        requestPanel.add(nameLabel);
        requestPanel.add(surnameLabel);
        requestPanel.add(groupLabel);
        requestPanel.add(requestTextArea);
        requestPanel.add(responseField);
        requestPanel.add(acceptButton);
        requestPanel.add(rejectButton);

        parentPanel.add(requestPanel);
        parentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        parentPanel.revalidate();
        parentPanel.repaint();
    }

    private void updateRequests(JPanel requestPanel) {
        requestPanel.removeAll();
        ArrayList<String> requestsDirty = new ArrayList<>();
        ArrayList<String> requests = new ArrayList<>();
        try (Jedis jedis = jedisPool.getResource()) {
            Set<String> keys = jedis.keys("request:*");
            requestsDirty = new ArrayList<>(keys);
            int size = requestsDirty.size();
            for (String s : requestsDirty) {
                if (!fromJson(jedis.get(s)).isReplied()) {
                    requests.add(s);
                }
            }
            if (requests.isEmpty()) {
                requestPanel.removeAll();
                JLabel noRequestsLabel = new JLabel("Запитів немає");
                requestPanel.add(noRequestsLabel);
            } else {
                JLabel requestCountLabel = new JLabel("Реквестів на лікарняний: " + requests.size());
                requestPanel.add(requestCountLabel, BorderLayout.NORTH);
                String requestID = requests.get(0);
                Request requestInfo = fromJson(jedis.get(requestID));
                requestInfo.setReplied(true);
                Student studentInfo = Student.convertJsonToStudent(jedis.get(requestInfo.getStudentID()));
                jedis.set(requestID, requestInfo.toJson());
                System.out.println(requestID + "внизу");
                addRequestPanel(requestPanel, requestInfo.getText(), studentInfo.getStudentID());

            }
        }
    }

}

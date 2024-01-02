package org.example.windows;

import org.example.Student;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SurveyWindow extends MedicalFrame {
    private LungCancerModel lungCancerModel;
    private JCheckBox smokingCheckBox;
    private JCheckBox yellowFingersCheckBox;
    private JCheckBox anxietyCheckBox;
    private JCheckBox peerPressureCheckBox;
    private JCheckBox chronicDiseaseCheckBox;
    private JCheckBox fatigueCheckBox;
    private JCheckBox allergyCheckBox;
    private JCheckBox wheezingCheckBox;
    private JCheckBox alcoholCheckBox;
    private JCheckBox coughingCheckBox;
    private JCheckBox shortnessOfBreathCheckBox;
    private JCheckBox swallowingDifficultyCheckBox;
    private JCheckBox chestPainCheckBox;
    private JCheckBox genderCheckBox;
    private JButton submitButton;
    private JTextField ageTextField;
    private Instances data;

    public SurveyWindow(Student student) {
        this.lungCancerModel = new LungCancerModel();

        try {
            lungCancerModel.trainModel("/home/kostiantyn/IdeaProjects/KPI_Medical_Campus/src/main/resources/surveyLungCancer.csv");
        } catch (Exception e) {
            e.printStackTrace();
        }

        setTitle("Опитування");
        setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(22, 22, 22, 22);

        smokingCheckBox = new JCheckBox("Ви палите?");
        yellowFingersCheckBox = new JCheckBox("Пальця біля нігтів жовті?");
        anxietyCheckBox = new JCheckBox("Відчуваєте час від часу тривогу?");
        peerPressureCheckBox = new JCheckBox("Відчуваєте тиск оточуючих людей?");
        chronicDiseaseCheckBox = new JCheckBox("Маєте хронічні захворювання?");
        fatigueCheckBox = new JCheckBox("Відчуваєте втому?");
        allergyCheckBox = new JCheckBox("Маєте сильну алергію?");
        wheezingCheckBox = new JCheckBox("Було у вас запалення легень?");
        alcoholCheckBox = new JCheckBox("Вживаєте алкоголь?");
        coughingCheckBox = new JCheckBox("Кашель більше 2 тижнів?");
        shortnessOfBreathCheckBox = new JCheckBox("Не можете повністю вдихнути?");
        swallowingDifficultyCheckBox = new JCheckBox("Складнощі під час прийому їжі?");
        chestPainCheckBox = new JCheckBox("Чоловік?");
        genderCheckBox = new JCheckBox("Біль у грудині?");

        constraints.gridx = 0;
        constraints.gridy = 0;
        add(smokingCheckBox, constraints);
        constraints.gridy = 1;
        add(yellowFingersCheckBox, constraints);
        constraints.gridy = 2;
        add(anxietyCheckBox, constraints);
        constraints.gridy = 3;
        add(peerPressureCheckBox, constraints);
        constraints.gridy = 4;
        add(chronicDiseaseCheckBox, constraints);
        constraints.gridy = 5;
        add(fatigueCheckBox, constraints);
        constraints.gridy = 6;
        add(genderCheckBox, constraints);

        // Second row
        constraints.gridx = 1;
        constraints.gridy = 0;
        add(allergyCheckBox, constraints);
        constraints.gridy = 1;
        add(wheezingCheckBox, constraints);
        constraints.gridy = 2;
        add(alcoholCheckBox, constraints);
        constraints.gridy = 3;
        add(coughingCheckBox, constraints);
        constraints.gridy = 4;
        add(shortnessOfBreathCheckBox, constraints);
        constraints.gridy = 5;
        add(swallowingDifficultyCheckBox, constraints);
        constraints.gridy = 6;
        add(chestPainCheckBox, constraints);

        // Add submit button
        constraints.gridx = 1;
        constraints.gridy = 7;
        ageTextField = new JTextField(10);
        add(new JLabel("Введіть вік:"), constraints);
        add(ageTextField, constraints);
        constraints.gridx = 0;
        constraints.gridy = 7;
        constraints.gridwidth = 1;

        submitButton = new JButton("Готово!");

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    ConverterUtils.DataSource source = new ConverterUtils.DataSource("/home/kostiantyn/IdeaProjects/KPI_Medical_Campus/src/main/resources/surveyLungCancer.csv");
                    data = source.getDataSet();
                    data.setClassIndex(data.numAttributes() - 1);
                    Instance newInstance = createInstance();
                    String prediction = lungCancerModel.makePrediction(newInstance, data);
                    JOptionPane.showMessageDialog(null, "Результат: " + prediction, "Прогноз", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        add(submitButton, constraints);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 600);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private Instance createInstance() throws Exception {
        Instance newInstance = new DenseInstance(15);

        String ageString = ageTextField.getText();
        if (!ageString.matches("\\d+")) {
            JOptionPane.showMessageDialog(null, "Введіть коректне значення для віку", "Помилка", JOptionPane.ERROR_MESSAGE);
            return null; // Повертаємо null у випадку помилки
        }
        int age = Integer.parseInt(ageString);

        newInstance.setValue(0, genderCheckBox.isSelected() ? 'M' : 'F');
        newInstance.setValue(1, age);
        newInstance.setValue(2, smokingCheckBox.isSelected() ? 2 : 1);
        newInstance.setValue(3, yellowFingersCheckBox.isSelected() ? 2 : 1);
        newInstance.setValue(4, anxietyCheckBox.isSelected() ? 2 : 1);
        newInstance.setValue(5, peerPressureCheckBox.isSelected() ? 2 : 1);
        newInstance.setValue(6, chronicDiseaseCheckBox.isSelected() ? 2 : 1);
        newInstance.setValue(7, fatigueCheckBox.isSelected() ? 2 : 1);
        newInstance.setValue(8, allergyCheckBox.isSelected() ? 2 : 1);
        newInstance.setValue(9, wheezingCheckBox.isSelected() ? 2 : 1);
        newInstance.setValue(10, alcoholCheckBox.isSelected() ? 2 : 1);
        newInstance.setValue(11, coughingCheckBox.isSelected() ? 2 : 1);
        newInstance.setValue(12, shortnessOfBreathCheckBox.isSelected() ? 2 : 1);
        newInstance.setValue(13, swallowingDifficultyCheckBox.isSelected() ? 2 : 1);
        newInstance.setValue(14, chestPainCheckBox.isSelected() ? 2 : 1);

        return newInstance;
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new SurveyWindow(null);
            }
        });
    }
}

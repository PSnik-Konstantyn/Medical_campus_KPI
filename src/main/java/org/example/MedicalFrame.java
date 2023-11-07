package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MedicalFrame extends JFrame {
    MedicalFrame(){
        setTitle("Медичний кампус");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setSize(600, 400);

        ImageIcon imageIcon = new ImageIcon("logo.jpg");
        setIconImage(imageIcon.getImage());
        getContentPane().setBackground(new Color(255, 204, 229));
        setLocationRelativeTo(null);
        setLayout(new FlowLayout(FlowLayout.CENTER));
    }
} 
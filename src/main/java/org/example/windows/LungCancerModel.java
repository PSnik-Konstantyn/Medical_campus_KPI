package org.example.windows;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.evaluation.ThresholdCurve;
import weka.classifiers.trees.J48;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Utils;
import weka.core.converters.ConverterUtils;
import weka.gui.visualize.PlotData2D;
import weka.gui.visualize.ThresholdVisualizePanel;

import java.awt.*;

public class LungCancerModel {
    private Classifier classifier;
    private Instances data;
    private Instances trainingData;
    private Instances testData;

    public void trainModel(String filePath) throws Exception {
        ConverterUtils.DataSource source = new ConverterUtils.DataSource(filePath);
        data = source.getDataSet();
        data.setClassIndex(data.numAttributes() - 1);

        int trainSize = (int) Math.round(data.numInstances() * 0.7);
        int testSize = data.numInstances() - trainSize;

        trainingData = new Instances(data, 0, trainSize);
        testData = new Instances(data, trainSize, testSize);

        classifier = new J48();
        classifier.buildClassifier(trainingData);
        Evaluation eval = new Evaluation(trainingData);
        eval.evaluateModel(classifier, testData);

//        System.out.println(eval.toSummaryString("\nResults\n======\n", false));
//
//        ThresholdCurve tc = new ThresholdCurve();
//        Instances result = tc.getCurve(eval.predictions());
//
//        ThresholdVisualizePanel vmc = new ThresholdVisualizePanel();
//        vmc.setROCString("(Area under ROC = " +
//                Utils.doubleToString(tc.getROCArea(result), 4) + ")");
//        vmc.setName(result.relationName());
//        PlotData2D tempd = new PlotData2D(result);
//        tempd.setPlotName(result.relationName());
//        tempd.addInstanceNumberAttribute();
//
//        boolean[] cp = new boolean[result.numInstances()];
//        for (int n = 1; n < cp.length; n++)
//            cp[n] = true;
//        tempd.setConnectPoints(cp);
//        vmc.addPlot(tempd);
//
//        String plotName = vmc.getName();
//        final javax.swing.JFrame jf =
//                new javax.swing.JFrame("Weka Classifier Visualize: "+plotName);
//        jf.setSize(500,400);
//        jf.getContentPane().setLayout(new BorderLayout());
//        jf.getContentPane().add(vmc, BorderLayout.CENTER);
//        jf.addWindowListener(new java.awt.event.WindowAdapter() {
//            public void windowClosing(java.awt.event.WindowEvent e) {
//                jf.dispose();
//            }
//        });
//        jf.setVisible(true);
    }

    public String makePrediction(Instance newInstance, Instances data) throws Exception {
        if (classifier == null) {
            throw new IllegalStateException("Model has not been trained yet.");
        }

        newInstance.setDataset(this.data);

        double prediction = classifier.classifyInstance(newInstance);
        return newInstance.attribute(newInstance.classIndex()).value((int) prediction);
    }

}

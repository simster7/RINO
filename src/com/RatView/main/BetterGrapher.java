package com.RatView.main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 *
 * @author Simon
 */
public class BetterGrapher extends javax.swing.JDialog {

    ArrayList<Rat> rats = new ArrayList<>();
    int type;
    int info;
    ArrayList<DailyInfo> average = new ArrayList<>();
    private int doAverage;

    public BetterGrapher(java.awt.Frame parent, boolean modal, ArrayList<Rat> rats, int[] settings) {
        super(parent, modal);
        initComponents();
        this.rats = rats;
        this.type = settings[0];
        this.info = settings[1];
        this.doAverage = settings[2];
        System.out.println(doAverage);
        if (type == 1) {
            setTitle("Weight Chart");
        } else {
            setTitle("Ramsey Chart");
        }

        setLayout(new BorderLayout(0, 5));

        final XYDataset dataset = createDataset();
        final JFreeChart chart = createChart(dataset);
        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));

        add(chartPanel, BorderLayout.CENTER);
        setContentPane(chartPanel);
    }

    private XYDataset createDataset() {
        final TimeSeriesCollection dataset = new TimeSeriesCollection();
        final TimeSeries a = new TimeSeries("Average");

        for (Rat r : rats) {

            final TimeSeries series = new TimeSeries(r.getNumber());
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

            float total = 0.0f;
            String date = "";

            try {
                ArrayList<DailyInfo> dInfo;
                if (info == 1) {
                    dInfo = r.getDailyInfo();
                } else {
                    dInfo = r.getPostDailyInfo();
                }
                for (DailyInfo i : dInfo) {
                    if (type == 1) {

                        series.add(new Day(format.parse(i.getDate().toString())), i.getWeight());
                    }
                    if (type == 2) {
                        series.add(new Day(format.parse(i.getDate().toString())), i.getRamsey());
                    }

                    date = i.getDate().toString();
                }

            } catch (ParseException ex) {
                Logger.getLogger(WeightChart.class.getName()).log(Level.SEVERE, null, ex);
            }

            dataset.addSeries(series);
        }
        if (doAverage == 1) {

            for (int k = 0; k < dataset.getSeries(0).getItemCount(); k++) {
                float average = 0.0f;
                for (int i = 0; i < dataset.getSeriesCount(); i++) {

                    average += dataset.getY(i, k).floatValue();
                }
                a.add(dataset.getSeries(0).getTimePeriod(k), average / dataset.getSeriesCount());
                System.out.println("add");
            }
            
            dataset.addSeries(a);
        }

        return dataset;

    }

    public void getAverage() {

    }

    private JFreeChart createChart(final XYDataset dataset) {

        String yAxis = "";
        String chartTitle = "";

        if (type == 1) {
            yAxis = "Weight";
            chartTitle = "Weight Chart";
        }
        if (type == 2) {
            yAxis = "Ramsey";
            chartTitle = "Ramsey Chart";
        }

        final JFreeChart chart = ChartFactory.createTimeSeriesChart(
                chartTitle, // chart title
                "Date", // x axis label
                yAxis, // y axis label
                dataset,
                true, // include legend
                false, // tooltips
                false // urls
        );

        // NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...
        chart.setBackgroundPaint(Color.white);

//        final StandardLegend legend = (StandardLegend) chart.getLegend();
        //      legend.setDisplaySeriesShapes(true);
        // get a reference to the plot for further customisation...
        final XYPlot plot = chart.getXYPlot();
        plot.setBackgroundPaint(Color.lightGray);
        //    plot.setAxisOffset(new Spacer(Spacer.ABSOLUTE, 5.0, 5.0, 5.0, 5.0));
        plot.setDomainGridlinePaint(Color.white);
        plot.setRangeGridlinePaint(Color.white);

        final XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
//        renderer.setSeriesLinesVisible(0, false);
//        renderer.setSeriesShapesVisible(1, false);
        plot.setRenderer(renderer);

        // change the auto tick unit selection to integer units only...
        final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        // OPTIONAL CUSTOMISATION COMPLETED.

        return chart;

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(BetterGrapher.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(BetterGrapher.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(BetterGrapher.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(BetterGrapher.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                BetterGrapher dialog = new BetterGrapher(new javax.swing.JFrame(), true, null, null);
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}

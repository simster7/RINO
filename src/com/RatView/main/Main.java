package com.RatView.main;

import com.RatView.exceptions.EmptyFieldException;
import java.awt.Component;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import org.jfree.ui.RefineryUtilities;
import org.joda.time.*;

public final class Main extends javax.swing.JFrame {

    private final String version = "v1.0";
    private ArrayList<Project> projects = new ArrayList<>();
    private ArrayList<EnvInfo> data = new ArrayList<>();
    private int project_selected = 0;
    private int rat_selected = 0;
    private DefaultListModel<String> model_project;
    private DefaultListModel<String> model_rats;
    private DefaultTableModel model_dailyInfo;
    private DefaultTableModel model_drugs;
    private DefaultTableModel model_surgery;
    private DefaultTableModel model_postSurgery;
    private DefaultTableModel model_postDrugs;
    private boolean allowTableChanges;
    private DefaultTableModel model_diet;
    private DefaultTableModel model_bbb;

    public Main() {
        initComponents();
        setTitle("RINO " + version);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
//        load();
        if (projects.isEmpty()) {
            Project p = new Project("New Project", "Strain", 0, 0, "Coords.", 0, "Reasercher", "Male", true);
            projects.add(p);
        }
        refreshProjects();
        refreshRats(projects.get(project_selected));
        defaultRatInfo();
        addActionListeners();

    }

    public void addActionListeners() {

        this.table_dailyInfo.getColumnModel().getColumn(0).setPreferredWidth(4);
        this.table_drugs.getColumnModel().getColumn(0).setPreferredWidth(4);

        addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent we) {
                String ObjButtons[] = {"Yes", "No"};
                int n = JOptionPane.showOptionDialog(null,
                        "Are you sure you want to exit?\nAll unsaved changes will be lost.", "Confirm Exit",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null,
                        ObjButtons, ObjButtons[1]);
                if (n == 0) {
                    System.exit(0);
                }
            }
        });

        list_project.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    project_selected = list_project.getSelectedIndex();
                    if (project_selected != -1) {
                        refreshRats(projects.get(project_selected));
                    }
                }
            }
        });

        list_rats.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    rat_selected = list_rats.getSelectedIndex();
                    if (rat_selected != -1) {
                        allowTableChanges = false;
                        defaultRatInfo();
                        refreshRatInfo(projects.get(project_selected).getRats().get(rat_selected));
                        allowTableChanges = true;
                    }
                }
            }
        });

        button_newWeightRamsey.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Object[] add = {model_dailyInfo.getRowCount() + 1, new LocalDate(), (float) 1.0, 1};
                    DailyInfo di = new DailyInfo(new LocalDate(), (float) 1.0, 1);
                    projects.get(project_selected).getRats().get(rat_selected).getDailyInfo().add(di);
                    model_dailyInfo.addRow(add);
//                save();
                } catch (Exception ex) {
                    errorDialog("Error adding new Weight/Ramsey.", "\nPlease make sure a rat is selected.");
                }
            }

        });

        button_newPostSurgeryWeightRamsey.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Object[] add = {model_postSurgery.getRowCount() + 1, new LocalDate(), (float) 1.0, 0.0f, 1};
                    DailyInfo di = new DailyInfo(new LocalDate(), (float) 1.0, 1);
                    projects.get(project_selected).getRats().get(rat_selected).getPostDailyInfo().add(di);
                    model_postSurgery.addRow(add);
//                save();
                } catch (Exception ex) {
                    errorDialog("Error adding new post-operative Weight/Ramsey.", "\nPlease make sure a rat is selected.");
                }
            }

        });

        button_newDrug.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Object[] add = {model_drugs.getRowCount() + 1, new LocalDate(), new LocalTime(), "Select Drug...", "0"};
                    DrugInfo di = new DrugInfo(new LocalDate(), new LocalTime(), "Select Drug...", "0");
                    projects.get(project_selected).getRats().get(rat_selected).getDrugs().add(di);
                    model_drugs.addRow(add);
//                save();
                } catch (Exception ex) {
                    errorDialog("Error adding new drug.", "\nPlease make sure a rat is selected.");
                }
            }

        });

        button_newPostDrug.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Object[] add = {model_postDrugs.getRowCount() + 1, new LocalDate(), new LocalTime(), "Select Drug...", "0"};
                    DrugInfo di = new DrugInfo(new LocalDate(), new LocalTime(), "Select Drug...", "0");
                    projects.get(project_selected).getRats().get(rat_selected).getPostDrugs().add(di);
                    model_postDrugs.addRow(add);
//                save();
                } catch (Exception ex) {
                    errorDialog("Error adding new post-operative drug.", "\nPlease make sure a rat is selected.");
                }
            }

        });

        button_newSurgery.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Object[] rowDataf = {new LocalDate().toString(), new LocalTime().toString(), 0.0f, 1, new LocalTime().toString(), false, false, 1, 1, 1, false, false, new LocalTime().toString(), new LocalTime().toString(), new LocalTime().toString(), 1, new LocalTime().toString(), new LocalTime().toString(), 1, ""};
                    SurgeryInfo si = new SurgeryInfo(new LocalDate(), new LocalTime(), 0.0f, 1, new LocalTime(), false, false, 1, 1, 1, false, false, new LocalTime(), new LocalTime(), new LocalTime(), 1, new LocalTime(), new LocalTime(), 1, "");
                    projects.get(project_selected).getRats().get(rat_selected).getSurgery().add(si);
                    model_surgery.addRow(rowDataf);
//                save();
                } catch (Exception ex) {
                    errorDialog("Error adding new surgery.", "\nPlease make sure a rat is selected.");
                }

            }

        });

        button_savePSNotes.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    projects.get(project_selected).getRats().get(rat_selected).setPreSurgeryNotesNotes(text_notes.getText());
//                save();
                } catch (Exception ex) {
                    errorDialog("Error saving notes.", "\nPlease make sure a rat is selected.");
                }
            }

        });

        button_savePSNotes1.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    projects.get(project_selected).getRats().get(rat_selected).setPostNotes(text_postNotes.getText());
//                save();
                } catch (Exception ex) {
                    errorDialog("Error saving post-operative notes.", "\nPlease make sure a rat is selected.");
                }
            }

        });

        button_removeWeightRamsey.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    projects.get(project_selected).getRats().get(rat_selected).getDailyInfo().remove(table_dailyInfo.getSelectedRow());
                    refreshTrigger();
                } catch (Exception ex) {
                    errorDialog("Error removing Weight/Ramsey.", "\nPlease make sure a row is selected.");
                }
            }

        });

        button_removeDrug.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    projects.get(project_selected).getRats().get(rat_selected).getDrugs().remove(table_drugs.getSelectedRow());
                    refreshTrigger();
                } catch (Exception ex) {
                    errorDialog("Error removing drug.", "\nPlease make sure a row is selected.");
                }
            }

        });

        button_removeSurgery.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    projects.get(project_selected).getRats().get(rat_selected).getSurgery().remove(table_surgery.getSelectedRow());
                    refreshTrigger();
                } catch (Exception ex) {
                    errorDialog("Error removing surgery.", "\nPlease make sure a row is selected.");
                }
            }

        });

        button_removePostWeightRamsey.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    projects.get(project_selected).getRats().get(rat_selected).getPostDailyInfo().remove(table_postDailyInfo.getSelectedRow());
                    refreshTrigger();
                } catch (Exception ex) {
                    errorDialog("Error removing post-operative Wieght/Ramsey.", "\nPlease make sure a row is selected.");
                }
            }

        });

        button_removePostDrug.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    projects.get(project_selected).getRats().get(rat_selected).getPostDrugs().remove(table_postDrugs.getSelectedRow());
                    refreshTrigger();
                } catch (Exception ex) {
                    errorDialog("Error removing post-operative drugs.", "\nPlease make sure a row is selected.");
                }
            }

        });
        button_newFood.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Object[] add = {new LocalDate(), "Food", 0.0, 0.0};
                    FoodInfo fi = new FoodInfo(new LocalDate(), "Food", 0.0f, 0.0f);
                    projects.get(project_selected).getRats().get(rat_selected).getDiet().add(fi);
                    model_diet.addRow(add);
//                save();
                } catch (Exception ex) {
                    errorDialog("Error adding new food.", "\nPlease make sure a rat is selected.");
                }
            }

        });
        button_newBBB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Object[] add = {new LocalDate(), 0};
                    BBBInfo fi = new BBBInfo(new LocalDate(), 0);
                    projects.get(project_selected).getRats().get(rat_selected).getBbb().add(fi);
                    model_bbb.addRow(add);
//                save();
                } catch (Exception ex) {
                    errorDialog("Error adding new BBB.", "\nPlease make sure a rat is selected.");
                }
            }

        });
        button_removeFood.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    projects.get(project_selected).getRats().get(rat_selected).getDiet().remove(table_diet.getSelectedRow());
                    refreshTrigger();
                } catch (Exception ex) {
                    errorDialog("Error removing food.", "\nPlease make sure a row is selected.");
                }
            }

        });
        button_removeBBB.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    projects.get(project_selected).getRats().get(rat_selected).getBbb().remove(table_bbb.getSelectedRow());
                    refreshTrigger();
                } catch (Exception ex) {
                    errorDialog("Error removing BBB.", "\nPlease make sure a row is selected.");
                }
            }

        });

        //pie
        model_drugs.addTableModelListener(new TableModelListener() {

            @Override
            public void tableChanged(TableModelEvent e) {
                if (allowTableChanges) {

                    int row = e.getFirstRow();
                    int col = e.getColumn();

                    if (col == 1) {
                        projects.get(project_selected).getRats().get(rat_selected).getDrugs().get(row).setDate(new LocalDate(model_drugs.getValueAt(row, col)));
                    }
                    if (col == 2) {
                        projects.get(project_selected).getRats().get(rat_selected).getDrugs().get(row).setTime(new LocalTime(model_drugs.getValueAt(row, col)));
                    }
                    if (col == 3) {
                        String test = (String) model_drugs.getValueAt(row, col);
                        projects.get(project_selected).getRats().get(rat_selected).getDrugs().get(row).setDrugName(test);
                    }
                    if (col == 4) {
                        projects.get(project_selected).getRats().get(rat_selected).getDrugs().get(row).setDosis((String) model_drugs.getValueAt(row, col));
                    }
                    refreshTrigger();
//                    save();
                }
            }

        });

        model_surgery.addTableModelListener(new TableModelListener() {

            @Override
            public void tableChanged(TableModelEvent e) {
                if (allowTableChanges) {

                    int row = e.getFirstRow();
                    int col = e.getColumn();

                    if (col == 0) {
                        projects.get(project_selected).getRats().get(rat_selected).getSurgery().get(row).setDate(new LocalDate(model_surgery.getValueAt(row, col)));
                    }
                    if (col == 1) {
                        projects.get(project_selected).getRats().get(rat_selected).getSurgery().get(row).setTime(new LocalTime(model_surgery.getValueAt(row, col)));
                    }
                    if (col == 2) {
                        projects.get(project_selected).getRats().get(rat_selected).getSurgery().get(row).setWeight((float) model_surgery.getValueAt(row, col));
                    }
                    if (col == 3) {
                        projects.get(project_selected).getRats().get(rat_selected).getSurgery().get(row).setSurgeryNumber((int) model_surgery.getValueAt(row, col));
                    }
                    if (col == 4) {
                        projects.get(project_selected).getRats().get(rat_selected).getSurgery().get(row).setaTime(new LocalTime(model_surgery.getValueAt(row, col)));
                    }
                    if (col == 5) {
                        projects.get(project_selected).getRats().get(rat_selected).getSurgery().get(row).setBladderEmpty((boolean) model_surgery.getValueAt(row, col));
                    }
                    if (col == 6) {
                        projects.get(project_selected).getRats().get(rat_selected).getSurgery().get(row).setEyes((boolean) model_surgery.getValueAt(row, col));
                    }
                    if (col == 7) {
                        projects.get(project_selected).getRats().get(rat_selected).getSurgery().get(row).setEnro((int) model_surgery.getValueAt(row, col));
                    }
                    if (col == 8) {
                        projects.get(project_selected).getRats().get(rat_selected).getSurgery().get(row).setBupre((int) model_surgery.getValueAt(row, col));
                    }
                    if (col == 9) {
                        projects.get(project_selected).getRats().get(rat_selected).getSurgery().get(row).setKeto((int) model_surgery.getValueAt(row, col));
                    }
                    if (col == 10) {
                        projects.get(project_selected).getRats().get(rat_selected).getSurgery().get(row).setShaved((boolean) model_surgery.getValueAt(row, col));
                    }
                    if (col == 11) {
                        projects.get(project_selected).getRats().get(rat_selected).getSurgery().get(row).setClean((boolean) model_surgery.getValueAt(row, col));
                    }
                    if (col == 12) {
                        projects.get(project_selected).getRats().get(rat_selected).getSurgery().get(row).setIncision(new LocalTime(model_surgery.getValueAt(row, col)));
                    }
                    if (col == 13) {
                        projects.get(project_selected).getRats().get(rat_selected).getSurgery().get(row).setInjury(new LocalTime(model_surgery.getValueAt(row, col)));
                    }
                    if (col == 14) {
                        projects.get(project_selected).getRats().get(rat_selected).getSurgery().get(row).setClose(new LocalTime(model_surgery.getValueAt(row, col)));
                    }
                    if (col == 15) {
                        projects.get(project_selected).getRats().get(rat_selected).getSurgery().get(row).setExternalPoints((int) model_surgery.getValueAt(row, col));
                    }
                    if (col == 16) {
                        projects.get(project_selected).getRats().get(rat_selected).getSurgery().get(row).setRehavilitation(new LocalTime(model_surgery.getValueAt(row, col)));
                    }
                    if (col == 17) {
                        projects.get(project_selected).getRats().get(rat_selected).getSurgery().get(row).setAwareness(new LocalTime(model_surgery.getValueAt(row, col)));
                    }
                    if (col == 18) {
                        projects.get(project_selected).getRats().get(rat_selected).getSurgery().get(row).setRamsey((int) model_surgery.getValueAt(row, col));
                    }
                    if (col == 19) {
                        projects.get(project_selected).getRats().get(rat_selected).getSurgery().get(row).setNotes((String) model_surgery.getValueAt(row, col));
                    }
                    refreshTrigger();
//                    save();
                }
            }

        });

        model_dailyInfo.addTableModelListener(new TableModelListener() {

            @Override
            public void tableChanged(TableModelEvent e) {

                if (allowTableChanges) {

                    int row = e.getFirstRow();
                    int col = e.getColumn();

                    if (col == 1) {
                        projects.get(project_selected).getRats().get(rat_selected).getDailyInfo().get(row).setDate(new LocalDate(model_dailyInfo.getValueAt(row, col)));
                    }
                    if (col == 2) {
                        projects.get(project_selected).getRats().get(rat_selected).getDailyInfo().get(row).setWeight((float) model_dailyInfo.getValueAt(row, col));
                    }
                    if (col == 3) {
                        projects.get(project_selected).getRats().get(rat_selected).getDailyInfo().get(row).setRamsey((int) model_dailyInfo.getValueAt(row, col));
                    }

                    refreshTrigger();
//                    save();
                }
            }

        });

        model_postSurgery.addTableModelListener(new TableModelListener() {

            @Override
            public void tableChanged(TableModelEvent e) {

                if (allowTableChanges) {
                    int row = e.getFirstRow();
                    int col = e.getColumn();

                    if (col == 1) {
                        projects.get(project_selected).getRats().get(rat_selected).getPostDailyInfo().get(row).setDate(new LocalDate(model_postSurgery.getValueAt(row, col)));
                    }
                    if (col == 2) {
                        projects.get(project_selected).getRats().get(rat_selected).getPostDailyInfo().get(row).setWeight((float) model_postSurgery.getValueAt(row, col));
                    }
                    if (col == 4) {
                        projects.get(project_selected).getRats().get(rat_selected).getPostDailyInfo().get(row).setRamsey((int) model_postSurgery.getValueAt(row, col));
                    }
                    refreshTrigger();
//                    save();
                }
            }

        });

        model_postDrugs.addTableModelListener(new TableModelListener() {

            @Override
            public void tableChanged(TableModelEvent e) {
                if (allowTableChanges) {

                    int row = e.getFirstRow();
                    int col = e.getColumn();

                    if (col == 1) {
                        projects.get(project_selected).getRats().get(rat_selected).getPostDrugs().get(row).setDate(new LocalDate(model_postDrugs.getValueAt(row, col)));
                    }
                    if (col == 2) {
                        projects.get(project_selected).getRats().get(rat_selected).getPostDrugs().get(row).setTime(new LocalTime(model_postDrugs.getValueAt(row, col)));
                    }
                    if (col == 3) {
                        String test = (String) model_postDrugs.getValueAt(row, col);
                        projects.get(project_selected).getRats().get(rat_selected).getPostDrugs().get(row).setDrugName(test);
                    }
                    if (col == 4) {
                        projects.get(project_selected).getRats().get(rat_selected).getPostDrugs().get(row).setDosis((String) model_postDrugs.getValueAt(row, col));
                    }
//                    save();
                }
            }

        });

        model_diet.addTableModelListener(new TableModelListener() {

            @Override
            public void tableChanged(TableModelEvent e) {
                if (allowTableChanges) {

                    int row = e.getFirstRow();
                    int col = e.getColumn();
                    if (col == 0) {
                        projects.get(project_selected).getRats().get(rat_selected).getDiet().get(row).setDate(new LocalDate(model_diet.getValueAt(row, col)));
                    }
                    if (col == 1) {
                        projects.get(project_selected).getRats().get(rat_selected).getDiet().get(row).setFood((String) model_diet.getValueAt(row, col));
                    }
                    if (col == 2) {
                        projects.get(project_selected).getRats().get(rat_selected).getDiet().get(row).setGiven((Float) model_diet.getValueAt(row, col));
                    }
                    if (col == 3) {
                        projects.get(project_selected).getRats().get(rat_selected).getDiet().get(row).setEaten((Float) model_diet.getValueAt(row, col));
                    }
//                    //save();
                }
            }

        });

        model_bbb.addTableModelListener(new TableModelListener() {

            @Override
            public void tableChanged(TableModelEvent e) {
                if (allowTableChanges) {

                    int row = e.getFirstRow();
                    int col = e.getColumn();

                    if (col == 0) {
                        projects.get(project_selected).getRats().get(rat_selected).getBbb().get(row).setDate(new LocalDate(model_bbb.getValueAt(row, col)));
                    }
                    if (col == 1) {
                        projects.get(project_selected).getRats().get(rat_selected).getBbb().get(row).setBBB((int) model_bbb.getValueAt(row, col));
                    }
//                    //save();
                }
            }

        });

    }

    public void refreshTrigger() {

        if (rat_selected != -1) {
            allowTableChanges = false;
            int rs = rat_selected;
            refreshRats(projects.get(project_selected));
            defaultRatInfo();
            rat_selected = rs;
            refreshRatInfo(projects.get(project_selected).getRats().get(rat_selected));
            list_rats.setSelectedIndex(rat_selected);
            allowTableChanges = true;
        }

    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        list_project = new javax.swing.JList<String>();
        jScrollPane2 = new javax.swing.JScrollPane();
        list_rats = new javax.swing.JList<String>();
        button_newProject = new javax.swing.JButton();
        button_editProject = new javax.swing.JButton();
        button_newRat = new javax.swing.JButton();
        button_editRat = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        label_projectInfo = new javax.swing.JLabel();
        label_ratInfo = new javax.swing.JLabel();
        tabs = new javax.swing.JTabbedPane();
        tab_pre = new javax.swing.JPanel();
        button_newWeightRamsey = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        text_notes = new javax.swing.JTextArea();
        panel_table = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        table_dailyInfo = new javax.swing.JTable();
        jLabel4 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        table_drugs = new javax.swing.JTable();
        jLabel3 = new javax.swing.JLabel();
        button_newDrug = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        button_savePSNotes = new javax.swing.JButton();
        button_removeWeightRamsey = new javax.swing.JButton();
        button_removeDrug = new javax.swing.JButton();
        tab_sur = new javax.swing.JPanel();
        jScrollPane10 = new javax.swing.JScrollPane();
        table_surgery = new javax.swing.JTable();
        button_newSurgery = new javax.swing.JButton();
        button_removeSurgery = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        panel_table1 = new javax.swing.JPanel();
        jScrollPane9 = new javax.swing.JScrollPane();
        table_postDailyInfo = new javax.swing.JTable();
        jLabel9 = new javax.swing.JLabel();
        button_newPostSurgeryWeightRamsey = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        table_postDrugs = new javax.swing.JTable();
        jLabel6 = new javax.swing.JLabel();
        button_newPostDrug = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        jScrollPane7 = new javax.swing.JScrollPane();
        text_postNotes = new javax.swing.JTextArea();
        button_savePSNotes1 = new javax.swing.JButton();
        button_removePostWeightRamsey = new javax.swing.JButton();
        button_removePostDrug = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        button_weightGraph = new javax.swing.JButton();
        button_ramseyGraph = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        button_postWeightGraph = new javax.swing.JButton();
        button_postRamseyGraph = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        label_preAvgW = new javax.swing.JLabel();
        label_preSDW = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        label_preSDR = new javax.swing.JLabel();
        label_preAvgR = new javax.swing.JLabel();
        label_preAvgR1 = new javax.swing.JLabel();
        label_preSDR1 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        label_preSDW1 = new javax.swing.JLabel();
        label_preAvgW1 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jScrollPane8 = new javax.swing.JScrollPane();
        table_diet = new javax.swing.JTable();
        button_newFood = new javax.swing.JButton();
        button_removeFood = new javax.swing.JButton();
        jScrollPane11 = new javax.swing.JScrollPane();
        table_bbb = new javax.swing.JTable();
        button_removeBBB = new javax.swing.JButton();
        button_newBBB = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        menuItem_save = new javax.swing.JMenuItem();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenu4 = new javax.swing.JMenu();
        jMenuItem4 = new javax.swing.JMenuItem();
        ramseyCalc = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        menuItem_report = new javax.swing.JMenuItem();
        menuItem_about = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMaximumSize(new java.awt.Dimension(1107, 615));
        setMinimumSize(new java.awt.Dimension(1107, 615));
        setResizable(false);

        jScrollPane1.setViewportView(list_project);

        jScrollPane2.setViewportView(list_rats);

        button_newProject.setText("New Project");
        button_newProject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_newProjectActionPerformed(evt);
            }
        });

        button_editProject.setText("Edit Project...");
        button_editProject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_editProjectActionPerformed(evt);
            }
        });

        button_newRat.setText("New Rat");
        button_newRat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_newRatActionPerformed(evt);
            }
        });

        button_editRat.setText("Edit Rat...");
        button_editRat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_editRatActionPerformed(evt);
            }
        });

        jLabel1.setText("Current Projects");

        jLabel2.setText("Rats");

        label_projectInfo.setText("Please select a project");

        label_ratInfo.setText("Please select a rat");

        button_newWeightRamsey.setText("New Weight/Ramsey");

        text_notes.setColumns(20);
        text_notes.setRows(5);
        text_notes.setMaximumSize(new java.awt.Dimension(164, 94));
        jScrollPane4.setViewportView(text_notes);

        table_dailyInfo.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "", "Date", "Weight", "Ramsey"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.Float.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, true, true, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane3.setViewportView(table_dailyInfo);

        jLabel4.setText("Weight History:");

        javax.swing.GroupLayout panel_tableLayout = new javax.swing.GroupLayout(panel_table);
        panel_table.setLayout(panel_tableLayout);
        panel_tableLayout.setHorizontalGroup(
            panel_tableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_tableLayout.createSequentialGroup()
                .addComponent(jLabel4)
                .addGap(250, 275, Short.MAX_VALUE))
            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );
        panel_tableLayout.setVerticalGroup(
            panel_tableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel_tableLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 299, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        table_drugs.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "", "Date", "Time", "Drug", "Dosis"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, true, true, true, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        TableColumn drugsColumn = table_drugs.getColumnModel().getColumn(3);
        JComboBox drugBox = new JComboBox();
        drugBox.addItem("Buprenorphine");
        drugBox.addItem("Ketoprofen");
        drugBox.addItem("Enrofloxacin");
        drugsColumn.setCellEditor(new DefaultCellEditor(drugBox));
        jScrollPane5.setViewportView(table_drugs);

        jLabel3.setText("Drugs Administered:");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jLabel3)
                .addContainerGap(254, Short.MAX_VALUE))
            .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 299, Short.MAX_VALUE))
        );

        button_newDrug.setText("New Drug");
        button_newDrug.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_newDrugActionPerformed(evt);
            }
        });

        jLabel5.setText("Notes:");

        button_savePSNotes.setText("Save Notes");

        button_removeWeightRamsey.setText("Remove Weight/Ramsey");
        button_removeWeightRamsey.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_removeWeightRamseyActionPerformed(evt);
            }
        });

        button_removeDrug.setText("Remove Drug");

        javax.swing.GroupLayout tab_preLayout = new javax.swing.GroupLayout(tab_pre);
        tab_pre.setLayout(tab_preLayout);
        tab_preLayout.setHorizontalGroup(
            tab_preLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tab_preLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(tab_preLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(panel_table, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(tab_preLayout.createSequentialGroup()
                        .addComponent(button_removeWeightRamsey)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(button_newWeightRamsey)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(tab_preLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(tab_preLayout.createSequentialGroup()
                        .addComponent(button_removeDrug)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(button_newDrug)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(tab_preLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tab_preLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel5)
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 325, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(button_savePSNotes, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap(26, Short.MAX_VALUE))
        );
        tab_preLayout.setVerticalGroup(
            tab_preLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tab_preLayout.createSequentialGroup()
                .addGroup(tab_preLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(tab_preLayout.createSequentialGroup()
                        .addGroup(tab_preLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tab_preLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(panel_table, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(tab_preLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(button_savePSNotes)
                            .addGroup(tab_preLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(button_newWeightRamsey)
                                .addComponent(button_removeWeightRamsey))))
                    .addGroup(tab_preLayout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(tab_preLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(button_newDrug)
                            .addComponent(button_removeDrug))))
                .addContainerGap(13, Short.MAX_VALUE))
        );

        tabs.addTab("Pre-Operative", tab_pre);

        table_surgery.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Date of Surgery", "Time of Surgery", "Weight", "Surgery Number", "Time of Anesthesia", "Bladder Empty", "Eye Protection", "Enrofloxacin", "Buprenorphine", "Ketoprofen", "Shaved", "Antisepsis", "Time of Insicion", "Time of Injury", "Time of Closure", "External Points", "Time of Rehabilitation", "Time of Awareness", "Ramsey", "Notes"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Float.class, java.lang.Integer.class, java.lang.String.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        table_surgery.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        jScrollPane10.setViewportView(table_surgery);

        button_newSurgery.setText("New Surgery");

        button_removeSurgery.setText("Remove Surgery");

        javax.swing.GroupLayout tab_surLayout = new javax.swing.GroupLayout(tab_sur);
        tab_sur.setLayout(tab_surLayout);
        tab_surLayout.setHorizontalGroup(
            tab_surLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tab_surLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(tab_surLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane10, javax.swing.GroupLayout.DEFAULT_SIZE, 1062, Short.MAX_VALUE)
                    .addGroup(tab_surLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(button_removeSurgery)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(button_newSurgery)))
                .addContainerGap())
        );
        tab_surLayout.setVerticalGroup(
            tab_surLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tab_surLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 316, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(tab_surLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(button_newSurgery)
                    .addComponent(button_removeSurgery))
                .addContainerGap(16, Short.MAX_VALUE))
        );

        tabs.addTab("Surgery", tab_sur);

        table_postDailyInfo.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "", "Date", "Weight", "% Change", "Ramsey"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Object.class, java.lang.Float.class, java.lang.Float.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, true, true, false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane9.setViewportView(table_postDailyInfo);

        jLabel9.setText("Weight History:");

        javax.swing.GroupLayout panel_table1Layout = new javax.swing.GroupLayout(panel_table1);
        panel_table1.setLayout(panel_table1Layout);
        panel_table1Layout.setHorizontalGroup(
            panel_table1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_table1Layout.createSequentialGroup()
                .addComponent(jLabel9)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jScrollPane9, javax.swing.GroupLayout.DEFAULT_SIZE, 348, Short.MAX_VALUE)
        );
        panel_table1Layout.setVerticalGroup(
            panel_table1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel_table1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 301, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        button_newPostSurgeryWeightRamsey.setText("New Weight/Ramsey");

        table_postDrugs.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "", "Date", "Time", "Drug", "Dosis"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, true, true, true, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        TableColumn postDrugColumn = table_postDrugs.getColumnModel().getColumn(3);
        JComboBox postDrugBox = new JComboBox();
        postDrugBox.addItem("Buprenorphine");
        postDrugBox.addItem("Ketoprofen");
        postDrugBox.addItem("Enrofloxacin");
        postDrugColumn.setCellEditor(new DefaultCellEditor(postDrugBox));
        jScrollPane6.setViewportView(table_postDrugs);

        jLabel6.setText("Drugs Administered:");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel6)
                .addGap(253, 253, 253))
            .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 299, Short.MAX_VALUE))
        );

        button_newPostDrug.setText("New Drug");

        jLabel7.setText("Notes:");

        text_postNotes.setColumns(20);
        text_postNotes.setRows(5);
        text_postNotes.setMaximumSize(new java.awt.Dimension(164, 94));
        jScrollPane7.setViewportView(text_postNotes);

        button_savePSNotes1.setText("Save Notes");

        button_removePostWeightRamsey.setText("Remove Weight/Ramsey");

        button_removePostDrug.setText("Remove Drug");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(button_removePostWeightRamsey)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(button_newPostSurgeryWeightRamsey))
                    .addComponent(panel_table1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(button_removePostDrug)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(button_newPostDrug)))
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(11, 11, 11)
                        .addComponent(jLabel7)
                        .addContainerGap(311, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane7, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(button_savePSNotes1)))
                        .addContainerGap())))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(button_savePSNotes1))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(panel_table1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(button_newPostSurgeryWeightRamsey)
                            .addComponent(button_newPostDrug)
                            .addComponent(button_removePostWeightRamsey)
                            .addComponent(button_removePostDrug))))
                .addContainerGap())
        );

        tabs.addTab("Post-Operative", jPanel4);

        button_weightGraph.setText("Weight Graph");
        button_weightGraph.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_weightGraphActionPerformed(evt);
            }
        });

        button_ramseyGraph.setText("Ramsey Graph");
        button_ramseyGraph.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_ramseyGraphActionPerformed(evt);
            }
        });

        jLabel8.setText("Pre-Operative");

        jLabel10.setText("Post-Operative");

        button_postWeightGraph.setText("Weight Graph");
        button_postWeightGraph.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_postWeightGraphActionPerformed(evt);
            }
        });

        button_postRamseyGraph.setText("Ramsey Graph");
        button_postRamseyGraph.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_postRamseyGraphActionPerformed(evt);
            }
        });

        jLabel11.setText("Average:");

        jLabel12.setText("Standard Deviation:");

        label_preAvgW.setText("0.0");

        label_preSDW.setText("0.0");

        jLabel15.setText("Weight");

        jLabel16.setText("Ramsey");

        label_preSDR.setText("0.0");

        label_preAvgR.setText("0.0");

        label_preAvgR1.setText("0.0");

        label_preSDR1.setText("0.0");

        jLabel17.setText("Ramsey");

        jLabel18.setText("Weight");

        label_preSDW1.setText("0.0");

        label_preAvgW1.setText("0.0");

        jLabel13.setText("Standard Deviation:");

        jLabel14.setText("Average:");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap(287, Short.MAX_VALUE)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(button_weightGraph)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(button_ramseyGraph))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel12, javax.swing.GroupLayout.Alignment.LEADING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(label_preAvgW)
                            .addComponent(label_preSDW)
                            .addComponent(jLabel15))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel16)
                            .addComponent(label_preAvgR)
                            .addComponent(label_preSDR))))
                .addGap(101, 101, 101)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel14, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel13, javax.swing.GroupLayout.Alignment.LEADING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(label_preAvgW1)
                            .addComponent(label_preSDW1)
                            .addComponent(jLabel18))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel17)
                            .addComponent(label_preAvgR1)
                            .addComponent(label_preSDR1)))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(button_postWeightGraph)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(button_postRamseyGraph))
                    .addComponent(jLabel10))
                .addGap(274, 274, 274))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(98, 98, 98)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jLabel10))
                .addGap(19, 19, 19)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel15)
                            .addComponent(jLabel16))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel11)
                                    .addComponent(label_preAvgW))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel12)
                                    .addComponent(label_preSDW)))
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(label_preAvgR)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(label_preSDR))))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel18)
                            .addComponent(jLabel17))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel14)
                                    .addComponent(label_preAvgW1))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel13)
                                    .addComponent(label_preSDW1)))
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(label_preAvgR1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(label_preSDR1)))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(button_weightGraph)
                        .addComponent(button_ramseyGraph))
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(button_postWeightGraph)
                        .addComponent(button_postRamseyGraph)))
                .addContainerGap(158, Short.MAX_VALUE))
        );

        tabs.addTab("Analysis and Graphs", jPanel6);

        table_diet.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, "Cheerios",  new Float(10.0),  new Float(2.7)}
            },
            new String [] {
                "Date", "Food Given", "Quantity Given", "Quantity Eaten"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Float.class, java.lang.Float.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane8.setViewportView(table_diet);

        button_newFood.setText("New Food");

        button_removeFood.setText("Remove Food");

        table_bbb.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"Cheerios", null}
            },
            new String [] {
                "Date", "BBB"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Integer.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane11.setViewportView(table_bbb);

        button_removeBBB.setText("Remove BBB");

        button_newBBB.setText("New BBB");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 499, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(button_removeFood)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(button_newFood)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 45, Short.MAX_VALUE)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane11, javax.swing.GroupLayout.PREFERRED_SIZE, 518, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                        .addComponent(button_removeBBB)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(button_newBBB)))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(43, 43, 43)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 271, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane11, javax.swing.GroupLayout.PREFERRED_SIZE, 271, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(button_newBBB)
                        .addComponent(button_removeBBB))
                    .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(button_newFood)
                        .addComponent(button_removeFood)))
                .addContainerGap(34, Short.MAX_VALUE))
        );

        tabs.addTab("Diet and BBB", jPanel7);

        jButton3.setText("Rat Died");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jMenu1.setText("File");

        menuItem_save.setText("Save...");
        menuItem_save.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItem_saveActionPerformed(evt);
            }
        });
        jMenu1.add(menuItem_save);

        jMenuItem1.setText("Open...");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuBar1.add(jMenu1);

        jMenu4.setText("Tools");

        jMenuItem4.setText("Enviornment Log...");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem4);

        ramseyCalc.setText("Ramsey Calculator...");
        ramseyCalc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ramseyCalcActionPerformed(evt);
            }
        });
        jMenu4.add(ramseyCalc);

        jMenuBar1.add(jMenu4);

        jMenu3.setText("Reports and Analysis");

        jMenuItem2.setText("Census...");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem2);

        jMenuItem3.setText("Custom Graphs...");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem3);

        jMenuBar1.add(jMenu3);

        jMenu2.setText("Help");

        menuItem_report.setText("Report an Error...");
        menuItem_report.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItem_reportActionPerformed(evt);
            }
        });
        jMenu2.add(menuItem_report);

        menuItem_about.setText("About...");
        menuItem_about.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItem_aboutActionPerformed(evt);
            }
        });
        jMenu2.add(menuItem_about);

        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 419, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(button_editProject, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(button_newProject, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(label_projectInfo, javax.swing.GroupLayout.PREFERRED_SIZE, 548, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(28, 28, 28)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 418, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(button_newRat, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(button_editRat, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(jLabel2)
                            .addComponent(label_ratInfo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(tabs))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(button_newRat)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(button_editRat)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton3))
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(button_newProject)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(button_editProject)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(label_projectInfo)
                            .addComponent(label_ratInfo))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tabs, javax.swing.GroupLayout.PREFERRED_SIZE, 405, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(46, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void button_newProjectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_newProjectActionPerformed
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                final NewProject projWindow = new NewProject(
                        new javax.swing.JFrame(), true, false);

                projWindow.button_done.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {

                        try {
                            if (projWindow.field_name.getText().equals("") || projWindow.field_strain.getText().equals("") || projWindow.field_room.getText().equals("") || projWindow.field_box.getText().equals("") || projWindow.field_coordinates.getText().equals("") || projWindow.field_number.getText().equals("") || projWindow.field_researcher.getText().equals("")) {
                                throw new EmptyFieldException();
                            }

                            String gender = projWindow.radio_male.isSelected() ? "Male" : "Female";
                            boolean operative = projWindow.radio_op.isSelected();


                            Project newProj = new Project(
                                    projWindow.field_name.getText(),
                                    projWindow.field_strain.getText(),
                                    Integer.parseInt(projWindow.field_room.getText()),
                                    Integer.parseInt(projWindow.field_box.getText()),
                                    projWindow.field_coordinates.getText(),
                                    Integer.parseInt(projWindow.field_number.getText()),
                                    projWindow.field_researcher.getText(),
                                    gender,
                                    operative
                            );

                            projects.add(newProj);

                            refreshProjects();

                            list_project.setSelectedIndex(projects.size() - 1);
                            projWindow.dispose();
//                        save();
                        } catch (Exception ex) {
                            errorDialog("Error creating new project from window.", "\nPlease make sure that all fields are complete and all numeric fields are filled with only numeric values.");
                        }
                    }
                }
                );
                projWindow.button_delete.addActionListener(
                        new ActionListener() {

                            @Override
                            public void actionPerformed(ActionEvent e
                            ) {
                                projWindow.dispose();
                            }
                        }
                );
                projWindow.setVisible(
                        true);
            }
        });
    }//GEN-LAST:event_button_newProjectActionPerformed

    private void button_editProjectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_editProjectActionPerformed
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void getValues() {

            }

            @Override
            public void run() {

                final NewProject projWindow = new NewProject(
                        new javax.swing.JFrame(), true, true);
                try {
                    projWindow.field_name.setText(projects.get(project_selected).getName());
                    projWindow.field_strain.setText(projects.get(project_selected).getStrain());
                    projWindow.field_room.setText("" + projects.get(project_selected).getRoom());
                    projWindow.field_box.setText("" + projects.get(project_selected).getBox());
                    projWindow.field_coordinates.setText(projects.get(project_selected).getCoordinates());
                    projWindow.field_number.setText("" + projects.get(project_selected).getNumber());
                    projWindow.field_researcher.setText("" + projects.get(project_selected).getResearcher());

                } catch (Exception e) {
                    errorDialog("Error displaying project information in \"Edit Project\" window.", "\nPlease make sure a project is selected.");
                    return;
                }

                projWindow.radio_male.setSelected(projects.get(project_selected).getGender().equals("Male"));
                projWindow.radio_female.setSelected(!projects.get(project_selected).getGender().equals("Male"));
                projWindow.radio_op.setSelected(projects.get(project_selected).isOperative());
                projWindow.radio_nonop.setSelected(!projects.get(project_selected).isOperative());

                projWindow.button_done.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {

                            if (projWindow.field_name.getText().equals("") || projWindow.field_strain.getText().equals("") || projWindow.field_room.getText().equals("") || projWindow.field_box.getText().equals("") || projWindow.field_coordinates.getText().equals("") || projWindow.field_number.getText().equals("") || projWindow.field_researcher.getText().equals("")) {
                                throw new EmptyFieldException();
                            }
                            String gender = projWindow.radio_male.isSelected() ? "Male" : "Female";
                            boolean operative = projWindow.radio_op.isSelected();

                            projects.get(project_selected).setName(projWindow.field_name.getText());
                            projects.get(project_selected).setStrain(projWindow.field_strain.getText());
                            projects.get(project_selected).setRoom(Integer.parseInt(projWindow.field_room.getText()));
                            projects.get(project_selected).setBox(Integer.parseInt(projWindow.field_box.getText()));
                            projects.get(project_selected).setCoordinates(projWindow.field_coordinates.getText());
                            projects.get(project_selected).setNumber(Integer.parseInt(projWindow.field_number.getText()));
                            projects.get(project_selected).setResearcher(projWindow.field_researcher.getText());
                            projects.get(project_selected).setGender(gender);
                            projects.get(project_selected).setOperative(operative);
                            refreshProjects();
                            projWindow.dispose();
//                        save();
                        } catch (Exception ex) {
                            errorDialog("Error saving project information in \"Edit Project\" window.", "\nPlease make sure that all fields are complete and all numeric fields are filled with only numeric values.");
                        }
                    }
                });
                projWindow.button_delete.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        projects.remove(project_selected);
                        refreshProjects();
                        projWindow.dispose();
//                        save();
                    }
                });
                projWindow.setVisible(true);
            }
        });

    }//GEN-LAST:event_button_editProjectActionPerformed

    private void button_newRatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_newRatActionPerformed
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                final NewRat dialog = new NewRat(
                        new javax.swing.JFrame(), true, false);

                dialog.button_done.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {
                            Rat newRat = new Rat(
                                    Integer.parseInt(dialog.field_number.getText()),
                                    dialog.field_box.getText(),
                                    dialog.field_notes.getText(),
                                    new LocalDate(dialog.field_dob.getText()));
                            projects.get(project_selected).getRats().add(newRat);
                            refreshRats(projects.get(project_selected));
                            list_rats.setSelectedIndex(projects.get(project_selected).getRats().size() - 1);
                            dialog.dispose();
//                        save();
                        } catch (Exception ex) {
                            errorDialog("Error creating new rat from window.", "\nPlease make sure that all fields are complete and all numeric fields are filled with only numeric values.");
                        }
                    }
                });
                dialog.button_delete.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        dialog.dispose();
                    }
                });
                dialog.setVisible(true);
            }
        });
    }//GEN-LAST:event_button_newRatActionPerformed

    private void button_editRatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_editRatActionPerformed
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {

                final NewRat dialog = new NewRat(
                        new javax.swing.JFrame(), true, true);
                try {
                    dialog.field_number.setText("" + projects.get(project_selected).getRats().get(rat_selected).getNumber());
                    dialog.field_box.setText("" + projects.get(project_selected).getRats().get(rat_selected).getBox());
//                dialog.field_ramsey.setText("" + projects.get(project_selected).getRats().get(rat_selected).getRamsey());
                    dialog.field_notes.setText(projects.get(project_selected).getRats().get(rat_selected).getPreSurgeryNotesNotes());
                } catch (Exception e) {
                    errorDialog("Error displaying rat information in \"Edit Rat\" window.", "\nPlease make sure a rat is selected.");
                    return;
                }
                dialog.button_done.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {
                            projects.get(project_selected).getRats().get(rat_selected).setNumber(Integer.parseInt(dialog.field_number.getText()));
                            projects.get(project_selected).getRats().get(rat_selected).setBox(dialog.field_box.getText());
//                        projects.get(project_selected).getRats().get(rat_selected).setRamsey(Integer.parseInt(dialog.field_ramsey.getText()));
                            projects.get(project_selected).getRats().get(rat_selected).setPreSurgeryNotesNotes(dialog.field_notes.getText());
                            refreshRats(projects.get(project_selected));
                            dialog.dispose();
//                        save();
                        } catch (Exception ex) {
                            errorDialog("Error saving edited Rat information.", "\nPlease make sure that all fields are complete and all numeric fields are filled with only numeric values.");
                        }
                    }
                });
                dialog.button_delete.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        projects.get(project_selected).getRats().remove(rat_selected);
                        refreshRats(projects.get(project_selected));
                        dialog.dispose();
//                        save();
                    }
                });
                dialog.setVisible(true);
            }
        });

    }//GEN-LAST:event_button_editRatActionPerformed

    private void menuItem_saveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItem_saveActionPerformed

        save();
    }//GEN-LAST:event_menuItem_saveActionPerformed

    private void menuItem_aboutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItem_aboutActionPerformed
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                About dialog = new About(new javax.swing.JFrame(), true, version);
                dialog.setVisible(true);
            }
        });
    }//GEN-LAST:event_menuItem_aboutActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        try {
            Rat r = projects.get(project_selected).getRats().get(rat_selected);
            r.setDead(!r.isDead());
            refreshTrigger();
//        save();

        } catch (Exception ex) {
            errorDialog("Error setting rat as dead/alive.", "\nPlease make sure a rat is selected.");
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        int n = JOptionPane.showConfirmDialog(
                new JFrame(),
                "Are you sure you want to load?\nYou will lose all your unsaved changes doing so.",
                "Confirm Load",
                JOptionPane.YES_NO_OPTION);
        if (n == 0) {
            load();
        }
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void button_weightGraphActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_weightGraphActionPerformed
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                WeightChart w = new WeightChart(new javax.swing.JFrame(), true, projects.get(project_selected).getRats().get(rat_selected).getDailyInfo(), 1);
                w.pack();
                RefineryUtilities.centerFrameOnScreen(w);
                w.setVisible(true);
            }
        });
    }//GEN-LAST:event_button_weightGraphActionPerformed

    private void button_ramseyGraphActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_ramseyGraphActionPerformed
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                WeightChart w = new WeightChart(new javax.swing.JFrame(), true, projects.get(project_selected).getRats().get(rat_selected).getDailyInfo(), 2);
                w.pack();
                RefineryUtilities.centerFrameOnScreen(w);
                w.setVisible(true);
            }
        });
    }//GEN-LAST:event_button_ramseyGraphActionPerformed

    private void button_postWeightGraphActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_postWeightGraphActionPerformed
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                WeightChart w = new WeightChart(new javax.swing.JFrame(), true, projects.get(project_selected).getRats().get(rat_selected).getPostDailyInfo(), 1);
                w.pack();
                RefineryUtilities.centerFrameOnScreen(w);
                w.setVisible(true);
            }
        });
    }//GEN-LAST:event_button_postWeightGraphActionPerformed

    private void button_postRamseyGraphActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_postRamseyGraphActionPerformed
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                WeightChart w = new WeightChart(new javax.swing.JFrame(), true, projects.get(project_selected).getRats().get(rat_selected).getPostDailyInfo(), 2);
                w.pack();
                RefineryUtilities.centerFrameOnScreen(w);
                w.setVisible(true);
            }
        });
    }//GEN-LAST:event_button_postRamseyGraphActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                Census dialog = new Census(new javax.swing.JFrame(), false, projects);
                dialog.setVisible(true);
            }
        });
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                ComparativeGraph dialog = new ComparativeGraph(new javax.swing.JFrame(), false, projects);
                dialog.setVisible(true);
            }
        });
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void button_newDrugActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_newDrugActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_button_newDrugActionPerformed

    private void menuItem_reportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItem_reportActionPerformed
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                Reporter dialog = new Reporter(new javax.swing.JFrame(), true, "");
                dialog.setVisible(true);
            }
        });
    }//GEN-LAST:event_menuItem_reportActionPerformed

    private void ramseyCalcActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ramseyCalcActionPerformed
        ramseyCalculator();
    }//GEN-LAST:event_ramseyCalcActionPerformed

    private void button_removeWeightRamseyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_removeWeightRamseyActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_button_removeWeightRamseyActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {

                ArrayList<EnvInfo> test = new ArrayList<>();

                final Enviorment dialog = new Enviorment(new javax.swing.JFrame(), true, data);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        //data = dialog.getData();
                        Collections.copy(data, dialog.getData());
                    }
                });
                dialog.setVisible(true);
            }
        });
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    public void resizeColumnWidth(JTable table) {
        final TableColumnModel columnModel = table.getColumnModel();
        for (int column = 0; column < table.getColumnCount(); column++) {
            int width = 50; // Min width
            for (int row = 0; row < table.getRowCount(); row++) {
                TableCellRenderer renderer = table.getCellRenderer(row, column);
                Component comp = table.prepareRenderer(renderer, row, column);
                width = Math.max(comp.getPreferredSize().width, width);
            }
            columnModel.getColumn(column).setPreferredWidth(width);
        }
    }

    public void save() {
        try {
            SaveController s = new SaveController();
            SaveFile save = new SaveFile(projects, data);
            s.save(save);
        } catch (Exception e) {
            errorDialog("Error trying to save file.", "");
        }
    }

    public void load() {
        try {
            SaveController s = new SaveController();
            SaveFile loaded = s.load();
            projects = loaded.getProjects();
            data = loaded.getData();
        } catch (Exception e) {
            errorDialog("Error trying to load file.", "");
            e.printStackTrace();
        }

        refreshProjects();
    }

    public void refreshProjects() {
        try {
            model_project = new DefaultListModel<String>();
            this.list_project.setModel(model_project);
            for (Project p : projects) {
                model_project.addElement(p.getName() + ", " + p.getResearcher());
            }
            this.list_project.setModel(model_project);
            this.list_project.setSelectedIndex(project_selected);
        } catch (Exception e) {
            errorDialog("Error refreshing projects.", "");
        }
    }

    public void refreshRats(Project p) {
        try {
            if (p.isOperative()) {
                tabs.setEnabledAt(0, true);
                tabs.setEnabledAt(1, true);
            } else {

                tabs.setEnabledAt(0, false);
                tabs.setEnabledAt(1, false);
                tabs.setSelectedIndex(2);
            }
            ArrayList<Rat> rats = p.getRats();
            label_projectInfo.setText(
                    "Strain: " + p.getStrain() + " | Room: " + p.getRoom() + " | Box: " + p.getBox() + " | Coords.: " + p.getCoordinates() + " | Number: " + p.getNumber() + " | Gender: " + p.getGender());

            model_rats = new DefaultListModel<String>();
            this.list_rats.setModel(model_rats);
            if (rats.size() > 0) {
                for (Rat r : rats) {
                    String e = "";
                    if (r.isDead()) {
                        e += "DEAD: ";
                    }
                    e += r.getNumber();
                    model_rats.addElement(e);
                }
            }
            this.list_rats.setModel(model_rats);
            defaultRatInfo();
        } catch (Exception e) {
            errorDialog("Error refreshing project information.", "");
        }
    }

    public static void errorDialog(final String mainText, final String subText) {
        Object[] options = {"OK",
            "Report Error"};
        int n = JOptionPane.showOptionDialog(new JFrame(),
                "An error has ocurred:\n"
                + mainText + subText,
                "Error",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.ERROR_MESSAGE,
                null,
                options,
                options[0]);

        if (n == 1) {
            java.awt.EventQueue.invokeLater(new Runnable() {
                public void run() {
                    Reporter dialog = new Reporter(new javax.swing.JFrame(), true, mainText);
                    dialog.setVisible(true);
                }
            });
        }
    }

    public void defaultRatInfo() {
        label_ratInfo.setText("Please select a rat");
        model_dailyInfo = (DefaultTableModel) table_dailyInfo.getModel();
        model_dailyInfo.setRowCount(0);
        model_drugs = (DefaultTableModel) table_drugs.getModel();
        model_drugs.setRowCount(0);

        TableColumnModel m = table_surgery.getColumnModel();

        TableColumn column = null;
        for (int i = 0; i < m.getColumnCount(); i++) {
            column = table_surgery.getColumnModel().getColumn(i);
            column.setPreferredWidth(100);
        }

        model_surgery = (DefaultTableModel) table_surgery.getModel();
        model_surgery.setRowCount(0);
        model_postSurgery = (DefaultTableModel) table_postDailyInfo.getModel();
        model_postSurgery.setRowCount(0);
        model_postDrugs = (DefaultTableModel) table_postDrugs.getModel();
        model_postDrugs.setRowCount(0);
        model_diet = (DefaultTableModel) table_diet.getModel();
        model_diet.setRowCount(0);
        model_bbb = (DefaultTableModel) table_bbb.getModel();
        model_bbb.setRowCount(0);
        text_notes.setText("");
    }

    public void refreshRatInfo(final Rat r) {
        try {
            //General
            String info = "";
            if (r.isDead()) {
                info += "DEAD - ";
            }
            info += "Number: " + r.getNumber() + " | Box: " + r.getBox() + " | Date of Birth: " + r.getDateOfBirth().toString();
            if (!r.getSurgery().isEmpty()) {
                info += " | Date of Last Surgery: " + r.getSurgery().get(r.getSurgery().size() - 1).getDate().toString();
            }
            label_ratInfo.setText(info);

            //Pre-Surgery
            model_dailyInfo = (DefaultTableModel) table_dailyInfo.getModel();

            if (r.getDailyInfo().isEmpty()) {
                model_dailyInfo.setRowCount(0);
            } else {
                int row = 1;
                for (DailyInfo i : r.getDailyInfo()) {
                    Object[] rowData = {row, i.getDate().toString(), i.getWeight(), i.getRamsey()};
                    model_dailyInfo.addRow(rowData);
                    row++;
                }
            }

            model_drugs = (DefaultTableModel) table_drugs.getModel();

            if (r.getDrugs().isEmpty()) {
                model_drugs.setRowCount(0);
            } else {
                int row = 1;
                for (DrugInfo d : r.getDrugs()) {
                    Object[] rowData = {row, d.getDate().toString(), d.getTime().toString("HH:mm"), d.getDrugName(), d.getDosis()};
                    model_drugs.addRow(rowData);
                    row++;
                }
            }

            text_notes.setText(r.getPreSurgeryNotesNotes());

            //Surgery
            model_surgery = (DefaultTableModel) table_surgery.getModel();

            if (r.getSurgery().isEmpty()) {
                model_surgery.setRowCount(0);
            } else {
                for (SurgeryInfo i : r.getSurgery()) {
                    Object[] rowData = {i.getDate().toString(), i.getTime().toString(), i.getWeight(), i.getSurgeryNumber(), i.getaTime().toString(), i.isBladderEmpty(), i.isEyes(), i.getEnro(), i.getBupre(), i.getKeto(), i.isShaved(), i.isClean(), i.getIncision().toString(), i.getInjury().toString(), i.getClose().toString(), i.getExternalPoints(), i.getRehavilitation().toString(), i.getAwareness().toString(), i.getRamsey(), i.getNotes()};
                    model_surgery.addRow(rowData);

                }
            }

            //Post surgery
            model_postSurgery = (DefaultTableModel) table_postDailyInfo.getModel();

            if (r.getPostDailyInfo().isEmpty()) {
                model_postSurgery.setRowCount(0);
            } else {

                int row = 1;
                float percentChange;
                for (DailyInfo i : r.getPostDailyInfo()) {
                    if (!r.getSurgery().isEmpty()) {
                        percentChange = (float) ((-(r.getSurgery().get(0).getWeight()) + (r.getPostDailyInfo().get(row - 1).getWeight())) * 100 / (r.getSurgery().get(0).getWeight()));
                    } else {
                        percentChange = 0.0f;
                    }
                    Object[] rowData = {row, i.getDate().toString(), i.getWeight(), percentChange, i.getRamsey()};
                    model_postSurgery.addRow(rowData);
                    row++;

                }
            }

            model_postDrugs = (DefaultTableModel) table_postDrugs.getModel();

            if (r.getPostDrugs().isEmpty()) {
                model_postDrugs.setRowCount(0);
            } else {
                int row = 1;
                for (DrugInfo d : r.getPostDrugs()) {
                    Object[] rowData = {row, d.getDate().toString(), d.getTime().toString("HH:mm"), d.getDrugName(), d.getDosis()};
                    model_postDrugs.addRow(rowData);
                    row++;
                }
            }

            text_postNotes.setText(r.getPostNotes());

            //Analysis and Graphs
            this.label_preAvgW.setText("" + Statistics.getMean(r.getWeightAsDouble(r.getDailyInfo())));
            this.label_preAvgR.setText("" + Statistics.getMean(r.getRamseyAsDouble(r.getDailyInfo())));
            this.label_preSDW.setText("" + Statistics.getStdDev(r.getWeightAsDouble(r.getDailyInfo())));
            this.label_preSDR.setText("" + Statistics.getStdDev(r.getRamseyAsDouble(r.getDailyInfo())));

            this.label_preAvgW1.setText("" + Statistics.getMean(r.getWeightAsDouble(r.getPostDailyInfo())));
            this.label_preAvgR1.setText("" + Statistics.getMean(r.getRamseyAsDouble(r.getPostDailyInfo())));
            this.label_preSDW1.setText("" + Statistics.getStdDev(r.getWeightAsDouble(r.getPostDailyInfo())));
            this.label_preSDR1.setText("" + Statistics.getStdDev(r.getRamseyAsDouble(r.getPostDailyInfo())));

            //Diet and BBB
            model_diet = (DefaultTableModel) table_diet.getModel();

            if (r.getDiet().isEmpty()) {
                model_diet.setRowCount(0);
            } else {
                for (FoodInfo f : r.getDiet()) {
                    Object[] rowData = {f.getDate().toString(), f.getFood(), f.getGiven(), f.getEaten()};
                    model_diet.addRow(rowData);
                }
            }

            model_bbb = (DefaultTableModel) table_bbb.getModel();

            if (r.getBbb().isEmpty()) {
                model_bbb.setRowCount(0);
            } else {
                for (BBBInfo b : r.getBbb()) {
                    Object[] rowData = {b.getDate().toString(), b.getBBB()};
                    model_bbb.addRow(rowData);
                }
            }

        } catch (Exception e) {
            errorDialog("Error refreshing rat information.", "");
        }
    }

    public void ramseyCalculator() {
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                RamseyCalculator dialog = new RamseyCalculator(new javax.swing.JFrame(), false);
                dialog.setVisible(true);
            }
        });
    }

    public static void main(String args[]) {

        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;

                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(NewProject.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(new Thread() {
            @Override
            public void run() {
                //try {
                new Main().setVisible(true);
                //} catch (Exception e) {
                //   Main.errorDialog("Fatal error.", "");
                //}

            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton button_editProject;
    private javax.swing.JButton button_editRat;
    private javax.swing.JButton button_newBBB;
    private javax.swing.JButton button_newDrug;
    private javax.swing.JButton button_newFood;
    private javax.swing.JButton button_newPostDrug;
    private javax.swing.JButton button_newPostSurgeryWeightRamsey;
    private javax.swing.JButton button_newProject;
    private javax.swing.JButton button_newRat;
    private javax.swing.JButton button_newSurgery;
    private javax.swing.JButton button_newWeightRamsey;
    private javax.swing.JButton button_postRamseyGraph;
    private javax.swing.JButton button_postWeightGraph;
    private javax.swing.JButton button_ramseyGraph;
    private javax.swing.JButton button_removeBBB;
    private javax.swing.JButton button_removeDrug;
    private javax.swing.JButton button_removeFood;
    private javax.swing.JButton button_removePostDrug;
    private javax.swing.JButton button_removePostWeightRamsey;
    private javax.swing.JButton button_removeSurgery;
    private javax.swing.JButton button_removeWeightRamsey;
    private javax.swing.JButton button_savePSNotes;
    private javax.swing.JButton button_savePSNotes1;
    private javax.swing.JButton button_weightGraph;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JLabel label_preAvgR;
    private javax.swing.JLabel label_preAvgR1;
    private javax.swing.JLabel label_preAvgW;
    private javax.swing.JLabel label_preAvgW1;
    private javax.swing.JLabel label_preSDR;
    private javax.swing.JLabel label_preSDR1;
    private javax.swing.JLabel label_preSDW;
    private javax.swing.JLabel label_preSDW1;
    private javax.swing.JLabel label_projectInfo;
    private javax.swing.JLabel label_ratInfo;
    private javax.swing.JList<String> list_project;
    private javax.swing.JList<String> list_rats;
    private javax.swing.JMenuItem menuItem_about;
    private javax.swing.JMenuItem menuItem_report;
    private javax.swing.JMenuItem menuItem_save;
    private javax.swing.JPanel panel_table;
    private javax.swing.JPanel panel_table1;
    private javax.swing.JMenuItem ramseyCalc;
    private javax.swing.JPanel tab_pre;
    private javax.swing.JPanel tab_sur;
    private javax.swing.JTable table_bbb;
    private javax.swing.JTable table_dailyInfo;
    private javax.swing.JTable table_diet;
    private javax.swing.JTable table_drugs;
    private javax.swing.JTable table_postDailyInfo;
    private javax.swing.JTable table_postDrugs;
    private javax.swing.JTable table_surgery;
    private javax.swing.JTabbedPane tabs;
    private javax.swing.JTextArea text_notes;
    private javax.swing.JTextArea text_postNotes;
    // End of variables declaration//GEN-END:variables
}

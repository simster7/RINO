/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.RatView.main;

import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author Simon
 */
public class Reporter extends javax.swing.JDialog {

    String error;

    /**
     * Creates new form Reporter
     */
    public Reporter(java.awt.Frame parent, boolean modal, String error) {
        super(parent, modal);
        initComponents();
        this.error = error;
        if (!error.equals("")) {
            field_error.setText(error);
        }
    }

    public void sendMail(String text) {
        String to = "ratman.bug@gmail.com";//change accordingly  
        String from = "ratman.bug@gmail.com";

        //Get the session object  
        Properties properties = System.getProperties();
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
//        properties.put("mail.smtp.user", "ratman.bug@gmail.com"); // User name
//        properties.put("mail.smtp.password", "ratview123"); // password
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        SmtpAuthenticator authenticatior = new SmtpAuthenticator();
        Session session = Session.getDefaultInstance(properties, authenticatior);

        //compose the message  
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject("Bug Report");
            
            message.setText(text);

            // Send message  
            Transport.send(message);

        } catch (MessagingException mex) {
            mex.printStackTrace();
        }

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        field_error = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        field_desc = new javax.swing.JTextArea();
        button_send = new javax.swing.JButton();
        button_cancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Report an Error");

        jLabel1.setText("Error:");

        field_error.setEditable(false);
        field_error.setText("Unknown Error");

        jLabel2.setText("Please fully describe the error, including any steps necessary in order to cause the error:");

        field_desc.setColumns(20);
        field_desc.setRows(5);
        jScrollPane1.setViewportView(field_desc);

        button_send.setText("Send");
        button_send.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_sendActionPerformed(evt);
            }
        });

        button_cancel.setText("Cancel");
        button_cancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_cancelActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(field_error))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(button_cancel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(button_send))
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 427, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel2)))
                        .addGap(0, 3, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(field_error, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 246, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(button_send)
                    .addComponent(button_cancel))
                .addContainerGap(15, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void button_cancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_cancelActionPerformed
        dispose();
        if (error.equals("Fatal error.")) {
            System.exit(0);
        }
    }//GEN-LAST:event_button_cancelActionPerformed

    private void button_sendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_sendActionPerformed
        String text = "";

        text += "Error: " + (error.equals("") ? "Unknown Error" : error);
        text += "\n\nDescription:\n" + field_desc.getText();

        sendMail(text);

        JOptionPane.showMessageDialog(new JFrame(),
                "The error has been reported. Thank you.");
        dispose();
        if (error.equals("Fatal error.")) {
            System.exit(0);
        }
    }//GEN-LAST:event_button_sendActionPerformed

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
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Reporter.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Reporter.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Reporter.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Reporter.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                Reporter dialog = new Reporter(new javax.swing.JFrame(), true, "");
                dialog.setVisible(true);
            }
        });
    }

    public class SmtpAuthenticator extends Authenticator {

        public SmtpAuthenticator() {

            super();
        }

        @Override
        public PasswordAuthentication getPasswordAuthentication() {
            String username = "ratman.bug@gmail.com";
            String password = "ratview123";
            if ((username != null) && (username.length() > 0) && (password != null)
                    && (password.length() > 0)) {

                return new PasswordAuthentication(username, password);
            }

            return null;
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton button_cancel;
    private javax.swing.JButton button_send;
    private javax.swing.JTextArea field_desc;
    private javax.swing.JTextField field_error;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}

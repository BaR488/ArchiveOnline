/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ArchiverServer;

import ArchiverClasses.Archiver;
import static Utils.ConsoleLogger.*;
import com.google.common.collect.ImmutableSet;
import com.google.common.reflect.ClassPath;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.PrintStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.AgeFileFilter;
import static org.apache.commons.io.filefilter.TrueFileFilter.TRUE;
import org.apache.commons.lang3.time.DateUtils;

/**
 *
 * @author minel
 */
public class jFrameMain extends javax.swing.JFrame {

    private static final String SERVER_STARTED = "Запущен";
    private static final String SERVER_STOPPED = "Остановлен";

    private static final Color SERVER_STARTED_COLOR = Color.GREEN;
    private static final Color SERVER_STOPPED_COLOR = Color.RED;

    private static final int CLEAR_DELAY = 300000;
    private static final int UPDATE_DELAY = 5000;

    //Стандартные поток вывода
    public PrintStream standartSystemOut = System.out;
    public PrintStream standartSystemErr = System.err;

    //Поток для запуска сервера
    private ExecutorService archiverThreadPool;
    private ExecutorService jettyThreadPool;

    private Timer updateTimer;
    private Timer deleteFilesTimer;

    private static Archiver<?> archiver = null;

    //Список хеш мапов
    HashMap<String, Class>[] mapArray = new HashMap[2];

    /**
     * Creates new form jFrameMain
     */
    public jFrameMain() {
        try {

            initComponents();

            jLabelInProgress.setVisible(false);
            jLabelInQueue.setVisible(false);
            jLabelForProgress.setVisible(false);
            jLabelForQueue.setVisible(false);

            //Перенаправляет потоки ввода вывода
            PrintStream printStream = new PrintStream(new CustomOutputStream(jTextAreaConsole), true, "Windows-1251");
            System.setOut(printStream);
            System.setErr(printStream);

            updateTimer = new Timer(UPDATE_DELAY, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent evt) {
                    if (archiver != null) {
                        jLabelInQueue.setText(((Integer) archiver.getFilesInQueue().size()).toString());
                        jLabelInProgress.setText((archiver.getRunningThreads()).toString());
                    }
                }
            });

            deleteFilesTimer = new Timer(CLEAR_DELAY, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    File outputDir = new File(Archiver.OUTPUTFILE_PATH);
                    if (outputDir.exists() && outputDir.isDirectory()) {
                        int filesDeleted = 0;
                        Date thresholdDate = DateUtils.addHours(new Date(), -1);
                        Iterator<File> filesToDelete = FileUtils.iterateFiles(outputDir, new AgeFileFilter(thresholdDate), TRUE);
                        while (filesToDelete.hasNext()) {
                            File f = filesToDelete.next();
                            filesDeleted += f.delete() ? 1 : 0;
                        }
                        if (filesDeleted > 0) {
                            logFilesCleanedUp(filesDeleted);
                        }
                    }
                }
            });
            deleteFilesTimer.start();

            mapArray[0] = new HashMap<>();
            mapArray[1] = new HashMap<>();

            //Берем все пути к классам этого проекта
            ClassPath classPath = ClassPath.from(Thread.currentThread().getContextClassLoader());

            //Извелкаем все имена классов в пакете ArhiverThreads
            ImmutableSet<ClassPath.ClassInfo> topLevelClasses = classPath.getTopLevelClasses("ArchiverThreads");

            for (ClassPath.ClassInfo classInfo : topLevelClasses) {
                Class<?> threadClass = Class.forName(classInfo.getName());
                int type = threadClass.getField("type").getInt(null);
                String format = threadClass.getField("format").get(null).toString();
                mapArray[type].put(format, threadClass);
            }

            jLabelServerState.setText(SERVER_STOPPED);
            jLabelServerState.setForeground(SERVER_STOPPED_COLOR);

        } catch (Exception ex) {
            Logger.getLogger(jFrameMain.class.getName()).log(Level.SEVERE, null, ex);
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

        jScrollPane1 = new javax.swing.JScrollPane();
        jTextAreaConsole = new javax.swing.JTextArea();
        jLabel1 = new javax.swing.JLabel();
        jTextFieldPort = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jComboBoxType = new javax.swing.JComboBox<>();
        jComboBoxFormat = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        jTextFieldQueueSize = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jTextFieldThreadCount = new javax.swing.JTextField();
        jButtonStart = new javax.swing.JButton();
        jButtonStop = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jLabelServerState = new javax.swing.JLabel();
        jLabelForQueue = new javax.swing.JLabel();
        jLabelInQueue = new javax.swing.JLabel();
        jLabelForProgress = new javax.swing.JLabel();
        jLabelInProgress = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("ArchiverOnline - сервер");
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jTextAreaConsole.setEditable(false);
        jTextAreaConsole.setBackground(new java.awt.Color(240, 240, 240));
        jTextAreaConsole.setColumns(20);
        jTextAreaConsole.setFont(new java.awt.Font("Consolas", 0, 12)); // NOI18N
        jTextAreaConsole.setRows(5);
        jTextAreaConsole.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jTextAreaConsole.setFocusable(false);
        jTextAreaConsole.setHighlighter(null);
        jScrollPane1.setViewportView(jTextAreaConsole);

        jLabel1.setText("Порт");

        jLabel2.setText("Формат");

        jLabel3.setText("Тип сервера");

        jComboBoxType.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Архиватор", "Разархиватор" }));
        jComboBoxType.setSelectedIndex(-1);
        jComboBoxType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxTypeActionPerformed(evt);
            }
        });

        jLabel4.setText("Размер очереди");

        jLabel5.setText("Количество потоков");

        jButtonStart.setText("Запустить");
        jButtonStart.setFocusable(false);
        jButtonStart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonStartActionPerformed(evt);
            }
        });

        jButtonStop.setText("Остановить");
        jButtonStop.setToolTipText("");
        jButtonStop.setEnabled(false);
        jButtonStop.setFocusable(false);
        jButtonStop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonStopActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel6.setText("Состояние сервера:");

        jLabelServerState.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabelServerState.setForeground(new java.awt.Color(255, 0, 0));
        jLabelServerState.setText("Остановлен");

        jLabelForQueue.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabelForQueue.setText("В очереди:");

        jLabelInQueue.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabelInQueue.setText("0");

        jLabelForProgress.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabelForProgress.setText("В обработке");

        jLabelInProgress.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabelInProgress.setText("0");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextFieldPort, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jComboBoxType, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jComboBoxFormat, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextFieldQueueSize, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextFieldThreadCount, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButtonStart)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButtonStop))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabelServerState, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabelForQueue)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabelInQueue, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabelForProgress)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabelInProgress, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabelServerState)
                        .addComponent(jLabelForQueue)
                        .addComponent(jLabelInQueue)
                        .addComponent(jLabelForProgress)
                        .addComponent(jLabelInProgress)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 272, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jTextFieldPort, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(jComboBoxType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBoxFormat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(jTextFieldQueueSize, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(jTextFieldThreadCount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonStart)
                    .addComponent(jButtonStop))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    //Запуск сервера
    private void jButtonStartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonStartActionPerformed

        if (checkFields()) {

            jTextAreaConsole.setText("");

            logServerStarting();

            archiver = null;

            //Значения с формы
            Integer port = new Integer(jTextFieldPort.getText().trim());
            String format = jComboBoxFormat.getSelectedItem().toString();
            Integer threadCount = new Integer(jTextFieldThreadCount.getText().trim());
            Integer queueSize = new Integer(jTextFieldQueueSize.getText().trim());
            Integer type = jComboBoxType.getSelectedIndex();
            Class archiverThreadClass = mapArray[type].get(format);

            //Создаем поток в котором будет запущен jetty и произведена регистация сервера
            StartJettyThread webThread = new StartJettyThread(archiverThreadClass, port, format,
                    threadCount, queueSize, Archiver.ServerType.values()[type]);
            jettyThreadPool = Executors.newSingleThreadExecutor();

            //Будующее архиватора
            Future<Archiver> archiverFuture = jettyThreadPool.submit(webThread);

            try {

                //Дожидаемся завершения запуска Jetty и регистрации архиватора
                archiver = archiverFuture.get();

                //Если был создан архиватор и зарегестрирован
                if (archiver != null && archiver.isRegistred()) {

                    logServerStarted();

                    //Создаем поток в котором будет происходить архивация
                    StartArchiverThread archiverThread = new StartArchiverThread(archiver);

                    //Запускаем этот поток
                    archiverThreadPool = Executors.newSingleThreadExecutor();
                    archiverThreadPool.submit(archiverThread);

                    updateTimer.start();

                    setControlsState(true);

                } else {
                    logServerStopping();
                    if (archiver != null) {
                        archiver.getJettyServer().stop();
                        archiver.getJettyServer().destroy();
                        archiver.unRegister();
                    }
                    setControlsState(false);
                    logServerStopped();
                }

            } catch (Exception ex) {
                Logger.getLogger(jFrameMain.class.getName()).log(Level.SEVERE, null, ex.getMessage());
            }

        }


    }//GEN-LAST:event_jButtonStartActionPerformed

    //Остановка сервера
    private void jButtonStopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonStopActionPerformed
        try {
            updateTimer.stop();
            logServerStopping();
            if (archiver != null) {
                archiver.getJettyServer().stop();
                archiver.getJettyServer().destroy();
                archiver.unRegister();
                archiverThreadPool.shutdownNow();
            }
            jLabelInProgress.setText("0");
            jLabelInQueue.setText("0");
            setControlsState(false);
        } catch (Exception ex) {
            Logger.getLogger(jFrameMain.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButtonStopActionPerformed

    private void jComboBoxTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxTypeActionPerformed
        jComboBoxFormat.removeAllItems();
        mapArray[jComboBoxType.getSelectedIndex()].keySet().forEach((key) -> {
            jComboBoxFormat.addItem(key);
        });
        jComboBoxFormat.setSelectedIndex(-1);
    }//GEN-LAST:event_jComboBoxTypeActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        updateTimer.stop();
        deleteFilesTimer.stop();
        logServerStopping();
        if (archiver != null) {
            try {
                archiver.getJettyServer().stop();
                archiver.getJettyServer().destroy();
                archiver.unRegister();
                archiverThreadPool.shutdownNow();
            } catch (Exception ex) {
                Logger.getLogger(jFrameMain.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_formWindowClosing

    //Изменяет состояние контролов на форме при запуске сервера
    private void setControlsState(boolean serverIsStarted) {
        jComboBoxFormat.setEnabled(!serverIsStarted);
        jComboBoxType.setEnabled(!serverIsStarted);
        jTextFieldPort.setEnabled(!serverIsStarted);
        jTextFieldQueueSize.setEnabled(!serverIsStarted);
        jTextFieldThreadCount.setEnabled(!serverIsStarted);
        jButtonStart.setEnabled(!serverIsStarted);
        jLabelInProgress.setVisible(serverIsStarted);
        jLabelInQueue.setVisible(serverIsStarted);
        jButtonStop.setEnabled(serverIsStarted);
        jLabelForProgress.setVisible(serverIsStarted);
        jLabelForQueue.setVisible(serverIsStarted);
        if (serverIsStarted) {
            jLabelServerState.setText(SERVER_STARTED);
            jLabelServerState.setForeground(SERVER_STARTED_COLOR);
        } else {
            jLabelServerState.setText(SERVER_STOPPED);
            jLabelServerState.setForeground(SERVER_STOPPED_COLOR);
        }
    }

    //Проверка полей
    private boolean checkFields() {

        if (!tryParseInt(jTextFieldPort.getText().trim())) {
            JOptionPane.showMessageDialog(this, "Порт должен быть натуральным числом", "Ошибка", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (new Integer(jTextFieldPort.getText().trim()) <= 0) {
            JOptionPane.showMessageDialog(this, "Порт должен быть натуральным числом", "Ошибка", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (jComboBoxType.getSelectedIndex() < 0) {
            JOptionPane.showMessageDialog(this, "Необходимо выбрать тип сервера", "Ошибка", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (jComboBoxFormat.getSelectedIndex() < 0) {
            JOptionPane.showMessageDialog(this, "Необходимо выбрать формат сервера", "Ошибка", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (!tryParseInt(jTextFieldQueueSize.getText().trim())) {
            JOptionPane.showMessageDialog(this, "Размер очереди должен быть целым неотрицательным числом", "Ошибка", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (new Integer(jTextFieldQueueSize.getText().trim()) < 0) {
            JOptionPane.showMessageDialog(this, "Размер очереди должен быть целым неотрицательным числом", "Ошибка", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (!tryParseInt(jTextFieldThreadCount.getText().trim())) {
            JOptionPane.showMessageDialog(this, "Количество потоков должно быть натуральным числом", "Ошибка", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (new Integer(jTextFieldThreadCount.getText().trim()) <= 0) {
            JOptionPane.showMessageDialog(this, "Количество потоков должно быть натуральным числом", "Ошибка", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    //Возвращает true если строку можно преобрзовать в целое число
    private boolean tryParseInt(String value) {
        try {
            int valueInt = Integer.parseInt(value);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

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
            java.util.logging.Logger.getLogger(jFrameMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(jFrameMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(jFrameMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(jFrameMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new jFrameMain().setVisible(true);
            }
        });

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonStart;
    private javax.swing.JButton jButtonStop;
    private javax.swing.JComboBox<String> jComboBoxFormat;
    private javax.swing.JComboBox<String> jComboBoxType;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabelForProgress;
    private javax.swing.JLabel jLabelForQueue;
    private javax.swing.JLabel jLabelInProgress;
    private javax.swing.JLabel jLabelInQueue;
    private javax.swing.JLabel jLabelServerState;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextAreaConsole;
    private javax.swing.JTextField jTextFieldPort;
    private javax.swing.JTextField jTextFieldQueueSize;
    private javax.swing.JTextField jTextFieldThreadCount;
    // End of variables declaration//GEN-END:variables

}

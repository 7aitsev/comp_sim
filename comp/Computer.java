package comp;

import comp.arithmetic_logic_unit.ArithLogicUnit;
import comp.assembler.Assembler;
import comp.assembler.Loader;
import comp.assembler.TextEditorPanel;
import comp.control_unit.Commands;
import comp.control_unit.ControlUnit;
import comp.random_access_memory.RandAccMemory;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;
import ui.StatusBar;

public class Computer extends    JPanel
                      implements ActionListener, Runnable {
  ControlUnit cu;
  RandAccMemory ram;
  ArithLogicUnit alu;
  private Thread process;

  private boolean running = false;
  private boolean threadStopped = true;

  private JButton assemble, load, run, step, halt, reset;
  private StatusBar status;
  private TextEditorPanel tep;
  private Assembler asm;
  private Loader loader;

  public Computer() {
    super(new BorderLayout());

    // Building our powerfull computer :)
    ram = new RandAccMemory(GenSettings.ramAmount);
    alu = new ArithLogicUnit();
    cu = new ControlUnit(0, ram, alu);

    // Creating buttons and the panel for them to control executing of the
    //  program
    JPanel ctrlPanel = new JPanel(new FlowLayout(FlowLayout.LEADING, 4, -1));
    assemble = new JButton("Assemble");
    load = new JButton("Load");
    run = new JButton("Run");
    step = new JButton("Single Step");
    halt = new JButton("Halt");
    reset = new JButton("Reset");
    ctrlPanel.add(assemble);
    ctrlPanel.add(load);
    ctrlPanel.add(run);
    ctrlPanel.add(step);
    ctrlPanel.add(halt);
    ctrlPanel.add(reset);

    // Создание панелей CPU, ALU и RAM в панели browser
    JPanel browser = new JPanel(new GridBagLayout());
    JPanel cpuPanel = new JPanel(new GridBagLayout()); 
    cpuPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(" CPU "));
    cpuPanel.add(cu.cuPanel, new GridBagConstraints(0, 0, 1, 1, 1, 1,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets(0, 10, 7, 10), 0, 0));
    cpuPanel.add(alu.ALUPanel, new GridBagConstraints(0, 1, 1, 1, 1, 1,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets(0, 10, 8, 10), 0, 0));
    JPanel ramPanel = new JPanel(new GridBagLayout());
    ramPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(" RAM "));
    ramPanel.add(ram.ramPanel, new GridBagConstraints(1, 0, 1, 2, 1, 1,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets(0, 11, 7, 9), 0, 0));
    browser.add(cpuPanel, new GridBagConstraints(0, 0, 1, 1, 0.4, 1,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets(0, 0, 0, 3), 0, 0));
    browser.add(ramPanel, new GridBagConstraints(1, 0, 1, 1, 0.6, 1,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets(0, 3, 0, 0), 0, 0));
    
    JPanel centerPanel = new JPanel(new GridBagLayout());

    // Creating the text editor component
    tep = new TextEditorPanel();
    
    // Arrangment of both panels to one, so we have the left part of a frame
    //  filled with a text editor and the right part with a browser of
    //  components
    centerPanel.add(tep, new GridBagConstraints(0, 0, 1, 1, 0.35, 1,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets(8, 0, 5, 4), 0, 0));
    centerPanel.add(browser, new GridBagConstraints(1, 0, 1, 1, 0.65, 1,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets(2, 4, 4, -3), 0, 0));

    // Creating the status bar for viwing a serval massages from assembler and
    //  computer
    status = new StatusBar("Assemble first."
                         + "\nType a program and press <Assemble>");

    add(ctrlPanel, BorderLayout.NORTH);
    add(centerPanel, BorderLayout.CENTER);
    add(status, BorderLayout.SOUTH);

    assemble.addActionListener(this);
    load.addActionListener(this);
    run.addActionListener(this);
    step.addActionListener(this);
    halt.addActionListener(this);
    reset.addActionListener(this);
    
    load.setEnabled(false);
    run.setEnabled(false);
    step.setEnabled(false);
    halt.setEnabled(false);
    reset.setEnabled(false);

    asm = new Assembler(tep.getTextComponent());
    loader = new Loader(ram);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    if(e.getSource() == assemble) {
      status.setStatus(asm.assemble());
      load.setEnabled(false);
      if(status.getStatus().contains("successfully"))
        load.setEnabled(true);
    }
    else if(e.getSource() == load) {
      status.setStatus(loader.load(asm.getBinary()));
      if(status.getStatus().contains("successfully")) {
        assemble.setEnabled(false);
        load.setEnabled(false);
        run.setEnabled(true);
        step.setEnabled(true);
        halt.setEnabled(true);
        reset.setEnabled(true);
      }
    }
    else if(e.getSource() == run) {
      if(process == null || !process.isAlive()) {
        process = new Thread(this, "Process");
        process.start();
      }
      else {
        resumeExecution();
      }
      step.setEnabled(false);
      status.setStatus("Running...");
    }
    else if(e.getSource() == step) {
      if (!running) {
        status.setStatus("Single step.");
        int instructionCode = cu.executeInstruction();
        if(instructionCode == Commands.HLT) {
          status.setStatus("Normal program termination.");
          run.setEnabled(false);
          step.setEnabled(false);
          halt.setEnabled(false);
        }
        if(instructionCode == Commands.ILLEGAL){
          int mess = cu.getAddrFromPC()-3; // pc refernces next instruction
          status.setStatus("Illegal instruction at location " + mess + ".");
          run.setEnabled(false);
          step.setEnabled(false);
          halt.setEnabled(false);
        }
      }
    }
    else if(e.getSource() == halt) {
      if(running) {
        suspendExecution();
        status.setStatus("Execution suspended.");
        step.setEnabled(true);
      }
    }
    else if(e.getSource() == reset) {
      if(running) {
        stop();
        sleepABit(100);
      }
      resetComputer();
      status.setStatus("Computer was reseted.\nType a program and press "
              + "<Assemlbe> to begin.");
    }
  }

  @Override
  public void run() {
    running = true;
    threadStopped = false;
    int instructionCode = Commands.HLT;
    while(!threadStopped) {
      if(running) {
        instructionCode = cu.executeInstruction();
        if (instructionCode == Commands.HLT) break;
        if (instructionCode == Commands.ILLEGAL){
          int mess = cu.getAddrFromPC()-3; // pc refernces next instruction
          status.setStatus("Illegal instruction at location " + mess + ".");
          break;
        }
      }
      sleepABit(100);
    }
    if (instructionCode == Commands.HLT) {
      status.setStatus("Normal program termination.");
      run.setEnabled(false);
      step.setEnabled(false);
      halt.setEnabled(false);
    }
    process = null;
    running = false;
  }

   public void stop() {
    threadStopped = true;
    suspendExecution();
  }

  private void sleepABit(int milliseconds) {
    try {
       Thread.sleep(milliseconds);
    }
    catch (InterruptedException e) {}
  }

  private void resumeExecution() {
    running = true;
  }

  private void suspendExecution() {
    running = false;
  }
  
  private void resetComputer() {
    ram.resetRam();
    alu.resetAlu();
    cu.resetCu();
    asm.resetAsm();
    
    assemble.setEnabled(true);
    load.setEnabled(false);
    run.setEnabled(false);
    step.setEnabled(false);
    halt.setEnabled(false);
    reset.setEnabled(false);
  }
}

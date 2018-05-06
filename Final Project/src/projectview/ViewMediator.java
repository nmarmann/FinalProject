package projectview;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.util.Observable;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import project.CodeAccessException;
import project.DivideByZeroException;
import project.MachineModel;
import project.Memory;

public class ViewMediator extends Observable {
	/*
	 * This will be more useful later on it's just a shell right now
	 */
	private MachineModel model;
	private CodeViewPanel codeViewPanel;
	private MemoryViewPanel memoryViewPanel1;
	private MemoryViewPanel memoryViewPanel2;
	private MemoryViewPanel memoryViewPanel3;
	private ControlPanel controlPanel;
	private ProcessorViewPanel processorPanel;
	public void setPeriod(int period) {
		animator.setPeriod(period);
	}

	private MenuBarBuilder menuBuilder;
	private JFrame frame;
	private FilesManager filesManager;
	private Animator animator;

	public MachineModel getModel() {
		return model;
	}

	public void setModel(MachineModel model) {
		this.model = model;
	}

	public JFrame getFrame() {
		return frame;
	}

	public void step(){
		if(model.getCurrentState()!=States.PROGRAM_HALTED && model.getCurrentState()!= States.NOTHING_LOADED){
			try{
				model.step();
			} catch (CodeAccessException e) { // import project.CodeAccessException at the start of the class
				JOptionPane.showMessageDialog(
						frame, 
						"Illegal access to code from line " + model.getInstructionPointer() + "\n"
								+ "Exception message: " + e.getMessage(),
								"Run time error",
								JOptionPane.OK_OPTION);
			}  catch (ArrayIndexOutOfBoundsException e) { // import project.CodeAccessException at the start of the class
				JOptionPane.showMessageDialog(
						frame, 
						"Illegal access to data from line " + model.getInstructionPointer() + "\n"
								+ "Exception message: " + e.getMessage(),
								"Run time error",
								JOptionPane.OK_OPTION);
			} catch (NullPointerException e) { // import project.CodeAccessException at the start of the class
				JOptionPane.showMessageDialog(
						frame, 
						"Illegal access to data from line " + model.getInstructionPointer() + "\n"
								+ "Exception message: " + e.getMessage(),
								"Run time error",
								JOptionPane.OK_OPTION);
			} catch (IllegalArgumentException e) { // import project.CodeAccessException at the start of the class
				JOptionPane.showMessageDialog(
						frame, 
						"Illegal access to data from line " + model.getInstructionPointer() + "\n"
								+ "Exception message: " + e.getMessage(),
								"Run time error",
								JOptionPane.OK_OPTION);
			} catch (DivideByZeroException e) { // import project.CodeAccessException at the start of the class
				JOptionPane.showMessageDialog(
						frame, 
						"Illegal access to data from line " + model.getInstructionPointer() + "\n"
								+ "Exception message: " + e.getMessage(),
								"Run time error",
								JOptionPane.OK_OPTION);
			} 
			setChanged();
			notifyObservers();

		}
	}

	public void execute(){
		while(model.getCurrentState()!=States.PROGRAM_HALTED && model.getCurrentState()!= States.NOTHING_LOADED){
			try{
				model.step();
			} catch (CodeAccessException e) { // import project.CodeAccessException at the start of the class
				JOptionPane.showMessageDialog(
						frame, 
						"Illegal access to code from line " + model.getInstructionPointer() + "\n"
								+ "Exception message: " + e.getMessage(),
								"Run time error",
								JOptionPane.OK_OPTION);
			}  catch (ArrayIndexOutOfBoundsException e) { // import project.CodeAccessException at the start of the class
				JOptionPane.showMessageDialog(
						frame, 
						"Illegal access to data from line " + model.getInstructionPointer() + "\n"
								+ "Exception message: " + e.getMessage(),
								"Run time error",
								JOptionPane.OK_OPTION);
			} catch (NullPointerException e) { // import project.CodeAccessException at the start of the class
				JOptionPane.showMessageDialog(
						frame, 
						"Illegal access to data from line " + model.getInstructionPointer() + "\n"
								+ "Exception message: " + e.getMessage(),
								"Run time error",
								JOptionPane.OK_OPTION);
			} catch (IllegalArgumentException e) { // import project.CodeAccessException at the start of the class
				JOptionPane.showMessageDialog(
						frame, 
						"Illegal access to data from line " + model.getInstructionPointer() + "\n"
								+ "Exception message: " + e.getMessage(),
								"Run time error",
								JOptionPane.OK_OPTION);
			} catch (DivideByZeroException e) { // import project.CodeAccessException at the start of the class
				JOptionPane.showMessageDialog(
						frame, 
						"Illegal access to data from line " + model.getInstructionPointer() + "\n"
								+ "Exception message: " + e.getMessage(),
								"Run time error",
								JOptionPane.OK_OPTION);
			} 


		}
		setChanged();
		notifyObservers();
	}

	public void clearJob() {
		model.clearJob();
		model.setCurrentState(States.NOTHING_LOADED);
		model.getCurrentState().enter();
		setChanged();
		notifyObservers("Clear");
	}

	public void toggleAutoStep(){
		animator.toggleAutoStep();
		if(animator.isAutoStepOn()){
			model.setCurrentState(States.AUTO_STEPPING);
		} else{
			model.setCurrentState(States.PROGRAM_LOADED_NOT_AUTOSTEPPING);
			model.getCurrentState().enter();
			setChanged();
			notifyObservers();
		}
	}

	public void reload(){
		animator.setAutoStepOn(false);
		clearJob();
		filesManager.finalLoad_ReloadStep(model.getCurrentJob());
	}

	public void assembleFile(){
		filesManager.assembleFile();
	}

	public void loadFile(){
		filesManager.loadFile(model.getCurrentJob());
	}

	public void setJob(int i){
		model.setJob(i);
		if(!(model.getCurrentState() == null)){
			model.getCurrentState().enter();
			setChanged();
			notifyObservers();
		}
	}

	private void createAndShowGUI(){
		animator = new Animator(this);
		filesManager = new FilesManager(this);
		filesManager.initialize();
		codeViewPanel = new CodeViewPanel(this, model);
		memoryViewPanel1 = new MemoryViewPanel(this, model, 0, 240);
		memoryViewPanel2 = new MemoryViewPanel(this, model, 240, Memory.DATA_SIZE/2);
		memoryViewPanel3 = new MemoryViewPanel(this, model, Memory.DATA_SIZE/2, Memory.DATA_SIZE);
		controlPanel = new ControlPanel(this);
		processorPanel = new ProcessorViewPanel(this, model);
		menuBuilder = new MenuBarBuilder(this);
		frame = new JFrame("Simulator");
		Container content = frame.getContentPane();
		content.setLayout(new BorderLayout(1,1));
		content.setBackground(Color.BLACK);
		frame.setSize(1200, 600);
		JPanel center = new JPanel();
		center.setLayout(new GridLayout(1,3));
		frame.add(codeViewPanel.createCodeDisplay(),BorderLayout.LINE_START);
		center.add(memoryViewPanel1.createMemoryDisplay());
		center.add(memoryViewPanel2.createMemoryDisplay());
		center.add(memoryViewPanel3.createMemoryDisplay());
		frame.add(controlPanel.createControlDisplay(),BorderLayout.PAGE_END);
		frame.add(processorPanel.createProcessorDisplay(),BorderLayout.PAGE_START);
		JMenuBar bar = new JMenuBar();
		frame.setJMenuBar(bar);
		bar.add(menuBuilder.createFileMenu());
		bar.add(menuBuilder.createExecuteMenu());
		bar.add(menuBuilder.createJobsMenu());

		frame.add(center);
		// RETURN HERE FOR OTHER GUI COMPONENTS		
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(WindowListenerFactory.
				windowClosingFactory(e -> exit()));
		model.setCurrentState(States.NOTHING_LOADED);
		animator.start();
		model.getCurrentState().enter();
		setChanged();
		notifyObservers();
		// return HERE for other setup details
		frame.setVisible(true);
	}

	public void makeReady(String string) {
		animator.setAutoStepOn(false);
		model.setCurrentState(States.PROGRAM_LOADED_NOT_AUTOSTEPPING);
		model.getCurrentState().enter();
		setChanged();
		notifyObservers(string);
	}

	public States getCurrentState(){
		return model.getCurrentState();
	}

	public void setCurrentState(States currentState){
		if(currentState == States.PROGRAM_HALTED){
			animator.setAutoStepOn(false);
		}
		model.getCurrentState().enter();
		setChanged();
		notifyObservers();

		model.setCurrentState(currentState);
	}

	public void exit(){ // this will run when the user wants to leave
		int decision = JOptionPane.showConfirmDialog(
				frame, "Do you really wish to exit?",
				"Confirmation", JOptionPane.YES_NO_OPTION);
		if (decision == JOptionPane.YES_OPTION) {
			System.exit(0);
		}
	}

	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				ViewMediator mediator = new ViewMediator();
				MachineModel model = new MachineModel(
						true,  
						() -> mediator.setCurrentState(States.PROGRAM_HALTED)
						);
				mediator.setModel(model);
				mediator.createAndShowGUI();
			}
		});
	}
}

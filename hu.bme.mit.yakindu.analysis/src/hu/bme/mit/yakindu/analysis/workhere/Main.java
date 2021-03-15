package hu.bme.mit.yakindu.analysis.workhere;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.junit.Test;
import org.yakindu.sct.model.sgraph.Statechart;
import org.yakindu.sct.model.stext.stext.EventDefinition;
import org.yakindu.sct.model.stext.stext.VariableDefinition;

import hu.bme.mit.model2gml.Model2GML;
import hu.bme.mit.yakindu.analysis.modelmanager.ModelManager;

public class Main {
	@Test
	public void test() {
		main(new String[0]);
	}
	
	public static void main(String[] args) {
		ModelManager manager = new ModelManager();
		Model2GML model2gml = new Model2GML();
		
		// Loading model
		EObject root = manager.loadModel("model_input/example.sct");
		
		// Reading model
		Statechart s = (Statechart) root;
		TreeIterator<EObject> Nameiterator = s.eAllContents();
		TreeIterator<EObject> Eventiterator = s.eAllContents();
		
		System.out.println("package hu.bme.mit.yakindu.analysis.workhere;");
		System.out.println("import java.io.IOException;");
		System.out.println("import java.io.BufferedReader;");
		System.out.println("import java.io.InputStreamReader;\n");
		
		System.out.println("import hu.bme.mit.yakindu.analysis.RuntimeService;");
		System.out.println("import hu.bme.mit.yakindu.analysis.TimerService;");
		System.out.println("import hu.bme.mit.yakindu.analysis.example.ExampleStatemachine;");
		System.out.println("import hu.bme.mit.yakindu.analysis.example.IExampleStatemachine;\n\n");

		System.out.println("public class RunStatechart {\n");
		System.out.println("	public static void main(String[] args) throws IOException {");
		System.out.println("		ExampleStatemachine s = new ExampleStatemachine();");
		System.out.println("		s.setTimer(new TimerService());");
		System.out.println("		RuntimeService.getInstance().registerStatemachine(s, 200);");
		System.out.println("		s.init();");
		System.out.println("		s.enter();");
		System.out.println("		s.runCycle();");
		System.out.println("		print(s);\n");
		
		System.out.println("		while(true) {");
		System.out.println("			System.out.println(\"Give an event: \");");
		System.out.println("			BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));");
		System.out.println("			String command = bufferRead.readLine();\n");
		System.out.println("			switch (command) {");
		
		while (Eventiterator.hasNext()) {
			EObject content = Eventiterator.next();
			if(content instanceof EventDefinition) {
				EventDefinition event = (EventDefinition) content;
				System.out.println("				case \"" + event.getName() + "\":");
				System.out.println("					s.raise" + event.getName().substring(0, 1).toUpperCase() + event.getName().substring(1) + "();");
				System.out.println("					s.runCycle();");
				System.out.println("					break;");
			}
		}
		
		System.out.println("				case \"exit\":\n					System.exit(0);\n					break;\n			}");
		System.out.println("			print(s);");
		System.out.println("		}");
		System.out.println("	}\n");
  
		
		System.out.println("	public static void print(IExampleStatemachine s) {");
		while (Nameiterator.hasNext()) {
			EObject content = Nameiterator.next();
			if(content instanceof VariableDefinition) {
				VariableDefinition var = (VariableDefinition) content;
				System.out.println("		System.out.println(\"W = \" + s.getSCInterface().get" + var.getName().substring(0, 1).toUpperCase() + var.getName().substring(1) + "());");
			}
		}
		System.out.println("	}");
		System.out.println("}");
		
		// Transforming the model into a graph representation
		String content = model2gml.transform(root);
		// and saving it
		manager.saveFile("model_output/graph.gml", content);
	}
}

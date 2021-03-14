package hu.bme.mit.yakindu.analysis.workhere;

import java.util.ArrayList;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.junit.Test;
import org.yakindu.sct.model.sgraph.State;
import org.yakindu.sct.model.sgraph.Statechart;
import org.yakindu.sct.model.sgraph.Transition;
import org.yakindu.sct.model.sgraph.Vertex;

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
		TreeIterator<EObject> iterator = s.eAllContents();
		while (iterator.hasNext()) {
			EObject content = iterator.next();
			if(content instanceof State) {
				State state = (State) content;
				EList<Transition> transList = state.getOutgoingTransitions();
				ArrayList<Vertex> targetState = new ArrayList();
				for (Transition t:transList) {
					targetState.add(t.getTarget());
				}
				for (Vertex v:targetState) {
					System.out.println(state.getName() + " -> " + v.getName());
					if (v.getName() == null) {
						for (Transition t:transList) {
							EList<Transition> nonameTrans = t.getTarget().getIncomingTransitions();
							for (Transition k:nonameTrans) {
								if (k.getTarget().getName() == null) { 
									System.out.println("Supposed name: " + state.getName() + " " + k.getSpecification());
								}
							}
						}
					}
				}
				if (transList.size() == 0)
				{
					System.out.println("Trapstate: " + state.getName());
				}
			}
		}
		
		// Transforming the model into a graph representation
		String content = model2gml.transform(root);
		// and saving it
		manager.saveFile("model_output/graph.gml", content);
	}
}

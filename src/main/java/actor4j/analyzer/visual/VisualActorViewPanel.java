/*
 * Copyright (c) 2015-2017, David A. Bauer. All rights reserved.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package actor4j.analyzer.visual;

import java.awt.BorderLayout;

import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTabbedPane;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxRectangle;
import com.mxgraph.view.mxGraph;
import com.mxgraph.view.mxGraphView;

import actor4j.core.ActorSystemImpl;

public abstract class VisualActorViewPanel extends JPanel {
	protected static final long serialVersionUID = 2646158450651956287L;
	
	protected ActorSystemImpl system;

	protected JTabbedPane tabbedPane;
	protected JPopupMenu popupMenu;
	
	protected JPanel paDesign;
	protected mxGraphComponent graphComponent;
	protected mxGraph graph;
	protected Object parent;
	
	public VisualActorViewPanel(ActorSystemImpl system) {
		super();
		
		this.system = system;
		
		initialize();
	}
	
	public void initialize() {
		setLayout(new BorderLayout(0, 0));
		tabbedPane = new JTabbedPane(JTabbedPane.BOTTOM, JTabbedPane.SCROLL_TAB_LAYOUT);
		add(tabbedPane);
		
		popupMenu = new JPopupMenu();
		JMenuItem saveAsPicture = new JMenuItem("Save as picture...");
		popupMenu.add(saveAsPicture);
		
		paDesign = new JPanel();
		paDesign.setLayout(new BorderLayout());
		
		graph = new mxGraph();
        parent = graph.getDefaultParent();
        graph.setAutoOrigin(true);
        graph.setAllowDanglingEdges(false);
		graph.setSplitEnabled(false);
		graph.setKeepEdgesInForeground(false);
		graph.setKeepEdgesInBackground(true);

        graphComponent = new mxGraphComponent(graph);
		graphComponent.setEnabled(false);
		
		paDesign.add(graphComponent, BorderLayout.CENTER);
	}
	
	public void add(String title, JPanel panel) {
		tabbedPane.add(title, panel);
	}
	
	public Object addVertex(String name) {
		return addVertex(name, null);
	}
	
	public Object addVertex(String name, String color) {
		Object result = graph.insertVertex(parent, null, name, 0, 0, 0, 0, "shape=ellipse;perimter=ellipsePerimeter;fontColor=black"+color);
		graph.updateCellSize(result);
		return result;
	}
	
	public void addEdge(String value, Object source, Object target) {
		graph.insertEdge(parent, null, value, source, target, "strokeColor=gray;fontStyle=1;fontColor=black");
	}
	
	public abstract void updateStructure();
	
	public void resetViewport() {
		mxGraphView view = graphComponent.getGraph().getView();
		view.setScale(1.0);
	}
	
	public void fitViewport() {
		int gap = 5;
		
		mxGraphView view = graphComponent.getGraph().getView();
		mxRectangle r = view.getGraphBounds();
		double scale = Math.min(
			(double)graphComponent.getWidth() /(r.getX()+r.getWidth() +gap), 
			(double)graphComponent.getHeight()/(r.getY()+r.getHeight()+gap));
		
		if (scale>=0.8)
			view.setScale(scale);
	}
}

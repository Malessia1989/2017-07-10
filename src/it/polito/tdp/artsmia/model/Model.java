package it.polito.tdp.artsmia.model;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.artsmia.db.ArtsmiaDAO;

public class Model {

	private SimpleWeightedGraph<ArtObject, DefaultWeightedEdge> grafo;
	
	public void popolaGrafo() {
		
		if(grafo == null) {
			grafo=new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
			ArtsmiaDAO dao=new ArtsmiaDAO();
			dao.popolaGrafo(grafo);
		}
		
		
	}

}

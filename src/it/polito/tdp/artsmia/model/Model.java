package it.polito.tdp.artsmia.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.artsmia.db.ArtsmiaDAO;

public class Model {
	
	private ArtsmiaDAO dao;
	private Map<Integer, ArtObject> idMap;
	private Graph<ArtObject, DefaultWeightedEdge> grafo;
	
	
	public Model() {
		dao= new ArtsmiaDAO();
		idMap= new HashMap<>();
		dao.listObjects(idMap);

	}


	public void creaGrafo() {
		
		grafo= new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		Graphs.addAllVertices(grafo, idMap.values());

		List<Adiacenza> adj = dao.getAdiacenza(idMap);
		for (Adiacenza a : adj) {
		

		}
		
		System.out.println(grafo.vertexSet().size());
		System.out.println(grafo.edgeSet().size());
		for(DefaultWeightedEdge edge :grafo.edgeSet()) {
			System.out.println(edge+ " peso: " +grafo.getEdgeWeight(edge));
		}
	}
}

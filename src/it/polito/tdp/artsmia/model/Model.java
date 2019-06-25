package it.polito.tdp.artsmia.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


import org.jgrapht.Graphs;
import org.jgrapht.alg.ConnectivityInspector;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.jgrapht.traverse.BreadthFirstIterator;

import it.polito.tdp.artsmia.db.ArtsmiaDAO;

public class Model {
	
	private ArtsmiaDAO dao;
	private Map<Integer, ArtObject> idMap;
	private SimpleWeightedGraph<ArtObject, DefaultWeightedEdge> grafo;
	
	
	public Model() {
		dao= new ArtsmiaDAO();
		idMap= new HashMap<>();
		dao.listObjects(idMap);

	}


	public void creaGrafo() {
		
		grafo= new SimpleWeightedGraph<ArtObject,DefaultWeightedEdge>(DefaultWeightedEdge.class);
		Graphs.addAllVertices(grafo, idMap.values());

		List<Adiacenza> adj = dao.getAdiacenza(idMap);
		for (Adiacenza a : adj) {
			DefaultWeightedEdge edge = grafo.getEdge(a.getA1(), a.getA2());
			if (edge==null) {
				Graphs.addEdgeWithVertices(grafo, a.getA1(), a.getA2(), a.getPeso());
			}
		
		}
		System.out.println(grafo.vertexSet().size());
		System.out.println(grafo.edgeSet().size());
		
	}


	public String componenteConnessa(String idInput) {
		String ris = "";

		List<ArtObject> atemp = new LinkedList<>();

		ArtObject o1 = idMap.get(Integer.parseInt(idInput));

		if (o1 != null) {
			BreadthFirstIterator<ArtObject, DefaultWeightedEdge> bft = new BreadthFirstIterator<>(grafo,o1);

			while (bft.hasNext()) {

				atemp.add(bft.next());

			}

			ris += "Id corrispondente all'opera: " + o1.getName()

					+ " Numero vertici che compongono la componente connessa: " + atemp.size();

		}else {

			ris= "l'oggetto corrispondente all'id inserito inesostente!";

		}
		return ris;

	
	}


	public boolean isDigit(String idInput) {
		
		return idInput.matches("\\d+");
	}
}

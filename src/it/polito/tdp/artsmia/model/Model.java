package it.polito.tdp.artsmia.model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.jgrapht.traverse.BreadthFirstIterator;

import it.polito.tdp.artsmia.db.ArtsmiaDAO;

public class Model {
	private ArtsmiaDAO dao;
	private Map<Integer,ArtObject> idMap;
	private Graph<ArtObject ,DefaultWeightedEdge> grafo;
	
	
	public Model () {
		dao=new ArtsmiaDAO();
		idMap= new HashMap<>();
		dao.listObjects(idMap);
	}


	public void creaGrafo() {
		grafo= new SimpleWeightedGraph<ArtObject ,DefaultWeightedEdge>(DefaultWeightedEdge.class);
		
		Graphs.addAllVertices(grafo, idMap.values());
		
		List<Adiacenza> adj=dao.getAdiacenze(idMap);
		for(Adiacenza a: adj) {
			Graphs.addEdge(grafo, a.getO1(), a.getO2(), a.getPeso());
		}
		
	System.out.println("Grafo creato! ");
	System.out.println("vertici: " +grafo.vertexSet().size());
	System.out.println("archi: " +grafo.edgeSet().size());
	for(DefaultWeightedEdge edge: grafo.edgeSet()) {
	System.out.println(edge +" peso: "+grafo.getEdgeWeight(edge));
	
		
	}
	
	
	}


	public boolean isValid(String idInput) {
		
		return idInput.matches("\\d+");
	}

	public String ComponenteConnessa(int idInput) { 
		String ris = "";
		List<ArtObject> atemp = new LinkedList<>();
		ArtObject o1 = idMap.get(idInput);
		if (o1 != null) {

			BreadthFirstIterator<ArtObject, DefaultWeightedEdge> bft = new BreadthFirstIterator<>(grafo);
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
	
}

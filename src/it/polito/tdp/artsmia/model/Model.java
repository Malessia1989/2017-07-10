package it.polito.tdp.artsmia.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;



import it.polito.tdp.artsmia.db.ArtsmiaDAO;

public class Model {
	
	private SimpleWeightedGraph<ArtObject, DefaultWeightedEdge> grafo;
	private Map<Integer,ArtObject> idMap;
	
	public Model() {
		
		//mappa vuota verrà popolata in crea grafo()
		idMap=new HashMap<Integer,ArtObject>();
		grafo=new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
	}
	
	public void creaGrafo() {
		ArtsmiaDAO dao= new ArtsmiaDAO();
		dao.listObjects(idMap);		
		
		//aggiungo i vertici
		Graphs.addAllVertices(grafo, idMap.values());
		
		//aggiungo gli archi
		List<Adiacenza> adj =dao.listAdiacenze();

		for (Adiacenza a : adj) {
			ArtObject source = idMap.get(a.getO1());
			ArtObject dest = idMap.get(a.getO2());
			try {
				Graphs.addEdge(grafo, source, dest, a.getPeso());
			} catch (Throwable t) {
			}
		}
		System.out.println(
				"Grafo creato : " + grafo.vertexSet().size() + " vertici e " + grafo.edgeSet().size() + " archi");
	}

	public int getVertexSize() {
		return grafo.vertexSet().size();
	}

	public int getEdgeSize() {
		return grafo.edgeSet().size();
	}
	
}

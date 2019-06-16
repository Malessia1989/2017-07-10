package it.polito.tdp.artsmia.model;

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

	public String getComponenteConnessa(String inserito) {
		
		creaGrafo();
	
		String risultato="";
		List<ArtObject> obj=new LinkedList<>();
		ArtObject otemp =idMap.get(Integer.parseInt(inserito));
		
		BreadthFirstIterator<ArtObject, DefaultWeightedEdge> it= new BreadthFirstIterator<>(grafo,otemp);
		
		while(it.hasNext()) {
			obj.add(it.next());
		}
		
		
		risultato= "Id corrispondente all'opera: " +otemp.getName() + " \n" + "Numero di vertici che compongono la componente connessa: "+obj.size();
		
//		for(ArtObject a1: obj) {
//			//if(a1.getId() == Integer.parseInt(inserito))
//			risultato= "inserito id_object:  "+a1.getId()+ "  numero di vertici componente connessa : " +obj.size();
//		}
		return risultato;
	}

	public boolean isDigit(String inserito) {
		
		return inserito.matches("\\d+");
	}
	
}

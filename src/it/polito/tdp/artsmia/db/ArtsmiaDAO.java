package it.polito.tdp.artsmia.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.artsmia.model.ArtObject;

public class ArtsmiaDAO {

	public List<ArtObject> listObjects() {
		
		String sql = "SELECT * from objects";
		List<ArtObject> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				ArtObject artObj = new ArtObject(res.getInt("object_id"), res.getString("classification"), res.getString("continent"), 
						res.getString("country"), res.getInt("curator_approved"), res.getString("dated"), res.getString("department"), 
						res.getString("medium"), res.getString("nationality"), res.getString("object_name"), res.getInt("restricted"), 
						res.getString("rights_type"), res.getString("role"), res.getString("room"), res.getString("style"), res.getString("title"));
				
				result.add(artObj);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public void creGrafo(SimpleWeightedGraph<ArtObject, DefaultWeightedEdge> grafo) {
		
		String sql="select o1.object_id id1,  eo1.exhibition_id ex1, o1.title name1, o2.object_id id2, eo2.exhibition_id ex2, o2.title name2, count(*) as peso\n" + 
				"from objects o1, objects o2, exhibitions ex, exhibition_objects eo1, exhibition_objects eo2\n" + 
				"where o1.object_id=eo1.object_id\n" + 
				"and o2.object_id=eo2.object_id\n" + 
				"and ex.exhibition_id=eo1.exhibition_id\n" + 
				"and ex.exhibition_id=eo2.exhibition_id\n" + 
				"and o1.object_id> o2.object_id\n" + 
				"group by id1,id2";
		
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				// posso farlo con idMap<Integer,ArtObject>
				
				ArtObject o1=new ArtObject(res.getInt("id1"), res.getString("name1"));
				ArtObject o2= new ArtObject(res.getInt("id2"), res.getString("name2"));
				
				if(!grafo.containsVertex(o1)) {
					grafo.addVertex(o1);
					
				}
				if(!grafo.containsVertex(o2)) {
					grafo.addVertex(o2);
				}
				if(!grafo.containsEdge(o1,o2) && !grafo.containsEdge(o2,o1)) {
					DefaultWeightedEdge edge= grafo.addEdge(o1, o2);
					grafo.setEdgeWeight(edge, res.getInt("peso"));
				}

			
			}
			conn.close();
			
			
		} catch (SQLException e) {
			throw new RuntimeException("Errore DB");
		}
		
	}
	
}

package it.polito.tdp.artsmia.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.artsmia.model.Adiacenza;
import it.polito.tdp.artsmia.model.ArtObject;

public class ArtsmiaDAO {

	public List<ArtObject> listObjects(Map<Integer, ArtObject> idMap) {
		
		String sql = "SELECT * from objects";
		List<ArtObject> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				if(idMap.get(res.getInt("object_id")) == null) {

				ArtObject artObj = new ArtObject(res.getInt("object_id"), res.getString("classification"), res.getString("continent"), 
						res.getString("country"), res.getInt("curator_approved"), res.getString("dated"), res.getString("department"), 
						res.getString("medium"), res.getString("nationality"), res.getString("object_name"), res.getInt("restricted"), 
						res.getString("rights_type"), res.getString("role"), res.getString("room"), res.getString("style"), res.getString("title"));
				
				result.add(artObj);
				idMap.put(artObj.getId(), artObj);
				}else {
					result.add(idMap.get(res.getInt("objext_id")));
				}
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<Adiacenza> getAdiacenza(Map<Integer, ArtObject> idMap) {
		
		final String sql="select eo1.object_id id1, eo2.object_id id2, count(*) as peso " + 
				"from exhibition_objects eo1, exhibition_objects eo2 " + 
				"where eo1.exhibition_id=eo2.exhibition_id " + 
				"and eo1.object_id > eo2.object_id " + 
				"group by id1,id2";
		
		List<Adiacenza> result= new ArrayList<>();
		
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {

				ArtObject a1 = idMap.get(res.getInt("id1"));
				ArtObject a2 = idMap.get(res.getInt("id2"));
				double peso = res.getDouble("peso");

				if (a1 != null && a1 != null) {
					Adiacenza a = new Adiacenza(a1, a2, peso);
					result.add(a);
				}
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
}

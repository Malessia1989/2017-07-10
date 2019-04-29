package it.polito.tdp.artsmia.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;

import it.polito.tdp.artsmia.model.ArtObject;
import it.polito.tdp.artsmia.model.ArtObjectAndCount;

public class ArtsmiaDAO {

	public List<ArtObject> listObjects() {
		
		String sql = "SELECT * from objects ";
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
	
	public int contaExhibitionComuni(ArtObject aop, ArtObject aoa) {
		
		String sql="  select count(*) as cnt " + 
				"from exhibition_objects as eo1, exhibition_objects as eo2 " + 
				"where eo1.exhibition_id=eo2.exhibition_id  " + 
				"and eo1.object_id= ? " + 
				"and eo2.object_id= ? ";
		
			Connection conn= DBConnect.getConnection();
		
			try {
				PreparedStatement st= conn.prepareStatement(sql);
				
				st.setInt(1, aop.getId());
				st.setInt(2,aop.getId());
				
				
				ResultSet res= st.executeQuery();
				
				res.next();
				int conteggio= res.getInt("cnt");
				
				conn.close();
				return conteggio;
		
					
				
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
	}
	
	public List<ArtObjectAndCount> listArtObjrctAndCount(ArtObject ao) {
		
		String sql=" select count(eo2.exhibition_id) as cnt ,eo2.object_id as id  " + 
				"from exhibition_objects as eo1, exhibition_objects as eo2 " + 
				"where eo1.exhibition_id=eo2.exhibition_id " + 
				"and eo1.object_id=? " + 
				"and eo2.object_id> eo1.object_id " + 
				"group by eo2.object_id ";
		
		Connection conn= DBConnect.getConnection();
		List<ArtObjectAndCount> result= new ArrayList<>();
		
		try {
			PreparedStatement st= conn.prepareStatement(sql);
			st.setInt(1, ao.getId());
			
			ResultSet rs=st.executeQuery();
			
			while(rs.next()) {
				result.add(new ArtObjectAndCount(rs.getInt("id"),rs.getInt("cnt")));
			}
			
			conn.close();
			return result;
			
		} catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException(e);
		}
	
	}
}

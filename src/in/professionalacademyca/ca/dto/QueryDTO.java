package in.professionalacademyca.ca.dto;

public class QueryDTO {
	String id;
	String query;
	String response;
	String date;
	
	public QueryDTO(String id, String query, String response, String date,
			String post) {
		super();
		this.id = id;
		this.query = query;
		this.response = response;
		this.date = date;
		this.post = post;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getQuery() {
		return query;
	}
	public void setQuery(String query) {
		this.query = query;
	}
	public String getResponse() {
		return response;
	}
	public void setResponse(String response) {
		this.response = response;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getPost() {
		return post;
	}
	public void setPost(String post) {
		this.post = post;
	}
	String post;
}

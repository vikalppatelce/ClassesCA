package in.professionalacademyca.ca.dto;

public class AnswerDTO {

	String _id;
	String query;
	String post;
	String date;
	String reponse;
	public String get_id() {
		return _id;
	}
	public void set_id(String _id) {
		this._id = _id;
	}
	public String getQuery() {
		return query;
	}
	public AnswerDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	public AnswerDTO(String _id, String query, String post, String date,
			String reponse) {
		super();
		this._id = _id;
		this.query = query;
		this.post = post;
		this.date = date;
		this.reponse = reponse;
	}
	public void setQuery(String query) {
		this.query = query;
	}
	public String getPost() {
		return post;
	}
	public void setPost(String post) {
		this.post = post;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getReponse() {
		return reponse;
	}
	public void setReponse(String reponse) {
		this.reponse = reponse;
	}
}

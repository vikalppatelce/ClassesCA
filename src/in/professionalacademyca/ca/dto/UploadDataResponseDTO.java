
package in.professionalacademyca.ca.dto;

public class UploadDataResponseDTO {

	String query;
	public UploadDataResponseDTO(String query) {
		super();
		this.query = query;
	}
	public UploadDataResponseDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	public UploadDataResponseDTO(String query, String response) {
		super();
		this.query = query;
		this.response = response;
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
	String response;
	}

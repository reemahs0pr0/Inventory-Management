package sg.edu.iss.model;

import javax.persistence.Column; 
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;


@Entity
@Table(name = "User")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name ="userId")
	private int userId;
	
	@Column(name ="userName")
	@NotEmpty(message="Please fill in username")
	@Size(min=2, message = "The username must be more than 2 characters")
	@Size(max=50, message= "The username cannot be more than 50 characters")
	@Pattern(regexp = "[a-zA-Z0-9]+", message="The username should not include special characters")
	private String userName;
	
	@Column(name ="password")
	@NotEmpty(message="Please fill in password")
	@Size(min=2, message = "The password must be more than 2 characters")
	@Size(max=50, message= "The password cannot be more than 50 characters")	
	private String password;
	
	@NotEmpty(message="Please fill in email address")
	@Column(name="email")
	@Email(message="please enter valid email")
	private String email;
	
	@Column(name ="roleType")
	private RoleType roleType;
	
	public User() {
		super();
	}
	
	public User(String userName, String password, String email, RoleType roleType) {
		super();
		this.userName = userName;
		this.password = password;
		this.email = email;
		this.roleType = roleType;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public RoleType getRoleType() {
		return roleType;
	}

	public void setRoleType(RoleType roleType) {
		this.roleType = roleType;
	}

	@Override
	public String toString() {
		return "User [userId=" + userId + ", userName=" + userName + ", password=" + password + ", email=" + email
				+ ", roleType=" + roleType + "]";
	}	
	
}


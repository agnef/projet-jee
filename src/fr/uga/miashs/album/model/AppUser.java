package fr.uga.miashs.album.model;



import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Entity 
@NamedQueries({
    @NamedQuery(name="AppUser.findAll",
                query="SELECT u FROM AppUser u"),
    @NamedQuery(name="AppUser.login",
    query="SELECT u FROM AppUser u WHERE u.email=:email AND u.password=:password")
})
//email sera unique
@Table(uniqueConstraints=@UniqueConstraint(columnNames="email"))
public class AppUser {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
 	
	@Pattern(regexp="^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,6}$", message="{appuser.email.regex}")
	@NotNull(message="{appuser.email.notnull}")
	private String email;
	
	
	
	@NotNull(message="{appuser.lastname.notnull}")
	private String lastname;
	
//	dans validation user.property il y a le message
	@NotNull(message="{appuser.firstname.notnull}")
	private String firstname;
	
	@NotNull(message="{appuser.password.notnull}")
	private String password;
	
	@OneToMany(mappedBy="owner", cascade = CascadeType.ALL)
	private List<Album> userAlbums;
	
	@OneToMany(mappedBy="owner")
	private List<Album> sharedAlbums;

	public AppUser() {
	}

	public long getId() {
		return id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public List<Album> getUserAlbums() {
		return userAlbums;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AppUser other = (AppUser) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
}

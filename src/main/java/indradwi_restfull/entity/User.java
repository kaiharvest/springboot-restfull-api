package indradwi_restfull.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user")

public class User {

	@Id
	private String username;

	private String password;

	private String name;

	private String token;

	@Column(name = "token_expire_at")
	private Long TokenExpireAt;

	@OneToMany(mappedBy = "user")
	private List<Contact> contacts;

}

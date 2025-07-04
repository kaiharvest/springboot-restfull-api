package indradwi_restfull.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "addresses")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Address {

	@Id
	private String id;

	private String street;

	private String city;

	private String province;

	private String country;

	@Column(name = "postal_code")
	private String postalCode;

	@ManyToOne
	@JoinColumn(name = "contact_id", referencedColumnName = "id")
	private Contact contact;

}
